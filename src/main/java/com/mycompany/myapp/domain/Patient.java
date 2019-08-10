package com.mycompany.myapp.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.Sex;

/**
 * A Patient.
 */
@Document(collection = "patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("patient_sex")
    private Sex patientSex;

    @DBRef
    @Field("study")
    private Set<Study> studies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Patient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getPatientSex() {
        return patientSex;
    }

    public Patient patientSex(Sex patientSex) {
        this.patientSex = patientSex;
        return this;
    }

    public void setPatientSex(Sex patientSex) {
        this.patientSex = patientSex;
    }

    public Set<Study> getStudies() {
        return studies;
    }

    public Patient studies(Set<Study> studies) {
        this.studies = studies;
        return this;
    }

    public Patient addStudy(Study study) {
        this.studies.add(study);
        study.setPatient(this);
        return this;
    }

    public Patient removeStudy(Study study) {
        this.studies.remove(study);
        study.setPatient(null);
        return this;
    }

    public void setStudies(Set<Study> studies) {
        this.studies = studies;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", patientSex='" + getPatientSex() + "'" +
            "}";
    }
}
