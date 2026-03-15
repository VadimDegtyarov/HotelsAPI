package com.learn.hotelsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDTO(
        @NotNull @Schema(example = "9") Integer houseNumber,
        @NotBlank @Schema(example = "Pobediteley Avenue") String street,
        @NotBlank @Schema(example = "Minsk") String city,
        @NotBlank @Schema(example = "Belarus") String country,
        @NotBlank @Schema(example = "220004") String postCode
) {
}
