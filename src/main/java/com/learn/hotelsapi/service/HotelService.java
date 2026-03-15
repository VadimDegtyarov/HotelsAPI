package com.learn.hotelsapi.service;

import com.learn.hotelsapi.dto.*;
import com.learn.hotelsapi.enums.HistogramParam;
import com.learn.hotelsapi.exception.ResourceNotFoundException;
import com.learn.hotelsapi.model.*;
import com.learn.hotelsapi.repository.AmenityRepository;
import com.learn.hotelsapi.repository.HotelRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;

    public HotelService(HotelRepository hotelRepository, AmenityRepository amenityRepository) {
        this.hotelRepository = hotelRepository;
        this.amenityRepository = amenityRepository;
    }
    @Transactional
    public HotelShortResponseDTO createHotel(HotelRequestDTO requestHotelDTO) {
        ArrivalTime arrivalTime = ArrivalTime.builder().checkIn(requestHotelDTO.arrivalTime().checkIn())
                .checkOut(requestHotelDTO.arrivalTime().checkOut()).build();
        Contacts contacts = Contacts.builder()
                .email(requestHotelDTO.contacts().email())
                .phone(requestHotelDTO.contacts().phone())
                .build();
        Address address = Address.builder()
                .postCode(requestHotelDTO.address().postCode())
                .country(requestHotelDTO.address().country())
                .city(requestHotelDTO.address().city())
                .houseNumber(requestHotelDTO.address().houseNumber())
                .street(requestHotelDTO.address().street())
                .build();
        String description = requestHotelDTO.description() != null && !requestHotelDTO.description().isBlank()
                ? requestHotelDTO.description()
                : "";
        Hotel hotel = Hotel.builder()
                .name(requestHotelDTO.name())
                .description(description)
                .brand(requestHotelDTO.brand())
                .address(address)
                .arrivalTime(arrivalTime)
                .contacts(contacts)
                .build();
        hotel.setAddress(address);
        hotel.setArrivalTime(arrivalTime);
        hotel.setContacts(contacts);
        Hotel saved = hotelRepository.save(hotel);
        return new HotelShortResponseDTO(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                addressToString(saved.getAddress()),
                saved.getContacts().getPhone()

        );
    }

    private String addressToString(Address address) {
        return address.getHouseNumber() + " " + address.getStreet()
                + ", " + address.getCity() + ", " + address.getPostCode() + ", " + address.getCountry();
    }

    @Transactional
    public void addAmenities(Long hotelId, Set<String> amenities) {

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("отель c id %s не найден".formatted(hotelId)));

        Set<String> names = amenities.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<Amenity> existingAmenities = amenityRepository.findByNameInIgnoreCase(names);

        Set<String> existingNames = existingAmenities.stream()
                .map(a -> a.getName().toLowerCase())
                .collect(Collectors.toSet());

        for (Amenity amenity : existingAmenities) {
            hotel.addAmenity(amenity);
        }

        for (String name : names) {
            if (!existingNames.contains(name)) {
                Amenity newAmenity = Amenity.builder()
                        .name(name)
                        .build();

                amenityRepository.save(newAmenity);
                hotel.addAmenity(newAmenity);
            }
        }
    }

    public List<HotelShortResponseDTO> findHotels(String name, String brand, String city, String country, List<String> amenities) {
        List<String> lowerAmenities = amenities != null
                ? amenities.stream().map(String::toLowerCase).toList()
                : null;
        List<Hotel> hotels = hotelRepository.searchHotels(name, brand, city, country, lowerAmenities);
        return hotels.stream().map(el -> {
            return new HotelShortResponseDTO(
                    el.getId(),
                    el.getName(),
                    el.getDescription(),
                    addressToString(el.getAddress()),
                    el.getContacts().getPhone()

            );
        }).toList();
    }

    public HotelResponseDTO getHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("отель c id %s не найден".formatted(id)));
        AddressDTO addressDTO = new AddressDTO(hotel.getAddress().getHouseNumber(),
                hotel.getAddress().getStreet(), hotel.getAddress().getCity(),
                hotel.getAddress().getCountry(), hotel.getAddress().getPostCode());
        ContactsDTO contactsDTO = new ContactsDTO(hotel.getContacts().getPhone(),
                hotel.getContacts().getEmail());
        ArrivalTimeDTO arrivalTimeDTO = new ArrivalTimeDTO(hotel.getArrivalTime().getCheckIn(),
                hotel.getArrivalTime().getCheckOut());
        List<String> amenityNames = hotel.getAmenitiesList().stream()
                .map(Amenity::getName)
                .toList();
        String description = hotel.getDescription() != null ? hotel.getDescription() : "";
        return new HotelResponseDTO(hotel.getId(), hotel.getName(),
                description, hotel.getBrand(),
                addressDTO, contactsDTO, arrivalTimeDTO, amenityNames);

    }

    public List<HotelShortResponseDTO> getHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelShortResponseDTO> hotelShortResponseDTOS = new ArrayList<>();
        for (Hotel hotel : hotels) {
            HotelShortResponseDTO hotelShortResponseDTO = new HotelShortResponseDTO(hotel.getId(),
                    hotel.getName(), hotel.getDescription(), addressToString(hotel.getAddress()),
                    hotel.getContacts().getPhone());
            hotelShortResponseDTOS.add(hotelShortResponseDTO);
        }

        return hotelShortResponseDTOS;
    }

    public @Nullable Map<String, Long> getHistogram(HistogramParam param) {
        return switch (param) {
            case BRAND -> toMap(hotelRepository.countHotelsByBrand());
            case CITY -> toMap(hotelRepository.countHotelsByCity());
            case COUNTRY -> toMap(hotelRepository.countHotelsByCountry());
            case AMENITIES -> toMap(hotelRepository.countHotelsByAmenity());
        };
    }
    private Map<String, Long> toMap(List<Object[]> list) {
        return list.stream()
                .collect(Collectors.toMap(
                        o -> (String) o[0],
                        o -> ((Number) o[1]).longValue()
                ));
    }
}
