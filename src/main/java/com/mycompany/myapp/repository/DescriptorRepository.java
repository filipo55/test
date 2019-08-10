package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Descriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the Descriptor entity.
 */
@Repository
public interface DescriptorRepository extends MongoRepository<Descriptor, String> {

    @Query("{}")
    Page<Descriptor> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Descriptor> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Descriptor> findOneWithEagerRelationships(String id);

}
