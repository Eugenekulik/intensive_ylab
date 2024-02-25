package by.eugenekulik.in.rest.controller;

import by.eugenekulik.dto.MetersDataRequestDto;
import by.eugenekulik.dto.MetersDataResponseDto;
import by.eugenekulik.service.MetersDataService;
import by.eugenekulik.starter.audit.annotation.Auditable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * MetersDataController handles HTTP requests related to meters data.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/meters-data", produces = "application/json")
public class MetersDataController {
    private final MetersDataService metersDataService;

    /**
     * Retrieves a page of meters data for administrators.
     *
     * @param pageable The pagination information.
     * @return ResponseEntity containing a page of meters data.
     */
    @GetMapping
    @Auditable
    @Secured("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<MetersDataResponseDto> getPage(@PageableDefault Pageable pageable){
        return metersDataService.getPage(pageable);
    }

    /**
     * Retrieves the last meters data for a specific agreement.
     *
     * @param type        The type of meters data.
     * @param agreementId The ID of the agreement.
     * @return MetersDataDto containing the information of last meters
     * data for the specified agreement and type.
     */
    @GetMapping("/user/last")
    @Auditable
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@agreementService.isUserAgreement(#agreementId, @userService.currentUser().id())")
    public MetersDataResponseDto getLast(@RequestParam String type,
                                         @RequestParam Long agreementId) {
        return metersDataService.findLastByAgreementAndType(agreementId, type);
    }

    /**
     * Retrieves a page of meters data for a specific user and agreement.
     *
     * @param pageable    The pagination information.
     * @param type        The type of meters data.
     * @param agreementId The ID of the agreement.
     * @return Iterable of meters data for the specified agreement and type.
     */
    @GetMapping("/user")
    @Auditable
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@agreementService.isUserAgreement(#agreementId, @userService.currentUser().id())")
    public Iterable<MetersDataResponseDto> getUserMeters(@PageableDefault Pageable pageable,
                                                                         @RequestParam String type,
                                                                         @RequestParam Long agreementId) {
        return metersDataService.findByAgreementAndType(agreementId, type, pageable);
    }

    /**
     * Creates new meters data.
     *
     * @param metersDataRequestDto The DTO containing information for creating meters data.
     * @return MetersDataResponseDto containing the info of created meters data.
     */
    @PostMapping()
    @Auditable
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@agreementService.isUserAgreement(#metersDataRequestDto.agreementId(), " +
        "@userService.currentUser().id())")
    public MetersDataResponseDto create(@Valid @RequestBody MetersDataRequestDto metersDataRequestDto){
        return metersDataService.create(metersDataRequestDto);
    }
}
