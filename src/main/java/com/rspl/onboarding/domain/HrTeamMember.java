package com.rspl.onboarding.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hr_team")
public class HrTeamMember {

  @Id
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String role;

  @Column(nullable = false)
  private String email;

  private String phone;

  public HrTeamMember() {}

  public HrTeamMember(Integer id, String name, String role, String email, String phone) {
    this.id = id;
    this.name = name;
    this.role = role;
    this.email = email;
    this.phone = phone;
  }

  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
}
