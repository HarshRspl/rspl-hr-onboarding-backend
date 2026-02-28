package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.Candidate;
import com.rspl.onboarding.domain.CandidateDocument;
import com.rspl.onboarding.repo.CandidateDocumentRepository;
import com.rspl.onboarding.repo.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/candidate/documents")
@CrossOrigin(origins = "*")
public class CandidateDocumentController {

    @Autowired private CandidateRepository candidateRepository;
    @Autowired private CandidateDocumentRepository documentRepository;

    private static final String UPLOAD_ROOT = "/app/uploads";
    private static final long MAX_BYTES = 5L * 1024 * 1024; // 5MB

    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("token") String token,
            @RequestParam("docType") String docType,
            @RequestParam("file") MultipartFile file
    ) {
        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid token"));
        Candidate c = opt.get();

        if (c.getTokenExpiry() != null && c.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Token expired"));
        }

        if (file == null || file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("success", false, "message", "File required"));
        if (file.getSize() > MAX_BYTES) return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Max file size is 5MB"));

        String contentType = (file.getContentType() != null) ? file.getContentType() : "";
        boolean allowed = contentType.startsWith("image/") || "application/pdf".equals(contentType);
        if (!allowed) return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Only PDF/JPG/PNG allowed"));

        try {
            Long candidateId = c.getId();
            String folderPath = UPLOAD_ROOT + "/" + candidateId;
            new File(folderPath).mkdirs();

            String originalName = (file.getOriginalFilename() != null) ? file.getOriginalFilename() : "document";
            String ext = "";
            int dot = originalName.lastIndexOf(".");
            if (dot > -1) ext = originalName.substring(dot);

            String storedName = UUID.randomUUID().toString() + ext;
            File dest = new File(folderPath + "/" + storedName);
            file.transferTo(dest);

            CandidateDocument doc = new CandidateDocument();
            doc.setCandidateId(candidateId);
            doc.setDocType(docType.toUpperCase());
            doc.setOriginalName(originalName);
            doc.setStoredName(storedName);
            doc.setContentType(contentType);
            doc.setFileSize(file.getSize());
            doc.setStoragePath(dest.getAbsolutePath());

            CandidateDocument saved = documentRepository.save(doc);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Uploaded",
                    "data", Map.of(
                            "id", saved.getId(),
                            "docType", saved.getDocType(),
                            "originalName", saved.getOriginalName(),
                            "fileSize", saved.getFileSize(),
                            "uploadedAt", saved.getUploadedAt().toString()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Upload failed: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(@RequestParam("token") String token) {
        Optional<Candidate> opt = candidateRepository.findByOnboardingToken(token);
        if (opt.isEmpty()) return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid token"));

        Candidate c = opt.get();
        List<CandidateDocument> docs = documentRepository.findByCandidateIdOrderByUploadedAtDesc(c.getId());

        List<Map<String, Object>> out = new ArrayList<>();
        for (CandidateDocument d : docs) {
            out.add(Map.of(
                    "id", d.getId(),
                    "docType", d.getDocType(),
                    "originalName", d.getOriginalName(),
                    "fileSize", d.getFileSize(),
                    "uploadedAt", d.getUploadedAt().toString()
            ));
        }
        return ResponseEntity.ok(Map.of("success", true, "data", out));
    }

    @GetMapping("/download/{docId}")
    public ResponseEntity<byte[]> download(@PathVariable Long docId) {
        CandidateDocument doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        try {
            File f = new File(doc.getStoragePath());
            if (!f.exists()) return ResponseEntity.notFound().build();

            byte[] bytes = Files.readAllBytes(f.toPath());
            MediaType mt = (doc.getContentType() != null && !doc.getContentType().isBlank())
                    ? MediaType.parseMediaType(doc.getContentType())
                    : MediaType.APPLICATION_OCTET_STREAM;

            return ResponseEntity.ok()
                    .contentType(mt)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getOriginalName() + "\"")
                    .body(bytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
