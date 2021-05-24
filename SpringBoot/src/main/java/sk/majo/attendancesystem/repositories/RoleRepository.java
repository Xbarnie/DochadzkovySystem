package sk.majo.attendancesystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.majo.attendancesystem.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findById(int id);
}
