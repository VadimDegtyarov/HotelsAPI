package com.learn.hotelsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HotelRequestDTO(
        @Schema(example = "DoubleTree by Hilton Minsk")
        @NotBlank String name,
        @Schema(example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in " +
                "the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
        String description,
        @NotBlank @Schema(example = "Hilton") String brand,
        @Valid @NotNull(message = "Адрес должен быть указан") AddressDTO address,
        @Valid @NotNull(message = "Контакты должны быть указаны") ContactsDTO contacts,
        @Valid @NotNull(message = "Время должно быть указано") ArrivalTimeDTO arrivalTime
) {
}

