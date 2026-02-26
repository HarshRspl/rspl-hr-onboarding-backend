package com.rspl.onboarding.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RejectRequest {
    @NotBlank(message = "Rejection reason is required")
    private String reason;
    private String rejectedBy;
}
