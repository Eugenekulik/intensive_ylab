package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.Agreement;
import by.eugenekulik.out.dao.AgreementRepository;
import by.eugenekulik.utils.Sequence;

import java.util.*;
import java.util.stream.IntStream;

public class InMemoryAgreementRepository implements AgreementRepository {

    private final List<Agreement> agreements;
    private final Sequence sequence;


    public InMemoryAgreementRepository(Sequence sequence) {
        agreements = new ArrayList<>();
        this.sequence = sequence;
    }


    @Override
    public Optional<Agreement> findById(Long id) {
        int left = 0;
        int right = agreements.size() - 1;
        while (left < right - 1) {
            if (agreements.get((left + right) / 2).getId().equals(id)) {
                return Optional.of(agreements.get((left + right) / 2));
            }
            if (agreements.get((left + right) / 2).getId() < id) {
                left = (left + right) / 2;
            } else {
                right = (left + right) / 2;
            }
        }
        if (agreements.get(left).getId().equals(id)) {
            return Optional.of(agreements.get(left));
        }
        if (agreements.get(right).getId().equals(id)) {
            return Optional.of(agreements.get(right));
        }
        return Optional.empty();
    }

    @Override
    public Agreement save(Agreement agreement) {
        agreement.setId(sequence.next());
        agreements.add(agreement);
        return agreement;
    }

    @Override
    public List<Agreement> findByUserIdAndAddressId(Long userId, Long addressId) {
        return agreements.stream()
            .filter(a -> a.getUserId().equals(userId))
            .filter(a -> a.getAddressId().equals(addressId))
            .toList();
    }

    @Override
    public List<Agreement> getPage(int page, int count) {
        return IntStream.range(1, agreements.size() + 1)
            .mapToObj(i -> agreements.get(agreements.size() - i))
            .skip(count * page).limit(count).toList();
    }

    @Override
    public List<Agreement> findByUserId(Long id) {
        return agreements.stream()
            .filter(agreement -> agreement.getUserId().equals(id))
            .toList();
    }


}
