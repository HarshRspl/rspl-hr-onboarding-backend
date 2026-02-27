package com.rspl.onboarding.service;

import com.rspl.onboarding.api.InitiateCandidateRequest;
import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.repo.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OnboardingService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private SmsService smsService;

    // ── getHRTeam() ─────────────────────────────────
    public List<Map<String, Object>> getHRTeam() {
        List<Map<String, Object>> team = new ArrayList<>();
        team.add(Map.of("id", 1, "name", "Sneha Kapoor",  "role", "Sr. HR Executive", "email", "sneha@rspl.com",  "phone", "9811001100"));
        team.add(Map.of("id", 2, "name", "Arjun Mehta",   "role", "HR Executive",     "email", "arjun@rspl.com",  "phone", "9822002200"));
        team.add(Map.of("id", 3, "name", "Pooja Nair",    "role", "HR Executive",     "email", "pooja@rspl.com",  "phone", "9833003300"));
        team.add(Map.of("id", 4, "name", "Kunal Bose",    "role", "Jr. HR Executive", "email", "kunal@rspl.com",  "phone", "9844004400"));
        team.add(Map.of("id", 5, "name", "Divya Rawat",   "role", "HR Manager",       "email", "divya@rspl.com",  "phone", "9855005500"));
        return team;
    }

    // helper: resolve HR name from ID using the static team list
    private String resolveHRName(Long hrId) {
        Map<Long, String> hrNames = Map.of(
            1L, "Sneha Kapoor",
            2L, "Arjun Mehta",
            3L, "Pooja Nair",
            4L, "Kunal Bose",
            5L, "Divya Rawat"
        );
        return hrNames.getOrDefault(hrId, "HR Admin");
    }

    // ── listCandidates ───────────────────────────────
    public Page<Candidate> listCandidates(int page, int size) {
        return candidateRepository.findAll(
            PageRequest.of(page, size, Sort.by("id").descending())
        );
    }

    // ── initiate ─────────────────────────────────────
    public Candidate initiate(InitiateCandidateRequest request) {
        Candidate candidate = new Candidate();
        candidate.setEmployeeName(request.getEmployeeName());

        // ✅ was candidate.setEmail()
        String email = request.getEmailId() != null ? request.getEmailId() : request.getEmail();
        candidate.setEmailId(email);

        candidate.setMobileNo(request.getMobileNo());
        candidate.setAadhaarNo(request.getAadhaarNo());
        candidate.setDesignation(request.getDesignation());
        candidate.setInitiatedBy(request.getInitiatedBy());

        // ✅ was candidate.setAssignedHr() with a String
        Long hrId = request.getAssignedHRId();
        candidate.setAssignedHRId(hrId);
        candidate.setAssignedHRName(resolveHRName(hrId));

        // ✅ was candidate.setStatus()
        candidate.setJoiningStatus("INITIATED");
        candidate.setLinkStatus("NOT_SENT");

        String token = UUID.randomUUID().toString();
        candidate.setOnboardingToken(token);
        candidate.setCreatedAt(LocalDateTime.now());
        candidate.setUpdatedAt(LocalDateTime.now());

        Candidate saved = candidateRepository.save(candidate);

        // send SMS safely — don't crash if it fails
        try {
            if (Boolean.TRUE.equals(request.getSendLinkImmediately())) {
                smsService.sendOnboardingLink(
                    saved.getMobileNo(),
                    saved.getEmployeeName(),
                    token
                );
                saved.setLinkStatus("SENT");
                saved = candidateRepository.save(saved);
            }
        } catch (Exception e) {
            System.err.println("SMS failed (non-fatal): " + e.getMessage());
        }

        return saved;
    }

    // ── approve ──────────────────────────────────────
    public Candidate approve(long id) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found: " + id));
        c.setJoiningStatus("APPROVED");   // ✅ was c.setStatus()
        c.setUpdatedAt(LocalDateTime.now());
        return candidateRepository.save(c);
    }

    // ── reject ───────────────────────────────────────
    public Candidate reject(long id, String reason) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found: " + id));
        c.setJoiningStatus("REJECTED");   // ✅ was c.setStatus()
        c.setRejectionReason(reason);
        c.setUpdatedAt(LocalDateTime.now());
        return candidateRepository.save(c);
    }

    // ── sendLink ─────────────────────────────────────
    public Candidate sendLink(long id) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found: " + id));

        if (c.getOnboardingToken() == null) {
            c.setOnboardingToken(UUID.randomUUID().toString());
        }

        try {
            smsService.sendOnboardingLink(
                c.getMobileNo(),
                c.getEmployeeName(),
                c.getOnboardingToken()
            );
            c.setLinkStatus("SENT");      // ✅ update link status after sending
        } catch (Exception e) {
            System.err.println("SMS failed (non-fatal): " + e.getMessage());
        }

        c.setUpdatedAt(LocalDateTime.now());
        return candidateRepository.save(c);
    }

    // ── getCandidateByToken ──────────────────────────
    public Candidate getCandidateByToken(String token) {
        return candidateRepository.findByOnboardingToken(token)
            .orElseThrow(() -> new RuntimeException("Invalid token"));
    }

    // ── deleteCandidate ──────────────────────────────
    public void deleteCandidate(Long id) {
        candidateRepository.deleteById(id);
    }
}
