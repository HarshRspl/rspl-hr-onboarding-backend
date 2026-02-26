package com.rspl.onboarding.api;

import jakarta.validation.constraints.NotBlank;

public class InitiateCandidateRequest {

    @NotBlank
    private String employeeName;

    @NotBlank
    private String email;        // ← must match getEmail()

    @NotBlank
    private String mobileNo;

    private String designation;

    private String assignedHr;   // ← must match getAssignedHr()

    // ─── Getters & Setters ───────────────────────────
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getAssignedHr() { return assignedHr; }
    public void setAssignedHr(String assignedHr) { this.assignedHr = assignedHr; }
}
