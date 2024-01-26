package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.utils.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryAddressRepository implements AddressRepository {


    private List<Address> addresses;
    private Sequence sequence;

    public InMemoryAddressRepository(Sequence sequence){
        addresses = new ArrayList<>();
        this.sequence = sequence;
    }




    @Override
    public Optional<Address> findById(Long id){
        int left = 0;
        int right = addresses.size() - 1;
        while(left < right-1){
            if(addresses.get((left + right)/2).getId().equals(id))
                return Optional.of(addresses.get((left + right)/2));
            if(addresses.get((left + right)/2).getId() < id)
                left = (left + right)/2;
            else
                right = (left + right)/2;
        }
        if(addresses.get(left).getId().equals(id)) return Optional.of(addresses.get(left));
        if(addresses.get(right).getId().equals(id)) return Optional.of(addresses.get(right));
        return Optional.empty();
    }
}
