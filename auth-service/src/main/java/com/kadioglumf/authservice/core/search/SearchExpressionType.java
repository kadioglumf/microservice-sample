package com.kadioglumf.authservice.core.search;

import com.kadioglumf.authservice.constant.ExceptionConstants;
import com.kadioglumf.authservice.core.exception.BusinessException;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import org.springframework.util.CollectionUtils;

public enum SearchExpressionType {
  USER_DETAILS {

    private final HashMap<SearchRoleType, List<String>> USER_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(SearchRoleType.ADMIN, ExpressionKeyConstants.getUserListAdminKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, USER_WHITE_LIST_PARAMETERS.get(searchRoleType));

      return switch (key) {
        case ExpressionKeyConstants.CREATION_DATE,
                ExpressionKeyConstants.NAME,
                ExpressionKeyConstants.SURNAME,
                ExpressionKeyConstants.EMAIL,
                ExpressionKeyConstants.ACTIVITY,
                ExpressionKeyConstants.ROLE ->
            root.get(key);
        case ExpressionKeyConstants.USER_ID -> root.get(ExpressionKeyConstants.ID);
        default -> throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      };
    }
  },

  GET_PARTNER_ACCOUNTS {

    private final HashMap<SearchRoleType, List<String>> GET_PARTNER_ACCOUNT_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(SearchRoleType.ADMIN, ExpressionKeyConstants.getPartnerListAdminKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, GET_PARTNER_ACCOUNT_WHITE_LIST_PARAMETERS.get(searchRoleType));

      return switch (key) {
        case ExpressionKeyConstants.CREATION_DATE,
                ExpressionKeyConstants.NAME,
                ExpressionKeyConstants.SURNAME,
                ExpressionKeyConstants.EMAIL,
                ExpressionKeyConstants.ACTIVITY ->
            root.get(key);
        case ExpressionKeyConstants.USER_ID -> root.get(ExpressionKeyConstants.ID);
        case ExpressionKeyConstants.CONSULTANT_ID ->
            root.join(ExpressionKeyConstants.PARTNER).get(key);
        case ExpressionKeyConstants.COMPANY_NAME ->
            root.join(ExpressionKeyConstants.PARTNER)
                .join(ExpressionKeyConstants.COMPANY)
                .get(ExpressionKeyConstants.NAME);
        default -> throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      };
    }
  },

  GET_REGISTRATION_APPLICATIONS {

    private final HashMap<SearchRoleType, List<String>>
        GET_REGISTRATION_APPLICATIONS_WHITE_LIST_PARAMETERS =
            new HashMap<>() {
              {
                put(
                    SearchRoleType.ADMIN,
                    ExpressionKeyConstants.getRegistrationApplicationListAdminKeys());
              }
            };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, GET_REGISTRATION_APPLICATIONS_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.CREATION_DATE:
        case ExpressionKeyConstants.LAST_UPDATE_DATE:
        case ExpressionKeyConstants.NAME:
        case ExpressionKeyConstants.SURNAME:
        case ExpressionKeyConstants.EMAIL:
        case ExpressionKeyConstants.MOBILE:
          return root.get(key);
        case ExpressionKeyConstants.COMPANY_NAME:
          return root.join(ExpressionKeyConstants.COMPANY).get(key);
        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  };

  public abstract <T> Expression<?> getExpression(
      Root<T> root, String key, SearchRoleType searchRoleType);

  public void checkSearchKey(String key, List<String> whiteListKeys) {
    if (CollectionUtils.isEmpty(whiteListKeys)) {
      throw new BusinessException(ExceptionConstants.FORBIDDEN_ERROR);
    }
    if (!whiteListKeys.contains(key)) {
      throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
    }
  }
}
