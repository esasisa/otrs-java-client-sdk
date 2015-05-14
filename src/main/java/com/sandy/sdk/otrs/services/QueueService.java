package com.sandy.sdk.otrs.services;

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

/**
 * 
 * @author Sandeep Kumar Singh
 *
 */

public class QueueService {
  public Object createQueueforSME(String queueName, String comment) throws MalformedURLException, SOAPException, IOException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.NAME, queueName);
    params.put(OtrsConstants.VALIED_ID, 1);
    params.put(OtrsConstants.GROUP_ID, 1);
    params.put(OtrsConstants.FOLLOW_UP_ID, 3);
    params.put(OtrsConstants.FOLLOW_UP_LOCK, 0);
    params.put(OtrsConstants.SYS_ADD_ID, 1);
    params.put(OtrsConstants.SOLLUTION_ID, 1);
    params.put(OtrsConstants.SIGNATURE_ID, 1);
    params.put(OtrsConstants.COMMENT, comment);
    params.put(OtrsConstants.USER_ID, 2);
   return createQueueforSME(params);
  }
  
  public Object createQueueforSME(Map<String, Object> params) throws MalformedURLException, SOAPException, IOException {
    checkNotNull(params);
    checkMandatoryFields(OtrsConstants.QUEUE_ADD, params);
    
    String agentName = (String) params.get(OtrsConstants.NAME);
    Map<String, Object> queueDetails =  (Map<String, Object>) getByName(agentName);
    if(queueDetails == null || queueDetails.size()<1){
      throw new IllegalArgumentException("Queue "+agentName + " already exists."); 
    }
    
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.QUEUE_OBJECT, OtrsConstants.QUEUE_ADD, params);
    Object[] result = new SimpleSoapMessageParser().nodesToArray(msg);
    return result[0];
  }

  public Map<String, Object> getQueues() throws SOAPException, MalformedURLException, IOException {
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.QUEUE_OBJECT, OtrsConstants.GET_ALL_QUEUES);
    Map<String, Object> result = new SimpleSoapMessageParser().nodesToMap(msg);
    return result;
  }

  /*
   * not Working {soap:Server=Can't locate object method
   * "QueueStandardTemplateMemberAdd" via package "Kernel::System::Queue" at
   * /opt/otrs/bin/cgi-bin/rpc.pl line 118.}
   */
  private void addTemplateForResponsetoaQueue(Integer queueId, Integer templateId) throws SOAPException, MalformedURLException, IOException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.QUEUE_ID, queueId);
    params.put(OtrsConstants.STANDRD_TMPLT_ID, templateId);
    params.put(OtrsConstants.ACTIVE, 1);
    params.put(OtrsConstants.USER_ID, 2);
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.QUEUE_OBJECT, OtrsConstants.QUEUE_STND_TEMP_MEMB_ADD, params);
    Map<String, Object> result = new SimpleSoapMessageParser().nodesToMap(msg);
  }
  
  public Object setQueueStandardResponse(Integer queueId, Integer responseId) throws MalformedURLException, SOAPException, IOException {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.RESPONSE_ID, 1);
    params.put(OtrsConstants.QUEUE_ID, 5);
    params.put(OtrsConstants.USER_ID, 2);
    return setQueueStandardResponse(params);
  }
  
  public Object setQueueStandardResponse( Map<String, Object> params) throws MalformedURLException, SOAPException, IOException {    
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.QUEUE_OBJECT, OtrsConstants.SET_QUEUE_STND_RESP, params);
    List<?> result = new SimpleSoapMessageParser().nodesToList(msg);
    return result.get(0);
  }
  
  public Object getByName(String name) throws MalformedURLException, SOAPException, IOException{
    isBlank(name);
    Map<String, Object> params = new HashMap<String, Object>();
    params.put(OtrsConstants.NAME, name);
    SOAPMessage msg = OTRSConnectionManager.getConnection().dispatchCall(OtrsConstants.QUEUE_OBJECT, OtrsConstants.QUEUE_GET, params);
    Map<String, Object> result = new SimpleSoapMessageParser().nodesToMap(msg);
    return result;
  }


}
