package com.kadioglumf.websocket.core.secure;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.core.dto.PermissionDto;
import com.kadioglumf.websocket.core.dto.UserDto;
import com.kadioglumf.websocket.util.UserThreadContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Predicate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Aspect
@Component
public class SecureValidator {

  @Before("@annotation(com.kadioglumf.websocket.core.secure.SecureGroup)")
  public void validSecureGroup(JoinPoint jp) throws NoSuchMethodException {
    Class<?> targetClass = jp.getTarget().getClass();
    Method interfaceMethod = ((MethodSignature) jp.getSignature()).getMethod();
    Method implementationMethod =
        targetClass.getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
    SecureGroup secureGroup = implementationMethod.getAnnotation(SecureGroup.class);
    boolean isValid = false;
    for (Secures secures : secureGroup.value()) {
      isValid = validateSecures(secures);
      if (isValid) {
        break;
      }
    }
    if (!isValid) {
      throw new SecureException(ExceptionConstants.UNAUTHORIZED);
    }
  }

  @Before("@annotation(com.kadioglumf.websocket.core.secure.Secures)")
  public void validSecures(JoinPoint jp) throws NoSuchMethodException {
    Class<?> targetClass = jp.getTarget().getClass();
    Method interfaceMethod = ((MethodSignature) jp.getSignature()).getMethod();
    Method implementationMethod =
        targetClass.getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
    Secures secures = implementationMethod.getAnnotation(Secures.class);
    boolean isValid = validateSecures(secures);
    if (!isValid) {
      throw new SecureException(ExceptionConstants.UNAUTHORIZED);
    }
  }

  private boolean validateSecures(Secures secures) {
    boolean isValid = false;
    for (Secure secure : secures.value()) {
      isValid = validateSecureAnnotation(secure);
      if ((isValid && secures.isAnyRole()) || (!isValid && !secures.isAnyRole())) {
        break;
      }
    }
    return isValid;
  }

  @Before("@annotation(com.kadioglumf.websocket.core.secure.Secure)")
  public void validSecure(JoinPoint jp) throws NoSuchMethodException {
    Class<?> targetClass = jp.getTarget().getClass();
    Method interfaceMethod = ((MethodSignature) jp.getSignature()).getMethod();
    Method implementationMethod =
        targetClass.getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
    Secure secure = implementationMethod.getAnnotation(Secure.class);
    if (!validateSecureAnnotation(secure)) {
      throw new SecureException(ExceptionConstants.UNAUTHORIZED);
    }
  }

  private boolean validateSecureAnnotation(Secure secure) {
    UserDto userDto = UserThreadContext.getUser();
    if (userDto == null) {
      throw new SecureException(ExceptionConstants.UNAUTHORIZED);
    }
    boolean isValid = true;

    if (!CollectionUtils.isEmpty(Arrays.asList(secure.role()))) {
      isValid = Arrays.stream(secure.role()).anyMatch(r -> r.equals(userDto.getRole()));
    }

    if (isValid && secure.permissions().length > 0) {
      isValid = userDto.getPermissions().stream().anyMatch(isAllMatchChain(secure));
    }
    return isValid;
  }

  private static Predicate<PermissionDto> isAllMatchChain(Secure secure) {
    return p ->
        Arrays.stream(secure.permissions())
            .anyMatch(
                requiredPermission -> {
                  if (requiredPermission.equals(p.getPermission())) {
                    return true;
                  }
                  if (p.getPermission().endsWith(".all")) {
                    String permissionPrefix =
                        p.getPermission().substring(0, p.getPermission().lastIndexOf(".all"));
                    return requiredPermission.startsWith(permissionPrefix);
                  }

                  return false;
                });
  }
}
