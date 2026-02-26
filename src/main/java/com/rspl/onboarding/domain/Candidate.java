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

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mobile_no", nullable = false)
    private String mobileNo;

    @Column(name = "designation")
    private String designation;

    @Column(name = "aadhaar_no")
    private String aadhaarNo;

    @Column(name = "assigned_hr")
    private String assignedHr;

    @Column(name = "status")
    private String status = "PENDING";

    @Column(name = "onboarding_token", unique = true)
    private String onboardingToken;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ─── Getters & Setters ───────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNo() { return mobileNo; }
    public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getAadhaarNo() { return aadhaarNo; }
    public void setAadhaarNo(String aadhaarNo) { this.aadhaarNo = aadhaarNo; }

    public String getAssignedHr() { return assignedHr; }
    public void setAssignedHr(String assignedHr) { this.assignedHr = assignedHr; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOnboardingToken() { return onboardingToken; }
    public void setOnboardingToken(String onboardingToken) { this.onboardingToken = onboardingToken; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
