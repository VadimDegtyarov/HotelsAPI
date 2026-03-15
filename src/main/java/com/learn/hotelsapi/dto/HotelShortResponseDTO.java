package com.learn.hotelsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Краткая информация о отеле")
public record HotelShortResponseDTO(@NotNull Long id,
                                    @Schema(example = "DoubleTree by Hilton Minsk")
                                    @NotBlank String name,
                                    @Schema(example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in " +
                                            "the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...")
                                    @NotBlank String description,
                                    @Schema(example = "9 Pobediteley Avenue, Minsk, 220004, Belarus", accessMode = Schema.AccessMode.READ_ONLY)
                                    @NotBlank String address,
                                    @Schema(example = "+375 17 309-80-00")
                                    @NotBlank String phone) {
}
