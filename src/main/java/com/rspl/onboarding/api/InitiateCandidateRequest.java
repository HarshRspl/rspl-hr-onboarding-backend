package com.rspl.onboarding.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class InitiateCandidateRequest {

  @NotBlank
  private String employeeName;

  private String aadhaarNo;

  @NotBlank
  @Email
  private String emailId;

  @NotBlank
  @Pattern(regexp = "^\d{10}$", message = "mobileNo must be 10 digits")
  private String mobileNo;

  @NotBlank
  private String designation;

  private Integer assignedHRId;
  private String initiatedBy;
  private Boolean sendLinkImmediately = Boolean.TRUE;

  public String getEmployeeName() { return employeeName; }
  public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
  public String getAadhaarNo() { return aadhaarNo; }
  public void setAadhaarNo(String aadhaarNo) { this.aadhaarNo = aadhaarNo; }
  public String getEmailId() { return emailId; }
  public void setEmailId(String emailId) { this.emailId = emailId; }
  public String getMobileNo() { return mobileNo; }
  public void setMobileNo(String mobileNo) { this.mobileNo = mobileNo; }
  public String getDesignation() { return designation; }
  public void setDesignation(String designation) { this.designation = designation; }
  public Integer getAssignedHRId() { return assignedHRId; }
  public void setAssignedHRId(Integer assignedHRId) { this.assignedHRId = assignedHRId; }
  public String getInitiatedBy() { return initiatedBy; }
  public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }
  public Boolean getSendLinkImmediately() { return sendLinkImmediately; }
  public void setSendLinkImmediately(Boolean sendLinkImmediately) { this.sendLinkImmediately = sendLinkImmediately; }
}
