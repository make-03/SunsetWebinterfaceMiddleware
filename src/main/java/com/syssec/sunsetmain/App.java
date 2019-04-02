package com.syssec.sunsetmain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = com.syssec.sunsetcontroller.SunsetController.class)
@SpringBootApplication
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
		System.out.println("[INFO: Sunset Webserver successfully started!]");
    }
}
