package com.kadioglumf.authservice.core.search;

import java.util.Arrays;
import java.util.List;

public class ExpressionKeyConstants {

  public static final String NAME = "name";
  public static final String SURNAME = "surname";
  public static final String EMAIL = "email";
  public static final String LAST_LOGIN_DATE = "lastLoginDate";
  public static final String CREATION_DATE = "creationDate";
  public static final String USER_ROLE = "name";
  public static final String USER_MODEL = "user";
  public static final String PARTNER = "partner";
  public static final String ROLE = "role";
  public static final String USER_ID = "userId";
  public static final String CONSULTANT_ID = "consultantId";
  public static final String COMPANY_NAME = "companyName";
  public static final String ACTIVITY = "activity";
  public static final String ID = "id";
  public static final String LAST_UPDATE_DATE = "lastUpdateDate";
  public static final String STREET = "street";
  public static final String NUMBER = "number";
  public static final String POST_CODE = "postCode";
  public static final String CITY = "city";
  public static final String PHONE = "phone";
  public static final String WEB_PAGE = "webPage";
  public static final String APPLICATION_ID = "applicationId";
  public static final String MOBILE = "mobile";
  public static final String COMPANY = "company";

  public static List<String> getUserListAdminKeys() {
    return Arrays.asList(NAME, EMAIL, SURNAME, CREATION_DATE, LAST_LOGIN_DATE, ROLE);
  }

  public static List<String> getPartnerListAdminKeys() {
    return Arrays.asList(
        ID, NAME, EMAIL, SURNAME, CREATION_DATE, CONSULTANT_ID, COMPANY_NAME, ACTIVITY);
  }

  public static List<String> getAllDealerCompaniesAdminKeys() {
    return Arrays.asList(
        ID,
        CREATION_DATE,
        LAST_UPDATE_DATE,
        CONSULTANT_ID,
        NAME,
        STREET,
        NUMBER,
        POST_CODE,
        CITY,
        PHONE,
        WEB_PAGE);
  }

  public static List<String> getRegistrationApplicationListAdminKeys() {
    return Arrays.asList(
        NAME,
        EMAIL,
        MOBILE,
        SURNAME,
        CREATION_DATE,
        LAST_UPDATE_DATE,
        APPLICATION_ID,
        COMPANY_NAME);
  }
}
