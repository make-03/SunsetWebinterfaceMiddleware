package com.syssec.sunsetmiddleware.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;
import javax.naming.SizeLimitExceededException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.syssec.sunsetmiddleware.main.App;
import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

/**
 * Class for managing the execution of the received code with sunset (via
 * execution of sunset process using stdin/stdout). Each thread from the 
 * ThreadPool starts its own separate sunset process.
 * 
 * @author Markus R.
 *
 */
public class SunsetExecutor {
	private static final Logger LOGGER = LogManager.getLogger(SunsetExecutor.class);
	
	private final String SUNSET_INTERPRETER_JAR_PATH = "./sunset.jar";
	private final int MAXIMUM_RESULT_STRING_LENGTH = 2097152;

	private Process process;
	private int timeoutSeconds;

	public SunsetExecutor() {		
		this.timeoutSeconds = App.threadPoolConfiguration.getKeepaliveseconds() + 5;
		
		try {
			this.process = Runtime.getRuntime().exec("java -jar " + this.SUNSET_INTERPRETER_JAR_PATH + " --cmd");
		} catch (IOException e) {
			LOGGER.warn(SunsetGlobalMessages.IO_EXCEPTION);
		}
	}

	/**
	 * Method for starting a calculation using a separate Sunset process passing 
	 * the received code for execution. Returns the calculated result as a String.
	 */
	public String executeCommand(String receivedCode) throws TimeoutException, InterruptedException {
		if (receivedCode.isEmpty()) {
			throw new IllegalArgumentException(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
		}

		LOGGER.debug(SunsetGlobalMessages.SUNSET_EXECUTION_VIA_COMMANDLINE);

		String code = receivedCode;
		String result = "EMPTY";

		if (!code.endsWith("END")) {
			code = code + "\nEND\n";
		}

		try {
			Instant startTime = Instant.now();

			long timeoutMilliseconds = this.timeoutSeconds * 1000;
			long timoutTime = startTime.toEpochMilli() + timeoutMilliseconds;

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

			out.write(code);
			out.flush();

			while (this.process.isAlive()) {
				if (System.currentTimeMillis() > timoutTime) {
					this.closeBufferedReaderAndWriter(in, out);
					this.destroyProcess();
					LOGGER.warn(String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION, this.timeoutSeconds));
					throw new TimeoutException(
							String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION, this.timeoutSeconds));
				}
			}
			
			in.readLine();
			
			result = "";
			String line = "";
			while ((line = in.readLine()) != null) {
				if (result.length() < this.MAXIMUM_RESULT_STRING_LENGTH) {
					result += line + "\n";
				} else {
					this.closeBufferedReaderAndWriter(in, out);
					this.destroyProcess();
					LOGGER.warn(String.format(SunsetGlobalMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION, this.MAXIMUM_RESULT_STRING_LENGTH));
					throw new SizeLimitExceededException(SunsetGlobalMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION);
				}
			}
			
			this.closeBufferedReaderAndWriter(in, out);

			Instant endTime = Instant.now();
			long elapsedTime = Duration.between(startTime, endTime).toMillis();
			LOGGER.info("Duration of sunset execution: " + elapsedTime + "ms");

			return result.trim();
		} catch(IOException e) {
			this.destroyProcess();
			LOGGER.warn(SunsetGlobalMessages.IO_EXCEPTION);
			return SunsetGlobalMessages.IO_EXCEPTION;
		} catch(SizeLimitExceededException e) {
			return String.format(SunsetGlobalMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION, this.MAXIMUM_RESULT_STRING_LENGTH)
					+ "\n" + result;
		}
	}
	
	private void closeBufferedReaderAndWriter(BufferedReader in, BufferedWriter out) throws IOException {
		in.close();
		out.close();
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

	@PreDestroy
	public void forciblyDestroyProcess() {
		System.out.println("(@PreDestroy) EXECUTOR: forcibly destroying process ...");
		this.process.destroyForcibly();
	}
	
}
