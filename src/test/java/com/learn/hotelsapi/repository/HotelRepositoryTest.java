package com.learn.hotelsapi.repository;

import com.learn.hotelsapi.model.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void saveAndFindById() {
        Hotel h = new Hotel();
        h.setName("Repo Test Hotel");
        h.setBrand("Hilton");
        h.setDescription("desc");

        Hotel saved = hotelRepository.save(h);

        Optional<Hotel> found = hotelRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Repo Test Hotel");
    }

    @Test
    void searchHotels_shouldFilterByBrand() {
        Hotel h1 = new Hotel();
        h1.setName("Hotel Minsk");
        h1.setBrand("Hilton");
        h1.setDescription("desc1");

        Hotel h2 = new Hotel();
        h2.setName("Hotel Moscow");
        h2.setBrand("Marriott");
        h2.setDescription("desc2");

        hotelRepository.saveAll(List.of(h1, h2));

        List<Hotel> result = hotelRepository.searchHotels(null, "Hilton", null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBrand()).isEqualTo("Hilton");
    }

    @Test
    void countHotelsByBrand_shouldReturnCorrectCounts() {
        Hotel h1 = new Hotel();
        h1.setName("Hotel A");
        h1.setBrand("Hilton");
        h1.setDescription("desc1");

        Hotel h2 = new Hotel();
        h2.setName("Hotel B");
        h2.setBrand("Hilton");
        h2.setDescription("desc2");

        Hotel h3 = new Hotel();
        h3.setName("Hotel C");
        h3.setBrand("Marriott");
        h3.setDescription("desc3");

        hotelRepository.saveAll(List.of(h1, h2, h3));

        List<Object[]> rows = hotelRepository.countHotelsByBrand();

        assertThat(rows).isNotEmpty();
        long hiltonCount = rows.stream()
                .filter(r -> "Hilton".equals(r[0]))
                .map(r -> ((Number) r[1]).longValue())
                .findFirst()
                .orElse(0L);

        long marriottCount = rows.stream()
                .filter(r -> "Marriott".equals(r[0]))
                .map(r -> ((Number) r[1]).longValue())
                .findFirst()
                .orElse(0L);

        assertThat(hiltonCount).isEqualTo(2L);
        assertThat(marriottCount).isEqualTo(1L);
    }
}