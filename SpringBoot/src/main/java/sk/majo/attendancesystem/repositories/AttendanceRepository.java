package sk.majo.attendancesystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sk.majo.attendancesystem.entities.Attendance;
import sk.majo.attendancesystem.entities.User;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    @Query(value = "SELECT attendance.* FROM attendance WHERE date(attendance.date_at) = :date", nativeQuery = true)
    List<Attendance> findAllByDatetime(String date);

    @Query(value = "SELECT attendance.* FROM attendance WHERE date(attendance.date_at) = :date AND attendance.user_id = :userId", nativeQuery = true)
    List<Attendance> findAllByDateAndUserId(String date, Integer userId);

    @Query(value = "SELECT attendance.* FROM attendance WHERE date(attendance.date_at) = :date AND attendance.user_id = :userId", nativeQuery = true)
    Attendance findByDateAndUserId(String date, Integer userId);

    @Query(value = "SELECT at from Attendance as at where at.user.department.id = :department AND year(at.dateAt) = year(:date) AND month(at.dateAt) = month(:date) AND day(at.dateAt) = day(:date)")
    List<Attendance> findAllByDepartment(int department, Date date);

    @Query(value = "select at from Attendance at where at.user.department.id = :department AND at.arrivalAt >= :dateFrom AND at.arrivalAt <= :dateTo")
    List<Attendance> findAllByDepartmentAndArrivedDate(int department, Date dateFrom, Date dateTo);

    List<Attendance> findByOrderByDateAtDesc();

    List<Attendance> findAllByUserOrderByDateAtDesc(User user);

    @Query(value = "SELECT at FROM Attendance at WHERE at.user.id = :employeeId AND YEAR(at.dateAt) = :year AND MONTH(at.dateAt) = :month")
    List<Attendance> findAllByUserAndYearAndMonth(int employeeId, Integer year, Integer month);

    Attendance findById(int id);

    List<Attendance> findAllByUser(User user);

}
