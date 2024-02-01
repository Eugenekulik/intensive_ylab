package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.Address;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.utils.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class InMemoryAddressRepository implements AddressRepository {


    private final List<Address> addresses;
    private final Sequence sequence;

    public InMemoryAddressRepository(Sequence sequence) {
        addresses = new ArrayList<>();
        this.sequence = sequence;
    }


    @Override
    public Optional<Address> findById(Long id) {
        int left = 0;
        int right = addresses.size() - 1;
        while (left < right - 1) {
            if (addresses.get((left + right) / 2).getId().equals(id)) {
                return Optional.of(addresses.get((left + right) / 2));
            }
            if (addresses.get((left + right) / 2).getId() < id) {
                left = (left + right) / 2;
            } else {
                right = (left + right) / 2;
            }
        }
        if (addresses.get(left).getId().equals(id)) {
            return Optional.of(addresses.get(left));
        }
        if (addresses.get(right).getId().equals(id)) {
            return Optional.of(addresses.get(right));
        }
        return Optional.empty();
    }

    @Override
    public boolean isPresent(Address address) {
        return addresses.stream()
            .filter(a -> a.getRegion().equals(address.getRegion()))
            .filter(a -> a.getDistrict().equals(address.getDistrict()))
            .filter(a -> a.getCity().equals(address.getCity()))
            .filter(a -> a.getStreet().equals(address.getStreet()))
            .filter(a -> a.getHouseNumber().equals(address.getHouseNumber()))
            .anyMatch(a -> a.getApartmentNumber().equals(address.getApartmentNumber()));
    }

    @Override
    public Address save(Address address) {
        address.setId(sequence.next());
        addresses.add(address);
        return address;
    }

    @Override
    public List<Address> getPage(int page, int count) {
        return addresses.stream()
            .skip(count * page).limit(count).toList();
    }


}
