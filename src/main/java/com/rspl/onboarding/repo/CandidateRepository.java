package com.rspl.onboarding.repo;

import com.rspl.onboarding.domain.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByOnboardingToken(String token);
    Optional<Candidate> findByEmailId(String emailId);
}
