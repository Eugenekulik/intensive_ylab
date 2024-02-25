package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
import by.eugenekulik.service.MetersTypeService;
import by.eugenekulik.starter.audit.annotation.Auditable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * MetersTypeController handles HTTP requests related to meters types.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/meters-type", produces = "application/json")
public class MetersTypeController {
    private final MetersTypeService metersTypeService;

    /**
     * Retrieves all meters types.
     *
     * @return iterable of all meters types.
     */
    @GetMapping
    @Auditable
    @ResponseStatus(HttpStatus.OK)
    public Iterable<MetersTypeResponseDto> getAll() {
        return metersTypeService.findAll();
    }

    /**
     * Creates a new meters type.
     *
     * @param metersTypeRequestDto The DTO containing information for creating a meters type.
     * @return MetersTypeResponseDto containing the info of created meters type.
     */
    @PostMapping
    @Auditable
    @Secured("ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    public MetersTypeResponseDto create(@Valid @RequestBody MetersTypeRequestDto metersTypeRequestDto) {
        return metersTypeService.create(metersTypeRequestDto);
    }
}
