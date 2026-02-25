package com.rspl.onboarding.api;

import jakarta.validation.constraints.NotBlank;

public class RejectRequest {
  @NotBlank
  private String reason;

  private String rejectedBy;

  public String getReason() { return reason; }
  public void setReason(String reason) { this.reason = reason; }
  public String getRejectedBy() { return rejectedBy; }
  public void setRejectedBy(String rejectedBy) { this.rejectedBy = rejectedBy; }
}
