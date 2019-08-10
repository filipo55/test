package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Patient;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

}
