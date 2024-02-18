package by.eugenekulik.in.rest;

import by.eugenekulik.dto.*;
import by.eugenekulik.service.AgreementService;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.service.UserService;
import by.eugenekulik.service.annotation.Auditable;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * MetersDataController handles HTTP requests related to meters data.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json")
public class MetersDataController {
    private final MetersDataService metersDataService;
    private final AgreementService agreementService;
    private final UserService userService;

    /**
     * Retrieves a page of meters data for administrators.
     *
     * @param pageable The pagination information.
     * @return ResponseEntity containing a page of meters data.
     */
    @GetMapping("/meters-data")
    @Auditable
    @RolesAllowed("ADMIN")
    public ResponseEntity<Iterable<MetersDataResponseDto>> getPage(@PageableDefault Pageable pageable){
        return ResponseEntity
            .ok(metersDataService.getPage(pageable));
    }

    /**
     * Retrieves the last meters data for a specific agreement.
     *
     * @param type        The type of meters data.
     * @param agreementId The ID of the agreement.
     * @return ResponseEntity containing the last meters data for the specified agreement.
     */
    @GetMapping("/user/meters-data/last")
    @Auditable
    public ResponseEntity<MetersDataResponseDto> getLast(@RequestParam String type,
                                                        @RequestParam Long agreementId) {
        AgreementResponseDto agreementResponseDto = agreementService.findById(agreementId);
        if (!userService.currentUser().id().equals(agreementResponseDto.userId())) {
            throw new AccessDeniedException("not allowed for this user");
        }
        return ResponseEntity
            .ok(metersDataService.findLastByAgreementAndType(agreementId, type));
    }

    /**
     * Retrieves a page of meters data for a specific user and agreement.
     *
     * @param pageable    The pagination information.
     * @param type        The type of meters data.
     * @param agreementId The ID of the agreement.
     * @return ResponseEntity containing a page of meters data for the specified user and agreement.
     */
    @GetMapping("/user/meters-data")
    @Auditable
    public ResponseEntity<Iterable<MetersDataResponseDto>> getUserMeters(@PageableDefault Pageable pageable,
                                                                         @RequestParam String type,
                                                                         @RequestParam Long agreementId) {
        AgreementResponseDto agreementResponseDto = agreementService.findById(agreementId);
        if (!userService.currentUser().id().equals(agreementResponseDto.userId())) {
            throw new AccessDeniedException("not allowed for this user");
        }
        return ResponseEntity
            .ok(metersDataService.findByAgreementAndType(agreementId, type, pageable));
    }

    /**
     * Creates new meters data.
     *
     * @param metersDataRequestDto The DTO containing information for creating meters data.
     * @return ResponseEntity containing the created meters data.
     */
    @PostMapping("/meters-data")
    @Auditable
    public ResponseEntity<MetersDataResponseDto> create(@Valid @RequestBody MetersDataRequestDto metersDataRequestDto){
        AgreementResponseDto agreement = agreementService.findById(metersDataRequestDto.agreementId());
        if(!agreement.userId().equals(userService.currentUser().id())) {
            throw new AccessDeniedException("not allowed for this user");
        }
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(metersDataService.create(metersDataRequestDto));
    }
}
