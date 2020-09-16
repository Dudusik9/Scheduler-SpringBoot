package scheduler.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import scheduler.model.Scripts;

@Repository
public interface ScriptDAO extends CrudRepository<Scripts, Long> {
}
