package com.rspl.onboarding.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "candidates")
public class Candidate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String employeeName;

  private String aadhaarNo;

  @Column(nullable = false)
  private String emailId;

  @Column(nullable = false)
  private String mobileNo;

  @Column(nullable = false)
  private String designation;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private JoiningStatus joiningStatus = JoiningStatus.INITIATED;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LinkStatus linkStatus = LinkStatus.NOT_SENT;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assigned_hr_id")
  private HrTeamMember assignedHR;

  private String initiatedBy;

  @Column(length = 1000)
  private String rejectionReason;

  @Column(nullable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  private String onboardingToken;
  private Instant onboardingTokenExpiresAt;

  public Candidate() {}

  @PrePersist
  void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = this.createdAt;
  }

  @PreUpdate
  void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
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
  public JoiningStatus getJoiningStatus() { return joiningStatus; }
  public void setJoiningStatus(JoiningStatus joiningStatus) { this.joiningStatus = joiningStatus; }
  public LinkStatus getLinkStatus() { return linkStatus; }
  public void setLinkStatus(LinkStatus linkStatus) { this.linkStatus = linkStatus; }
  public HrTeamMember getAssignedHR() { return assignedHR; }
  public void setAssignedHR(HrTeamMember assignedHR) { this.assignedHR = assignedHR; }
  public String getInitiatedBy() { return initiatedBy; }
  public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }
  public String getRejectionReason() { return rejectionReason; }
  public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
  public Instant getCreatedAt() { return createdAt; }
  public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
  public String getOnboardingToken() { return onboardingToken; }
  public void setOnboardingToken(String onboardingToken) { this.onboardingToken = onboardingToken; }
  public Instant getOnboardingTokenExpiresAt() { return onboardingTokenExpiresAt; }
  public void setOnboardingTokenExpiresAt(Instant onboardingTokenExpiresAt) { this.onboardingTokenExpiresAt = onboardingTokenExpiresAt; }
}
