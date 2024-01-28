package by.eugenekulik.service;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.out.dao.UserRepository;

import java.util.List;

public class AgreementService {

    private final AgreementRepository agreementRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AgreementService(AgreementRepository agreementRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.agreementRepository = agreementRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }


    public Agreement create(Agreement agreement){
        if(addressRepository.findById(agreement.getAddressId()).isEmpty())
            throw new IllegalArgumentException("not found address with id: " + agreement.getAddressId());
        if(userRepository.findById(agreement.getUserId()).isEmpty())
            throw new IllegalArgumentException("not found user with id: " + agreement.getUserId());
        if(!agreementRepository
            .findByUserIdAndAddressId(agreement.getUserId(), agreement.getAddressId()).isEmpty())
            throw new IllegalArgumentException("agreement with this user and address are already exist");
        return agreementRepository.save(agreement);
    }


    public List<Agreement> getPage(int page, int count) {
        return agreementRepository.getPage(page, count);
    }

    public List<Agreement> findByUser(Long id) {
        return agreementRepository.findByUserId(id);
    }

    public Agreement findById(Long agreementId) {
        return agreementRepository.findById(agreementId)
            .orElseThrow(()->new IllegalArgumentException("Not found agreement with id: " + agreementId));
    }
}
