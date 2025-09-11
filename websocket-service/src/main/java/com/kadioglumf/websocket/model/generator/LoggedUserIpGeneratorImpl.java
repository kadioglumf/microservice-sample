package com.kadioglumf.websocket.model.generator;

import static org.hibernate.generator.EventTypeSets.INSERT_AND_UPDATE;

import com.kadioglumf.websocket.core.enums.IpTypeEnum;
import com.kadioglumf.websocket.util.UserDeviceDetailsUtils;
import java.util.EnumSet;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

public class LoggedUserIpGeneratorImpl implements BeforeExecutionGenerator {

  @Override
  public Object generate(
      SharedSessionContractImplementor sharedSessionContractImplementor,
      Object o,
      Object o1,
      EventType eventType) {
    return UserDeviceDetailsUtils.getIpAddr(IpTypeEnum.CLIENT);
  }

  @Override
  public EnumSet<EventType> getEventTypes() {
    return INSERT_AND_UPDATE;
  }
}
