package com.angga.xphr.model.dto;

public class ReportDTO {
    private String employeeName;
    private String projectName;
    private Double totalHours;

    public ReportDTO(String employeeName, String projectName, Double totalHours) {
        this.employeeName = employeeName;
        this.projectName = projectName;
        this.totalHours = totalHours;
    }

    // Getter and Setter
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }
}
