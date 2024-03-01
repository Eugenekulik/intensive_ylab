package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.starter.audit.model.AuditRecord;
import by.eugenekulik.starter.audit.service.RecordStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * JdbcRecordStorage allowed
 * for saving related to auditRecord in the database using jdbcTemplate.
 * The class is annotated with @Repository, indicating that it may be managed
 * by spring framework container.
 *
 * @author Eugene Kulik
 * @see JdbcTemplate
 */
@Repository
@RequiredArgsConstructor
public class JdbcRecordStorage implements RecordStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(AuditRecord auditRecord) {
        jdbcTemplate.update("""
                    INSERT INTO func.rec(id, message, time)
                    VALUES(?, ?, ?);
            """, auditRecord.getId(), auditRecord.getMessage(), auditRecord.getTime());
    }
}
