package com.rspl.onboarding.api;

import jakarta.validation.constraints.NotBlank;

public class RejectRequest {

    @NotBlank(message = "Rejection reason is required")
    private String reason;

    private String rejectedBy;

    public String getReason() { return reason; }
    public void setReason(String v) { this.reason = v; }

    public String getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(String v) { this.rejectedBy = v; }
}
