package sk.majo.attendancesystem.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*@Column(name = "user_id")
    private int userId;*/

    @Column(name = "date_at")
    private Date dateAt;

    @Column(name = "arrival_at")
    private Date arrivalAt;

    @Column(name = "leave_at")
    private Date leaveAt;

    @Column(name = "lunch_from")
    private String lunchFrom;

    @Column(name = "lunch_to")
    private String lunchTo;

    @Column(name = "doctor_from")
    private String doctorFrom;

    @Column(name = "doctor_to")
    private String doctorTo;

    @Column(name = "business_from")
    private String businessFrom;

    @Column(name = "business_to")
    private String businessTo;

    @Column(name = "private_from")
    private String privateFrom;

    @Column(name = "private_to")
    private String privateTo;

    @Column(name = "vacation_from")
    private String vacationFrom;

    @Column(name = "vacation_to")
    private String vacationTo;

    @Column(name = "active")
    private Integer active =1;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Attendance() {
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

    public String getLunchFrom() {
        return lunchFrom;
    }

    public void setLunchFrom(String lunchFrom) {
        this.lunchFrom = lunchFrom;
    }

    public String getLunchTo() {
        return lunchTo;
    }

    public void setLunchTo(String lunchTo) {
        this.lunchTo = lunchTo;
    }

    public String getDoctorFrom() {
        return doctorFrom;
    }

    public void setDoctorFrom(String doctorFrom) {
        this.doctorFrom = doctorFrom;
    }

    public String getDoctorTo() {
        return doctorTo;
    }

    public void setDoctorTo(String doctorTo) {
        this.doctorTo = doctorTo;
    }

    public String getBusinessFrom() {
        return businessFrom;
    }

    public void setBusinessFrom(String businessFrom) {
        this.businessFrom = businessFrom;
    }

    public String getBusinessTo() {
        return businessTo;
    }

    public void setBusinessTo(String businessTo) {
        this.businessTo = businessTo;
    }

    public String getPrivateFrom() {
        return privateFrom;
    }

    public void setPrivateFrom(String privateFrom) {
        this.privateFrom = privateFrom;
    }

    public String getPrivateTo() {
        return privateTo;
    }

    public void setPrivateTo(String privateTo) {
        this.privateTo = privateTo;
    }

    public String getVacationFrom() {
        return vacationFrom;
    }

    public void setVacationFrom(String vacationFrom) {
        this.vacationFrom = vacationFrom;
    }

    public String getVacationTo() {
        return vacationTo;
    }

    public void setVacationTo(String vacationTo) {
        this.vacationTo = vacationTo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }
}
