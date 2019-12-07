package com.syssec.sunsetmiddleware.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.ComponentScan;

import com.syssec.sunsetmiddleware.configuration.SunsetThreadPoolConfiguration;
import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.annotation.PreDestroy;

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
	public static SunsetThreadPoolConfiguration threadPoolConfiguration = new SunsetThreadPoolConfiguration();

	private static final Logger logger = Logger.getLogger(App.class);

	private static final String FILE_PATH_PID = "./bin/shutdown.pid";

	public static void main(String[] args) {
		if (args.length == 0) {
			// starting application without a parameter (default scenario)
			SpringApplicationBuilder app = new SpringApplicationBuilder(App.class);
			app.build().addListeners(new ApplicationPidFileWriter("./bin/shutdown.pid"));
			app.run();

			logger.info(String.format(SunsetGlobalMessages.THREAD_POOL_DEFAULT_VALUES,
					threadPoolConfiguration.getCorepoolsize(), threadPoolConfiguration.getMaxpoolsize(),
					threadPoolConfiguration.getQueuecapacity(), threadPoolConfiguration.getKeepaliveseconds()));
			logger.info(SunsetGlobalMessages.WEBSERVER_SUCCESSFULLY_STARTED);
		} else if (args.length == 1 && args[0].equals("--shutdown")) {
			// passing argument for shutdown of an already running SpringBoot Application!
			logger.info(SunsetGlobalMessages.WEBSERVER_SHUTDOWN_ARGUMENT_REVEIVED);
			shutdownRunningApplication();
		} else if (args.length == 1 && args[0].equals("--restart")) {
			// passing argument for restarting an already running SpringBoot Application!
			//logger.info(SunsetGlobalMessages.WEBSERVER_RESTART_ARGUMENT_REVEIVED);

			// TODO: implement method to restart spring boot application via process signals (if possible)
			System.exit(0);
		} else {
			// not supported (illegal argument(s)), throw exception and exit!
			logger.info(SunsetGlobalMessages.ILLEGAL_ARGUMENTS_RECEIVED);
			System.exit(0);
		}
	}

	private static void shutdownRunningApplication() {
		Integer pid = -1;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH_PID));
			pid = Integer.parseInt(reader.readLine());
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		if (pid == null || pid < 0) {
			throw new IllegalArgumentException(SunsetGlobalMessages.PID_IS_NOT_VALID);
		}

		System.out.println("PID = " + pid);

		String cmd = "TASKKILL /PID " + pid + " /T /F";
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		overwritePidInFile();

		System.out.println("Shutdown Application!");

		System.exit(0);
	}

	private static void overwritePidInFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH_PID));
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@PreDestroy
	public void shutdownApplication() {
		System.out.println("(@PreDestroy) APP: shutting down application, deleting \"./bin/shutdown.pid\" ...");
		File newFile = new File("./bin/shutdown.pid");
		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
