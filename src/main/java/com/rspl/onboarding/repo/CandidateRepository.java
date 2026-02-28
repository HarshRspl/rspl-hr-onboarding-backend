package com.rspl.onboarding.repo;

import com.rspl.onboarding.domain.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    // ── Existing ──────────────────────────────────────────────────────────────
    Optional<Candidate> findByOnboardingToken(String token);
    Optional<Candidate> findByEmailId(String emailId);

    // ── Stats ─────────────────────────────────────────────────────────────────
    Long countByJoiningStatus(String joiningStatus);

    // ── Filtered listing (status + search combinations) ───────────────────────

    // status only
    Page<Candidate> findByJoiningStatus(
            String joiningStatus, Pageable pageable);

    // search only (name OR mobile)
    @Query("SELECT c FROM Candidate c WHERE " +
           "LOWER(c.employeeName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "c.mobileNo LIKE CONCAT('%', :search, '%')")
    Page<Candidate> findBySearch(
            @Param("search") String search, Pageable pageable);

    // status + search combined
    @Query("SELECT c FROM Candidate c WHERE " +
           "c.joiningStatus = :status AND (" +
           "LOWER(c.employeeName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "c.mobileNo LIKE CONCAT('%', :search, '%'))")
    Page<Candidate> findByJoiningStatusAndSearch(
            @Param("status") String status,
            @Param("search") String search,
            Pageable pageable);
}
