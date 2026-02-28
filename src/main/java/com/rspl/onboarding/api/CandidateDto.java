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

    // ✅ NEW (for HR copy/share)
    private String onboardingToken;
    private String tokenExpiry;   // keep as String to avoid timezone/serialization issues
    private String portalLink;

    /**
     * Backward compatible method (kept).
     * Note: portalLink will be null when baseUrl isn't provided.
     */
    public static CandidateDto from(Candidate c) {
        return from(c, null);
    }

    /**
     * ✅ Use this for HR APIs where you want portalLink to be available.
     */
    public static CandidateDto from(Candidate c, String baseUrl) {
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
                ? c.getCreatedAt().toLocalDate().toString()
                : null;

        // ✅ NEW fields from Candidate (token-based onboarding)
        dto.onboardingToken = c.getOnboardingToken(); // Candidate has onboardingToken [1](https://rsplgroups-my.sharepoint.com/personal/harsh_sharma_rsplgroup_com/Documents/Microsoft%20Copilot%20Chat%20Files/Candidate%20(1).java).java)
        dto.java).java)
        dto.tokenExpiry     = (c.getTokenExpiry() != null) ? c.getTokenExpiry().toString() : null; // Candidate has tokenExpiry [1](https://rsplgroups-my.sharepoint.com/personal/harsh_sharma_rsplgroup_com/Documents/Microsoft%20Copilot%20Chat%20Files/Candidate%20(1).java).java).java)

        // ✅ Computed portal link (safe for HR copy/share)
        if (baseUrl != null && !baseUrl && !baseUrl.isBlank() && dto.onboardingToken != null && !dto.onboardingToken.isBlank()) {
            dto.portalLink = baseUrl + "/onboarding.html?token=" + dto.onboardingToken;
        } else {
            dto.portalLink = null;
        }

        return dto;
return dto;
    }

    public Long getId() { return id; }
    public String getEmployeeName() { return employeeName; }
me; }
    public String getEmailId() { return emailId; }
    public String getc String getMobileNo() { return mobileNo; }
    public String getAadhaarNo() { return aadhaaradhaarNo; }
    public String getDesignation() { return designationnation; }
    public Map<String, Object> getAssignedHR() { return assignedHRn assignedHR; }
    public String getJoiningStatus() { return joiningStatus; }
    public String getLinkStatus() { return linkn linkStatus; }
    public String getRejectionReason() { return rejectionReason;eason; }
    public String getInitiatedBy() { return initiatedBy; }
By; }
    public String getCreatedAt() { return createdAt; }

    // ✅ NEW getters
    ✅ NEW getters
    public String getOnboardingToken() { return onboardingToken; }
    public String getTokenExpiry() { return tokenExpiry; }
    public String getPortalLink() { return portalLink; }
}
