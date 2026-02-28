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

    // Extra fields for HR copy/share
    private String onboardingToken;
    private String tokenExpiry;
    private String portalLink;

    // Backward compatible
    public static CandidateDto from(Candidate c) {
        return from(c, null);
    }

    // Preferred for HR endpoints so portalLink is included
    public static CandidateDto from(Candidate c, String baseUrl) {
        CandidateDto dto = new CandidateDto();
        dto.id = c.getId();
        dto.employeeName = c.getEmployeeName();
        dto.emailId = c.getEmailId();
        dto.mobileNo = c.getMobileNo();
        dto.aadhaarNo = c.getAadhaarNo();
        dto.designation = c.getDesignation();
        dto.assignedHR = c.getAssignedHR();
        dto.joiningStatus = c.getJoiningStatus();
        dto.linkStatus = c.getLinkStatus();
        dto.rejectionReason = c.getRejectionReason();
        dto.initiatedBy = c.getInitiatedBy();
        dto.createdAt = (c.getCreatedAt() != null)
                ? c.getCreatedAt().toLocalDate().toString()
                : null;

        dto.onboardingToken = c.getOnboardingToken();
        dto.tokenExpiry = (c.getTokenExpiry() != null) ? c.getTokenExpiry().toString() : null;

        if (baseUrl != null && !baseUrl.isBlank()
                && dto.onboardingToken != null && !dto.onboardingToken.isBlank()) {
            dto.portalLink = baseUrl + "/onboarding.html?token=" + dto.onboardingToken;
        } else {
            dto.portalLink = null;
        }

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

    public String getOnboardingToken() { return onboardingToken; }
    public String getTokenExpiry() { return tokenExpiry; }
    public String getPortalLink() { return portalLink; }
}
