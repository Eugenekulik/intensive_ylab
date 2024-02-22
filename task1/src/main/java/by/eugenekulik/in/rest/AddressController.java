package by.eugenekulik.in.rest;

import by.eugenekulik.dto.AddressRequestDto;
import by.eugenekulik.dto.AddressResponseDto;
import by.eugenekulik.service.AddressService;
import by.eugenekulik.service.annotation.Auditable;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * AddressController handles HTTP requests related to addresses.
 */
@RestController
@RequestMapping(value = "/address", produces = "application/json")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    /**
     * Retrieves a paginated list of addresses.
     *
     * @param pageable The pageable configuration for pagination.
     * @return Iterable of addresses.
     */
    @GetMapping
    @Auditable
    @RolesAllowed("ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<AddressResponseDto> getPage(@PageableDefault(sort = "id,asc") Pageable pageable){
        return addressService.getPage(pageable);
    }

    /**
     * Creates a new address.
     *
     * @param addressRequestDto The DTO containing information for creating the address.
     * @return AddressResponseDto containing information about created address.
     */
    @PostMapping
    @RolesAllowed("ADMIN")
    @Auditable
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponseDto create(@Valid @RequestBody AddressRequestDto addressRequestDto) {
        return addressService.create(addressRequestDto);
    }
}