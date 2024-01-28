package by.eugenekulik.service;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;

import java.util.List;

public class MetersTypeService {

    private final MetersTypeRepository metersTypeRepository;


    public MetersTypeService(MetersTypeRepository metersTypeRepository) {
        this.metersTypeRepository = metersTypeRepository;
    }

    public MetersType create(MetersType metersType){
        if(metersTypeRepository.findByName(metersType.getName()).isPresent())
            throw new IllegalArgumentException(
                "meters with name " + metersType.getName() + " are already exist");
        return metersTypeRepository.save(metersType);
    }


    public MetersType findByName(String name) {
        return metersTypeRepository.findByName(name)
            .orElseThrow(()-> new IllegalArgumentException("not found metersType with name: " + name));
    }

    public List<MetersType> findAll(){
        return metersTypeRepository.findAll();
    }
}
