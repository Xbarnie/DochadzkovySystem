package sk.majo.attendancesystem.entities.helper.dom;

import sk.majo.attendancesystem.entities.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class AttendanceFilterDom {

    @Id
    @Column(name = "id")
    private int id;
    private Date dateAt;
    private Date arrivalAt;
    private Date leaveAt;
    private Date lunchAt;

    @OneToOne
    private User user;

    private String department;

    public AttendanceFilterDom() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateAt() {
        return dateAt;
    }

    public void setDateAt(Date dateAt) {
        this.dateAt = dateAt;
    }

    public Date getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(Date arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public Date getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(Date leaveAt) {
        this.leaveAt = leaveAt;
    }

    public Date getLunchAt() {
        return lunchAt;
    }

    public void setLunchAt(Date lunchAt) {
        this.lunchAt = lunchAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
