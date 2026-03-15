package com.learn.hotelsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactsDTO(
        @NotBlank @Schema(example = "+375 17 309-80-00") String phone,
        @NotBlank @Schema(example = "doubletreeminsk.info@hilton.com") @Email String email
) {
}
