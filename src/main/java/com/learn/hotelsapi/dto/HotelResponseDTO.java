package com.learn.hotelsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(name = "Полная информация об отеле")
public record HotelResponseDTO(
        @NotNull Long id,
        @Schema(example = "DoubleTree by Hilton Minsk")
        @NotBlank String name,
        @Schema(example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in " +
                "the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
        String description,
        @Schema(example = "Hilton")
        @NotBlank String brand,
        @Valid @NotNull AddressDTO address,
        @Valid @NotNull ContactsDTO contacts,
        @Valid @NotNull ArrivalTimeDTO arrivalTime,
        @NotNull List<String> amenities
) {
}
