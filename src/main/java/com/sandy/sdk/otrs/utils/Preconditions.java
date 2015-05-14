package com.sandy.sdk.otrs.utils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author Sandeep Kumar Singh
 *
 */
public class Preconditions {

  private static Properties otrsProps = new Properties();
  
  static {
    try {
      otrsProps.load(Preconditions.class.getClassLoader().getResourceAsStream("field_validation.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public static <T> T checkNotNull(T reference) {
    if (reference == null) {
      throw new NullPointerException(" can not be null");
    }
    return reference;
  }

  public static void checkMandatoryFields(String methodName, Map<String, Object> paramList) throws IllegalArgumentException {
    String[] mandatoryParamsList = getMadatoryParams(methodName);
    if (mandatoryParamsList != null) {
      for (String params : mandatoryParamsList) {
        if(params.contains(":")){
          String array[] = params.split(":");
          Map<String, Object> innerMap =  (Map<String, Object>) paramList.get(array[0]);
          if(paramList.get(array[1]) != null){
            checkMandatoryFields(methodName, innerMap);
          }
        }
        if (paramList.containsKey(params.trim())) {
          Object value = paramList.get(params);
          if (value instanceof String && (value == null || ((String) value).trim().equals(""))) {
            throw new IllegalArgumentException("Parameter "+params + " can not be blank.");
          } else if (value instanceof Integer && value != null && (Integer) value <= 0) {
            throw new IllegalArgumentException("Parameter "+params + " should be positive number.");
          } else if (value instanceof Map<?, ?>) {
            checkMandatoryFields(methodName, (Map<String, Object>) value);
          }
        } else {
          throw new IllegalArgumentException("Mandatory Field Missing.");
        }
      }
    } /*else {
      throw new IllegalArgumentException("Method name does not exist in property.");
    }*/

  }

  private static String[] getMadatoryParams(String methodName) {
    if (otrsProps.containsKey(methodName)) {
      String methodProps = otrsProps.getProperty(methodName);
      return methodProps.split(",");
    }
    return null;
  }
  
  public static Boolean isBlank(String params){
    if(params == null || params.trim().length()<=0){
      throw new IllegalArgumentException("Parameter "+params + " can not be blank."); 
    }else{
      return false;
    }
  }
}
