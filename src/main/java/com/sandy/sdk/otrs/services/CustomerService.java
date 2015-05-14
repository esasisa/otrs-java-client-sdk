package com.sandy.sdk.otrs.services;

import static com.sandy.sdk.otrs.utils.Preconditions.checkMandatoryFields;
import static com.sandy.sdk.otrs.utils.Preconditions.checkNotNull;

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

public class CustomerService {

  
  public String addcustomerToGroup() throws SOAPException, MalformedURLException, IOException {
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
   return addcustomerToGroup(params);
  }

  private String addcustomerToGroup(Map<String, Object> params) throws SOAPException, MalformedURLException, IOException {
    checkNotNull(params);
    checkMandatoryFields(OtrsConstants.GROUP_MEMBER_ADD, params);
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.CUSTOMER_GRP_OBJ, OtrsConstants.GROUP_MEMBER_ADD, params);
    List<?> result = new SimpleSoapMessageParser().nodesToList(msg);
    return result.get(0).toString();
    
  }

}
