package com.learn.hotelsapi.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "contacts")
public class Contacts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "phone", nullable = false, unique = true, length = 19)
    private String phone;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @OneToOne()
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Contacts contacts = new Contacts();

        public Builder phone(String phone) {
            contacts.phone = phone;
            return this;
        }
        public Builder email(String email) {
            contacts.email = email;
            return this;
        }
        public Builder hotel(Hotel hotel) {
            contacts.hotel = hotel;
            return this;
        }
        public Contacts build() {
            return contacts;
        }
    }
    public Contacts() {
    }

    public Contacts(Long id, String phone, String email, Hotel hotel) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.hotel = hotel;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", hotel=" + hotel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Contacts contacts = (Contacts) o;
        return Objects.equals(id, contacts.id) && Objects.equals(phone, contacts.phone) && Objects.equals(email, contacts.email) && Objects.equals(hotel, contacts.hotel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phone, email, hotel);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Long getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Hotel getHotel() {
        return hotel;
    }
}
