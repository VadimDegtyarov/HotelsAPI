package com.learn.hotelsapi.repository;

import com.learn.hotelsapi.model.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity,Long> {
    Optional<Amenity> findByNameIgnoreCase(String name);
    @Query("select a from Amenity a where lower(a.name) in :names")
    List<Amenity> findByNameInIgnoreCase(@Param("names") Collection<String> names);
}
