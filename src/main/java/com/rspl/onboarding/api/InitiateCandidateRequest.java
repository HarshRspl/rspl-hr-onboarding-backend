package com.rspl.onboarding.api;

import jakarta.validation.constraints.NotBlank;

public class InitiateCandidateRequest {

    @NotBlank
    private String employeeName;

    @NotBlank
    private String email;

    @NotBlank
    private String mobileNo;

    private String designation;

    private String assignedHr;

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String v) { this.employeeName = v; }

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }

    public String getDesignation() { return designation; }
    public void setDesignation(String v) { this.designation = v; }

    public String getAssignedHr() { return assignedHr; }
    public void setAssignedHr(String v) { this.assignedHr = v; }
}
