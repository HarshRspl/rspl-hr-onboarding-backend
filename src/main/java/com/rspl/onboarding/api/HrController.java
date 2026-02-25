package com.rspl.onboarding.api;

import com.rspl.onboarding.domain.HrTeamMember;
import com.rspl.onboarding.repo.HrTeamRepository;
import com.rspl.onboarding.service.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
public class HrController {

  private final HrTeamRepository hrRepo;
  private final OnboardingService service;

  public HrController(HrTeamRepository hrRepo, OnboardingService service) {
    this.hrRepo = hrRepo;
    this.service = service;
  }

  @GetMapping("/team")
  public ApiResponse<List<HrTeamMember>> team() {
    return ApiResponse.ok(hrRepo.findAll(), "OK");
  }

  @GetMapping("/candidates")
  public ApiResponse<PageDto<CandidateDto>> list(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "100") int size) {
    return ApiResponse.ok(service.listCandidates(page, Math.min(Math.max(size, 1), 500)), "OK");
  }

  @PostMapping("/candidates/initiate")
  public ApiResponse<CandidateDto> initiate(@Valid @RequestBody InitiateCandidateRequest req) {
    return ApiResponse.ok(service.initiate(req), "CANDIDATE_INITIATED");
  }

  @PostMapping("/candidates/{id}/approve")
  public ApiResponse<Boolean> approve(@PathVariable long id) {
    service.approve(id);
    return ApiResponse.ok(true, "APPROVED");
  }

  @PostMapping("/candidates/{id}/reject")
  public ApiResponse<Boolean> reject(@PathVariable long id, @Valid @RequestBody RejectRequest req) {
    service.reject(id, req.getReason());
    return ApiResponse.ok(true, "REJECTED");
  }

  @PostMapping("/candidates/{id}/send-link")
  public ApiResponse<Object> sendLink(@PathVariable long id) {
    service.sendLink(id);
    return ApiResponse.ok(new java.util.HashMap<String, Object>() {{ put("linkStatus", "SENT"); }}, "LINK_SENT");
  }
}
