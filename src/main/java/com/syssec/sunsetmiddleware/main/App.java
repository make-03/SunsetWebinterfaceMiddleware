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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class used to start the SpringBoot-Application. CompoentScan-Annotation is
 * used to scan for classes that use the spring framework but are located in a
 * different package than this main class. Contains logic for handling arguments
 * passed via the command line when starting the Spring Boot Application. The
 * arguments that are currently implemented are: --shutdown and --restart.
 * 
 * @author Markus R.
 *
 */
@ComponentScan(basePackageClasses = com.syssec.sunsetmiddleware.controller.SunsetController.class)
@ComponentScan(basePackageClasses = com.syssec.sunsetmiddleware.controller.CustomErrorController.class)
@ComponentScan(basePackageClasses = com.syssec.sunsetmiddleware.configuration.SunsetHttpsConfiguration.class)
@SpringBootApplication
public class App{
	public static SunsetThreadPoolConfiguration threadPoolConfiguration = new SunsetThreadPoolConfiguration();

	private static final Logger LOGGER = LogManager.getLogger(App.class);

	private static final String PID_FILE_PATH = "./bin/shutdown.pid";

	public static void main(String[] args) {
		if (args.length == 0) {
			// starting application without a parameter (default scenario)
			runNewSpringApplication();
		} else if (args.length == 1 && args[0].equals("--shutdown")) {
			// passing argument for shutdown of an already running SpringBoot Application!
			LOGGER.info(SunsetGlobalMessages.WEBSERVER_SHUTDOWN_ARGUMENT_REVEIVED);
			shutdownAlreadyRunningApplication();
			System.exit(0);
		} else if (args.length == 1 && args[0].equals("--restart")) {
			// passing argument for restarting an already running SpringBoot Application!
			LOGGER.info(SunsetGlobalMessages.WEBSERVER_RESTART_ARGUMENT_REVEIVED);
			restartRunningApplication();
		} else {
			// not supported (illegal argument(s)), throw exception and exit!
			LOGGER.info(SunsetGlobalMessages.ILLEGAL_ARGUMENTS_RECEIVED);
			System.exit(1);
		}
	}

	private static void runNewSpringApplication() {
		SpringApplicationBuilder app = new SpringApplicationBuilder(App.class);
		app.build().addListeners(new ApplicationPidFileWriter(PID_FILE_PATH));
		app.run();

		LOGGER.info(String.format(SunsetGlobalMessages.THREAD_POOL_DEFAULT_VALUES,
				threadPoolConfiguration.getCorepoolsize(), threadPoolConfiguration.getMaxpoolsize(),
				threadPoolConfiguration.getQueuecapacity(), threadPoolConfiguration.getKeepaliveseconds()));
		LOGGER.info(SunsetGlobalMessages.WEBSERVER_SUCCESSFULLY_STARTED);
	}

	private static void shutdownAlreadyRunningApplication() {
		Integer pid = readPidFromFile();

		if (!isPidValid(pid)) {
			LOGGER.error(SunsetGlobalMessages.PID_IS_NOT_VALID);
			throw new IllegalArgumentException(SunsetGlobalMessages.PID_IS_NOT_VALID);
		}

		String cmd = "TASKKILL /PID " + pid + " /T /F";
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			System.exit(1);
		}

		overwritePidInFile();

		System.out.println("Shutdown running Spring Boot Application!");
	}

	private static void restartRunningApplication() {
		shutdownAlreadyRunningApplication();
		runNewSpringApplication();
	}

	private static Integer readPidFromFile() {
		Integer pid = -1;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(PID_FILE_PATH));
			pid = Integer.parseInt(reader.readLine());
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			System.exit(1);
		}

		return pid;
	}

	private static boolean isPidValid(Integer pid) {
		if (pid == null || pid < 0) {
			return false;
		}
		return true;
	}

	private static void overwritePidInFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(PID_FILE_PATH));
			writer.write("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			System.exit(1);
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
