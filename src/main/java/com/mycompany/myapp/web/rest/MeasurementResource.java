package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Measurement;
import com.mycompany.myapp.repository.MeasurementRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Measurement}.
 */
@RestController
@RequestMapping("/api")
public class MeasurementResource {

    private final Logger log = LoggerFactory.getLogger(MeasurementResource.class);

    private static final String ENTITY_NAME = "measurement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MeasurementRepository measurementRepository;

    public MeasurementResource(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    /**
     * {@code POST  /measurements} : Create a new measurement.
     *
     * @param measurement the measurement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new measurement, or with status {@code 400 (Bad Request)} if the measurement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/measurements")
    public ResponseEntity<Measurement> createMeasurement(@Valid @RequestBody Measurement measurement) throws URISyntaxException {
        log.debug("REST request to save Measurement : {}", measurement);
        if (measurement.getId() != null) {
            throw new BadRequestAlertException("A new measurement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Measurement result = measurementRepository.save(measurement);
        return ResponseEntity.created(new URI("/api/measurements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /measurements} : Updates an existing measurement.
     *
     * @param measurement the measurement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated measurement,
     * or with status {@code 400 (Bad Request)} if the measurement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the measurement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/measurements")
    public ResponseEntity<Measurement> updateMeasurement(@Valid @RequestBody Measurement measurement) throws URISyntaxException {
        log.debug("REST request to update Measurement : {}", measurement);
        if (measurement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Measurement result = measurementRepository.save(measurement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, measurement.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /measurements} : get all the measurements.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of measurements in body.
     */
    @GetMapping("/measurements")
    public ResponseEntity<List<Measurement>> getAllMeasurements(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Measurements");
        Page<Measurement> page = measurementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /measurements/:id} : get the "id" measurement.
     *
     * @param id the id of the measurement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the measurement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/measurements/{id}")
    public ResponseEntity<Measurement> getMeasurement(@PathVariable String id) {
        log.debug("REST request to get Measurement : {}", id);
        Optional<Measurement> measurement = measurementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(measurement);
    }

    /**
     * {@code DELETE  /measurements/:id} : delete the "id" measurement.
     *
     * @param id the id of the measurement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/measurements/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable String id) {
        log.debug("REST request to delete Measurement : {}", id);
        measurementRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
