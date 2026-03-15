package com.learn.hotelsapi.repository;

import com.learn.hotelsapi.model.Amenity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AmenityRepositoryTest {

    @Autowired
    private AmenityRepository amenityRepository;

    @Test
    void findByNameInIgnoreCase_shouldReturnSaved() {
        Amenity a1 = new Amenity();
        a1.setName("Free WiFi");
        amenityRepository.save(a1);

        List<Amenity> result = amenityRepository.findByNameInIgnoreCase(List.of("free wifi"));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Free WiFi");
    }
}