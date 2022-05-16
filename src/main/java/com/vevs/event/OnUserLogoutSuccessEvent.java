package com.vevs.event;

import com.vevs.entity.virtualObject.LogoutRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class OnUserLogoutSuccessEvent extends ApplicationEvent {
  private static final long serialVersionUID = 1L;

  private final String userEmail;

  private final String jwtToken;

  private final transient LogoutRequest logoutRequest;

  private final Date eventTime;

  public OnUserLogoutSuccessEvent(String userEmail, String jwtToken, LogoutRequest logoutRequest) {
    super(userEmail);
    this.userEmail = userEmail;
    this.jwtToken = jwtToken;
    this.logoutRequest = logoutRequest;
    this.eventTime = Date.from(Instant.now());
  }
}
