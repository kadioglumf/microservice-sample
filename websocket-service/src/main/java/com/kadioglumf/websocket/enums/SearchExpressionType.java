package com.kadioglumf.websocket.enums;

import com.kadioglumf.websocket.constant.ExceptionConstants;
import com.kadioglumf.websocket.constant.ExpressionKeyConstants;
import com.kadioglumf.websocket.core.exception.BusinessException;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import org.springframework.util.CollectionUtils;

public enum SearchExpressionType {
  GET_CONTACT_US {

    private final HashMap<RoleType, List<String>> CONTACT_US_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(RoleType.ADMIN, ExpressionKeyConstants.getContactUsAdminKeys());
            put(RoleType.ADMIN_OR_MODERATOR, ExpressionKeyConstants.getContactUsAdminOrModKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(Root<T> root, String key, RoleType searchRoleType) {
      checkSearchKey(key, CONTACT_US_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.CREATION_DATE:
        case ExpressionKeyConstants.CONTACT_REQUEST_ID:
        case ExpressionKeyConstants.STATUS:
        case ExpressionKeyConstants.NAME:
        case ExpressionKeyConstants.SURNAME:
        case ExpressionKeyConstants.EMAIL:
        case ExpressionKeyConstants.PHONE:
        case ExpressionKeyConstants.SUBJECT:
          return root.get(key);
        case ExpressionKeyConstants.USER_ID:
          return root.get(ExpressionKeyConstants.ASSIGNED_PARTNER_ID);
        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  },

  GET_CONSULTING_REQUESTS {

    private final HashMap<RoleType, List<String>> HEALTH_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(
                RoleType.ADMIN_OR_MODERATOR,
                ExpressionKeyConstants.getConsultingRequestsAdminOrModKeys());
            put(RoleType.ADMIN, ExpressionKeyConstants.getConsultingRequestsAdminKeys());
            put(RoleType.PARTNER, ExpressionKeyConstants.getConsultingRequestsPartnerKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(Root<T> root, String key, RoleType searchRoleType) {
      checkSearchKey(key, HEALTH_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.CONSULTING_REQUEST_ID:
        case ExpressionKeyConstants.CREATION_DATE:
        case ExpressionKeyConstants.STATUS:
        case ExpressionKeyConstants.NAME:
        case ExpressionKeyConstants.SURNAME:
        case ExpressionKeyConstants.PHONE:
        case ExpressionKeyConstants.CATEGORY:
        case ExpressionKeyConstants.CALLBACK_TIME_END:
        case ExpressionKeyConstants.CALLBACK_TIME_START:
        case ExpressionKeyConstants.ASSIGNED_PARTNER_ID:
          return root.get(key);
        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  },

  GET_SMS_COUNTER_LIST {

    private final HashMap<RoleType, List<String>> GET_SMS_COUNTER_LIST_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(RoleType.ADMIN, ExpressionKeyConstants.getSmsCounterListAdminKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(Root<T> root, String key, RoleType searchRoleType) {
      checkSearchKey(key, GET_SMS_COUNTER_LIST_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.ID:
        case ExpressionKeyConstants.CREATED_DATE:
        case ExpressionKeyConstants.UPDATED_DATE:
        case ExpressionKeyConstants.USER_IP_ADDRESS:
        case ExpressionKeyConstants.USER_AGENT:
        case ExpressionKeyConstants.ORIGIN:
        case ExpressionKeyConstants.RECIPIENT_NUMBER:
        case ExpressionKeyConstants.NUMBER_OF_SMS_SENDING:
          return root.get(key);

        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  },

  GET_LOTTERY_APPLICATION {

    private final HashMap<RoleType, List<String>> LOTTERY_APPLICATION_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(RoleType.ADMIN, ExpressionKeyConstants.getLotteryAdminKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(Root<T> root, String key, RoleType searchRoleType) {
      checkSearchKey(key, LOTTERY_APPLICATION_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.CREATION_DATE:
        case ExpressionKeyConstants.NAME:
        case ExpressionKeyConstants.SURNAME:
        case ExpressionKeyConstants.APPLICATION_ID:
        case ExpressionKeyConstants.ID:
        case ExpressionKeyConstants.EMAIL:
        case ExpressionKeyConstants.GENDER:
        case ExpressionKeyConstants.LANGUAGE:
        case ExpressionKeyConstants.IS_ADVERTISING_APPROVED:
          return root.get(key);
        case ExpressionKeyConstants.CAMPAIGN_CODE:
          return root.join(ExpressionKeyConstants.CAMPAIGN).get(key);
        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  },

  GET_CAMPAIGNS {

    private final HashMap<RoleType, List<String>> CAMPAIGNS_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(RoleType.ADMIN, ExpressionKeyConstants.getCampaignsAdminKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(Root<T> root, String key, RoleType searchRoleType) {
      checkSearchKey(key, CAMPAIGNS_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.CREATION_DATE:
        case ExpressionKeyConstants.STATUS:
        case ExpressionKeyConstants.ID:
        case ExpressionKeyConstants.CAMPAIGN_CODE:
        case ExpressionKeyConstants.START_DATE:
        case ExpressionKeyConstants.END_DATE:
          return root.get(key);
        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  },

  GET_MOVING_CLEANINGS {

    private final HashMap<RoleType, List<String>> HEALTH_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(
                RoleType.ADMIN_OR_MODERATOR,
                ExpressionKeyConstants.getMovingCleaningAdminOrModKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(Root<T> root, String key, RoleType searchRoleType) {
      checkSearchKey(key, HEALTH_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.MOVING_CLEANING_ID:
        case ExpressionKeyConstants.CREATION_DATE:
        case ExpressionKeyConstants.STATUS:
        case ExpressionKeyConstants.LAST_UPDATE_DATE:
        case ExpressionKeyConstants.TYPE:
          return root.get(key);
        case ExpressionKeyConstants.NAME:
        case ExpressionKeyConstants.SURNAME:
          return root.join(ExpressionKeyConstants.CONTACT).get(key);
        case ExpressionKeyConstants.POST_CODE:
          return root.join(ExpressionKeyConstants.CONTACT)
              .join(ExpressionKeyConstants.ADDRESS)
              .get(key);
        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  },

  GET_SOCIAL_LEADS {

    private final HashMap<RoleType, List<String>> SOCIAL_LEADS_WHITE_LIST_PARAMETERS =
        new HashMap<>() {
          {
            put(RoleType.ADMIN, ExpressionKeyConstants.getSocialLeadsAdminKeys());
            put(RoleType.PARTNER, ExpressionKeyConstants.getSocialLeadsPartnerKeys());
          }
        };

    @Override
    public <T> Expression<?> getExpression(Root<T> root, String key, RoleType searchRoleType) {
      checkSearchKey(key, SOCIAL_LEADS_WHITE_LIST_PARAMETERS.get(searchRoleType));

      switch (key) {
        case ExpressionKeyConstants.CREATION_DATE_OF_VALUE:
        case ExpressionKeyConstants.STATUS:
        case ExpressionKeyConstants.NAME:
        case ExpressionKeyConstants.PHONE:
        case ExpressionKeyConstants.EMAIL:
        case ExpressionKeyConstants.ASSIGNED_PARTNER_ID:
          return root.get(key);
        default:
          throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
      }
    }
  };

  public abstract <T> Expression<?> getExpression(
      Root<T> root, String key, RoleType searchRoleType);

  public void checkSearchKey(String key, List<String> whiteListKeys) {
    if (CollectionUtils.isEmpty(whiteListKeys)) {
      throw new BusinessException(ExceptionConstants.FORBIDDEN_ERROR);
    }
    if (!whiteListKeys.contains(key)) {
      throw new BusinessException(ExceptionConstants.FILTER_PARAMETER_BLACKLIST_ERROR);
    }
  }
}
