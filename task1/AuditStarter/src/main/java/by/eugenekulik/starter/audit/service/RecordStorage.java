package by.eugenekulik.starter.audit.service;

import by.eugenekulik.starter.audit.model.AuditRecord;

@FunctionalInterface
public interface RecordStorage {

    void save(AuditRecord auditRecord);

}
