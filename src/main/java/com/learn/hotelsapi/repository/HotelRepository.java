package com.learn.hotelsapi.repository;

import com.learn.hotelsapi.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("""
            SELECT DISTINCT h
            FROM Hotel h
            LEFT JOIN h.address a
            LEFT JOIN h.amenitiesList am
            WHERE (:name IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:brand IS NULL OR LOWER(h.brand) LIKE LOWER(CONCAT('%', :brand, '%')))
            AND (:city IS NULL OR LOWER(a.city) Like LOWER(CONCAT('%', :city, '%')))
            AND (:country IS NULL OR LOWER(a.country) = LOWER(:country))
            AND (:amenities IS NULL OR LOWER(am.name) IN :amenities)
            """)
    List<Hotel> searchHotels(@Param("name") String name,
                             @Param("brand") String brand,
                             @Param("city") String city,
                             @Param("country") String country,
                             @Param("amenities") List<String> amenities);

    @Query("""
            SELECT h.brand, COUNT(h)
            FROM Hotel h
            GROUP BY h.brand
            """)
    List<Object[]> countHotelsByBrand();

    @Query("""
            SELECT a.city, COUNT(h)
            FROM Hotel h
            JOIN h.address a
            GROUP BY a.city
            """)
    List<Object[]> countHotelsByCity();

    @Query("""
            SELECT a.country, COUNT(h)
            FROM Hotel h
            JOIN h.address a
            GROUP BY a.country
            """)
    List<Object[]> countHotelsByCountry();

    @Query("""
            SELECT am.name, COUNT(h)
            FROM Hotel h
            JOIN h.amenitiesList am
            GROUP BY am.name
            """)
    List<Object[]> countHotelsByAmenity();
}
