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

        if (java.util.Arrays.asList("FORM_SUBMITTED", "SIGNED", "APPROVED")
                .contains(c.getJoiningStatus())) {
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

        // ✅ NEW: UAN yes/no + number
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
