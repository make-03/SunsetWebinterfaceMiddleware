package com.syssec.sunsetmiddleware.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.boot.SpringApplication.*;

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
	public static void main(String[] args) {
		run(App.class, args);
		System.out.println("[INFO: Sunset Webserver successfully started!]");
	}
}
