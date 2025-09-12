package com.kadioglumf.dataservice.listener;

import com.kadioglumf.dataservice.constant.RedisCacheValueConstants;
import com.kadioglumf.dataservice.event.EmailChangedEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EmailChangedEventLister extends BaseListener<EmailChangedEvent> {

  @EventListener
  @Override
  @CacheEvict(
      value = RedisCacheValueConstants.FIVE_MINUTE_CACHE_KEY,
      key = "'findByEmail,' + #event.getEmail()")
  public void handle(EmailChangedEvent event) {
    log.info("EmailChangedEventListener: {}", event);
  }
}
