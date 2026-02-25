package com.rspl.onboarding.api;

public class CandidateDto {
  private Long id;
  private String employeeName;
  private String emailId;
  private String mobileNo;
  private String aadhaarNo;
  private String designation;
  private String joiningStatus;
  private String linkStatus;
  private AssignedHrDto assignedHR;
  private String createdAt;
  private String rejectionReason;

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
  public String getJoiningStatus() { return joiningStatus; }
  public void setJoiningStatus(String joiningStatus) { this.joiningStatus = joiningStatus; }
  public String getLinkStatus() { return linkStatus; }
  public void setLinkStatus(String linkStatus) { this.linkStatus = linkStatus; }
  public AssignedHrDto getAssignedHR() { return assignedHR; }
  public void setAssignedHR(AssignedHrDto assignedHR) { this.assignedHR = assignedHR; }
  public String getCreatedAt() { return createdAt; }
  public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
  public String getRejectionReason() { return rejectionReason; }
  public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
