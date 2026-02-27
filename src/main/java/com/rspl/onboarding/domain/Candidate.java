package com.rspl.onboarding.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "initiated_by")
    private String initiatedBy;

    // ── Personal Details ─────────────────────────────
    @Column(name = "fathers_name")
    private String fathersName;

    @Column(name = "dob")
    private String dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "higher_education")
    private String higherEducation;

    @Column(name = "pan_card_no")
    private String panCardNo;

    // ── Address ──────────────────────────────────────
    @Column(name = "permanent_address", length = 500)
    private String permanentAddress;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "pin_code")
    private String pinCode;

    // ── Bank Details ─────────────────────────────────
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_no")
    private String bankAccountNo;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "branch_name")
    private String branchName;

    // ── Nominee ──────────────────────────────────────
    @Column(name = "nominee_name")
    private String nomineeName;

    @Column(name = "nominee_relation")
    private String nomineeRelation;

    @Column(name = "nominee_dob")
    private String nomineeDob;

    @Column(name = "nominee_address", length = 500)
    private String nomineeAddress;

    // ── Timestamps ───────────────────────────────────
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

    public LocalDateTime getTokenExpiry() { return tokenExpiry; }
    public void setTokenExpiry(LocalDateTime v) { this.tokenExpiry = v; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String v) { this.employeeId = v; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String v) { this.initiatedBy = v; }

    public String getFathersName() { return fathersName; }
    public void setFathersName(String v) { this.fathersName = v; }

    public String getDob() { return dob; }
    public void setDob(String v) { this.dob = v; }

    public String getGender() { return gender; }
    public void setGender(String v) { this.gender = v; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String v) { this.maritalStatus = v; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String v) { this.bloodGroup = v; }

    public String getHigherEducation() { return higherEducation; }
    public void setHigherEducation(String v) { this.higherEducation = v; }

    public String getPanCardNo() { return panCardNo; }
    public void setPanCardNo(String v) { this.panCardNo = v; }

    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String v) { this.permanentAddress = v; }

    public String getDistrict() { return district; }
    public void setDistrict(String v) { this.district = v; }

    public String getState() { return state; }
    public void setState(String v) { this.state = v; }

    public String getPinCode() { return pinCode; }
    public void setPinCode(String v) { this.pinCode = v; }

    public String getBankName() { return bankName; }
    public void setBankName(String v) { this.bankName = v; }

    public String getBankAccountNo() { return bankAccountNo; }
    public void setBankAccountNo(String v) { this.bankAccountNo = v; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String v) { this.ifscCode = v; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String v) { this.branchName = v; }

    public String getNomineeName() { return nomineeName; }
    public void setNomineeName(String v) { this.nomineeName = v; }

    public String getNomineeRelation() { return nomineeRelation; }
    public void setNomineeRelation(String v) { this.nomineeRelation = v; }

    public String getNomineeDob() { return nomineeDob; }
    public void setNomineeDob(String v) { this.nomineeDob = v; }

    public String getNomineeAddress() { return nomineeAddress; }
    public void setNomineeAddress(String v) { this.nomineeAddress = v; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime v) { this.createdAt = v; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }

    public Map<String, Object> getAssignedHR() {
        Map<String, Object> hr = new HashMap<>();
        hr.put("id",   assignedHRId   != null ? assignedHRId   : 0);
        hr.put("name", assignedHRName != null ? assignedHRName : "-");
        return hr;
    }
}
