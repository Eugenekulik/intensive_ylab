package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.MetersType;
import by.eugenekulik.out.dao.MetersTypeRepository;
import by.eugenekulik.utils.Sequence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryMetersTypeRepository implements MetersTypeRepository {

    private final List<MetersType> types;
    private final Sequence sequence;

    public InMemoryMetersTypeRepository(Sequence sequence) {
        this.types = new ArrayList<>();
        this.sequence = sequence;
    }


    @Override
    public Optional<MetersType> findById(Long id) {
        int left = 0;
        int right = types.size() - 1;
        while (left < right - 1) {
            if (types.get((left + right) / 2).getId().equals(id)) {
                return Optional.of(types.get((left + right) / 2));
            }
            if (types.get((left + right) / 2).getId() < id) {
                left = (left + right) / 2;
            } else {
                right = (left + right) / 2;
            }
        }
        if (types.get(left).getId().equals(id)) {
            return Optional.of(types.get(left));
        }
        if (types.get(right).getId().equals(id)) {
            return Optional.of(types.get(right));
        }
        return Optional.empty();
    }

    @Override
    public Optional<MetersType> findByName(String name) {
        return types.stream().filter(t -> t.getName().equals(name))
            .findAny();
    }

    @Override
    public MetersType save(MetersType metersType) {
        metersType.setId(sequence.next());
        types.add(metersType);
        return metersType;
    }

    @Override
    public List<MetersType> findAll() {
        return List.copyOf(types);
    }


}
