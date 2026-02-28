package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;

public class CandidateDto {

    private Long   id;
    private String employeeName;
    private String emailId;
    private String mobileNo;
    private String aadhaarNo;
    private String designation;
    private HrInfo assignedHr;       // ✅ lowercase 'r' + typed object
    private String joiningStatus;
    private String linkStatus;
    private String rejectionReason;
    private String initiatedBy;
    private String createdAt;
    private String employeeId;       // ✅ needed for approve display
    private String onboardingToken;
    private String tokenExpiry;
    private String portalLink;

    // ✅ Nested HR info — frontend reads .assignedHr.name
    public static class HrInfo {
        private Long   id;
        private String name;
        private String role;

        public HrInfo(Long id, String name, String role) {
            this.id   = id;
            this.name = name;
            this.role = role;
        }
        public Long   getId()   { return id;   }
        public String getName() { return name; }
        public String getRole() { return role; }
    }

    public static CandidateDto from(Candidate c) {
        return from(c, null);
    }

    public static CandidateDto from(Candidate c, String baseUrl) {
        CandidateDto dto = new CandidateDto();
        dto.id             = c.getId();
        dto.employeeName   = c.getEmployeeName();
        dto.emailId        = c.getEmailId();
        dto.mobileNo       = c.getMobileNo();
        dto.aadhaarNo      = c.getAadhaarNo();
        dto.designation    = c.getDesignation();
        dto.joiningStatus  = c.getJoiningStatus();
        dto.linkStatus     = c.getLinkStatus();
        dto.rejectionReason= c.getRejectionReason();
        dto.initiatedBy    = c.getInitiatedBy();
        dto.employeeId     = c.getEmployeeId();
        dto.createdAt      = (c.getCreatedAt() != null)
                             ? c.getCreatedAt().toLocalDate().toString() : null;
        dto.onboardingToken= c.getOnboardingToken();
        dto.tokenExpiry    = (c.getTokenExpiry() != null)
                             ? c.getTokenExpiry().toString() : null;

        // ✅ Build nested HrInfo from stored HR fields
        if (c.getAssignedHRId() != null) {
            dto.assignedHr = new HrInfo(
                c.getAssignedHRId(),
                c.getAssignedHRName() != null ? c.getAssignedHRName() : "HR",
                null
            );
        }

        // ✅ Portal link
        if (baseUrl != null && !baseUrl.isBlank()
                && dto.onboardingToken != null && !dto.onboardingToken.isBlank()) {
            dto.portalLink = baseUrl + "/onboarding.html?token=" + dto.onboardingToken;
        }

        return dto;
    }

    // Getters
    public Long    getId()             { return id; }
    public String  getEmployeeName()   { return employeeName; }
    public String  getEmailId()        { return emailId; }
    public String  getMobileNo()       { return mobileNo; }
    public String  getAadhaarNo()      { return aadhaarNo; }
    public String  getDesignation()    { return designation; }
    public HrInfo  getAssignedHr()     { return assignedHr; }  // ✅ lowercase 'r'
    public String  getJoiningStatus()  { return joiningStatus; }
    public String  getLinkStatus()     { return linkStatus; }
    public String  getRejectionReason(){ return rejectionReason; }
    public String  getInitiatedBy()    { return initiatedBy; }
    public String  getCreatedAt()      { return createdAt; }
    public String  getEmployeeId()     { return employeeId; }
    public String  getOnboardingToken(){ return onboardingToken; }
    public String  getTokenExpiry()    { return tokenExpiry; }
    public String  getPortalLink()     { return portalLink; }
}
