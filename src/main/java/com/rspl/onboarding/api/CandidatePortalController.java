package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.repo.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@RestController
@RequestMapping("/api/candidate")
// ✅ REMOVED @CrossOrigin — handled globally in SecurityConfig
@RequiredArgsConstructor
public class CandidatePortalController {

    private final CandidateRepository candidateRepository;

    // ✅ GET /api/candidate/verify-token?token=xxx
    @GetMapping("/verify-token")
    public ResponseEntity<Map<String, Object>> verifyToken(@RequestParam String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Token is required"
            ));
        }

        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false, "message", "Token expired or invalid"
            ));
        }

        Candidate c = opt.get();

        if (c.getTokenExpiry() != null && c.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false, "message", "Token has expired. Please contact HR."
            ));
        }

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("candidateId", c.getId());
        data.put("employeeName", c.getEmployeeName());
        data.put("emailId", c.getEmailId() != null ? c.getEmailId() : "");
        data.put("designation", c.getDesignation() != null ? c.getDesignation() : "");
        data.put("mobileNo", c.getMobileNo() != null ? c.getMobileNo() : "");
        data.put("aadhaarNo", c.getAadhaarNo() != null ? c.getAadhaarNo() : "");
        data.put("joiningStatus", c.getJoiningStatus());
        data.put("tokenValid", true);

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    // ✅ GET /api/candidate/details?token=xxx
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> getDetails(@RequestParam String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Token is required"
            ));
        }

        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false, "message", "Invalid token"
            ));
        }

        Candidate c = opt.get();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("candidateId", c.getId());
        data.put("employeeName", c.getEmployeeName());
        data.put("designation", c.getDesignation() != null ? c.getDesignation() : "");
        data.put("joiningStatus", c.getJoiningStatus() != null ? c.getJoiningStatus() : "");
        data.put("fathersName", c.getFathersName() != null ? c.getFathersName() : "");
        data.put("dob", c.getDob() != null ? c.getDob() : "");
        data.put("gender", c.getGender() != null ? c.getGender() : "");
        data.put("maritalStatus", c.getMaritalStatus() != null ? c.getMaritalStatus() : "");
        data.put("bloodGroup", c.getBloodGroup() != null ? c.getBloodGroup() : "");
        data.put("higherEducation", c.getHigherEducation() != null ? c.getHigherEducation() : "");
        data.put("panCardNo", c.getPanCardNo() != null ? c.getPanCardNo() : "");
        data.put("uanAvailable", c.getUanAvailable() != null ? c.getUanAvailable() : "NO");
        data.put("uanNo", c.getUanNo() != null ? c.getUanNo() : "");
        data.put("permanentAddress", c.getPermanentAddress() != null ? c.getPermanentAddress() : "");
        data.put("district", c.getDistrict() != null ? c.getDistrict() : "");
        data.put("state", c.getState() != null ? c.getState() : "");
        data.put("pinCode", c.getPinCode() != null ? c.getPinCode() : "");
        data.put("bankName", c.getBankName() != null ? c.getBankName() : "");
        data.put("bankAccountNo", c.getBankAccountNo() != null ? c.getBankAccountNo() : "");
        data.put("ifscCode", c.getIfscCode() != null ? c.getIfscCode() : "");
        data.put("branchName", c.getBranchName() != null ? c.getBranchName() : "");
        data.put("nomineeName", c.getNomineeName() != null ? c.getNomineeName() : "");
        data.put("nomineeRelation", c.getNomineeRelation() != null ? c.getNomineeRelation() : "");
        data.put("nomineeDob", c.getNomineeDob() != null ? c.getNomineeDob() : "");
        data.put("nomineeAddress", c.getNomineeAddress() != null ? c.getNomineeAddress() : "");

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }

    // ✅ POST /api/candidate/submit-form
    @PostMapping("/submit-form")
    @Transactional
    public ResponseEntity<Map<String, Object>> submitForm(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Token is required"
            ));
        }

        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false, "message", "Invalid token"
            ));
        }

        Candidate c = opt.get();

        if (Arrays.asList("FORM_SUBMITTED", "SIGNED", "APPROVED").contains(c.getJoiningStatus())) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Form already submitted. Use update-form to make changes."
            ));
        }

        BiFunction<String, String, String> val =
            (k, fallback) -> { String v = body.get(k); return (v == null) ? fallback : v; };

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
        c.setPermanentAddress(val.apply("permanentAddress", c.getPermanentAddress()));
        c.setDistrict(val.apply("district", c.getDistrict()));
        c.setState(val.apply("state", c.getState()));
        c.setPinCode(val.apply("pinCode", c.getPinCode()));
        c.setWorkingState(val.apply("workingState", c.getWorkingState()));
        c.setHqDistrict(val.apply("hqDistrict", c.getHqDistrict()));
        c.setDepartment(val.apply("department", c.getDepartment()));
        c.setDateOfJoining(val.apply("dateOfJoining", c.getDateOfJoining()));
        c.setPrevOrgName(val.apply("prevOrgName", c.getPrevOrgName()));
        c.setPrevOrgLocation(val.apply("prevOrgLocation", c.getPrevOrgLocation()));
        c.setPrevEmpFrom(val.apply("prevEmpFrom", c.getPrevEmpFrom()));
        c.setPrevEmpTo(val.apply("prevEmpTo", c.getPrevEmpTo()));
        c.setPrevNatureOfJob(val.apply("prevNatureOfJob", c.getPrevNatureOfJob()));
        c.setPrevSalary(val.apply("prevSalary", c.getPrevSalary()));
        c.setPrevReasonLeaving(val.apply("prevReasonLeaving", c.getPrevReasonLeaving()));
        c.setBankName(val.apply("bankName", c.getBankName()));
        c.setBankAccountNo(val.apply("bankAccountNo", c.getBankAccountNo()));
        c.setIfscCode(val.apply("ifscCode", c.getIfscCode()));
        c.setBranchName(val.apply("branchName", c.getBranchName()));
        c.setNomineeName(val.apply("nomineeName", c.getNomineeName()));
        c.setNomineeGender(val.apply("nomineeGender", c.getNomineeGender()));
        c.setNomineeRelation(val.apply("nomineeRelation", c.getNomineeRelation()));
        c.setNomineeDob(val.apply("nomineeDob", c.getNomineeDob()));
        c.setNomineeAddress(val.apply("nomineeAddress", c.getNomineeAddress()));

        c.setJoiningStatus("FORM_SUBMITTED");
        c.setUpdatedAt(LocalDateTime.now());
        candidateRepository.save(c);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("candidateId", c.getId());
        data.put("status", "FORM_SUBMITTED");
        data.put("employeeName", c.getEmployeeName());

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Form submitted successfully!",
            "data", data
        ));
    }

    // ✅ POST /api/candidate/update-form
    @PostMapping("/update-form")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateForm(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Token is required"
            ));
        }

        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false, "message", "Invalid token"
            ));
        }

        Candidate c = opt.get();

        if (Arrays.asList("SIGNED", "APPROVED").contains(c.getJoiningStatus())) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Editing is disabled after signing/approval."
            ));
        }

        if (!Arrays.asList("FORM_SUBMITTED", "REJECTED").contains(c.getJoiningStatus())) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Form is not submitted yet. Please submit first."
            ));
        }

        BiFunction<String, String, String> val =
            (k, fallback) -> { String v = body.get(k); return (v == null) ? fallback : v; };

        c.setFathersName(val.apply("fathersName", c.getFathersName()));
        c.setDob(val.apply("dob", c.getDob()));
        c.setGender(val.apply("gender", c.getGender()));
        c.setMaritalStatus(val.apply("maritalStatus", c.getMaritalStatus()));
        c.setBloodGroup(val.apply("bloodGroup", c.getBloodGroup()));
        c.setHigherEducation(val.apply("higherEducation", c.getHigherEducation()));
        c.setPanCardNo(val.apply("panCardNo", c.getPanCardNo()));
        c.setUanAvailable(val.apply("uanAvailable", c.getUanAvailable()));
        c.setUanNo(val.apply("uanNo", c.getUanNo()));
        c.setPermanentAddress(val.apply("permanentAddress", c.getPermanentAddress()));
        c.setDistrict(val.apply("district", c.getDistrict()));
        c.setState(val.apply("state", c.getState()));
        c.setPinCode(val.apply("pinCode", c.getPinCode()));
        c.setBankName(val.apply("bankName", c.getBankName()));
        c.setBankAccountNo(val.apply("bankAccountNo", c.getBankAccountNo()));
        c.setIfscCode(val.apply("ifscCode", c.getIfscCode()));
        c.setBranchName(val.apply("branchName", c.getBranchName()));
        c.setNomineeName(val.apply("nomineeName", c.getNomineeName()));
        c.setNomineeRelation(val.apply("nomineeRelation", c.getNomineeRelation()));
        c.setNomineeDob(val.apply("nomineeDob", c.getNomineeDob()));
        c.setNomineeAddress(val.apply("nomineeAddress", c.getNomineeAddress()));

        if ("REJECTED".equalsIgnoreCase(c.getJoiningStatus())) {
            c.setJoiningStatus("FORM_SUBMITTED");
        }

        c.setUpdatedAt(LocalDateTime.now());
        candidateRepository.save(c);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("candidateId", c.getId());
        data.put("status", c.getJoiningStatus());

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Form updated successfully!",
            "data", data
        ));
    }

    // ✅ GET /api/candidate/status?token=xxx
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus(@RequestParam String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "Token is required"
            ));
        }

        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false, "message", "Invalid token"
            ));
        }

        Candidate c = opt.get();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("candidateId", c.getId());
        data.put("employeeName", c.getEmployeeName());
        data.put("status", c.getJoiningStatus());
        data.put("employeeId", c.getEmployeeId() != null ? c.getEmployeeId() : "");
        data.put("rejectionReason", c.getRejectionReason() != null ? c.getRejectionReason() : "");

        return ResponseEntity.ok(Map.of("success", true, "data", data));
    }
}
