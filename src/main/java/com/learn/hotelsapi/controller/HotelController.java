package com.learn.hotelsapi.controller;

import com.learn.hotelsapi.dto.HotelRequestDTO;
import com.learn.hotelsapi.dto.HotelResponseDTO;
import com.learn.hotelsapi.dto.HotelShortResponseDTO;
import com.learn.hotelsapi.enums.HistogramParam;
import com.learn.hotelsapi.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Tag(name = "Hotel controller", description = "Контроллер для работы с отелями")
@RestController
@Validated
@RequestMapping("/property-view")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(summary = "Создание нового отеля")
    @PostMapping("/hotels")
    public ResponseEntity<HotelShortResponseDTO> createHotel(@RequestBody @Valid HotelRequestDTO hotelRequestDTO) {
        return ResponseEntity.ok(hotelService.createHotel(hotelRequestDTO));
    }

    @Operation(summary = "Получение информации по отелю",
            description = "Предоставляет расширенную информацию по конкретному отелю")
    @GetMapping("/hotels/{id}")
    public ResponseEntity<HotelResponseDTO> getHotel(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotel(id));

    }

    @Operation(summary = "Получение списка всех отелей",
            description = "Предоставляет краткую информацию по каждому отелю")
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelShortResponseDTO>> getHotels() {
        return ResponseEntity.ok(hotelService.getHotels());

    }

    @Operation(summary = "Добавление списка amenities к отелю")
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<Void> addAmenities(@PathVariable("id") Long id, @RequestBody @NotEmpty Set<@NotBlank String> amenities) {
        hotelService.addAmenities(id, amenities);
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @Operation(summary = "Поиск списка всех отелей по параметрам",
            description = "Предоставляет краткую информацию по каждому отелю, соответствующему одному из следующих параметров: name, brand, city, country, amenities ")
    @GetMapping("/search")
    public ResponseEntity<List<HotelShortResponseDTO>> search(@RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String brand,
                                                              @RequestParam(required = false) String city,
                                                              @RequestParam(required = false) String country,
                                                              @RequestParam(required = false) List<String> amenities
    ) {
        return ResponseEntity.ok(hotelService.findHotels(name, brand, city, country, amenities));

    }

    @Operation(summary = "Получение количества отелей сгруппированных по каждому значению указанного параметра",
            description = "Предоставляет количество отелей, сгруппированных по значению одного из следующих параметров: brand, city, country, amenities")
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable @Parameter(description = "Параметр группировки") HistogramParam param) {
        return ResponseEntity.ok(hotelService.getHistogram(param));
    }
}
