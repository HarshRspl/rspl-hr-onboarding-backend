package com.rspl.onboarding.config;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.repo.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CandidateRepository candidateRepository;

    @Override
    public void run(String... args) {
        if (candidateRepository.count() > 0) return;

        // Rejected candidate sample
        Candidate rej = new Candidate();
        rej.setEmployeeName("Rejected User");
        rej.setEmailId("rejected@rspl.com");          // ✅ was setEmail()
        rej.setMobileNo("9000000001");
        rej.setDesignation("Sales");
        rej.setAssignedHRId(1L);                      // ✅ was setAssignedHr()
        rej.setAssignedHRName("HR Admin");            // ✅ new field
        rej.setJoiningStatus("REJECTED");             // ✅ was setStatus()
        rej.setRejectionReason("Did not pass background check");
        rej.setOnboardingToken(UUID.randomUUID().toString());
        rej.setCreatedAt(LocalDateTime.now());
        rej.setUpdatedAt(LocalDateTime.now());
        candidateRepository.save(rej);

        // Pending candidate sample
        Candidate c = new Candidate();
        c.setEmployeeName("New Joinee");
        c.setEmailId("joinee@rspl.com");              // ✅ was setEmail()
        c.setMobileNo("9000000002");
        c.setDesignation("Manager");
        c.setAssignedHRId(1L);                        // ✅ was setAssignedHr()
        c.setAssignedHRName("HR Admin");              // ✅ new field
        c.setJoiningStatus("INITIATED");              // ✅ was setStatus("PENDING")
        c.setOnboardingToken(UUID.randomUUID().toString());
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        candidateRepository.save(c);
    }
}
