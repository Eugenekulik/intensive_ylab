package by.eugenekulik.starter.audit.service;

import by.eugenekulik.starter.audit.model.AuditRecord;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultRecordStorage implements RecordStorage{
    @Override
    public void save(AuditRecord auditRecord) {
        log.info("Record: {}", auditRecord);
    }
}
