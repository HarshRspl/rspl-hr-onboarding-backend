package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.service.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hr")
public class HrController {

    @Autowired
    private OnboardingService service;

    // ── GET /api/hr/candidates?page=0&size=10 ────────
    @GetMapping("/candidates")
    public ResponseEntity<PageDto<CandidateDto>> listCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Candidate> result = service.listCandidates(page, size);
        Page<CandidateDto> dtoPage = result.map(CandidateDto::from);
        return ResponseEntity.ok(ApiResponse.success(PageDto.from(dtoPage)).getData());
    }

    // ── POST /api/hr/candidates ──────────────────────
    @PostMapping("/candidates")
    public ResponseEntity<CandidateDto> initiate(
            @Valid @RequestBody InitiateCandidateRequest request) {

        Candidate candidate = service.initiate(request);
        return ResponseEntity.ok(ApiResponse.success(CandidateDto.from(candidate)).getData());
    }

    // ── POST /api/hr/candidates/{id}/approve ─────────
    @PostMapping("/candidates/{id}/approve")
    public ResponseEntity<CandidateDto> approve(@PathVariable long id) {
        return ResponseEntity.ok(CandidateDto.from(service.approve(id)));
    }

    // ── POST /api/hr/candidates/{id}/reject ──────────
    @PostMapping("/candidates/{id}/reject")
    public ResponseEntity<CandidateDto> reject(
            @PathVariable long id,
            @RequestBody RejectRequest body) {
        return ResponseEntity.ok(CandidateDto.from(service.reject(id, body.getReason())));
    }

    // ── POST /api/hr/candidates/{id}/send-link ───────
    @PostMapping("/candidates/{id}/send-link")
    public ResponseEntity<CandidateDto> sendLink(@PathVariable long id) {
        return ResponseEntity.ok(CandidateDto.from(service.sendLink(id)));
    }
}
