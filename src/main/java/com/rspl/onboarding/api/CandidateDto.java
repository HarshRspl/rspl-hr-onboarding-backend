package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import java.util.Map;

public class CandidateDto {

    private Long id;
    private String employeeName;
    private String emailId;
    private String mobileNo;
    private String aadhaarNo;
    private String designation;
    private Map<String, Object> assignedHR;
    private String joiningStatus;
    private String linkStatus;
    private String rejectionReason;
    private String initiatedBy;
    private String createdAt;

    public static CandidateDto from(Candidate c) {
        CandidateDto dto = new CandidateDto();
        dto.id              = c.getId();
        dto.employeeName    = c.getEmployeeName();
        dto.emailId         = c.getEmailId();
        dto.mobileNo        = c.getMobileNo();
        dto.aadhaarNo       = c.getAadhaarNo();
        dto.designation     = c.getDesignation();
        dto.assignedHR      = c.getAssignedHR();
        dto.joiningStatus   = c.getJoiningStatus();
        dto.linkStatus      = c.getLinkStatus();
        dto.rejectionReason = c.getRejectionReason();
        dto.initiatedBy     = c.getInitiatedBy();
        dto.createdAt       = c.getCreatedAt() != null
            ? c.getCreatedAt().toLocalDate().toString() : null;
        return dto;
    }

    public Long getId() { return id; }
    public String getEmployeeName() { return employeeName; }
    public String getEmailId() { return emailId; }
    public String getMobileNo() { return mobileNo; }
    public String getAadhaarNo() { return aadhaarNo; }
    public String getDesignation() { return designation; }
    public Map<String, Object> getAssignedHR() { return assignedHR; }
    public String getJoiningStatus() { return joiningStatus; }
    public String getLinkStatus() { return linkStatus; }
    public String getRejectionReason() { return rejectionReason; }
    public String getInitiatedBy() { return initiatedBy; }
    public String getCreatedAt() { return createdAt; }
}
