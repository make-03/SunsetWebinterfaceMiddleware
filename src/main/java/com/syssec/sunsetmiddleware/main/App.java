package com.syssec.sunsetmiddleware.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;

import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;
import com.syssec.sunsetmiddleware.threadpool.SunsetThreadPoolConfiguration;

import org.apache.log4j.Logger;

/**
 * Main class used to start the SpringBoot-Application. CompoentScan-Annotion is
 * used to scan for classes that use the spring framework but are located in a
 * different package than this main class.
 * 
 * @author Markus R.
 *
 */
@ComponentScan(basePackageClasses = com.syssec.sunsetmiddleware.controller.SunsetController.class)
@ComponentScan(basePackageClasses = com.syssec.sunsetmiddleware.controller.CustomErrorController.class)
@ComponentScan(basePackageClasses = com.syssec.sunsetmiddleware.configuration.SunsetHttpsConfiguration.class)
@SpringBootApplication
public class App {
	private final static Logger logger = Logger.getLogger(App.class);
	
	public static void main(String[] args) {
		SpringApplicationBuilder app = new SpringApplicationBuilder(App.class);
		app.build().addListeners(new ApplicationPidFileWriter("./bin/shutdown.pid"));
		app.run();
		
		logger.info("Default values for ThreadPool: [Core Pool Size=" 
					+ SunsetThreadPoolConfiguration.CORE_POOL_SIZE_DEFAULT
					+ ", Max Pool Size=" 
					+ SunsetThreadPoolConfiguration.MAX_POOL_SIZE_DEFAULT
					+ ", Queue Capacity=" 
					+ SunsetThreadPoolConfiguration.QUEUE_CAPACITY_DEFAULT
					+ ", Keep Alive Seconds=" 
					+ SunsetThreadPoolConfiguration.KEEP_ALIVE_SECONDS_DEFAULT
					+ "]");
		logger.info(SunsetGlobalMessages.WEBSERVER_SUCCESSFULLY_STARTED);
	}
}
