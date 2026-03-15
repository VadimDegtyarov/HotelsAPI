package com.learn.hotelsapi.repository;

import com.learn.hotelsapi.model.Amenity;
import com.learn.hotelsapi.model.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime,Long> {
}
