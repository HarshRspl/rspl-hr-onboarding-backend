package com.rspl.onboarding.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "aadhaar_no")
    private String aadhaarNo;

    @Column(name = "designation")
    private String designation;

    @Column(name = "assigned_hr_id")
    private Long assignedHRId;

    @Column(name = "assigned_hr_name")
    private String assignedHRName;

    @Column(name = "joining_status")
    private String joiningStatus = "INITIATED";

    @Column(name = "link_status")
    private String linkStatus = "NOT_SENT";

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "onboarding_token", unique = true)
    private String onboardingToken;

    @Column(name = "initiated_by")
    private String initiatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ── Getters & Setters ────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String v) { this.employeeName = v; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String v) { this.emailId = v; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String v) { this.mobileNo = v; }

    public String getAadhaarNo() { return aadhaarNo; }
    public void setAadhaarNo(String v) { this.aadhaarNo = v; }

    public String getDesignation() { return designation; }
    public void setDesignation(String v) { this.designation = v; }

    public Long getAssignedHRId() { return assignedHRId; }
    public void setAssignedHRId(Long v) { this.assignedHRId = v; }

    public String getAssignedHRName() { return assignedHRName; }
    public void setAssignedHRName(String v) { this.assignedHRName = v; }

    public String getJoiningStatus() { return joiningStatus; }
    public void setJoiningStatus(String v) { this.joiningStatus = v; }

    public String getLinkStatus() { return linkStatus; }
    public void setLinkStatus(String v) { this.linkStatus = v; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String v) { this.rejectionReason = v; }

    public String getOnboardingToken() { return onboardingToken; }
    public void setOnboardingToken(String v) { this.onboardingToken = v; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String v) { this.initiatedBy = v; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }

    public java.util.Map<String, Object> getAssignedHR() {
        java.util.Map<String, Object> hr = new java.util.HashMap<>();
        hr.put("id",   assignedHRId   != null ? assignedHRId   : 0);
        hr.put("name", assignedHRName != null ? assignedHRName : "-");
        return hr;
    }
}
