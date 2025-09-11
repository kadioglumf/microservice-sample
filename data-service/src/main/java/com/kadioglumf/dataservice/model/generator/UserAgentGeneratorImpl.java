package com.kadioglumf.dataservice.model.generator;

import static org.hibernate.generator.EventTypeSets.INSERT_AND_UPDATE;

import com.kadioglumf.dataservice.util.UserDeviceDetailsUtils;
import java.util.EnumSet;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

public class UserAgentGeneratorImpl implements BeforeExecutionGenerator {

  @Override
  public Object generate(
      SharedSessionContractImplementor sharedSessionContractImplementor,
      Object o,
      Object o1,
      EventType eventType) {
    return UserDeviceDetailsUtils.getUserAgent();
  }

  @Override
  public EnumSet<EventType> getEventTypes() {
    return INSERT_AND_UPDATE;
  }
}
