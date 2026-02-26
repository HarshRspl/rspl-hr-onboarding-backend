package com.rspl.onboarding.service;

import com.rspl.onboarding.api.InitiateCandidateRequest;
import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.repo.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class OnboardingService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SmsService smsService;

    // ── listCandidates(int page, int size) ───────────
    public Page<Candidate> listCandidates(int page, int size) {
        return candidateRepository.findAll(PageRequest.of(page, size));
    }

    // ── initiate(InitiateCandidateRequest) ───────────
    public Candidate initiate(InitiateCandidateRequest request) {
        Candidate candidate = new Candidate();
        candidate.setEmployeeName(request.getEmployeeName());
        candidate.setEmail(request.getEmail());
        candidate.setMobileNo(request.getMobileNo());
        candidate.setDesignation(request.getDesignation());
        candidate.setAssignedHr(request.getAssignedHr());
        candidate.setStatus("PENDING");

        String token = UUID.randomUUID().toString();
        candidate.setOnboardingToken(token);

        Candidate saved = candidateRepository.save(candidate);

        smsService.sendOnboardingLink(
            saved.getMobileNo(),
            saved.getEmployeeName(),
            token
        );

        return saved;
    }

    // ── approve(long id) ─────────────────────────────
    public Candidate approve(long id) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));
        c.setStatus("APPROVED");
        return candidateRepository.save(c);
    }

    // ── reject(long id, String reason) ───────────────
    public Candidate reject(long id, String reason) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));
        c.setStatus("REJECTED");
        c.setRejectionReason(reason);
        return candidateRepository.save(c);
    }

    // ── sendLink(long id) ────────────────────────────
    public Candidate sendLink(long id) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));

        if (c.getOnboardingToken() == null) {
            c.setOnboardingToken(UUID.randomUUID().toString());
        }

        smsService.sendOnboardingLink(
            c.getMobileNo(),
            c.getEmployeeName(),
            c.getOnboardingToken()
        );

        c.setStatus("LINK_SENT");
        return candidateRepository.save(c);
    }

    // ── getCandidateByToken(String token) ────────────
    public Candidate getCandidateByToken(String token) {
        return candidateRepository.findByOnboardingToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));
    }

    // ── deleteCandidate(Long id) ─────────────────────
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
}
