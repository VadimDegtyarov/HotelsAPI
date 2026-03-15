package com.learn.hotelsapi.service;

import com.learn.hotelsapi.dto.*;
import com.learn.hotelsapi.enums.HistogramParam;
import com.learn.hotelsapi.exception.ResourceNotFoundException;
import com.learn.hotelsapi.model.Amenity;
import com.learn.hotelsapi.model.Address;
import com.learn.hotelsapi.model.Contacts;
import com.learn.hotelsapi.model.Hotel;
import com.learn.hotelsapi.repository.AmenityRepository;
import com.learn.hotelsapi.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void createHotel_shouldReturnShortDto() {
        Hotel saving = new Hotel();
        saving.setId(1L);
        saving.setName("Test Hotel");
        saving.setDescription("desc");
        Address address = new Address();
        address.setHouseNumber(9);
        address.setStreet("Pobediteley Avenue");
        address.setCity("Minsk");
        address.setCountry("Belarus");
        address.setPostCode("220004");
        saving.setAddress(address);
        Contacts contacts = new Contacts();
        contacts.setPhone("+375 17 309-80-00");
        saving.setContacts(contacts);

        when(hotelRepository.save(any(Hotel.class))).thenReturn(saving);

        var req = new HotelRequestDTO(
                "Test Hotel",
                "desc",
                "Hilton",
                new AddressDTO(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004"),
                new ContactsDTO("+375 17 309-80-00", "doubletreeminsk.info@hilton.com"),
                new ArrivalTimeDTO(LocalTime.of(14, 0), LocalTime.of(12, 0))
        );

        HotelShortResponseDTO result = hotelService.createHotel(req);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Test Hotel");

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void getHotels_shouldReturnShortDtos() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setDescription("desc");
        Address address = new Address();
        address.setHouseNumber(9);
        address.setStreet("Pobediteley Avenue");
        address.setCity("Minsk");
        address.setCountry("Belarus");
        address.setPostCode("220004");
        hotel.setAddress(address);
        Contacts contacts = new Contacts();
        contacts.setPhone("+375 17 309-80-00");
        hotel.setContacts(contacts);

        when(hotelRepository.findAll()).thenReturn(List.of(hotel));

        List<HotelShortResponseDTO> result = hotelService.getHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Test Hotel");
    }

    @Test
    void addAmenities_shouldSaveNewAmenity_whenNotExists() {
        Hotel hotel = new Hotel();
        hotel.setId(2L);
        when(hotelRepository.findById(2L)).thenReturn(Optional.of(hotel));

        Amenity existing = new Amenity();
        existing.setId(1L);
        existing.setName("free parking");
        when(amenityRepository.findByNameInIgnoreCase(any())).thenReturn(List.of(existing));

        hotelService.addAmenities(2L, Set.of("Free parking", "New Amenity"));

        verify(amenityRepository, times(1)).save(argThat(a -> "new amenity".equalsIgnoreCase(a.getName())));
    }

    @Test
    void findHotels_shouldLowercaseAmenitiesBeforeCallRepository() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setDescription("desc");
        Address address = new Address();
        address.setHouseNumber(9);
        address.setStreet("Pobediteley Avenue");
        address.setCity("Minsk");
        address.setCountry("Belarus");
        address.setPostCode("220004");
        hotel.setAddress(address);
        Contacts contacts = new Contacts();
        contacts.setPhone("+375 17 309-80-00");
        hotel.setContacts(contacts);

        when(hotelRepository.searchHotels(any(), any(), any(), any(), anyList()))
                .thenReturn(List.of(hotel));

        ArgumentCaptor<List<String>> amenitiesCaptor = ArgumentCaptor.forClass(List.class);

        List<HotelShortResponseDTO> result = hotelService.findHotels(
                "Test", "Hilton", "Minsk", "Belarus", List.of("Free WiFi", "Free Parking"));

        assertThat(result).hasSize(1);

        verify(hotelRepository).searchHotels(eq("Test"), eq("Hilton"), eq("Minsk"), eq("Belarus"), amenitiesCaptor.capture());
        List<String> passedAmenities = amenitiesCaptor.getValue();
        assertThat(passedAmenities).containsExactlyInAnyOrder("free wifi", "free parking");
    }

    @Test
    void getHistogram_shouldReturnMapForBrand() {
        when(hotelRepository.countHotelsByBrand())
                .thenReturn(List.of(new Object[]{"Hilton", 2L}, new Object[]{"Marriott", 1L}));

        Map<String, Long> result = hotelService.getHistogram(HistogramParam.BRAND);

        assertThat(result).hasSize(2);
        assertThat(result.get("Hilton")).isEqualTo(2L);
        assertThat(result.get("Marriott")).isEqualTo(1L);
    }

    @Test
    void getHotel_notFound_shouldThrow() {
        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> hotelService.getHotel(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}