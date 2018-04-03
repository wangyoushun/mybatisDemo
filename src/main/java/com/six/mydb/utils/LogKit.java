package com.six.mydb.utils;

import org.apache.log4j.Logger;

public class LogKit {
	private static Logger logger = Logger.getLogger(LogKit.class);

	
	public static void debug(String msg){
		logger.debug(msg);
	}
	
	public static void info(String msg){
		logger.info(msg);
	}
	
	public static void warn(String msg){
		logger.warn(msg);
	}
	
	public static void error(String msg){
		logger.error(msg);
	}
	
}
