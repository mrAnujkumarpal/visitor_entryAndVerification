package com.vevs.event;

import com.vevs.cache.LoggedOutJwtTokenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnUserLogoutSuccessEventListener implements ApplicationListener<OnUserLogoutSuccessEvent> {
  private static final Logger logger = LoggerFactory.getLogger(OnUserLogoutSuccessEventListener.class);
  private final LoggedOutJwtTokenCache tokenCache;

  @Autowired
  OnUserLogoutSuccessEventListener(LoggedOutJwtTokenCache tokenCache) {
    this.tokenCache = tokenCache;
  }

  public void onApplicationEvent(OnUserLogoutSuccessEvent event) {
    if(null !=null){
      logger.info(String.format("Logout success event received for user {} ", event.getUserEmail()));
      tokenCache.markLogoutEventForToken(event);
    }
  }
}
