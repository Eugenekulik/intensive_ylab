package by.eugenekulik.service;

import by.eugenekulik.dto.AddressDto;
import by.eugenekulik.out.dao.Pageable;

import java.util.List;

public interface AddressService {
    AddressDto create(AddressDto addressDto);

    List<AddressDto> getPage(Pageable pageable);

    AddressDto findById(long id);

    List<AddressDto> findByUser(Long userId, Pageable pageable);
}
