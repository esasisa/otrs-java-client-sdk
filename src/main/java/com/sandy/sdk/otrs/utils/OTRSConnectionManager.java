package com.sandy.sdk.otrs.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import com.sandy.sdk.otrs.OtrsConnector;

public class OTRSConnectionManager {

	private static Properties otrsProps = new Properties();

	static {
		try {
			otrsProps.load(OTRSConnectionManager.class.getClassLoader().getResourceAsStream("otrs_config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String URL = otrsProps.getProperty("otrs.url");
	private static String USER_NAME = otrsProps.getProperty("otrs.admin.username");
	private static String PASSWORD = otrsProps.getProperty("otrs.admin.password");

	public static OtrsConnector getConnection() throws MalformedURLException {

		return new OtrsConnector(URL, USER_NAME, PASSWORD);
	}

}
