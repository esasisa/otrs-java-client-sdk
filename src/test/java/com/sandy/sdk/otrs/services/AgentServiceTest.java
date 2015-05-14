package com.sandy.sdk.otrs.services;

import java.io.IOException;

import javax.xml.soap.SOAPException;

import org.junit.Test;

/**
 * 
 * @author Sandeep Kumar Singh
 *
 */

public class AgentServiceTest {

	@Test
	public void test() {
		AgentService service = new AgentService();		
	    try {
			service.addAgent("Sandeep", "Singh", "sandeep", "sandeep", "sandeep@xyz.com");
		} catch (SOAPException | IOException e) {		
			e.printStackTrace();
		}
	}

}
