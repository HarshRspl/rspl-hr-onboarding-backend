package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.repo.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin(origins = "*")
public class CandidatePortalController {

    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping("/verify-token")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestParam String token) {
        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Token expired or invalid"
            ));
        }
        Candidate c = opt.get();
        if (c.getTokenExpiry() != null && c.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Token has expired. Please contact HR."
            ));
        }
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", Map.of(
                "candidateId",   c.getId(),
                "employeeName",  c.getEmployeeName(),
                "emailId",       c.getEmailId()     != null ? c.getEmailId()     : "",
                "designation",   c.getDesignation() != null ? c.getDesignation() : "",
                "joiningStatus", c.getJoiningStatus(),
                "tokenValid",    true
            )
        ));
    }

    @PostMapping("/submit-form")
    public ResponseEntity<Map<String, Object>> submitForm(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(
                Map.of("success", false, "message", "Invalid token")
            );
        }
        Candidate c = opt.get();
        if (java.util.Arrays.asList("FORM_SUBMITTED","SIGNED","APPROVED")
                .contains(c.getJoiningStatus())) {
            return ResponseEntity.badRequest().body(
                Map.of("success", false, "message", "Form already submitted")
            );
        }

        c.setFathersName(body.get("fathersName"));
        c.setDob(body.get("dob"));
        c.setGender(body.get("gender"));
        c.setMaritalStatus(body.get("maritalStatus"));
        c.setBloodGroup(body.get("bloodGroup"));
        c.setHigherEducation(body.get("higherEducation"));
        c.setPanCardNo(body.get("panCardNo"));
        c.setPermanentAddress(body.get("permanentAddress"));
        c.setDistrict(body.get("district"));
        c.setState(body.get("state"));
        c.setPinCode(body.get("pinCode"));
        c.setBankName(body.get("bankName"));
        c.setBankAccountNo(body.get("bankAccountNo"));
        c.setIfscCode(body.get("ifscCode"));
        c.setBranchName(body.get("branchName"));
        c.setNomineeName(body.get("nomineeName"));
        c.setNomineeRelation(body.get("nomineeRelation"));
        c.setNomineeDob(body.get("nomineeDob"));
        c.setNomineeAddress(body.get("nomineeAddress"));
        c.setJoiningStatus("FORM_SUBMITTED");
        c.setUpdatedAt(LocalDateTime.now());
        candidateRepository.save(c);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Form submitted successfully!",
            "data", Map.of(
                "candidateId",  c.getId(),
                "status",       "FORM_SUBMITTED",
                "employeeName", c.getEmployeeName()
            )
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus(@RequestParam String token) {
        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(
                Map.of("success", false, "message", "Invalid token")
            );
        }
        Candidate c = opt.get();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "data", Map.of(
                "candidateId",     c.getId(),
                "employeeName",    c.getEmployeeName(),
                "status",          c.getJoiningStatus(),
                "employeeId",      c.getEmployeeId()      != null ? c.getEmployeeId()      : "",
                "rejectionReason", c.getRejectionReason() != null ? c.getRejectionReason() : ""
            )
        ));
    }
}

