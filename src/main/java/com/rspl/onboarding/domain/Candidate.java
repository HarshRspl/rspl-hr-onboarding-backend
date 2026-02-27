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

    // ✅ FIXED: was "email" → now "email_id" to match frontend
    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_no")
    private String mobileNo;

    // ✅ ADDED: was missing
    @Column(name = "aadhaar_no")
    private String aadhaarNo;

    @Column(name = "designation")
    private String designation;

    // ✅ FIXED: was a String → now assignedHRId (Long) + assignedHRName (String)
    @Column(name = "assigned_hr_id")
    private Long assignedHRId;

    @Column(name = "assigned_hr_name")
    private String assignedHRName;

    // ✅ FIXED: was "status" → now "joining_status" to match frontend
    @Column(name = "joining_status")
    private String joiningStatus = "INITIATED";

    // ✅ ADDED: was missing entirely
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

    // ─── Getters & Setters ───────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getAadhaarNo() { return aadhaarNo; }
    public void setAadhaarNo(String aadhaarNo) { this.aadhaarNo = aadhaarNo; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public Long getAssignedHRId() { return assignedHRId; }
    public void setAssignedHRId(Long assignedHRId) { this.assignedHRId = assignedHRId; }

    public String getAssignedHRName() { return assignedHRName; }
    public void setAssignedHRName(String assignedHRName) { this.assignedHRName = assignedHRName; }

    public String getJoiningStatus() { return joiningStatus; }
    public void setJoiningStatus(String joiningStatus) { this.joiningStatus = joiningStatus; }

    public String getLinkStatus() { return linkStatus; }
    public void setLinkStatus(String linkStatus) { this.linkStatus = linkStatus; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getOnboardingToken() { return onboardingToken; }
    public void setOnboardingToken(String onboardingToken) { this.onboardingToken = onboardingToken; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ─── Helper: build assignedHR object for frontend JSON ───
    public java.util.Map<String, Object> getAssignedHR() {
        java.util.Map<String, Object> hr = new java.util.HashMap<>();
        hr.put("id", assignedHRId != null ? assignedHRId : 0);
        hr.put("name", assignedHRName != null ? assignedHRName : "-");
        return hr;
    }
}
