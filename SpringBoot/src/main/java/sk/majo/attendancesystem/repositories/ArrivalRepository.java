package sk.majo.attendancesystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.majo.attendancesystem.entities.Arrival;

@Repository
public interface ArrivalRepository extends JpaRepository<Arrival, Integer> {
}
