package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.utils.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryAgreementRepository implements AgreementRepository {

    private List<Agreement> agreements;
    private Sequence sequence;


    public InMemoryAgreementRepository(Sequence sequence){
        agreements = new ArrayList<>();
        this.sequence = sequence;
    }


    @Override
    public Optional<Agreement> findById(Long id){
        int left = 0;
        int right = agreements.size() - 1;
        while(left < right-1){
            if(agreements.get((left + right)/2).getId().equals(id))
                return Optional.of(agreements.get((left + right)/2));
            if(agreements.get((left + right)/2).getId() < id)
                left = (left + right)/2;
            else
                right = (left + right)/2;
        }
        if(agreements.get(left).getId().equals(id)) return Optional.of(agreements.get(left));
        if(agreements.get(right).getId().equals(id)) return Optional.of(agreements.get(right));
        return Optional.empty();
    }



}
