package by.eugenekulik.in.rest;

import by.eugenekulik.dto.MetersTypeRequestDto;
import by.eugenekulik.dto.MetersTypeResponseDto;
import by.eugenekulik.service.MetersTypeService;
import by.eugenekulik.service.annotation.Auditable;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @return ResponseEntity containing a list of all meters types.
     */
    @GetMapping
    @Auditable
    public ResponseEntity<Iterable<MetersTypeResponseDto>> getAll() {
        return ResponseEntity.ok(metersTypeService.findAll());
    }

    /**
     * Creates a new meters type.
     *
     * @param metersTypeRequestDto The DTO containing information for creating a meters type.
     * @return ResponseEntity containing the created meters type.
     */
    @PostMapping
    @Auditable
    @RolesAllowed("ADMIN")
    public ResponseEntity<MetersTypeResponseDto> create(@Valid @RequestBody MetersTypeRequestDto metersTypeRequestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(metersTypeService.create(metersTypeRequestDto));
    }
}
