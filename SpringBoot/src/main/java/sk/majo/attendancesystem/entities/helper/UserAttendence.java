package sk.majo.attendancesystem.entities.helper;

import sk.majo.attendancesystem.entities.Attendance;
import sk.majo.attendancesystem.entities.User;

import java.util.List;

public class UserAttendence {

    private User user;
    private List<Attendance> attendance;

    public UserAttendence() {
    }

    public UserAttendence(User user, List<Attendance> attendance) {
        this.user = user;
        this.attendance = attendance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Attendance> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }
}
