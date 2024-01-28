package by.eugenekulik.service;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MetersDataService {

    private final MetersDataRepository metersDataRepository;


    public MetersDataService(MetersDataRepository metersDataRepository) {
        this.metersDataRepository = metersDataRepository;
    }

    public MetersData create(MetersData metersData){
        metersData.setPlacedAt(LocalDateTime.now());
        if(metersDataRepository.findByAgreementAndTypeAndMonth(metersData.getAgreementId(),
                                                                metersData.getMetersTypeId(),
                                                                metersData.getPlacedAt().toLocalDate()).isPresent())
            throw new IllegalArgumentException("The readings of this meter have " +
                "already been submitted this month");
        return metersDataRepository.save(metersData);
    }

    public MetersData findByAgreementAndTypeAndMonth(Long agreementId, Long metersTypeId, LocalDate localDate){
        return metersDataRepository.findByAgreementAndTypeAndMonth(agreementId, metersTypeId, localDate)
            .orElseThrow(()->new IllegalArgumentException("not fount metersData"));
    }

    public MetersData findLastByAgreementAndType(Long agreementId, Long metersTypeId){
        return metersDataRepository.findLastByAgreementAndType(agreementId, metersTypeId)
            .orElseThrow(()->new IllegalArgumentException("not found metersData"));
    }


    public List<MetersData> getPage(int page, int count) {
        return metersDataRepository.getPage(page, count);
    }
}
