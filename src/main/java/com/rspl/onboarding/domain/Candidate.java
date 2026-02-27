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

    // ===== Core =====
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

    // ===== Personal (Excel matched) =====
    @Column(name = "fathers_name")
    private String fathersName;

    @Column(name = "mothers_name")
    private String mothersName;

    @Column(name = "dob")
    private String dob; // yyyy-MM-dd from HTML date input

    @Column(name = "gender")
    private String gender;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "higher_education")
    private String higherEducation;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "pan_card_no")
    private String panCardNo;

    @Column(name = "uan_no")
    private String uanNo;

    @Column(name = "esi_no")
    private String esiNo;

    // ===== Address (Excel matched) =====
    @Column(name = "permanent_address", length = 500)
    private String permanentAddress;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "working_state")
    private String workingState;

    @Column(name = "hq_district")
    private String hqDistrict;

    // ===== Employment (Excel matched) =====
    @Column(name = "department")
    private String department;

    @Column(name = "date_of_joining")
    private String dateOfJoining; // yyyy-MM-dd

    // Previous employment block
    @Column(name = "prev_org_name")
    private String prevOrgName;

    @Column(name = "prev_org_location")
    private String prevOrgLocation;

    @Column(name = "prev_emp_from")
    private String prevEmpFrom; // yyyy-MM-dd

    @Column(name = "prev_emp_to")
    private String prevEmpTo; // yyyy-MM-dd

    @Column(name = "prev_nature_of_job")
    private String prevNatureOfJob;

    @Column(name = "prev_salary")
    private String prevSalary;

    @Column(name = "prev_reason_leaving", length = 300)
    private String prevReasonLeaving;

    // ===== Bank =====
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_account_no")
    private String bankAccountNo;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "branch_name")
    private String branchName;

    // ===== Nominee (Excel matched) =====
    @Column(name = "nominee_name")
    private String nomineeName;

    @Column(name = "nominee_gender")
    private String nomineeGender;

    @Column(name = "nominee_relation")
    private String nomineeRelation;

    @Column(name = "nominee_dob")
    private String nomineeDob;

    @Column(name = "nominee_address", length = 500)
    private String nomineeAddress;

    // ===== Timestamps =====
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ===== Getters & Setters =====
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

    public LocalDateTime getTokenExpiry() { return tokenExpiry; }
    public void setTokenExpiry(LocalDateTime tokenExpiry) { this.tokenExpiry = tokenExpiry; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }

    public String getFathersName() { return fathersName; }
    public void setFathersName(String fathersName) { this.fathersName = fathersName; }

    public String getMothersName() { return mothersName; }
    public void setMothersName(String mothersName) { this.mothersName = mothersName; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getHigherEducation() { return higherEducation; }
    public void setHigherEducation(String higherEducation) { this.higherEducation = higherEducation; }

    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }

    public String getPanCardNo() { return panCardNo; }
    public void setPanCardNo(String panCardNo) { this.panCardNo = panCardNo; }

    public String getUanNo() { return uanNo; }
    public void setUanNo(String uanNo) { this.uanNo = uanNo; }

    public String getEsiNo() { return esiNo; }
    public void setEsiNo(String esiNo) { this.esiNo = esiNo; }

    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPinCode() { return pinCode; }
    public void setPinCode(String pinCode) { this.pinCode = pinCode; }

    public String getWorkingState() { return workingState; }
    public void setWorkingState(String workingState) { this.workingState = workingState; }

    public String getHqDistrict() { return hqDistrict; }
    public void setHqDistrict(String hqDistrict) { this.hqDistrict = hqDistrict; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(String dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public String getPrevOrgName() { return prevOrgName; }
    public void setPrevOrgName(String prevOrgName) { this.prevOrgName = prevOrgName; }

    public String getPrevOrgLocation() { return prevOrgLocation; }
    public void setPrevOrgLocation(String prevOrgLocation) { this.prevOrgLocation = prevOrgLocation; }

    public String getPrevEmpFrom() { return prevEmpFrom; }
    public void setPrevEmpFrom(String prevEmpFrom) { this.prevEmpFrom = prevEmpFrom; }

    public String getPrevEmpTo() { return prevEmpTo; }
    public void setPrevEmpTo(String prevEmpTo) { this.prevEmpTo = prevEmpTo; }

    public String getPrevNatureOfJob() { return prevNatureOfJob; }
    public void setPrevNatureOfJob(String prevNatureOfJob) { this.prevNatureOfJob = prevNatureOfJob; }

    public String getPrevSalary() { return prevSalary; }
    public void setPrevSalary(String prevSalary) { this.prevSalary = prevSalary; }

    public String getPrevReasonLeaving() { return prevReasonLeaving; }
    public void setPrevReasonLeaving(String prevReasonLeaving) { this.prevReasonLeaving = prevReasonLeaving; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getBankAccountNo() { return bankAccountNo; }
    public void setBankAccountNo(String bankAccountNo) { this.bankAccountNo = bankAccountNo; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getNomineeName() { return nomineeName; }
    public void setNomineeName(String nomineeName) { this.nomineeName = nomineeName; }

    public String getNomineeGender() { return nomineeGender; }
    public void setNomineeGender(String nomineeGender) { this.nomineeGender = nomineeGender; }

    public String getNomineeRelation() { return nomineeRelation; }
    public void setNomineeRelation(String nomineeRelation) { this.nomineeRelation = nomineeRelation; }

    public String getNomineeDob() { return nomineeDob; }
    public void setNomineeDob(String nomineeDob) { this.nomineeDob = nomineeDob; }

    public String getNomineeAddress() { return nomineeAddress; }
    public void setNomineeAddress(String nomineeAddress) { this.nomineeAddress = nomineeAddress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Map<String, Object> getAssignedHR() {
        Map<String, Object> hr = new HashMap<>();
        hr.put("id", assignedHRId != null ? assignedHRId : 0);
        hr.put("name", assignedHRName != null ? assignedHRName : "-");
        return hr;
    }
}
