package com.mycompany.myapp.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Measurement.
 */
@Document(collection = "measurement")
public class Measurement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @DecimalMin(value = "0")
    @Field("area")
    private Float area;

    @Field("label")
    private String label;

    @Field("description")
    private String description;

    @Field("patient_id")
    private String patientId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getArea() {
        return area;
    }

    public Measurement area(Float area) {
        this.area = area;
        return this;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public String getLabel() {
        return label;
    }

    public Measurement label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public Measurement description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPatientId() {
        return patientId;
    }

    public Measurement patientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Measurement)) {
            return false;
        }
        return id != null && id.equals(((Measurement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Measurement{" +
            "id=" + getId() +
            ", area=" + getArea() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", patientId='" + getPatientId() + "'" +
            "}";
    }
}
