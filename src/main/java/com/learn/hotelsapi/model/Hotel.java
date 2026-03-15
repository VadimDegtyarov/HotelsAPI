package com.learn.hotelsapi.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "brand", nullable = false, length = 50)
    private String brand;
    @Column(name = "description", nullable = false)
    private String description;
    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    private Address address;
    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    private Contacts contacts;
    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL)
    private ArrivalTime arrivalTime;
    @ManyToMany
    @JoinTable(name = "hotels_amenities",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id"))
    private Set<Amenity> amenitiesList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(Address address) {
        this.address = address;
        address.setHotel(this);
    }
    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
        contacts.setHotel(this);
    }
    public void setArrivalTime(ArrivalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
        arrivalTime.setHotel(this);
    }

    public ArrivalTime getArrivalTime() {
        return arrivalTime;
    }

    public Hotel() {

    }

    public Hotel(Long id,String description, String name, String brand, Address address, Contacts contacts, ArrivalTime arrivalTime, Set<Amenity> amenitiesList) {
        this.id = id;
        this.name = name;
        this.description=description;
        this.brand = brand;
        this.address = address;
        this.contacts = contacts;
        this.arrivalTime = arrivalTime;
        this.amenitiesList = amenitiesList;
    }

    public Address getAddress() {
        return address;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void addAmenity(Amenity amenity) {
        getAmenitiesList().add(amenity);
        amenity.getHotelList().add(this);
    }

    public Set<Amenity> getAmenitiesList() {
        if (amenitiesList == null) amenitiesList = new HashSet<>();
        return amenitiesList;
    }

    public void setAmenitiesList(Set<Amenity> amenitiesList) {
        this.amenitiesList = amenitiesList;
    }



    public Contacts getContacts() {
        return contacts;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", address=" + address +
                ", contacts=" + contacts +
                ", arrivalTime=" + arrivalTime +
                ", amenitiesList=" + amenitiesList +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Hotel hotel = new Hotel();

        public Builder name(String name) {
            hotel.name = name;
            return this;
        }

        public Builder brand(String brand) {
            hotel.brand = brand;
            return this;
        }

        public Builder address(Address address) {
            hotel.address = address;
            return this;
        }

        public Builder contacts(Contacts contacts) {
            hotel.contacts = contacts;
            return this;
        }

        public Builder arrivalTime(ArrivalTime arrivalTime) {
            hotel.arrivalTime = arrivalTime;
            return this;
        }

        public Builder description(String description) {
            hotel.description = description;
            return this;
        }
        public Builder amenitiesList(Set<Amenity> amenitiesList) {
            hotel.amenitiesList = amenitiesList;
            return this;
        }
        public Hotel build() {
            return hotel;
        }
    }
}
