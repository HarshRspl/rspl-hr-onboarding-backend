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
                        "candidateId", c.getId(),
                        "employeeName", c.getEmployeeName(),
                        "emailId", c.getEmailId() != null ? c.getEmailId() : "",
                        "designation", c.getDesignation() != null ? c.getDesignation() : "",
                        "mobileNo", c.getMobileNo() != null ? c.getMobileNo() : "",
                        "aadhaarNo", c.getAadhaarNo() != null ? c.getAadhaarNo() : "",
                        "joiningStatus", c.getJoiningStatus(),
                        "tokenValid", true
                )
        ));
    }

    /**
     * ✅ NEW: fetch candidate saved form details (for Edit Mode)
     */
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getDetails(@RequestParam String token) {
        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid token"));
        }

        Candidate c = opt.get();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", Map.of(
                        "candidateId", c.getId(),
                        "employeeName", c.getEmployeeName(),
                        "designation", c.getDesignation() != null ? c.getDesignation() : "",
                        "joiningStatus", c.getJoiningStatus() != null ? c.getJoiningStatus() : "",

                        // Personal
                        "fathersName", c.getFathersName() != null ? c.getFathersName() : "",
                        "dob", c.getDob() != null ? c.getDob() : "",
                        "gender", c.getGender() != null ? c.getGender() : "",
                        "maritalStatus", c.getMaritalStatus() != null ? c.getMaritalStatus() : "",
                        "bloodGroup", c.getBloodGroup() != null ? c.getBloodGroup() : "",
                        "higherEducation", c.getHigherEducation() != null ? c.getHigherEducation() : "",
                        "panCardNo", c.getPanCardNo() != null ? c.getPanCardNo() : "",
                        "uanAvailable", c.getUanAvailable() != null ? c.getUanAvailable() : "NO",
                        "uanNo", c.getUanNo() != null ? c.getUanNo() : "",

                        // Address
                        "permanentAddress", c.getPermanentAddress() != null ? c.getPermanentAddress() : "",
                        "district", c.getDistrict() != null ? c.getDistrict() : "",
                        "state", c.getState() != null ? c.getState() : "",
                        "pinCode", c.getPinCode() != null ? c.getPinCode() : "",

                        // Bank
                        "bankName", c.getBankName() != null ? c.getBankName() : "",
                        "bankAccountNo", c.getBankAccountNo() != null ? c.getBankAccountNo() : "",
                        "ifscCode", c.getIfscCode() != null ? c.getIfscCode() : "",
                        "branchName", c.getBranchName() != null ? c.getBranchName() : "",

                        // Nominee
                        "nomineeName", c.getNomineeName() != null ? c.getNomineeName() : "",
                        "nomineeRelation", c.getNomineeRelation() != null ? c.getNomineeRelation() : "",
                        "nomineeDob", c.getNomineeDob() != null ? c.getNomineeDob() : "",
                        "nomineeAddress", c.getNomineeAddress() != null ? c.getNomineeAddress() : ""
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

        // Existing block: prevents re-submit when already submitted/signed/approved
        if (java.util.Arrays.asList("FORM_SUBMITTED", "SIGNED", "APPROVED").contains(c.getJoiningStatus())) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Form already submitted")
            );
        }

        // Helper: avoid overwriting with null
        java.util.function.BiFunction<String, String, String> val =
                (k, fallback) -> {
                    String v = body.get(k);
                    return (v == null) ? fallback : v;
                };

        // ===== Personal =====
        c.setAadhaarNo(val.apply("aadhaarNo", c.getAadhaarNo()));
        c.setMobileNo(val.apply("mobileNo", c.getMobileNo()));
        c.setPersonalEmail(val.apply("personalEmail", c.getPersonalEmail()));
        c.setFathersName(val.apply("fathersName", c.getFathersName()));
        c.setMothersName(val.apply("mothersName", c.getMothersName()));
        c.setDob(val.apply("dob", c.getDob()));
        c.setGender(val.apply("gender", c.getGender()));
        c.setNationality(val.apply("nationality", c.getNationality()));
        c.setMaritalStatus(val.apply("maritalStatus", c.getMaritalStatus()));
        c.setBloodGroup(val.apply("bloodGroup", c.getBloodGroup()));
        c.setHigherEducation(val.apply("higherEducation", c.getHigherEducation()));
        c.setPanCardNo(val.apply("panCardNo", c.getPanCardNo()));
        c.setUanAvailable(val.apply("uanAvailable", c.getUanAvailable()));
        c.setUanNo(val.apply("uanNo", c.getUanNo()));
        c.setEsiNo(val.apply("esiNo", c.getEsiNo()));

        // ===== Address =====
        c.setPermanentAddress(val.apply("permanentAddress", c.getPermanentAddress()));
        c.setDistrict(val.apply("district", c.getDistrict()));
        c.setState(val.apply("state", c.getState()));
        c.setPinCode(val.apply("pinCode", c.getPinCode()));
        c.setWorkingState(val.apply("workingState", c.getWorkingState()));
        c.setHqDistrict(val.apply("hqDistrict", c.getHqDistrict()));

        // ===== Employment =====
        c.setDepartment(val.apply("department", c.getDepartment()));
        c.setDateOfJoining(val.apply("dateOfJoining", c.getDateOfJoining()));
        c.setPrevOrgName(val.apply("prevOrgName", c.getPrevOrgName()));
        c.setPrevOrgLocation(val.apply("prevOrgLocation", c.getPrevOrgLocation()));
        c.setPrevEmpFrom(val.apply("prevEmpFrom", c.getPrevEmpFrom()));
        c.setPrevEmpTo(val.apply("prevEmpTo", c.getPrevEmpTo()));
        c.setPrevNatureOfJob(val.apply("prevNatureOfJob", c.getPrevNatureOfJob()));
        c.setPrevSalary(val.apply("prevSalary", c.getPrevSalary()));
        c.setPrevReasonLeaving(val.apply("prevReasonLeaving", c.getPrevReasonLeaving()));

        // ===== Bank =====
        c.setBankName(val.apply("bankName", c.getBankName()));
        c.setBankAccountNo(val.apply("bankAccountNo", c.getBankAccountNo()));
        c.setIfscCode(val.apply("ifscCode", c.getIfscCode()));
        c.setBranchName(val.apply("branchName", c.getBranchName()));

        // ===== Nominee =====
        c.setNomineeName(val.apply("nomineeName", c.getNomineeName()));
        c.setNomineeGender(val.apply("nomineeGender", c.getNomineeGender()));
        c.setNomineeRelation(val.apply("nomineeRelation", c.getNomineeRelation()));
        c.setNomineeDob(val.apply("nomineeDob", c.getNomineeDob()));
        c.setNomineeAddress(val.apply("nomineeAddress", c.getNomineeAddress()));

        // ===== Status =====
        c.setJoiningStatus("FORM_SUBMITTED");
        c.setUpdatedAt(LocalDateTime.now());
        candidateRepository.save(c);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Form submitted successfully!",
                "data", Map.of(
                        "candidateId", c.getId(),
                        "status", "FORM_SUBMITTED",
                        "employeeName", c.getEmployeeName()
                )
        ));
    }

    /**
     * ✅ NEW: Update submitted form (Edit Mode)
     * Allowed only when status is FORM_SUBMITTED or REJECTED.
     * Block if SIGNED/APPROVED.
     */
    @PostMapping("/update-form")
    public ResponseEntity<Map<String, Object>> updateForm(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);

        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid token"));
        }

        Candidate c = opt.get();

        // Block edit after HR progress
        if (java.util.Arrays.asList("SIGNED", "APPROVED").contains(c.getJoiningStatus())) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Editing is disabled after signing/approval.")
            );
        }

        // Allow edit only after submission (and optionally REJECTED)
        if (!java.util.Arrays.asList("FORM_SUBMITTED", "REJECTED").contains(c.getJoiningStatus())) {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "Form is not submitted yet. Please submit first.")
            );
        }

        // Helper: avoid overwriting with null
        java.util.function.BiFunction<String, String, String> val =
                (k, fallback) -> {
                    String v = body.get(k);
                    return (v == null) ? fallback : v;
                };

        // ===== Personal =====
        c.setFathersName(val.apply("fathersName", c.getFathersName()));
        c.setDob(val.apply("dob", c.getDob()));
        c.setGender(val.apply("gender", c.getGender()));
        c.setMaritalStatus(val.apply("maritalStatus", c.getMaritalStatus()));
        c.setBloodGroup(val.apply("bloodGroup", c.getBloodGroup()));
        c.setHigherEducation(val.apply("higherEducation", c.getHigherEducation()));
        c.setPanCardNo(val.apply("panCardNo", c.getPanCardNo()));
        c.setUanAvailable(val.apply("uanAvailable", c.getUanAvailable()));
        c.setUanNo(val.apply("uanNo", c.getUanNo()));

        // ===== Address =====
        c.setPermanentAddress(val.apply("permanentAddress", c.getPermanentAddress()));
        c.setDistrict(val.apply("district", c.getDistrict()));
        c.setState(val.apply("state", c.getState()));
        c.setPinCode(val.apply("pinCode", c.getPinCode()));

        // ===== Bank =====
        c.setBankName(val.apply("bankName", c.getBankName()));
        c.setBankAccountNo(val.apply("bankAccountNo", c.getBankAccountNo()));
        c.setIfscCode(val.apply("ifscCode", c.getIfscCode()));
        c.setBranchName(val.apply("branchName", c.getBranchName()));

        // ===== Nominee =====
        c.setNomineeName(val.apply("nomineeName", c.getNomineeName()));
        c.setNomineeRelation(val.apply("nomineeRelation", c.getNomineeRelation()));
        c.setNomineeDob(val.apply("nomineeDob", c.getNomineeDob()));
        c.setNomineeAddress(val.apply("nomineeAddress", c.getNomineeAddress()));

        // If REJECTED → re-submit to HR as FORM_SUBMITTED
        if ("REJECTED".equalsIgnoreCase(c.getJoiningStatus())) {
            c.setJoiningStatus("FORM_SUBMITTED");
        }

        c.setUpdatedAt(LocalDateTime.now());
        candidateRepository.save(c);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Form updated successfully!",
                "data", Map.of(
                        "candidateId", c.getId(),
                        "status", c.getJoiningStatus()
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
                        "candidateId", c.getId(),
                        "employeeName", c.getEmployeeName(),
                        "status", c.getJoiningStatus(),
                        "employeeId", c.getEmployeeId() != null ? c.getEmployeeId() : "",
                        "rejectionReason", c.getRejectionReason() != null ? c.getRejectionReason() : ""
                )
        ));
    }
}
