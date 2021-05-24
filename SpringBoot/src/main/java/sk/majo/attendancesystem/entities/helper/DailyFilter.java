package sk.majo.attendancesystem.entities.helper;

import sk.majo.attendancesystem.entities.Department;

import java.util.Date;

public class DailyFilter {

    private Department department;
    private String arrivalFrom;
    private String arrivalTo;
    private String departureFrom;
    private String departureTo;

    public DailyFilter() {
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getArrivalFrom() {
        return arrivalFrom;
    }

    public void setArrivalFrom(String arrivalFrom) {
        this.arrivalFrom = arrivalFrom;
    }

    public String getArrivalTo() {
        return arrivalTo;
    }

    public void setArrivalTo(String arrivalTo) {
        this.arrivalTo = arrivalTo;
    }

    public String getDepartureFrom() {
        return departureFrom;
    }

    public void setDepartureFrom(String departureFrom) {
        this.departureFrom = departureFrom;
    }

    public String getDepartureTo() {
        return departureTo;
    }

    public void setDepartureTo(String departureTo) {
        this.departureTo = departureTo;
    }
}
