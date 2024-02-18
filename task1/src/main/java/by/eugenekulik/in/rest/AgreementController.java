package by.eugenekulik.in.rest;

import by.eugenekulik.dto.AgreementRequestDto;
import by.eugenekulik.dto.AgreementResponseDto;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.annotation.Auditable;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AgreementController handles HTTP requests related to agreements.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/agreement", produces = "application/json")
public class AgreementController {
    private final AgreementService agreementService;

    /**
     * Retrieves a paginated list of agreements.
     *
     * @param pageable The pageable configuration for pagination.
     * @return ResponseEntity containing the paginated list of agreements.
     */
    @GetMapping
    @Auditable
    @RolesAllowed("ADMIN")
    public ResponseEntity<Iterable<AgreementResponseDto>> getPage(@PageableDefault Pageable pageable) {
        return ResponseEntity
            .ok(agreementService.getPage(pageable));
    }

    /**
     * Creates a new agreement.
     *
     * @param agreementRequestDto The DTO containing information for creating the agreement.
     * @return ResponseEntity containing the created agreement.
     */
    @PostMapping
    @Auditable
    @RolesAllowed("ADMIN")
    public ResponseEntity<AgreementResponseDto> create(@Valid @RequestBody AgreementRequestDto agreementRequestDto){
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(agreementService.create(agreementRequestDto));
    }
}
