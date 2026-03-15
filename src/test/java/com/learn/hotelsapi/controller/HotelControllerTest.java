package com.learn.hotelsapi.controller;

import com.learn.hotelsapi.dto.*;
import com.learn.hotelsapi.enums.HistogramParam;
import com.learn.hotelsapi.exception.ResourceNotFoundException;
import com.learn.hotelsapi.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void getHotels_shouldReturnList() throws Exception {
        var dto = new HotelShortResponseDTO(
                1L,
                "DoubleTree by Hilton Minsk",
                "desc",
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                "+375 17 309-80-00"
        );

        when(hotelService.getHotels()).thenReturn(List.of(dto));

        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void createHotel_shouldReturnShortDto() throws Exception {
        var response = new HotelShortResponseDTO(
                1L,
                "DoubleTree by Hilton Minsk",
                "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                "+375 17 309-80-00"
        );

        when(hotelService.createHotel(any(HotelRequestDTO.class))).thenReturn(response);

        String body = """
                {
                  "name": "DoubleTree by Hilton Minsk",
                  "description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms...",
                  "brand": "Hilton",
                  "address": {
                    "houseNumber": 9,
                    "street": "Pobediteley Avenue",
                    "city": "Minsk",
                    "country": "Belarus",
                    "postCode": "220004"
                  },
                  "contacts": {
                    "phone": "+375 17 309-80-00",
                    "email": "doubletreeminsk.info@hilton.com"
                  },
                  "arrivalTime": {
                    "checkIn": "14:00",
                    "checkOut": "12:00"
                  }
                }
                """;

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("DoubleTree by Hilton Minsk"));
    }

    @Test
    void createHotel_invalidRequest_shouldReturn400() throws Exception {
        String invalidBody = "{}";

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHotel_shouldReturnFullDto() throws Exception {
        var address = new AddressDTO(9, "Pobediteley Avenue", "Minsk", "Belarus", "220004");
        var contacts = new ContactsDTO("+375 17 309-80-00", "doubletreeminsk.info@hilton.com");
        var arrival = new ArrivalTimeDTO(LocalTime.of(14, 0), LocalTime.of(12, 0));
        var response = new HotelResponseDTO(
                1L,
                "DoubleTree by Hilton Minsk",
                "desc",
                "Hilton",
                address,
                contacts,
                arrival,
                List.of("Free parking", "Free WiFi")
        );

        when(hotelService.getHotel(1L)).thenReturn(response);

        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amenities", hasSize(2)))
                .andExpect(jsonPath("$.amenities[0]").value("Free parking"));
    }

    @Test
    void getHotel_whenNotFound_shouldReturn404() throws Exception {
        when(hotelService.getHotel(999L)).thenThrow(new ResourceNotFoundException("отель c id 999 не найден"));

        mockMvc.perform(get("/property-view/hotels/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchHotels_shouldDelegateToService() throws Exception {
        var dto = new HotelShortResponseDTO(
                1L,
                "DoubleTree by Hilton Minsk",
                "desc",
                "9 Pobediteley Avenue, Minsk, 220004, Belarus",
                "+375 17 309-80-00"
        );

        when(hotelService.findHotels(any(), any(), any(), any(), anyList())).thenReturn(List.of(dto));

        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk")
                        .param("brand", "Hilton")
                        .param("amenities", "Free parking", "Free WiFi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("DoubleTree by Hilton Minsk"));
    }

    @Test
    void addAmenities_shouldCallServiceAndReturnOk() throws Exception {
        String body = """
                ["Free parking", "Free WiFi"]
                """;

        mockMvc.perform(post("/property-view/hotels/1/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(hotelService).addAmenities(eq(1L), any(Set.class));
    }

    @Test
    void getHistogram_shouldAcceptLowercaseParam() throws Exception {
        when(hotelService.getHistogram(HistogramParam.CITY)).thenReturn(Map.of("Minsk", 1L, "Moscow", 2L));

        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(1))
                .andExpect(jsonPath("$.Moscow").value(2));
    }
}