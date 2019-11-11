package com.syssec.sunsetmiddleware.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;
import com.syssec.sunsetmiddleware.threadpool.SunsetThreadPoolConfiguration;

/**
 * Class for managing the execution of the received code with sunset (via
 * execution of sunset process using stdin/stdout).
 * 
 * @author Markus R.
 *
 */
public class SunsetExecutor {
	private final Logger logger = Logger.getLogger(SunsetExecutor.class);

	private String sunsetPath = "sunset.jar";
	private Process process;
	private int timeoutSeconds;

	public SunsetExecutor() {
		this.timeoutSeconds = SunsetThreadPoolConfiguration.KEEP_ALIVE_SECONDS_DEFAULT + 5; // TODO: what value?
		
		try {
			this.process = Runtime.getRuntime().exec("java -jar " + sunsetPath + " --cmd");
		} catch (IOException e) {
			logger.warn(SunsetGlobalMessages.IO_EXCEPTION);
		}
	}

	/**
	 * Method used for executing sunset via commandline using the code sent by the
	 * user.
	 * 
	 * @param code - Code received by the user
	 * @return result of code execution as plain text (String)
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	public String executeCommand(String receivedCode) throws TimeoutException, InterruptedException {
		if (receivedCode.isEmpty()) {
			throw new IllegalArgumentException(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
		}

		logger.debug(SunsetGlobalMessages.SUNSET_EXECUTION_VIA_COMMANDLINE);

		String code = receivedCode;
		String result = "EMPTY";

		if (!code.endsWith("END")) {
			code = code + "\nEND\n";
		}

		try {
			// TODO: create private method for this part of the code (better structure!)
			
			Instant startTime = Instant.now();

			long timeoutMilliseconds = this.timeoutSeconds * 1000;
			long timoutTime = startTime.toEpochMilli() + timeoutMilliseconds;

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

			out.write(code);
			out.flush();

			while (this.isAlive(process)) {
				if (System.currentTimeMillis() > timoutTime) {
					this.destroyProcess();
					logger.warn(String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION, this.timeoutSeconds));
					throw new TimeoutException(
							String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION, this.timeoutSeconds));
				}
			}

			in.readLine();
			result = "";
			String line = null;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			
			Instant endTime = Instant.now();
			long elapsedTime = Duration.between(startTime, endTime).toMillis();
			logger.debug("Duration of sunset execution: " + elapsedTime + "ms");

			return result.trim();

		} catch (IOException e) {
			this.destroyProcess();
			logger.warn(SunsetGlobalMessages.IO_EXCEPTION);
			return SunsetGlobalMessages.IO_EXCEPTION;
		}
	}

	private boolean isAlive(Process p) {
		try {
			p.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	public void destroyProcess() {
		this.process.destroy();
	}

	public boolean isProcessAlive() {
		return this.process.isAlive();
	}

	public void setTimeoutSeconds(int timeoutSeconds) {
		if (timeoutSeconds <= 0) {
			throw new IllegalArgumentException("Value for TimeoutSeconds must be >0!");
		}

		this.timeoutSeconds = timeoutSeconds;
	}

}
