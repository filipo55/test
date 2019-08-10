package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.domain.Descriptor;
import com.mycompany.myapp.repository.DescriptorRepository;
import com.mycompany.myapp.service.DescriptorService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link DescriptorResource} REST controller.
 */
@SpringBootTest(classes = {TestApp.class, TestSecurityConfiguration.class})
public class DescriptorResourceIT {

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private DescriptorRepository descriptorRepository;

    @Mock
    private DescriptorRepository descriptorRepositoryMock;

    @Mock
    private DescriptorService descriptorServiceMock;

    @Autowired
    private DescriptorService descriptorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restDescriptorMockMvc;

    private Descriptor descriptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DescriptorResource descriptorResource = new DescriptorResource(descriptorService);
        this.restDescriptorMockMvc = MockMvcBuilders.standaloneSetup(descriptorResource)
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
    public static Descriptor createEntity() {
        Descriptor descriptor = new Descriptor()
            .dateCreated(DEFAULT_DATE_CREATED);
        return descriptor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Descriptor createUpdatedEntity() {
        Descriptor descriptor = new Descriptor()
            .dateCreated(UPDATED_DATE_CREATED);
        return descriptor;
    }

    @BeforeEach
    public void initTest() {
        descriptorRepository.deleteAll();
        descriptor = createEntity();
    }

    @Test
    public void createDescriptor() throws Exception {
        int databaseSizeBeforeCreate = descriptorRepository.findAll().size();

        // Create the Descriptor
        restDescriptorMockMvc.perform(post("/api/descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descriptor)))
            .andExpect(status().isCreated());

        // Validate the Descriptor in the database
        List<Descriptor> descriptorList = descriptorRepository.findAll();
        assertThat(descriptorList).hasSize(databaseSizeBeforeCreate + 1);
        Descriptor testDescriptor = descriptorList.get(descriptorList.size() - 1);
        assertThat(testDescriptor.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
    }

    @Test
    public void createDescriptorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = descriptorRepository.findAll().size();

        // Create the Descriptor with an existing ID
        descriptor.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restDescriptorMockMvc.perform(post("/api/descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descriptor)))
            .andExpect(status().isBadRequest());

        // Validate the Descriptor in the database
        List<Descriptor> descriptorList = descriptorRepository.findAll();
        assertThat(descriptorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllDescriptors() throws Exception {
        // Initialize the database
        descriptorRepository.save(descriptor);

        // Get all the descriptorList
        restDescriptorMockMvc.perform(get("/api/descriptors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(descriptor.getId())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDescriptorsWithEagerRelationshipsIsEnabled() throws Exception {
        DescriptorResource descriptorResource = new DescriptorResource(descriptorServiceMock);
        when(descriptorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restDescriptorMockMvc = MockMvcBuilders.standaloneSetup(descriptorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDescriptorMockMvc.perform(get("/api/descriptors?eagerload=true"))
        .andExpect(status().isOk());

        verify(descriptorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDescriptorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        DescriptorResource descriptorResource = new DescriptorResource(descriptorServiceMock);
            when(descriptorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restDescriptorMockMvc = MockMvcBuilders.standaloneSetup(descriptorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restDescriptorMockMvc.perform(get("/api/descriptors?eagerload=true"))
        .andExpect(status().isOk());

            verify(descriptorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    public void getDescriptor() throws Exception {
        // Initialize the database
        descriptorRepository.save(descriptor);

        // Get the descriptor
        restDescriptorMockMvc.perform(get("/api/descriptors/{id}", descriptor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(descriptor.getId()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()));
    }

    @Test
    public void getNonExistingDescriptor() throws Exception {
        // Get the descriptor
        restDescriptorMockMvc.perform(get("/api/descriptors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateDescriptor() throws Exception {
        // Initialize the database
        descriptorService.save(descriptor);

        int databaseSizeBeforeUpdate = descriptorRepository.findAll().size();

        // Update the descriptor
        Descriptor updatedDescriptor = descriptorRepository.findById(descriptor.getId()).get();
        updatedDescriptor
            .dateCreated(UPDATED_DATE_CREATED);

        restDescriptorMockMvc.perform(put("/api/descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDescriptor)))
            .andExpect(status().isOk());

        // Validate the Descriptor in the database
        List<Descriptor> descriptorList = descriptorRepository.findAll();
        assertThat(descriptorList).hasSize(databaseSizeBeforeUpdate);
        Descriptor testDescriptor = descriptorList.get(descriptorList.size() - 1);
        assertThat(testDescriptor.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
    }

    @Test
    public void updateNonExistingDescriptor() throws Exception {
        int databaseSizeBeforeUpdate = descriptorRepository.findAll().size();

        // Create the Descriptor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDescriptorMockMvc.perform(put("/api/descriptors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(descriptor)))
            .andExpect(status().isBadRequest());

        // Validate the Descriptor in the database
        List<Descriptor> descriptorList = descriptorRepository.findAll();
        assertThat(descriptorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteDescriptor() throws Exception {
        // Initialize the database
        descriptorService.save(descriptor);

        int databaseSizeBeforeDelete = descriptorRepository.findAll().size();

        // Delete the descriptor
        restDescriptorMockMvc.perform(delete("/api/descriptors/{id}", descriptor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Descriptor> descriptorList = descriptorRepository.findAll();
        assertThat(descriptorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Descriptor.class);
        Descriptor descriptor1 = new Descriptor();
        descriptor1.setId("id1");
        Descriptor descriptor2 = new Descriptor();
        descriptor2.setId(descriptor1.getId());
        assertThat(descriptor1).isEqualTo(descriptor2);
        descriptor2.setId("id2");
        assertThat(descriptor1).isNotEqualTo(descriptor2);
        descriptor1.setId(null);
        assertThat(descriptor1).isNotEqualTo(descriptor2);
    }
}
