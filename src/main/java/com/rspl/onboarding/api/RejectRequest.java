package com.rspl.onboarding.api;

import jakarta.validation.constraints.NotBlank;

public class RejectRequest {

    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;

    private String rejectedBy;

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String v) { this.rejectionReason = v; }

    public String getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(String v) { this.rejectedBy = v; }
}
