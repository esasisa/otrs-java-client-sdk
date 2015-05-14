package com.sandy.sdk.otrs.services;

/*import static com.sandy.sdk.otrs.utils.Preconditions.checkMandatoryFields;
import static com.sandy.sdk.otrs.utils.Preconditions.checkNotNull;
import static com.sandy.sdk.otrs.utils.Preconditions.isBlank;*/

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

/**
 * 
 * @author Sandeep Kumar Singh
 *
 */

public class AgentService {

  public Object addAgent(String fName, String lName, String login, String password, String email) throws SOAPException, MalformedURLException, IOException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.USER_FIRST_NAME, fName);
    params.put(OtrsConstants.USER_LAST_NAME,lName);
    params.put(OtrsConstants.USER_LOGIN, login);
    params.put(OtrsConstants.USER_PW, password);
    params.put(OtrsConstants.USER_EMAIL, email);
    params.put(OtrsConstants.VALIED_ID, 1);
    params.put(OtrsConstants.CHANGE_USER_ID, 1);
    return addAgent(params);
  }

  public Object addAgent(Map<String, Object> params) throws SOAPException, MalformedURLException, IOException {
    //checkNotNull(params);
    //checkMandatoryFields(OtrsConstants.USER_ADD, params);

    String agentName = (String) params.get(OtrsConstants.USER_LOGIN);
    Map<String, Object> agentDetails = (Map<String, Object>) getByName(agentName);
    if (agentDetails == null || agentDetails.size() >= 1) {
      throw new IllegalArgumentException("Agent " + agentName + " already exists.");
    }

    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.USER_OBJECT, OtrsConstants.USER_ADD, params);
    List<?> result = new SimpleSoapMessageParser().nodesToList(msg);
    return result.get(0).toString();
  }

  public Object getByName(String name) throws MalformedURLException, SOAPException, IOException {
   // isBlank(name);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.USER, name);
    /*
     * params.put("Valid", 1); params.put("NoOutOfOffice", 1);
     */
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.USER_OBJECT, OtrsConstants.GET_USER_DATA, params);
    Map<String, Object> agent = new SimpleSoapMessageParser().nodesToMap(msg);
    return agent;
  }

  public Object list() throws MalformedURLException, SOAPException, IOException {
    Map<String, Object> params = new HashMap<String, Object>();
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.USER_OBJECT, OtrsConstants.USER_LIST, params);
    Map<String, Object> agent = new SimpleSoapMessageParser().nodesToMap(msg);
    return agent;
  }

  public String addAgentToGroup(String grpId, String userId, String ro, String move_into, String create, String owner, String priority, String rw) throws SOAPException, MalformedURLException, IOException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.GID, 8);
    params.put(OtrsConstants.UID, 5);

    Map<String, Object> permismap = new HashMap<String, Object>();
    permismap.put(OtrsConstants.RO, 1);
    permismap.put(OtrsConstants.MOVE_INTO, 1);
    permismap.put(OtrsConstants.CREATE, 1);
    permismap.put(OtrsConstants.OWNER, 1);
    permismap.put(OtrsConstants.PRIORITY, 0);
    permismap.put(OtrsConstants.RW, 0);
    params.put(OtrsConstants.PERMISSION, permismap);
    params.put(OtrsConstants.USER_ID, 2);
    return addAgentToGroup(params);
  }

  public String addAgentToGroup(Map<String, Object> params) throws SOAPException, MalformedURLException, IOException {
    //checkNotNull(params);
    //checkMandatoryFields(OtrsConstants.GROUP_MEMBER_ADD, params);
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.USER_OBJECT, OtrsConstants.GROUP_MEMBER_ADD, params);
    List<?> result = new SimpleSoapMessageParser().nodesToList(msg);
    return result.get(0).toString();
  }

  public static void main(String[] args) {
    AgentService agentService = new AgentService();
    try {
      // System.out.println("List "+agentService.list());
      System.out.println("List " + agentService.getByName("manish"));
    } catch (SOAPException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
