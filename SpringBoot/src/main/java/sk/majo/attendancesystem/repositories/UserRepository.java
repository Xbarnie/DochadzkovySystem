package sk.majo.attendancesystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.majo.attendancesystem.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByUuid(UUID uuid);

    Optional<User> findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    Optional<User> findByFirstNameOrEmail(String username, String email);

    Optional<User> findById(int id);

    Boolean existsByEmail(String email);

    //User findById(int id);
}
