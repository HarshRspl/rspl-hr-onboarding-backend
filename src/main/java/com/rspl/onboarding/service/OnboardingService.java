package com.rspl.onboarding.service;

import com.rspl.onboarding.api.*;
import com.rspl.onboarding.domain.*;
import com.rspl.onboarding.repo.CandidateRepository;
import com.rspl.onboarding.repo.HrTeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class OnboardingService {

  private static final Logger log = LoggerFactory.getLogger(OnboardingService.class);
  private static final SecureRandom random = new SecureRandom();
  private static final HexFormat hex = HexFormat.of();
  private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      .withZone(ZoneId.systemDefault());

  private final CandidateRepository candidateRepo;
  private final HrTeamRepository hrRepo;

  public OnboardingService(CandidateRepository candidateRepo, HrTeamRepository hrRepo) {
    this.candidateRepo = candidateRepo;
    this.hrRepo = hrRepo;
  }

  public PageDto<CandidateDto> listCandidates(int page, int size) {
    Page<Candidate> p = candidateRepo.findAll(PageRequest.of(page, size));
    var content = p.getContent().stream().sorted((a,b) -> Long.compare(b.getId(), a.getId()))
        .map(this::toDto).toList();
    return new PageDto<>(content, page, size, p.getTotalElements(), p.getTotalPages());
  }

  public CandidateDto initiate(InitiateCandidateRequest req) {
    // Aadhaar: allow empty, validate if present
    if (req.getAadhaarNo() != null && !req.getAadhaarNo().isBlank()) {
      if (!req.getAadhaarNo().matches("^\d{12}$")) {
        throw new ValidationException("aadhaarNo must be 12 digits");
      }
    }

    Optional<Candidate> existing = candidateRepo.findByEmailIdIgnoreCase(req.getEmailId());
    if (existing.isPresent()) {
      throw new ConflictException("EMAIL_ALREADY_EXISTS", existing.get().getId());
    }

    Candidate c = new Candidate();
    c.setEmployeeName(req.getEmployeeName().trim());
    c.setAadhaarNo(req.getAadhaarNo() == null ? "" : req.getAadhaarNo().trim());
    c.setEmailId(req.getEmailId().trim());
    c.setMobileNo(req.getMobileNo().trim());
    c.setDesignation(req.getDesignation().trim());
    c.setJoiningStatus(JoiningStatus.INITIATED);
    c.setInitiatedBy(req.getInitiatedBy() == null ? "hr_admin" : req.getInitiatedBy());

    if (req.getAssignedHRId() != null) {
      HrTeamMember hr = hrRepo.findById(req.getAssignedHRId())
          .orElseThrow(() -> new ValidationException("assignedHRId not found"));
      c.setAssignedHR(hr);
    }

    boolean send = req.getSendLinkImmediately() == null || req.getSendLinkImmediately();
    c.setLinkStatus(send ? LinkStatus.SENT : LinkStatus.NOT_SENT);
    String token = newToken();
    c.setOnboardingToken(token);
    c.setOnboardingTokenExpiresAt(Instant.now().plusSeconds(7 * 24 * 3600));

    Candidate saved = candidateRepo.save(c);

    if (send) {
      // Stub: just log. Replace with email via SMTP / Microsoft Graph later.
      String onboardingLink = "http://localhost:5500/candidate.html?token=" + token;
      log.info("[SEND_LINK] to: {} link: {}", saved.getEmailId(), onboardingLink);
    }

    return toDto(saved);
  }

  @Transactional
  public void approve(long id) {
    Candidate c = candidateRepo.findById(id).orElseThrow(() -> new NotFoundException("NOT_FOUND"));
    if (c.getJoiningStatus() == JoiningStatus.REJECTED) {
      throw new ConflictException("CANNOT_APPROVE_REJECTED", id);
    }
    c.setJoiningStatus(JoiningStatus.APPROVED);
  }

  @Transactional
  public void reject(long id, String reason) {
    Candidate c = candidateRepo.findById(id).orElseThrow(() -> new NotFoundException("NOT_FOUND"));
    if (c.getJoiningStatus() == JoiningStatus.APPROVED) {
      throw new ConflictException("CANNOT_REJECT_APPROVED", id);
    }
    c.setJoiningStatus(JoiningStatus.REJECTED);
    c.setRejectionReason(reason.trim());
  }

  @Transactional
  public void sendLink(long id) {
    Candidate c = candidateRepo.findById(id).orElseThrow(() -> new NotFoundException("NOT_FOUND"));
    String token = newToken();
    c.setOnboardingToken(token);
    c.setOnboardingTokenExpiresAt(Instant.now().plusSeconds(7 * 24 * 3600));
    c.setLinkStatus(LinkStatus.SENT);

    String onboardingLink = "http://localhost:5500/candidate.html?token=" + token;
    log.info("[SEND_LINK] to: {} link: {}", c.getEmailId(), onboardingLink);
  }

  private CandidateDto toDto(Candidate c) {
    CandidateDto d = new CandidateDto();
    d.setId(c.getId());
    d.setEmployeeName(c.getEmployeeName());
    d.setEmailId(c.getEmailId());
    d.setMobileNo(c.getMobileNo());
    d.setAadhaarNo(c.getAadhaarNo() == null ? "" : c.getAadhaarNo());
    d.setDesignation(c.getDesignation());
    d.setJoiningStatus(c.getJoiningStatus().name());
    d.setLinkStatus(c.getLinkStatus().name());
    if (c.getAssignedHR() != null) {
      d.setAssignedHR(new AssignedHrDto(c.getAssignedHR().getId(), c.getAssignedHR().getName()));
    }
    if (c.getCreatedAt() != null) {
      d.setCreatedAt(DATE.format(c.getCreatedAt()));
    }
    if (c.getRejectionReason() != null && !c.getRejectionReason().isBlank()) {
      d.setRejectionReason(c.getRejectionReason());
    }
    return d;
  }

  private String newToken() {
    byte[] b = new byte[12];
    random.nextBytes(b);
    return hex.formatHex(b); // 24-char token
  }

  // --- Exception types used by controller advice ---
  public static class NotFoundException extends RuntimeException {
    public NotFoundException(String m) { super(m); }
  }
  public static class ValidationException extends RuntimeException {
    public ValidationException(String m) { super(m); }
  }
  public static class ConflictException extends RuntimeException {
    private final Object details;
    public ConflictException(String m, Object details) { super(m); this.details = details; }
    public Object getDetails() { return details; }
  }
}
