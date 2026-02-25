package com.rspl.onboarding.config;

import com.rspl.onboarding.domain.*;
import com.rspl.onboarding.repo.CandidateRepository;
import com.rspl.onboarding.repo.HrTeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class DataSeeder {

  @Bean
  public CommandLineRunner seed(HrTeamRepository hrRepo, CandidateRepository candRepo) {
    return args -> {
      if (hrRepo.count() == 0) {
        hrRepo.save(new HrTeamMember(1, "Sneha Kapoor", "Sr. HR Executive", "sneha@rspl.com", "9811001100"));
        hrRepo.save(new HrTeamMember(2, "Arjun Mehta", "HR Executive", "arjun@rspl.com", "9822002200"));
        hrRepo.save(new HrTeamMember(3, "Pooja Nair", "HR Executive", "pooja@rspl.com", "9833003300"));
        hrRepo.save(new HrTeamMember(4, "Kunal Bose", "Jr. HR Executive", "kunal@rspl.com", "9844004400"));
        hrRepo.save(new HrTeamMember(5, "Divya Rawat", "HR Manager", "divya@rspl.com", "9855005500"));
      }

      if (candRepo.count() == 0) {
        var hr1 = hrRepo.findById(1).orElse(null);
        var hr2 = hrRepo.findById(2).orElse(null);
        var hr3 = hrRepo.findById(3).orElse(null);
        var hr4 = hrRepo.findById(4).orElse(null);
        var hr5 = hrRepo.findById(5).orElse(null);

        candRepo.save(make("Amit Kumar", "amit@test.com", "9876543210", "Sales Executive", JoiningStatus.INITIATED, LinkStatus.SENT, hr1));
        candRepo.save(make("Priya Sharma", "priya@test.com", "9123456780", "HR Manager", JoiningStatus.APPROVED, LinkStatus.OPENED, hr5));
        candRepo.save(make("Rahul Singh", "rahul@test.com", "9988776655", "Engineer", JoiningStatus.SIGNED, LinkStatus.OPENED, hr2));
        candRepo.save(make("Neha Gupta", "neha@test.com", "9871234560", "Accountant", JoiningStatus.FORM_SUBMITTED, LinkStatus.OPENED, hr3));
        candRepo.save(make("Vikram Patel", "vikram@test.com", "9765432100", "Manager", JoiningStatus.INITIATED, LinkStatus.NOT_SENT, hr1));

        Candidate rej = make("Ravi Verma", "ravi@test.com", "9654321090", "Developer", JoiningStatus.REJECTED, LinkStatus.SENT, hr4);
        rej.setRejectionReason("Incomplete documents");
        candRepo.save(rej);

        candRepo.save(make("Anita Mishra", "anita@test.com", "9543210980", "Team Lead", JoiningStatus.APPROVED, LinkStatus.OPENED, hr3));
        candRepo.save(make("Kavya Reddy", "kavya@test.com", "9432109870", "Data Scientist", JoiningStatus.APPROVED, LinkStatus.OPENED, hr5));
      }
    };
  }

  private Candidate make(String name, String email, String mobile, String desig,
                         JoiningStatus js, LinkStatus ls, HrTeamMember hr) {
    Candidate c = new Candidate();
    c.setEmployeeName(name);
    c.setEmailId(email);
    c.setMobileNo(mobile);
    c.setDesignation(desig);
    c.setJoiningStatus(js);
    c.setLinkStatus(ls);
    c.setAssignedHR(hr);
    c.setInitiatedBy("seed");
    // Ensure createdAt set even when ddl-auto update doesn't call entity lifecycle? It does, but set to be safe.
    c.setCreatedAt(Instant.now());
    c.setUpdatedAt(Instant.now());
    return c;
  }
}
