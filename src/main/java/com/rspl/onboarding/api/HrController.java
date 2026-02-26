package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.service.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = "*")
public class HrController {

    @Autowired
    private OnboardingService service;

    // ✅ ADDED - Frontend calls /api/hr/team
    @GetMapping("/team")
    public ResponseEntity<?> getTeam() {
        return ResponseEntity.ok(
            Map.of("success", true, "data", service.getHRTeam())
        );
    }

    // ✅ FIXED - Returns success wrapper
    @GetMapping("/candidates")
    public ResponseEntity<?> listCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Candidate> result = service.listCandidates(page, size);
        List<CandidateDto> list = result.getContent()
                .stream()
                .map(CandidateDto::from)
                .collect(Collectors.toList());

        PageDto<CandidateDto> pageDto = new PageDto<>();
        pageDto.setContent(list);
        pageDto.setPage(result.getNumber());
        pageDto.setSize(result.getSize());
        pageDto.setTotalElements(result.getTotalElements());
        pageDto.setTotalPages(result.getTotalPages());

        return ResponseEntity.ok(Map.of("success", true, "data", pageDto));
    }

    // ✅ FIXED - Frontend calls /api/hr/candidates/initiate not /api/hr/candidates
    @PostMapping("/candidates/initiate")
    public ResponseEntity<?> initiate(
            @Valid @RequestBody InitiateCandidateRequest request) {
        Candidate candidate = service.initiate(request);
        return ResponseEntity.ok(Map.of("success", true, "data", CandidateDto.from(candidate)));
    }

    @PostMapping("/candidates/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable long id) {
        return ResponseEntity.ok(Map.of("success", true, "data", CandidateDto.from(service.approve(id))));
    }

    @PostMapping("/candidates/{id}/reject")
    public ResponseEntity<?> reject(
            @PathVariable long id,
            @RequestBody RejectRequest body) {
        return ResponseEntity.ok(Map.of("success", true, "data", CandidateDto.from(service.reject(id, body.getReason()))));
    }

    @PostMapping("/candidates/{id}/send-link")
    public ResponseEntity<?> sendLink(@PathVariable long id) {
        return ResponseEntity.ok(Map.of("success", true, "data", CandidateDto.from(service.sendLink(id))));
    }
}
