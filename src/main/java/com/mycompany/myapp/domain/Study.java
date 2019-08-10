package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Study.
 */
@Document(collection = "study")
public class Study implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("study_instance_uid")
    private String studyInstanceUID;

    @Field("study_date")
    private LocalDate studyDate;

    @Field("requested_procedure_description")
    private String requestedProcedureDescription;

    @Field("accession_number")
    private String accessionNumber;

    @DBRef
    @Field("patient")
    @JsonIgnoreProperties("studies")
    private Patient patient;

    @DBRef
    @Field("descriptor")
    private Set<Descriptor> descriptors = new HashSet<>();


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudyInstanceUID() {
        return studyInstanceUID;
    }

    public Study studyInstanceUID(String studyInstanceUID) {
        this.studyInstanceUID = studyInstanceUID;
        return this;
    }

    public void setStudyInstanceUID(String studyInstanceUID) {
        this.studyInstanceUID = studyInstanceUID;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public Study studyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
        return this;
    }

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }

    public String getRequestedProcedureDescription() {
        return requestedProcedureDescription;
    }

    public Study requestedProcedureDescription(String requestedProcedureDescription) {
        this.requestedProcedureDescription = requestedProcedureDescription;
        return this;
    }

    public void setRequestedProcedureDescription(String requestedProcedureDescription) {
        this.requestedProcedureDescription = requestedProcedureDescription;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public Study accessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
        return this;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }


    public Set<Descriptor> getDescriptors() {
        return descriptors;
    }

    public Study descriptors(Set<Descriptor> descriptors) {
        this.descriptors = descriptors;
        return this;
    }

    public Study addDescriptor(Descriptor descriptor) {
        this.descriptors.add(descriptor);
        descriptor.setStudy(this);
        return this;
    }

    public Study removeDescriptor(Descriptor descriptor) {
        this.descriptors.remove(descriptor);
        descriptor.setStudy(null);
        return this;
    }

    public void setDescriptors(Set<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }

    public Patient getPatient() {
        return patient;
    }

    public Study patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Study)) {
            return false;
        }
        return id != null && id.equals(((Study) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Study{" +
            "id=" + getId() +
            ", studyInstanceUID='" + getStudyInstanceUID() + "'" +
            ", studyDate='" + getStudyDate() + "'" +
            ", requestedProcedureDescription='" + getRequestedProcedureDescription() + "'" +
            ", accessionNumber='" + getAccessionNumber() + "'" +
            "}";
    }
}
