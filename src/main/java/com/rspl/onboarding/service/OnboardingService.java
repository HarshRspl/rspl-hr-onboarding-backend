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

    public List<Map<String, Object>> getHRTeam() {
        List<Map<String, Object>> team = new ArrayList<>();
        team.add(Map.of("id",1,"name","Sneha Kapoor","role","Sr. HR Executive","email","sneha@rspl.com","phone","9811001100"));
        team.add(Map.of("id",2,"name","Arjun Mehta", "role","HR Executive",    "email","arjun@rspl.com","phone","9822002200"));
        team.add(Map.of("id",3,"name","Pooja Nair",  "role","HR Executive",    "email","pooja@rspl.com","phone","9833003300"));
        team.add(Map.of("id",4,"name","Kunal Bose",  "role","Jr. HR Executive","email","kunal@rspl.com","phone","9844004400"));
        team.add(Map.of("id",5,"name","Divya Rawat", "role","HR Manager",      "email","divya@rspl.com","phone","9855005500"));
        return team;
    }

    private String resolveHRName(Long hrId) {
        if (hrId == null) return "HR Admin";
        Map<Long, String> names = Map.of(
            1L,"Sneha Kapoor", 2L,"Arjun Mehta",
            3L,"Pooja Nair",   4L,"Kunal Bose", 5L,"Divya Rawat"
        );
        return names.getOrDefault(hrId, "HR Admin");
    }

    // ✅ UPDATED — now supports status filter + search
    public Page<Candidate> listCandidates(int page, int size, String status, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        boolean hasStatus = status != null && !status.isBlank();
        boolean hasSearch = search != null && !search.isBlank();

        if (hasStatus && hasSearch)
            return candidateRepository.findByJoiningStatusAndSearch(status, search, pageable);
        else if (hasStatus)
            return candidateRepository.findByJoiningStatus(status, pageable);
        else if (hasSearch)
            return candidateRepository.findBySearch(search, pageable);
        else
            return candidateRepository.findAll(pageable);
    }

    // ✅ ADDED — for GET /candidates/{id}
    public Candidate getCandidate(long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found: " + id));
    }

    // ✅ ADDED — for stats total count
    public long countAll() {
        return candidateRepository.count();
    }

    // ✅ ADDED — for stats per-status count
    public long countByStatus(String status) {
        return candidateRepository.countByJoiningStatus(status);
    }

    public Candidate initiate(InitiateCandidateRequest request) {
        Candidate candidate = new Candidate();
        candidate.setEmployeeName(request.getEmployeeName());

        String email = request.getEmailId() != null
            ? request.getEmailId() : request.getEmail();
        candidate.setEmailId(email);

        candidate.setMobileNo(request.getMobileNo());
        candidate.setAadhaarNo(request.getAadhaarNo());
        candidate.setDesignation(request.getDesignation());
        candidate.setInitiatedBy(
            request.getInitiatedBy() != null ? request.getInitiatedBy() : "hradmin"
        );

        Long hrId = request.getAssignedHRId();
        candidate.setAssignedHRId(hrId);
        candidate.setAssignedHRName(resolveHRName(hrId));

        candidate.setJoiningStatus("INITIATED");
        candidate.setLinkStatus("NOT_SENT");

        String token = UUID.randomUUID().toString();
        candidate.setOnboardingToken(token);
        candidate.setTokenExpiry(LocalDateTime.now().plusDays(7));
        candidate.setCreatedAt(LocalDateTime.now());
        candidate.setUpdatedAt(LocalDateTime.now());

        Candidate saved = candidateRepository.save(candidate);

        Boolean sendLink = request.getSendLinkImmediately();
        if (sendLink == null || sendLink) {
            String portalUrl = "https://rspl-hr-onboarding-backend-production.up.railway.app/onboarding.html?token=" + token;
            String smsText   = "Welcome to RSPL! Complete your joining: " + portalUrl + " Valid 7 days. -RSPL HR";
            try {
                smsService.sendSms(saved.getMobileNo(), smsText);
                saved.setLinkStatus("SENT");
                candidateRepository.save(saved);
            } catch (Exception e) {
                System.err.println("SMS failed for " + saved.getMobileNo() + ": " + e.getMessage());
            }
        }
        return saved;
    }

    public Candidate approve(long id) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));
        c.setJoiningStatus("APPROVED");
        long count = candidateRepository.count();
        String empId = "RSPL-" + java.time.Year.now().getValue() + "-"
            + String.format("%03d", count);
        c.setEmployeeId(empId);
        c.setUpdatedAt(LocalDateTime.now());
        return candidateRepository.save(c);
    }

    public Candidate reject(long id, String reason) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));
        c.setJoiningStatus("REJECTED");
        c.setRejectionReason(reason);
        c.setUpdatedAt(LocalDateTime.now());
        return candidateRepository.save(c);
    }

    public Candidate sendLink(long id) {
        Candidate c = candidateRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Candidate not found"));
        String portalUrl = "https://rspl-hr-onboarding-backend-production.up.railway.app/onboarding.html?token=" + c.getOnboardingToken();
        String smsText   = "Welcome to RSPL! Complete your joining: " + portalUrl + " Valid 7 days. -RSPL HR";
        try {
            smsService.sendSms(c.getMobileNo(), smsText);
            c.setLinkStatus("SENT");
            c.setUpdatedAt(LocalDateTime.now());
            candidateRepository.save(c);
        } catch (Exception e) {
            System.err.println("SMS failed for " + c.getMobileNo() + ": " + e.getMessage());
        }
        return c;
    }
}
