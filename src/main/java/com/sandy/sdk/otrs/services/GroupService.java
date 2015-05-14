package com.sandy.sdk.otrs.services;

/**
 * 
 * @author Sandeep Kumar Singh
 *
 */

import static com.sandy.sdk.otrs.utils.Preconditions.checkMandatoryFields;
import static com.sandy.sdk.otrs.utils.Preconditions.checkNotNull;
import static com.sandy.sdk.otrs.utils.Preconditions.isBlank;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.sandy.sdk.otrs.SimpleSoapMessageParser;
import com.sandy.sdk.otrs.utils.OTRSConnectionManager;
import com.sandy.sdk.otrs.utils.OtrsConstants;

public class GroupService {

  public Map<String, Object> getGroups() throws MalformedURLException, SOAPException, IOException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.VALIED, 1);
    return getGroups(params);
  }

  public Map<String, Object> getGroups(Map<String, Object> params) throws MalformedURLException, SOAPException, IOException {
    checkNotNull(params);
    checkMandatoryFields(OtrsConstants.GROUP_LIST, params);
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.GROUP_OBJECT, OtrsConstants.GROUP_LIST, params);
    Map<String, Object> result = new SimpleSoapMessageParser().nodesToMap(msg);
    return result;
  }

  public Object getByName(String name) throws MalformedURLException, SOAPException, IOException {
    isBlank(name);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.GROUP, name);
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.GROUP_OBJECT, OtrsConstants.GROUP_LOOK_UP, params);
    List<?> result = new SimpleSoapMessageParser().nodesToList(msg);
    return result.get(0);
  }

  public Object createGroup(String name, String comment) throws MalformedURLException, SOAPException, IOException {
    isBlank(name);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.NAME, name);
    params.put(OtrsConstants.COMMENT, comment);
    params.put(OtrsConstants.VALIED_ID, 1);
    params.put(OtrsConstants.USER_ID, 2);
    return createGroup(params);
  }

  public Object createGroup(Map<String, Object> params) throws MalformedURLException, SOAPException, IOException {
    checkNotNull(params);
    checkMandatoryFields(OtrsConstants.GROUP_ADD, params);
    String groupName = (String) params.get(OtrsConstants.NAME);
    Integer groupId = (Integer) getByName(groupName);
    if (groupId < 1) {
      throw new IllegalArgumentException("Group " + groupName + " already exists.");
    }
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.GROUP_OBJECT, OtrsConstants.GROUP_ADD, params);
    Object[] result = new SimpleSoapMessageParser().nodesToArray(msg);
    return result[0];
  }

}
