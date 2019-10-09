package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.parser.TwoDimensionSpatialCoordinate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Descriptor.
 */
@Document(collection = "descriptor")
public class Descriptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("date_created")
    private LocalDate dateCreated;

    @DBRef
    @Field("study")
    @JsonIgnoreProperties("descriptors")
    private Study study;

    @DBRef
    @Field("descriptor")
    @JsonIgnoreProperties("descriptors")
    private Study descriptor;

    @DBRef
    @Field("descriptors")
    private Set<Descriptor> descriptors = new HashSet<>();

    //@DBRef
    //@Field("twoDimensionalCoordinates")
    //private Set<TwoDimensionSpatialCoordinate> twoDimensionSpatialCoordinates = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public Descriptor dateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Study getStudy() {
        return study;
    }

    public Descriptor study(Study study) {
        this.study = study;
        return this;
    }

    public void setStudy(Study study) {
        this.study = study;
    }

    public Study getDescriptor() {
        return descriptor;
    }

    public Descriptor descriptor(Study study) {
        this.descriptor = study;
        return this;
    }

    public void setDescriptor(Study study) {
        this.descriptor = study;
    }

    public Set<Descriptor> getDescriptors() {
        return descriptors;
    }

    public Descriptor descriptors(Set<Descriptor> descriptors) {
        this.descriptors = descriptors;
        return this;
    }

    public Descriptor addDescriptor(Descriptor descriptor) {
        this.descriptors.add(descriptor);
        descriptor.getDescriptors().add(this);
        return this;
    }

    public Descriptor removeDescriptor(Descriptor descriptor) {
        this.descriptors.remove(descriptor);
        descriptor.getDescriptors().remove(this);
        return this;
    }

//    public void addTwoDimensionSpatialCoordinate(TwoDimensionSpatialCoordinate twoDimensionSpatialCoordinate)
//    {
//        this.twoDimensionSpatialCoordinates.add(twoDimensionSpatialCoordinate);
//        twoDimensionSpatialCoordinate.setDescriptor(this);
//
//    }

//    public void removeTwoDimensionSpatialCoordinate(TwoDimensionSpatialCoordinate twoDimensionSpatialCoordinate)
//    {
//        this.twoDimensionSpatialCoordinates.remove(twoDimensionSpatialCoordinate);
//
//    }

    public void setDescriptors(Set<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Descriptor)) {
            return false;
        }
        return id != null && id.equals(((Descriptor) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Descriptor{" +
            "id=" + getId() +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
