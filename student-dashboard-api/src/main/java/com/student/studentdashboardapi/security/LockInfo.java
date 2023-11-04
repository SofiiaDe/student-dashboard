package com.student.studentdashboardapi.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Clock;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LockInfo {

  private static final int LOCKING_TIME_IN_MINUTES = 5;
  private static final int MAX_ATTEMPTS = 3;

  private String username;
  private LocalDateTime lastLoginDatetime;
  private int loginAttempts;

  public boolean isLockTimeExpired() {
    return LocalDateTime.now(Clock.systemUTC()).isAfter(getLockedUntilDateTime());
  }

  public LocalDateTime getLockedUntilDateTime() {
    return lastLoginDatetime.plusMinutes(LOCKING_TIME_IN_MINUTES);
  }

  public boolean isReachedMaxLoginAttempts() {
    return loginAttempts >= MAX_ATTEMPTS;
  }

  public void updateLoginAttemptsAndLoginDateTime() {
    loginAttempts++;
    this.lastLoginDatetime = LocalDateTime.now(Clock.systemUTC());
  }
}
