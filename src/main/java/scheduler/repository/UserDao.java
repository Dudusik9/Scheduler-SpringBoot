package scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scheduler.model.User;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
