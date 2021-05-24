package sk.majo.attendancesystem.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.majo.attendancesystem.entities.Attendance;
import sk.majo.attendancesystem.entities.UpdateHistory;
import sk.majo.attendancesystem.entities.User;
import sk.majo.attendancesystem.entities.helper.AttendanceFilter;
import sk.majo.attendancesystem.entities.helper.AttendanceOverviewSelector;
import sk.majo.attendancesystem.entities.helper.UserAttendence;
import sk.majo.attendancesystem.entities.helper.dom.AttendanceFilterDom;
import sk.majo.attendancesystem.repositories.AttendanceRepository;
import sk.majo.attendancesystem.repositories.UpdateHistoryRepository;
import sk.majo.attendancesystem.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/attendance")
public class AttendanceOverviewController {

    final private static Logger logger = LoggerFactory.getLogger(AttendanceOverviewController.class);

    private final AttendanceRepository attendanceRepository;
    private  final UserRepository userRepository;
    private final EntityManager entityManager;
    private final UpdateHistoryRepository updateHistoryRepository;

    public AttendanceOverviewController(AttendanceRepository attendanceRepository, UserRepository userRepository, EntityManager entityManager, UpdateHistoryRepository updateHistoryRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.updateHistoryRepository = updateHistoryRepository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Attendance> getAttendances() {
        logger.info("Get All attendance");
        return attendanceRepository.findByOrderByDateAtDesc();
    }

    @RequestMapping(value = "/overviewSelect", method = RequestMethod.POST)
    public List<Attendance> overviewSelect(@RequestBody AttendanceOverviewSelector aos) {
        logger.info("Select overview");
        String[] parts = aos.getPeriod().split("-");
        if(parts.length == 2 && aos.getEmployeeId() != null) {
            List<Attendance> attendances = attendanceRepository.findAllByUserAndYearAndMonth(aos.getEmployeeId(), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            return attendances;
        }
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAttendance(@PathVariable Integer id) {
        logger.info("Deleting " +id);
        try {
            // Get attendance
            Attendance attendance = attendanceRepository.findById(id).orElse(null);
            if(attendance != null) {
                attendance.setActive(0);
                attendanceRepository.save(attendance);
            }
            logger.info("Deleted:" +id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity editAttendance(@RequestBody Attendance attendance) {
        logger.info("EDIT");
        // Find Attendance
        try {
            Attendance findAttendance = attendanceRepository.findById(attendance.getId());
            if(findAttendance != null) {
                findAttendance.setArrivalAt(attendance.getArrivalAt());
                findAttendance.setLeaveAt(attendance.getLeaveAt());
                findAttendance.setLunchFrom(attendance.getLunchFrom());
                findAttendance.setLunchTo(attendance.getLunchTo());
                findAttendance.setVacationFrom(attendance.getVacationFrom());
                findAttendance.setVacationTo(attendance.getVacationTo());
                findAttendance.setBusinessFrom(attendance.getBusinessFrom());
                findAttendance.setBusinessTo(attendance.getBusinessTo());
                findAttendance.setPrivateFrom(attendance.getPrivateFrom());
                findAttendance.setPrivateTo(attendance.getPrivateTo());
                findAttendance.setDoctorFrom(attendance.getDoctorFrom());
                findAttendance.setDoctorTo(attendance.getDoctorTo());
                Attendance savedAttendance = attendanceRepository.save(findAttendance);
                if(savedAttendance != null) {
                    return new ResponseEntity<>(savedAttendance, HttpStatus.OK);
                } else {
                    return ResponseEntity.badRequest().body("Can't Save");
                }
            }
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Can't Save");
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public ResponseEntity<List<AttendanceFilterDom>> attendanceFilter(@RequestBody AttendanceFilter attendanceFilter) {

        logger.info("POST attendanceFilter");

        String sql = "SELECT attendance.*, dp.name AS department,";
        sql += "JSON_OBJECT('first_name', user.first_name, 'last_name', user.last_name) AS user ";
        sql += "FROM attendance INNER JOIN user ON user.id = attendance.user_id ";
        sql += "INNER JOIN department AS dp ON dp.id = user.department_id ";
        sql += "WHERE attendance.id > 0 ";

        if(attendanceFilter.getUserId() > 0) {
            sql += "AND attendance.user_id = "+attendanceFilter.getUserId()+" ";
        }

        if(attendanceFilter.getDepartmentId() > 0) {
            sql += "AND user.department_id = "+attendanceFilter.getDepartmentId()+" ";
        }

        if(attendanceFilter.getArrivalDate() != null && !attendanceFilter.getArrivalDate().isEmpty()) {
            sql += "AND date(attendance.arrival_at) >= '"+attendanceFilter.getArrivalDate()+"' ";
        }

        if(attendanceFilter.getLeaveDate() != null && !attendanceFilter.getLeaveDate().isEmpty()) {
            sql += "AND date(attendance.leave_at) <= '"+attendanceFilter.getLeaveDate()+"' ";
        }

        // if is set arrivedFrom
        if(attendanceFilter.getArrivalFrom() != null && !attendanceFilter.getArrivalFrom().isEmpty()) {
            sql += "AND hour(attendance.arrival_at) >= '"+attendanceFilter.getArrivalFrom().substring(0,2)+"' AND minute(attendance.arrival_at) >= '"+attendanceFilter.getArrivalFrom().substring(attendanceFilter.getArrivalFrom().length() -2)+"' ";
        }
        // if is set arrivedTo
        if(attendanceFilter.getArrivalTo() != null && !attendanceFilter.getArrivalTo().isEmpty()) {
            sql += "AND hour(attendance.arrival_at) <= '"+attendanceFilter.getArrivalTo().substring(0,2)+"' AND minute(attendance.arrival_at) <= '"+attendanceFilter.getArrivalTo().substring(attendanceFilter.getArrivalTo().length() -2)+"' ";
        }
        // if is set departureFrom
        if(attendanceFilter.getDepartureFrom() != null && !attendanceFilter.getDepartureFrom().isEmpty()) {
            sql += "AND hour(attendance.leave_at) <= '"+attendanceFilter.getDepartureFrom().substring(0,2)+"' AND minute(attendance.leave_at) <= '"+attendanceFilter.getDepartureFrom().substring(attendanceFilter.getDepartureFrom().length() -2)+"' ";
        }
        // if is set departureTo
        if(attendanceFilter.getDepartureTo() != null && !attendanceFilter.getDepartureTo().isEmpty()) {
            sql += "AND hour(attendance.leave_at) <= '"+attendanceFilter.getDepartureTo().substring(0,2)+"' AND minute(attendance.leave_at) <= '"+attendanceFilter.getDepartureTo().substring(attendanceFilter.getDepartureTo().length() -2)+"' ";
        }

        logger.info(sql);
        Query query = entityManager.createNativeQuery(sql, AttendanceFilterDom.class);
        List<AttendanceFilterDom> wps = new ArrayList<>();
        try {
            wps = (List<AttendanceFilterDom>) query.getResultList();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }



        return new ResponseEntity<>(wps, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getAttendanceForUser(@PathVariable int id) {
        try {
            // Find User
            User user = userRepository.findById(id).orElse(null);
            if(user != null) {
                user.setPassword(null);
                user.setUuid(null);
                // Get Attendance for User
                List<Attendance> attendances = attendanceRepository.findAllByUserOrderByDateAtDesc(user);
                UserAttendence userAttendence = new UserAttendence(user, attendances);
                return new ResponseEntity<>(userAttendence, HttpStatus.OK);
            }
        } catch ( Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<?> getAttendanceHistory(@RequestParam Integer page, @RequestParam Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page updateHistories = updateHistoryRepository.findByOrderByEditTimeDesc(pageable);
            return new ResponseEntity<>(updateHistories, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
