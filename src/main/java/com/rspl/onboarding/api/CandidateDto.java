package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import java.time.LocalDateTime;

public class CandidateDto {

    private Long id;
    private String employeeName;
    private String email;
    private String mobileNo;
    private String designation;
    private String assignedHr;
    private String status;
    private String rejectionReason;
    private String onboardingToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ── Static factory: Candidate → CandidateDto ────
    public static CandidateDto from(Candidate c) {
        CandidateDto dto = new CandidateDto();
        dto.id = c.getId();
        dto.employeeName = c.getEmployeeName();
        dto.email = c.getEmail();
        dto.mobileNo = c.getMobileNo();
        dto.designation = c.getDesignation();
        dto.assignedHr = c.getAssignedHr();
        dto.status = c.getStatus();
        dto.rejectionReason = c.getRejectionReason();
        dto.onboardingToken = c.getOnboardingToken();
        dto.createdAt = c.getCreatedAt();
        dto.updatedAt = c.getUpdatedAt();
        return dto;
    }

    // ─── Getters ─────────────────────────────────────
    public Long getId() { return id; }
    public String getEmployeeName() { return employeeName; }
    public String getEmail() { return email; }
    public String getMobileNo() { return mobileNo; }
    public String getDesignation() { return designation; }
    public String getAssignedHr() { return assignedHr; }
    public String getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }
    public String getOnboardingToken() { return onboardingToken; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
