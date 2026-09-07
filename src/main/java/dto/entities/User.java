package dto.entities;

import java.math.BigDecimal;

public class User {
  private String username;
  private String email;
  private Long id;
  private BigDecimal balance;

  public User() {
  }

  public User(Long id, String username, String email, BigDecimal balance) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getBalance() {
    return balance;
  }
}
