package com.kadioglumf.dataservice.enums;

import com.kadioglumf.dataservice.constant.ExceptionConstants;
import com.kadioglumf.dataservice.constant.ExpressionKeyConstants;
import com.kadioglumf.dataservice.core.exception.BusinessException;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import org.springframework.util.CollectionUtils;

public enum SearchExpressionType {
  GET_CONTACT_US {

    private final HashMap<SearchRoleType, List<String>> CONTACT_US_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(SearchRoleType.ADMIN, ExpressionKeyConstants.getContactUsAdminKeys());
            put(
                SearchRoleType.ADMIN_OR_MODERATOR,
                ExpressionKeyConstants.getContactUsAdminOrModKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, CONTACT_US_WHITE_LIST_PARAMETERS.get(searchRoleType));

      return switch (key) {
        case ExpressionKeyConstants.CREATION_DATE,
                ExpressionKeyConstants.CONTACT_REQUEST_ID,
                ExpressionKeyConstants.STATUS,
                ExpressionKeyConstants.NAME,
                ExpressionKeyConstants.SURNAME,
                ExpressionKeyConstants.EMAIL,
                ExpressionKeyConstants.PHONE,
                ExpressionKeyConstants.SUBJECT ->
            root.get(key);
        case ExpressionKeyConstants.USER_ID -> root.get(ExpressionKeyConstants.ASSIGNED_PARTNER_ID);
        default -> throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST);
      };
    }
  },

  GET_CONSULTING_REQUESTS {

    private final HashMap<SearchRoleType, List<String>> HEALTH_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(
                SearchRoleType.ADMIN_OR_MODERATOR,
                ExpressionKeyConstants.getConsultingRequestsAdminOrModKeys());
            put(SearchRoleType.ADMIN, ExpressionKeyConstants.getConsultingRequestsAdminKeys());
            put(SearchRoleType.PARTNER, ExpressionKeyConstants.getConsultingRequestsPartnerKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, HEALTH_WHITE_LIST_PARAMETERS.get(searchRoleType));

      return switch (key) {
        case ExpressionKeyConstants.CONSULTING_REQUEST_ID,
                ExpressionKeyConstants.CREATION_DATE,
                ExpressionKeyConstants.STATUS,
                ExpressionKeyConstants.NAME,
                ExpressionKeyConstants.SURNAME,
                ExpressionKeyConstants.PHONE,
                ExpressionKeyConstants.CATEGORY,
                ExpressionKeyConstants.CALLBACK_TIME_END,
                ExpressionKeyConstants.CALLBACK_TIME_START,
                ExpressionKeyConstants.ASSIGNED_PARTNER_ID ->
            root.get(key);
        default -> throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST);
      };
    }
  },

  GET_SMS_COUNTER_LIST {

    private final HashMap<SearchRoleType, List<String>> GET_SMS_COUNTER_LIST_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(SearchRoleType.ADMIN, ExpressionKeyConstants.getSmsCounterListAdminKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, GET_SMS_COUNTER_LIST_WHITE_LIST_PARAMETERS.get(searchRoleType));

      return switch (key) {
        case ExpressionKeyConstants.ID,
                ExpressionKeyConstants.CREATED_DATE,
                ExpressionKeyConstants.UPDATED_DATE,
                ExpressionKeyConstants.USER_IP_ADDRESS,
                ExpressionKeyConstants.USER_AGENT,
                ExpressionKeyConstants.ORIGIN,
                ExpressionKeyConstants.RECIPIENT_NUMBER,
                ExpressionKeyConstants.NUMBER_OF_SMS_SENDING ->
            root.get(key);
        default -> throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST);
      };
    }
  },

  GET_SOCIAL_LEADS {

    private final HashMap<SearchRoleType, List<String>> SOCIAL_LEADS_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(SearchRoleType.ADMIN, ExpressionKeyConstants.getSocialLeadsAdminKeys());
            put(SearchRoleType.PARTNER, ExpressionKeyConstants.getSocialLeadsPartnerKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, SOCIAL_LEADS_WHITE_LIST_PARAMETERS.get(searchRoleType));

      return switch (key) {
        case ExpressionKeyConstants.CREATION_DATE_OF_VALUE,
                ExpressionKeyConstants.STATUS,
                ExpressionKeyConstants.NAME,
                ExpressionKeyConstants.PHONE,
                ExpressionKeyConstants.EMAIL,
                ExpressionKeyConstants.ASSIGNED_PARTNER_ID ->
            root.get(key);
        default -> throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST);
      };
    }
  },

  GET_RETIREMENT_FUND {

    private final HashMap<SearchRoleType, List<String>> RETIREMENT_FUND_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(SearchRoleType.ADMIN, ExpressionKeyConstants.getRetirementFundAdminKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(
        Root<T> root, String key, SearchRoleType searchRoleType) {
      checkSearchKey(key, RETIREMENT_FUND_WHITE_LIST_PARAMETERS.get(searchRoleType));

      return switch (key) {
        case ExpressionKeyConstants.CREATION_DATE,
                ExpressionKeyConstants.LAST_UPDATE_DATE,
                ExpressionKeyConstants.STATUS,
                ExpressionKeyConstants.REQUEST_ID,
                ExpressionKeyConstants.FIRST_NAME,
                ExpressionKeyConstants.LAST_NAME,
                ExpressionKeyConstants.BIRTH_DATE,
                ExpressionKeyConstants.EMAIL,
                ExpressionKeyConstants.GENDER,
                ExpressionKeyConstants.MARITAL_STATUS ->
            root.get(key);
        default -> throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST);
      };
    }
  };

  public abstract <T> Expression<?> getExpression(
      Root<T> root, String key, SearchRoleType searchRoleType);

  public void checkSearchKey(String key, List<String> whiteListKeys) {
    if (CollectionUtils.isEmpty(whiteListKeys)) {
      throw new BusinessException(ExceptionConstants.FORBIDDEN_ERROR);
    }
    if (!whiteListKeys.contains(key)) {
      throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST);
    }
  }
}
