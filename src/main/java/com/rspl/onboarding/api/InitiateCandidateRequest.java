package com.rspl.onboarding.api;

import jakarta.validation.constraints.NotBlank;

public class InitiateCandidateRequest {

    @NotBlank
    private String employeeName;

    // frontend sends "emailId" — we accept both
    private String email;
    private String emailId;

    @NotBlank
    private String mobileNo;

    @NotBlank
    private String designation;

    private String aadhaarNo;

    private String assignedHr;

    // frontend sends "assignedHRId" as integer
    private Integer assignedHRId;

    private String initiatedBy;

    private Boolean sendLinkImmediately;

    // ── Getters & Setters ────────────────────────────

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String v) { this.employeeName = v; }

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String v) { this.emailId = v; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }

    public String getDesignation() { return designation; }
    public void setDesignation(String v) { this.designation = v; }

    public String getAadhaarNo() { return aadhaarNo; }
    public void setAadhaarNo(String v) { this.aadhaarNo = v; }

    public String getAssignedHr() { return assignedHr; }
    public void setAssignedHr(String v) { this.assignedHr = v; }

    public Integer getAssignedHRId() { return assignedHRId; }
    public void setAssignedHRId(Integer v) { this.assignedHRId = v; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String v) { this.initiatedBy = v; }

    public Boolean getSendLinkImmediately() { return sendLinkImmediately; }
    public void setSendLinkImmediately(Boolean v) { this.sendLinkImmediately = v; }
}
