package com.kadioglumf.authservice.core.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ExceptionBundleConstants {
  public static final String CLASSPATH_FOR_EXCEPTION_PROPERTIES = "classpath:" + "/exception/";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS =
      "application-exception";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS =
      "business-exception";

  public static final String MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES =
      "Es gibt keine Beschreibung des Fehlers im System. Bitte wenden Sie sich mit Ihrem Fehlercode und der Transaktionsnummer an die Systemadministratoren.";
  public static final String MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES_EN =
      "There is no error explanation present in the system for the exception you recieved, please contact system administrators with your transaction number and exception code";
  public static final String MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES_FR =
      "Il n'y a pas d'explication d'erreur présente dans le système pour l'exception que vous avez reçue, veuillez contacter les administrateurs du système avec votre numéro de transaction et votre code d'exception";
  public static final String MESSAGE_DEFAULT_FOR_BUSINESS_EXCEPTIONS_FOR_NOT_FOUND_ERROR_CODES_IT =
      "Non c'è alcuna spiegazione dell'errore presente nel sistema per l'eccezione che hai ricevuto, per favore contattare gli amministratori del sistema con il tuo numero di transazione e il tuo codice di eccezione";

  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_DEFAULT_PATH =
      "exception/business-exception.properties";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_EN_PATH =
      "exception/business-exception_en.properties";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_FR_PATH =
      "exception/business-exception_fr.properties";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_BUSINESS_EXCEPTIONS_IT_PATH =
      "exception/business-exception_it.properties";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_DEFAULT_PATH =
      "exception/application-exception.properties";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_EN_PATH =
      "exception/application-exception_en.properties";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_FR_PATH =
      "exception/application-exception_fr.properties";
  public static final String MESSAGE_BUNDLE_FILE_NAME_FOR_APPLICATION_EXCEPTIONS_IT_PATH =
      "exception/application-exception_it.properties";
}
