package com.learn.hotelsapi.model;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "arrival_time")
public class ArrivalTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "check_in", nullable = false)
    private LocalTime checkIn;
    @Column(name = "check_out")
    private LocalTime checkOut;
    @OneToOne()
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ArrivalTime arrivalTime = new ArrivalTime();

        public Builder checkIn(LocalTime checkIn) {
            arrivalTime.checkIn = checkIn;
            return this;
        }

        public Builder checkOut(LocalTime checkOut) {
            arrivalTime.checkOut = checkOut;
            return this;
        }
        public ArrivalTime build() {
            return arrivalTime;
        }
    }

    @Override
    public String toString() {
        return "ArrivalTime{" +
                "id=" + id +
                ", checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ArrivalTime that = (ArrivalTime) o;
        return Objects.equals(id, that.id) && Objects.equals(checkIn, that.checkIn) && Objects.equals(checkOut, that.checkOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, checkIn, checkOut);
    }

    public ArrivalTime() {

    }

    public ArrivalTime(Long id, LocalTime checkIn, LocalTime checkOut) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }
}
