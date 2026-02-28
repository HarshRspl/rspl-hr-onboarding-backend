package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.service.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = "*")
public class HrController {

    @Autowired
    private OnboardingService service;

    @Value("${app.base.url}")
    private String baseUrl;

    // ── Team ──────────────────────────────────────────────────────────────────
    @GetMapping("/team")
    public ResponseEntity<Map<String, Object>> getTeam() {
        return ResponseEntity.ok(
                Map.of("success", true, "data", service.getHRTeam())
        );
    }

    // ── List with status filter + search ─────────────────────────────────────
    @GetMapping("/candidates")
    public ResponseEntity<Map<String, Object>> listCandidates(
            @RequestParam(defaultValue = "0")   int page,
            @RequestParam(defaultValue = "15")  int size,        // ✅ match frontend page size
            @RequestParam(required = false)     String status,   // ✅ ADDED
            @RequestParam(required = false)     String search) { // ✅ ADDED

        Page<Candidate> result = service.listCandidates(page, size, status, search);

        List<CandidateDto> list = result.getContent()
                .stream()
                .map(c -> CandidateDto.from(c, baseUrl))
                .collect(Collectors.toList());

        PageDto<CandidateDto> pageDto = new PageDto<>();
        pageDto.setContent(list);
        pageDto.setPage(result.getNumber());
        pageDto.setSize(result.getSize());
        pageDto.setTotalElements(result.getTotalElements());
        pageDto.setTotalPages(result.getTotalPages());

        return ResponseEntity.ok(Map.of("success", true, "data", pageDto));
    }

    // ✅ ADDED — GET single candidate for viewDetail()
    @GetMapping("/candidates/{id}")
    public ResponseEntity<Map<String, Object>> getCandidate(@PathVariable long id) {
        Candidate c = service.getCandidate(id);
        return ResponseEntity.ok(
                Map.of("success", true, "data", CandidateDto.from(c, baseUrl))
        );
    }

    // ✅ ADDED — Stats endpoint for dashboard counters
    @GetMapping("/candidates/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total",          service.countAll());
        stats.put("INITIATED",      service.countByStatus("INITIATED"));
        stats.put("FORM_SUBMITTED", service.countByStatus("FORM_SUBMITTED"));
        stats.put("SIGNED",         service.countByStatus("SIGNED"));
        stats.put("APPROVED",       service.countByStatus("APPROVED"));
        stats.put("REJECTED",       service.countByStatus("REJECTED"));
        return ResponseEntity.ok(Map.of("success", true, "data", stats));
    }

    // ── Initiate ──────────────────────────────────────────────────────────────
    @PostMapping("/candidates/initiate")
    public ResponseEntity<Map<String, Object>> initiate(
            @Valid @RequestBody InitiateCandidateRequest request) {
        Candidate c = service.initiate(request);
        return ResponseEntity.ok(
                Map.of("success", true, "data", CandidateDto.from(c, baseUrl))
        );
    }

    // ── Approve ───────────────────────────────────────────────────────────────
    @PostMapping("/candidates/{id}/approve")
    public ResponseEntity<Map<String, Object>> approve(@PathVariable long id) {
        return ResponseEntity.ok(
                Map.of("success", true, "data", CandidateDto.from(service.approve(id), baseUrl))
        );
    }

    // ── Reject ────────────────────────────────────────────────────────────────
    @PostMapping("/candidates/{id}/reject")
    public ResponseEntity<Map<String, Object>> reject(
            @PathVariable long id,
            @RequestBody RejectRequest body) {
        return ResponseEntity.ok(
                Map.of("success", true, "data",
                        // ✅ FIX 4: use getRejectionReason() to match frontend payload
                        CandidateDto.from(service.reject(id, body.getRejectionReason()), baseUrl))
        );
    }

    // ── Send Link ─────────────────────────────────────────────────────────────
    @PostMapping("/candidates/{id}/send-link")
    public ResponseEntity<Map<String, Object>> sendLink(@PathVariable long id) {
        return ResponseEntity.ok(
                Map.of("success", true, "data", CandidateDto.from(service.sendLink(id), baseUrl))
        );
    }
}
