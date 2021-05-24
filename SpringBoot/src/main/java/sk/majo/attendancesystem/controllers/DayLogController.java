package sk.majo.attendancesystem.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sk.majo.attendancesystem.entities.Attendance;
import sk.majo.attendancesystem.entities.helper.DailyFilter;
import sk.majo.attendancesystem.entities.helper.dom.DailyFilterDom;
import sk.majo.attendancesystem.repositories.AttendanceRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/day-log")
public class DayLogController {

    final private static Logger logger = LoggerFactory.getLogger(DayLogController.class);

    private final AttendanceRepository attendanceRepository;
    private final EntityManager entityManager;

    public DayLogController(AttendanceRepository attendanceRepository, EntityManager entityManager) {
        this.attendanceRepository = attendanceRepository;
        this.entityManager = entityManager;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ResponseEntity<List<Attendance>> getTodayAttendence() {

        // Get all Attendence from today
        LocalDate now = LocalDate.now();
        String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
        List<Attendance> attendances = attendanceRepository.findAllByDatetime(currentDate);

        return new ResponseEntity<>(attendances, HttpStatus.OK);
    }

    @RequestMapping(path = "/filter", method = RequestMethod.POST)
    public ResponseEntity<List<DailyFilterDom>> filterDailyLog(@RequestBody DailyFilter filter) {
        logger.info("Get Filter");
        LocalDate now = LocalDate.now();
        String currentDate = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();

        String sql = "SELECT attendance.*, dp.name AS department,";
        sql += "JSON_OBJECT('first_name', user.first_name, 'last_name', user.last_name) AS user ";
        sql += "FROM attendance INNER JOIN user ON user.id = attendance.user_id ";
        sql += "INNER JOIN department AS dp ON dp.id = user.department_id ";
        sql += "WHERE date(attendance.date_at) = '"+currentDate+"' ";

        // If is set department ID
        if(filter.getDepartment() != null) {
            sql += "AND user.department_id = "+filter.getDepartment().getId()+" ";
        }
        // if is set arrivedFrom
        if(filter.getArrivalFrom() != null && !filter.getArrivalFrom().isEmpty()) {
            sql += "AND hour(attendance.arrival_at) >= '"+filter.getArrivalFrom().substring(0,2)+"' AND minute(attendance.arrival_at) >= '"+filter.getArrivalFrom().substring(filter.getArrivalFrom().length() -2)+"' ";
        }
        // if is set arrivedTo
        if(filter.getArrivalTo() != null && !filter.getArrivalTo().isEmpty()) {
            sql += "AND hour(attendance.arrival_at) <= '"+filter.getArrivalTo().substring(0,2)+"' AND minute(attendance.arrival_at) <= '"+filter.getArrivalTo().substring(filter.getArrivalTo().length() -2)+"' ";
        }
        // if is set departureFrom
        if(filter.getDepartureFrom() != null && !filter.getDepartureFrom().isEmpty()) {
            sql += "AND hour(attendance.leave_at) <= '"+filter.getDepartureFrom().substring(0,2)+"' AND minute(attendance.leave_at) <= '"+filter.getDepartureFrom().substring(filter.getDepartureFrom().length() -2)+"' ";
        }
        // if is set departureTo
        if(filter.getDepartureTo() != null && !filter.getDepartureTo().isEmpty()) {
            sql += "AND hour(attendance.leave_at) <= '"+filter.getDepartureTo().substring(0,2)+"' AND minute(attendance.leave_at) <= '"+filter.getDepartureTo().substring(filter.getDepartureTo().length() -2)+"' ";
        }

        Query query = entityManager.createNativeQuery(sql, DailyFilterDom.class);
        List<DailyFilterDom> wps = new ArrayList<>();
        try {
            wps = (List<DailyFilterDom>) query.getResultList();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }


        //List<Attendance> attendances = attendenceRepository.findAllByDepartment(3, new Date());

        return new ResponseEntity<>(wps, HttpStatus.OK);
    }

    /*private List<Attendence> getUsersToAttendence(List<Attendence> attendences) {

    }*/
}
