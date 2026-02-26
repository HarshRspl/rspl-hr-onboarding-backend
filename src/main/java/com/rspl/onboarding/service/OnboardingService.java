package com.rspl.onboarding.service;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.repo.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class OnboardingService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SmsService smsService;

    // ── Create candidate + send SMS ──────────────────
    public Candidate createCandidate(Candidate candidate) {
        // Generate unique token
        String token = UUID.randomUUID().toString();
        candidate.setOnboardingToken(token);
        candidate.setStatus("PENDING");

        // Save to DB
        Candidate saved = candidateRepository.save(candidate);

        // Send SMS with onboarding link
        smsService.sendOnboardingLink(
            saved.getMobileNo(),
            saved.getEmployeeName(),
            token
        );

        return saved;
    }

    // ── Get all candidates ───────────────────────────
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    // ── Get candidate by token ───────────────────────
    public Candidate getCandidateByToken(String token) {
        return candidateRepository.findByOnboardingToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));
    }

    // ── Update candidate status ──────────────────────
    public Candidate updateStatus(Long id, String status) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));
        c.setStatus(status);
        return candidateRepository.save(c);
    }

    // ── Delete candidate ─────────────────────────────
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
}
