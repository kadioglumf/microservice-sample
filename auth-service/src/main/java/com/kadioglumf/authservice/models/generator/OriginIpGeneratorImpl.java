package com.kadioglumf.authservice.models.generator;

import static org.hibernate.generator.EventTypeSets.INSERT_AND_UPDATE;

import com.kadioglumf.authservice.enums.IpTypeEnum;
import com.kadioglumf.authservice.util.UserDeviceDetailsUtils;
import java.util.EnumSet;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

public class OriginIpGeneratorImpl implements BeforeExecutionGenerator {
  @Override
  public Object generate(
      SharedSessionContractImplementor sharedSessionContractImplementor,
      Object o,
      Object o1,
      EventType eventType) {
    return UserDeviceDetailsUtils.getIpAddr(IpTypeEnum.ORIGIN);
  }

  @Override
  public EnumSet<EventType> getEventTypes() {
    return INSERT_AND_UPDATE;
  }
}
