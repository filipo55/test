package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.Study;
import com.mycompany.myapp.repository.StudyRepository;
import com.mycompany.myapp.service.StudyService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link StudyResource} REST controller.
 */
@SpringBootTest(classes = {TestApp.class, TestSecurityConfiguration.class})
public class StudyResourceIT {

    private static final String DEFAULT_STUDY_INSTANCE_UID = "AAAAAAAAAA";
    private static final String UPDATED_STUDY_INSTANCE_UID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STUDY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STUDY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REQUESTED_PROCEDURE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTED_PROCEDURE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESSION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCESSION_NUMBER = "BBBBBBBBBB";

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyService studyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restStudyMockMvc;

    private Study study;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudyResource studyResource = new StudyResource(studyService);
        this.restStudyMockMvc = MockMvcBuilders.standaloneSetup(studyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createEntity() {
        Study study = new Study()
            .studyInstanceUID(DEFAULT_STUDY_INSTANCE_UID)
            .studyDate(DEFAULT_STUDY_DATE)
            .requestedProcedureDescription(DEFAULT_REQUESTED_PROCEDURE_DESCRIPTION)
            .accessionNumber(DEFAULT_ACCESSION_NUMBER);
        return study;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Study createUpdatedEntity() {
        Study study = new Study()
            .studyInstanceUID(UPDATED_STUDY_INSTANCE_UID)
            .studyDate(UPDATED_STUDY_DATE)
            .requestedProcedureDescription(UPDATED_REQUESTED_PROCEDURE_DESCRIPTION)
            .accessionNumber(UPDATED_ACCESSION_NUMBER);
        return study;
    }

    @BeforeEach
    public void initTest() {
        studyRepository.deleteAll();
        study = createEntity();
    }

    @Test
    public void createStudy() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // Create the Study
        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isCreated());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate + 1);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getStudyInstanceUID()).isEqualTo(DEFAULT_STUDY_INSTANCE_UID);
        assertThat(testStudy.getStudyDate()).isEqualTo(DEFAULT_STUDY_DATE);
        assertThat(testStudy.getRequestedProcedureDescription()).isEqualTo(DEFAULT_REQUESTED_PROCEDURE_DESCRIPTION);
        assertThat(testStudy.getAccessionNumber()).isEqualTo(DEFAULT_ACCESSION_NUMBER);
    }

    @Test
    public void createStudyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studyRepository.findAll().size();

        // Create the Study with an existing ID
        study.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyMockMvc.perform(post("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllStudies() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        // Get all the studyList
        restStudyMockMvc.perform(get("/api/studies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(study.getId())))
            .andExpect(jsonPath("$.[*].studyInstanceUID").value(hasItem(DEFAULT_STUDY_INSTANCE_UID.toString())))
            .andExpect(jsonPath("$.[*].studyDate").value(hasItem(DEFAULT_STUDY_DATE.toString())))
            .andExpect(jsonPath("$.[*].requestedProcedureDescription").value(hasItem(DEFAULT_REQUESTED_PROCEDURE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].accessionNumber").value(hasItem(DEFAULT_ACCESSION_NUMBER.toString())));
    }
    
    @Test
    public void getStudy() throws Exception {
        // Initialize the database
        studyRepository.save(study);

        // Get the study
        restStudyMockMvc.perform(get("/api/studies/{id}", study.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(study.getId()))
            .andExpect(jsonPath("$.studyInstanceUID").value(DEFAULT_STUDY_INSTANCE_UID.toString()))
            .andExpect(jsonPath("$.studyDate").value(DEFAULT_STUDY_DATE.toString()))
            .andExpect(jsonPath("$.requestedProcedureDescription").value(DEFAULT_REQUESTED_PROCEDURE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.accessionNumber").value(DEFAULT_ACCESSION_NUMBER.toString()));
    }

    @Test
    public void getNonExistingStudy() throws Exception {
        // Get the study
        restStudyMockMvc.perform(get("/api/studies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateStudy() throws Exception {
        // Initialize the database
        studyService.save(study);

        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Update the study
        Study updatedStudy = studyRepository.findById(study.getId()).get();
        updatedStudy
            .studyInstanceUID(UPDATED_STUDY_INSTANCE_UID)
            .studyDate(UPDATED_STUDY_DATE)
            .requestedProcedureDescription(UPDATED_REQUESTED_PROCEDURE_DESCRIPTION)
            .accessionNumber(UPDATED_ACCESSION_NUMBER);

        restStudyMockMvc.perform(put("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudy)))
            .andExpect(status().isOk());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
        Study testStudy = studyList.get(studyList.size() - 1);
        assertThat(testStudy.getStudyInstanceUID()).isEqualTo(UPDATED_STUDY_INSTANCE_UID);
        assertThat(testStudy.getStudyDate()).isEqualTo(UPDATED_STUDY_DATE);
        assertThat(testStudy.getRequestedProcedureDescription()).isEqualTo(UPDATED_REQUESTED_PROCEDURE_DESCRIPTION);
        assertThat(testStudy.getAccessionNumber()).isEqualTo(UPDATED_ACCESSION_NUMBER);
    }

    @Test
    public void updateNonExistingStudy() throws Exception {
        int databaseSizeBeforeUpdate = studyRepository.findAll().size();

        // Create the Study

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyMockMvc.perform(put("/api/studies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(study)))
            .andExpect(status().isBadRequest());

        // Validate the Study in the database
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteStudy() throws Exception {
        // Initialize the database
        studyService.save(study);

        int databaseSizeBeforeDelete = studyRepository.findAll().size();

        // Delete the study
        restStudyMockMvc.perform(delete("/api/studies/{id}", study.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Study> studyList = studyRepository.findAll();
        assertThat(studyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Study.class);
        Study study1 = new Study();
        study1.setId("id1");
        Study study2 = new Study();
        study2.setId(study1.getId());
        assertThat(study1).isEqualTo(study2);
        study2.setId("id2");
        assertThat(study1).isNotEqualTo(study2);
        study1.setId(null);
        assertThat(study1).isNotEqualTo(study2);
    }
}
