package sk.majo.attendancesystem.entities.helper;

public class AttendanceOverviewSelector {

    private Integer employeeId;
    private String period;

    public AttendanceOverviewSelector(Integer employeeId, String period) {
        this.employeeId = employeeId;
        this.period = period;
    }

    public AttendanceOverviewSelector() {
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
