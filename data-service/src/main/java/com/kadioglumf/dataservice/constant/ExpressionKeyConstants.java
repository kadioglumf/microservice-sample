package com.kadioglumf.dataservice.constant;

import java.util.Arrays;
import java.util.List;

public class ExpressionKeyConstants {

  public static final String CREATION_DATE = "creationDate";
  public static final String LAST_UPDATE_DATE = "lastUpdateDate";
  public static final String CONTACT_REQUEST_ID = "contactRequestId";
  public static final String STATUS = "status";
  public static final String NAME = "name";
  public static final String SURNAME = "surname";
  public static final String EMAIL = "email";
  public static final String PHONE = "phone";
  public static final String SUBJECT = "subject";
  public static final String CONSULTING_REQUEST_ID = "consultingRequestId";
  public static final String CATEGORY = "category";
  public static final String ASSIGNED_PARTNER_ID = "assignedPartnerId";
  public static final String USER_ID = "userId";
  public static final String ID = "id";
  public static final String UPDATED_DATE = "updatedDate";
  public static final String CREATED_DATE = "createdDate";
  public static final String USER_IP_ADDRESS = "userIpAddress";
  public static final String USER_AGENT = "userAgent";
  public static final String ORIGIN = "origin";
  public static final String NUMBER_OF_SMS_SENDING = "numberOfSmsSending";
  public static final String RECIPIENT_NUMBER = "recipientNumber";
  public static final String CALLBACK_TIME_END = "callbackTimeEnd";
  public static final String CALLBACK_TIME_START = "callbackTimeStart";
  public static final String APPLICATION_ID = "applicationId";
  public static final String GENDER = "gender";
  public static final String LANGUAGE = "language";
  public static final String IS_ADVERTISING_APPROVED = "isAdvertisingApproved";
  public static final String CAMPAIGN_CODE = "campaignCode";
  public static final String CAMPAIGN = "campaign";
  public static final String START_DATE = "startDate";
  public static final String END_DATE = "endDate";
  public static final String MOVING_CLEANING_ID = "movingCleaningId";
  public static final String POST_CODE = "postCode";
  public static final String CONTACT = "contact";
  public static final String ADDRESS = "address";
  public static final String TYPE = "type";
  public static final String CREATION_DATE_OF_VALUE = "creationDateOfValue";
  public static final String REQUEST_ID = "requestId";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String BIRTH_DATE = "birthDate";
  public static final String MARITAL_STATUS = "maritalStatus";

  public static List<String> getContactUsAdminKeys() {
    return Arrays.asList(
        CREATION_DATE, CONTACT_REQUEST_ID, STATUS, NAME, SURNAME, EMAIL, PHONE, SUBJECT, USER_ID);
  }

  public static List<String> getContactUsAdminOrModKeys() {
    return Arrays.asList(
        CREATION_DATE, CONTACT_REQUEST_ID, STATUS, NAME, SURNAME, EMAIL, PHONE, SUBJECT, USER_ID);
  }

  public static List<String> getConsultingRequestsAdminOrModKeys() {
    return Arrays.asList(
        CONSULTING_REQUEST_ID,
        CREATION_DATE,
        STATUS,
        NAME,
        SURNAME,
        PHONE,
        CATEGORY,
        USER_ID,
        CALLBACK_TIME_END,
        CALLBACK_TIME_START);
  }

  public static List<String> getConsultingRequestsAdminKeys() {
    return Arrays.asList(
        CONSULTING_REQUEST_ID,
        CREATION_DATE,
        STATUS,
        NAME,
        SURNAME,
        PHONE,
        CATEGORY,
        USER_ID,
        CALLBACK_TIME_END,
        CALLBACK_TIME_START);
  }

  public static List<String> getConsultingRequestsPartnerKeys() {
    return Arrays.asList(
        CONSULTING_REQUEST_ID,
        CREATION_DATE,
        STATUS,
        NAME,
        SURNAME,
        PHONE,
        CATEGORY,
        ASSIGNED_PARTNER_ID,
        CALLBACK_TIME_END,
        CALLBACK_TIME_START);
  }

  public static List<String> getSmsCounterListAdminKeys() {
    return Arrays.asList(
        ID,
        CREATED_DATE,
        UPDATED_DATE,
        USER_IP_ADDRESS,
        USER_AGENT,
        ORIGIN,
        RECIPIENT_NUMBER,
        NUMBER_OF_SMS_SENDING);
  }

  public static List<String> getLotteryAdminKeys() {
    return Arrays.asList(
        CREATION_DATE,
        STATUS,
        NAME,
        APPLICATION_ID,
        ID,
        CAMPAIGN,
        SURNAME,
        EMAIL,
        GENDER,
        LANGUAGE,
        IS_ADVERTISING_APPROVED,
        CAMPAIGN_CODE);
  }

  public static List<String> getCampaignsAdminKeys() {
    return Arrays.asList(CREATION_DATE, STATUS, ID, CAMPAIGN_CODE, START_DATE, END_DATE);
  }

  public static List<String> getMovingCleaningAdminOrModKeys() {
    return Arrays.asList(
        MOVING_CLEANING_ID,
        CREATION_DATE,
        STATUS,
        NAME,
        SURNAME,
        POST_CODE,
        LAST_UPDATE_DATE,
        TYPE);
  }

  public static List<String> getSocialLeadsAdminKeys() {
    return Arrays.asList(
        CREATION_DATE, STATUS, NAME, PHONE, EMAIL, CREATION_DATE_OF_VALUE, ASSIGNED_PARTNER_ID);
  }

  public static List<String> getSocialLeadsPartnerKeys() {
    return Arrays.asList(
        CREATION_DATE, STATUS, NAME, PHONE, EMAIL, CREATION_DATE_OF_VALUE, ASSIGNED_PARTNER_ID);
  }

  public static List<String> getRetirementFundAdminKeys() {
    return Arrays.asList(
        CREATION_DATE,
        LAST_UPDATE_DATE,
        STATUS,
        REQUEST_ID,
        FIRST_NAME,
        LAST_NAME,
        BIRTH_DATE,
        EMAIL,
        GENDER,
        MARITAL_STATUS);
  }
}
