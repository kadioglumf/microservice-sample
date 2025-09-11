package com.kadioglumf.dataservice.base;

import static org.mockito.Mockito.mockStatic;

import com.kadioglumf.dataservice.core.config.SpringBeanContext;
import com.kadioglumf.dataservice.core.dto.UserDto;
import com.kadioglumf.dataservice.core.enums.RoleTypeEnum;
import com.kadioglumf.dataservice.core.exception.ExceptionMetaData;
import com.kadioglumf.dataservice.util.UserThreadContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@EnableAutoConfiguration
public abstract class BaseTest {

  @Mock private ExceptionMetaData exceptionMetaData;
  private MockedStatic<SpringBeanContext> springBeanContextMockedStatic;
  protected MockedStatic<UserThreadContext> userThreadContextMockedStatic;

  @BeforeEach
  public void beforeEach() {
    springBeanContextMockedStatic = mockStatic(SpringBeanContext.class);
    springBeanContextMockedStatic
        .when(SpringBeanContext.getBean("exceptionMetaData"))
        .thenReturn(exceptionMetaData);
    springBeanContextMockedStatic
        .when(() -> SpringBeanContext.getBean(ExceptionMetaData.class))
        .thenReturn(exceptionMetaData);
    springBeanContextMockedStatic
        .when(() -> SpringBeanContext.getBean("businessExceptionMessageSource"))
        .thenReturn(exceptionMetaData);
    springBeanContextMockedStatic
        .when(() -> SpringBeanContext.getBean("applicationExceptionMessageSource"))
        .thenReturn(exceptionMetaData);

    userThreadContextMockedStatic = mockStatic(UserThreadContext.class);
    userThreadContextMockedStatic.when(UserThreadContext::getUser).thenReturn(adminUser());

    /*        when(exceptionMetaData.getExceptionMessagesFromSource(any(BaseException.class))).thenAnswer(
            invocation -> {
                BaseException exception = invocation.getArgument(0);
                return new SimplifiedExceptionLog(exception.getExceptionCode(), "log message");
            }
    );*/
  }

  protected UserDto adminUser() {
    return UserDto.builder().id(1L).email("admin@mail.com").role(RoleTypeEnum.ROLE_ADMIN).build();
  }

  protected UserDto partnerUser() {
    return UserDto.builder()
        .id(3L)
        .email("partner@mail.com")
        .role(RoleTypeEnum.ROLE_PARTNER)
        .build();
  }

  protected UserDto user() {
    return UserDto.builder().id(2L).email("user@mail.com").role(RoleTypeEnum.ROLE_USER).build();
  }

  @AfterEach
  public void afterEach() {
    springBeanContextMockedStatic.close();
    userThreadContextMockedStatic.close();
  }
}
