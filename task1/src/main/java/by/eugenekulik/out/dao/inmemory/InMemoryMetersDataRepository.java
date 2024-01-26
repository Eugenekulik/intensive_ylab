package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.utils.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryMetersDataRepository implements MetersDataRepository {


    private List<MetersData> metersData;
    private Sequence sequence;


    public InMemoryMetersDataRepository(Sequence sequence){
        metersData = new ArrayList<>();
        this.sequence = sequence;
    }


    @Override
    public Optional<MetersData> findById(Long id){
        int left = 0;
        int right = metersData.size() - 1;
        while(left < right-1){
            if(metersData.get((left + right)/2).getId().equals(id))
                return Optional.of(metersData.get((left + right)/2));
            if(metersData.get((left + right)/2).getId() < id)
                left = (left + right)/2;
            else
                right = (left + right)/2;
        }
        if(metersData.get(left).getId().equals(id)) return Optional.of(metersData.get(left));
        if(metersData.get(right).getId().equals(id)) return Optional.of(metersData.get(right));
        return Optional.empty();
    }






}
