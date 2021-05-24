package sk.majo.attendancesystem.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import sk.majo.attendancesystem.entities.Attendance;
import sk.majo.attendancesystem.entities.User;
import sk.majo.attendancesystem.entities.helper.UserAttendence;
import sk.majo.attendancesystem.repositories.AttendanceRepository;
import sk.majo.attendancesystem.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    final private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    public UserController(UserRepository userRepository, AttendanceRepository attendanceRepository) {
        this.userRepository = userRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {

        return userRepository.save(user);
        //return user;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public UserAttendence getUserById(@PathVariable int id) {
        // Get user by ID
        try {
            User user = userRepository.findById(id).orElse(null);
            // Get attendence by User
            if(user != null) {
             List<Attendance> attendances = attendanceRepository.findAllByUserOrderByDateAtDesc(user);
             UserAttendence userAttendence = new UserAttendence();
             userAttendence.setUser(user);
             userAttendence.setAttendance(attendances);
             return userAttendence;
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
