package by.eugenekulik.out.dao.jdbc.repository;

import by.eugenekulik.model.Rec;
import by.eugenekulik.out.dao.AddressRepository;
import by.eugenekulik.out.dao.jdbc.utils.JdbcTemplate;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RecRepository allowed
 * for saving related to rec in the database using jdbcTemplate.
 *
 * The class is annotated with @ApplicationScoped, indicating that it may be managed
 * by a CDI (Contexts and Dependency Injection) container.
 *
 *
 * @author Eugene Kulik
 * @see JdbcTemplate
 */
@ApplicationScoped
@NoArgsConstructor
@Slf4j
public class RecRepository {


    @Inject
    private JdbcTemplate jdbcTemplate;

    @Inject
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
