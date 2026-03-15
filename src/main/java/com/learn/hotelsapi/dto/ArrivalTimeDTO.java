package com.learn.hotelsapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

public record ArrivalTimeDTO(
        @JsonFormat(pattern = "HH:mm") @Schema(example = "14:00")  LocalTime checkIn,
        @JsonFormat(pattern = "HH:mm") @Schema(example = "12:00")  LocalTime checkOut
) {
}
