package com.edzzn.daboat.LOGIC;

public class Jobs extends ObjetoTabla {

    private String job_id;
    private String job_title;
    private Integer min_salary;
    private Integer max_salary;

    public Jobs(String job_id, String jobTitle, Integer min_salary, Integer max_salary) {
        this.setJob_id(job_id);
        this.setJob_title(jobTitle);
        this.setMin_salary(min_salary);
        this.setMax_salary(max_salary);
    }

    @Override
    public String toString() {
        return getJob_id() + "\t" + getJob_title() + "\t" + getMin_salary() + "\t" + getMax_salary();
    }

    @Override
    public Object getObjectID() {
        return getJob_id();
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public Integer getMin_salary() {
        return min_salary;
    }

    public void setMin_salary(Integer min_salary) {
        this.min_salary = min_salary;
    }

    public Integer getMax_salary() {
        return max_salary;
    }

    public void setMax_salary(Integer max_salary) {
        this.max_salary = max_salary;
    }
}

