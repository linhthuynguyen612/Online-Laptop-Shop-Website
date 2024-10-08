package vn.hoidanit.laptopshop.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.hoidanit.laptopshop.domain.User;
import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User x);
    void deleteById(long id);
    List<User> findOneByEmail(String email);
    List<User> findAll();
    User findById(long id);
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
