package sk.majo.attendancesystem.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "update_history")
public class UpdateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "edit_time")
    private Date editTime;

    @Column(name = "updated")
    private String updated;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "old_value")
    private String oldValue;

    @ManyToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    public UpdateHistory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }
}
