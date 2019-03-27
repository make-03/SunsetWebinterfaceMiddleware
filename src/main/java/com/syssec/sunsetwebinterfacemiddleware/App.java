package com.syssec.sunsetwebinterfacemiddleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = com.syssec.sunsetwebinterfacecontroller.SunsetController.class)
@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
		System.out.println("[INFO: Sunset Webserver successfully started!]");
    }
}
