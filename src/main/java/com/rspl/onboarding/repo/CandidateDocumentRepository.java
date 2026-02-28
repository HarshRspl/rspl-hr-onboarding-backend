package com.rspl.onboarding.repo;

import com.rspl.onboarding.domain.CandidateDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CandidateDocumentRepository extends JpaRepository<CandidateDocument, Long> {
    List<CandidateDocument> findByCandidateIdOrderByUploadedAtDesc(Long candidateId);
}
