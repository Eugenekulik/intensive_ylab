package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.Rec;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * RecRepository allowed
 * for saving related to rec in the database using jdbcTemplate.
 * The class is annotated with @Repository, indicating that it may be managed
 * by spring framework container.
 *
 * @author Eugene Kulik
 * @see JdbcTemplate
 */
@Repository
public class RecRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void audit(Rec rec) {
        jdbcTemplate.update("""
                    INSERT INTO func.rec(id, message, time)
                    VALUES(?, ?, ?);
            """, rec.id(), rec.message(), rec.time());
    }


}
