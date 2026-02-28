package com.rspl.onboarding.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "candidate_documents")
public class CandidateDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="candidate_id", nullable=false)
    private Long candidateId;

    @Column(name="doc_type", nullable=false)
    private String docType; // AADHAAR, PAN, BANK_PROOF, PHOTO, EDU_EXP

    @Column(name="original_name")
    private String originalName;

    @Column(name="stored_name", nullable=false)
    private String storedName;

    @Column(name="content_type")
    private String contentType;

    @Column(name="file_size")
    private Long fileSize;

    @Column(name="storage_path", length = 700)
    private String storagePath;

    @Column(name="uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() { return id; }

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }

    public String getDocType() { return docType; }
    public void setDocType(String docType) { this.docType = docType; }

    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }

    public String getStoredName() { return storedName; }
    public void setStoredName(String storedName) { this.storedName = storedName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public String getStoragePath() { return storagePath; }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
