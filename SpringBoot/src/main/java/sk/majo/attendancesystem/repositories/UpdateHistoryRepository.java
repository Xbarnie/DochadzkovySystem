package sk.majo.attendancesystem.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sk.majo.attendancesystem.entities.UpdateHistory;

import java.util.List;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, Integer> {

    Page findByOrderByEditTimeDesc(Pageable pageable);
}
