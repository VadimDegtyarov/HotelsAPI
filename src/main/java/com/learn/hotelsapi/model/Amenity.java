package com.learn.hotelsapi.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "amenities")
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;
    @ManyToMany(mappedBy = "amenitiesList")
    private Set<Hotel> hotelList = new HashSet<>();

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Amenity amenity = new Amenity();

        public Builder name(String name) {
            amenity.name = name;
            return this;
        }
        public Builder hotelList(Set<Hotel> hotelList) {
            amenity.hotelList = hotelList;
            return this;
        }
        public Amenity build() {
            return amenity;
        }

    }

    public Amenity() {

    }

    public Amenity(Long id, String name, Set<Hotel> hotelList) {
        this.id = id;
        this.name = name;
        this.hotelList = hotelList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Hotel> getHotelList() {
        if (hotelList == null) hotelList = new HashSet<>();
        return hotelList;
    }

    public void setHotelList(Set<Hotel> hotelList) {
        this.hotelList = hotelList;
    }

    @Override
    public String toString() {
        return "Amenity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hotelList=" + hotelList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Amenity amenity = (Amenity) o;
        return Objects.equals(id, amenity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
