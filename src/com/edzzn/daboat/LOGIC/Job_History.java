package com.edzzn.daboat.LOGIC;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Job_History extends ObjetoTabla {
    private Integer employee_id;
    private LocalDateTime start_date;
    private LocalDateTime end_date;
    private String job_id;
    private Integer department_id;

    public Job_History(Integer employee_id, LocalDateTime start_date, LocalDateTime end_date, String job_id, Integer department_id) {
        this.setEmployee_id(employee_id);
        this.setStart_date(start_date);
        this.setEnd_date(end_date);
        this.setJob_id(job_id);
        this.setDepartment_id(department_id);
    }

    @Override
    public String toString() {
        return getEmployee_id() + "\t" + getStart_date() + "\t" + getEnd_date() + "\t" + getJob_id() + "\t" + getDepartment_id();
    }

    @Override
    public Object getObjectID() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s");
        String[] objectId = new String[2];
        objectId[0] = getEmployee_id().toString();
        objectId[1] = getStart_date().format(formatter);

        return objectId;
    }

    public Integer getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Integer employee_id) {
        this.employee_id = employee_id;
    }

    public LocalDateTime getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        this.start_date = start_date;
    }

    public LocalDateTime getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDateTime end_date) {
        this.end_date = end_date;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public Integer getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }
}
