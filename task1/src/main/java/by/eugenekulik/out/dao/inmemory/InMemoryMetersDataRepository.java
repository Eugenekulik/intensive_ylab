package by.eugenekulik.out.dao.inmemory;

import by.eugenekulik.model.MetersData;
import by.eugenekulik.out.dao.MetersDataRepository;
import by.eugenekulik.utils.Sequence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class InMemoryMetersDataRepository implements MetersDataRepository {


    private final List<MetersData> data;
    private final Sequence sequence;


    public InMemoryMetersDataRepository(Sequence sequence){
        data = new ArrayList<>();
        this.sequence = sequence;
    }


    @Override
    public Optional<MetersData> findById(Long id){
        int left = 0;
        int right = data.size() - 1;
        while(left < right-1){
            if(data.get((left + right)/2).getId().equals(id))
                return Optional.of(data.get((left + right)/2));
            if(data.get((left + right)/2).getId() < id)
                left = (left + right)/2;
            else
                right = (left + right)/2;
        }
        if(data.get(left).getId().equals(id)) return Optional.of(data.get(left));
        if(data.get(right).getId().equals(id)) return Optional.of(data.get(right));
        return Optional.empty();
    }

    @Override
    public MetersData save(MetersData metersData) {
        metersData.setId(sequence.next());
        data.add(metersData);
        return metersData;
    }


    @Override
    public Optional<MetersData> findByAgreementAndTypeAndMonth(Long agreementId,
                                                               Long metersTypeId,
                                                               LocalDate placedAt) {
        return data.stream()
            .filter(m->m.getAgreementId().equals(agreementId))
            .filter(m->m.getMetersTypeId().equals(metersTypeId))
            .filter(m->m.getPlacedAt().getMonth().equals(placedAt.getMonth()))
            .filter(m->m.getPlacedAt().getYear() == placedAt.getYear())
            .findAny();
    }

    @Override
    public List<MetersData> getPage(int page, int count) {
        return IntStream.range(1,data.size()+1)
            .mapToObj(i->data.get(data.size() - i))
            .skip(count*page).limit(count).toList();
    }

    @Override
    public List<MetersData> getPageByAgreement(Long agreementId, int page, int count) {
        return IntStream.range(1,data.size()+1)
            .mapToObj(i->data.get(data.size() - i))
            .filter(m-> m.getAgreementId().equals(agreementId))
            .skip(count*page)
            .limit(count)
            .toList();
    }

    @Override
    public Optional<MetersData> findLastByAgreementAndType(Long agreementId, Long metersTypeId) {
        return IntStream.range(1,data.size()+1)
            .mapToObj(i->data.get(data.size() - i))
            .filter(m-> m.getAgreementId().equals(agreementId))
            .filter(m->m.getMetersTypeId().equals(metersTypeId))
            .findFirst();
    }


}
