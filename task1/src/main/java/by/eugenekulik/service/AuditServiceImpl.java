package by.eugenekulik.service;

import by.eugenekulik.model.Rec;

import java.util.ArrayList;
import java.util.List;

public class AuditServiceImpl implements AuditService {

    private List<Rec> storage = new ArrayList<>();

    @Override
    public void createRec(Rec rec) {
        storage.add(rec);
    }
}
