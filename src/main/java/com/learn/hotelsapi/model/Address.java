package com.learn.hotelsapi.model;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "house_number", nullable = false)
    private Integer houseNumber;
    @Column(name = "street", nullable = false, length = 168)
    private String street;
    @Column(name = "country", nullable = false, length = 56)
    private String country;
    @Column(name = "city", nullable = false, length = 100)
    private String city;
    @Column(name = "post_code", nullable = false)
    private String postCode;
    @OneToOne()
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Address address = new Address();



        public Builder houseNumber(Integer houseNumber) {
            address.houseNumber = houseNumber;
            return this;
        }

        public Builder street(String street) {
            address.street = street;
            return this;
        }

        public Builder city(String city) {
            address.city = city;
            return this;
        }

        public Builder country(String country) {
            address.country = country;
            return this;
        }

        public Builder postCode(String postCode) {
            address.postCode = postCode;
            return this;
        }

        public Builder hotel(Hotel hotel) {
            address.hotel = hotel;
            return this;
        }

        public Address build() {
            return address;
        }
    }

    public Address() {
    }

    public Address(Long id, Integer houseNumber, String street, String country, String postCode, Hotel hotel,String city) {
        this.id = id;
        this.houseNumber = houseNumber;
        this.street = street;
        this.country = country;
        this.postCode = postCode;
        this.city = city;
        this.hotel = hotel;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCountry() {
        return country;
    }

    public String getPostCode() {
        return postCode;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id) && Objects.equals(houseNumber, address.houseNumber) && Objects.equals(street, address.street) && Objects.equals(country, address.country) && Objects.equals(postCode, address.postCode) && Objects.equals(hotel, address.hotel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, houseNumber, street, country, postCode, hotel);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", houseNumber=" + houseNumber +
                ", street='" + street + '\'' +
                ", country='" + country + '\'' +
                ", postCode=" + postCode +
                ", hotel=" + hotel +
                '}';
    }
}
