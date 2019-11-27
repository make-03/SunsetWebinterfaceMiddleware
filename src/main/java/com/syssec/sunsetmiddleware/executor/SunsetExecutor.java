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

import org.apache.log4j.Logger;

import com.syssec.sunsetmiddleware.configuration.SunsetThreadPoolConfiguration;
import com.syssec.sunsetmiddleware.main.App;
import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

/**
 * Class for managing the execution of the received code with sunset (via
 * execution of sunset process using stdin/stdout).
 * 
 * @author Markus R.
 *
 */
public class SunsetExecutor {
	private final Logger logger = Logger.getLogger(SunsetExecutor.class);
	
	private final int MAXIMUM_RESULT_STRING_LENGTH = 32768;

	private String sunsetPath = "sunset.jar";
	private Process process;
	private int timeoutSeconds;

	public SunsetExecutor() {
		this.timeoutSeconds = App.threadPoolConfiguration.getKeepaliveseconds() + 5;

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
	 * @throws SizeLimitExceededException 
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
			Instant startTime = Instant.now();

			long timeoutMilliseconds = this.timeoutSeconds * 1000;
			long timoutTime = startTime.toEpochMilli() + timeoutMilliseconds;

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

			out.write(code);
			out.flush();

			while (this.process.isAlive()) {
				if (System.currentTimeMillis() > timoutTime) {
					this.closeBufferedReaders(in, out);
					this.destroyProcess();
					logger.warn(String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION, this.timeoutSeconds));
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
					this.closeBufferedReaders(in, out);
					this.destroyProcess();
					logger.warn(String.format(SunsetGlobalMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION, this.MAXIMUM_RESULT_STRING_LENGTH));
					throw new SizeLimitExceededException(SunsetGlobalMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION);
				}
			}

			this.closeBufferedReaders(in, out);

			Instant endTime = Instant.now();
			long elapsedTime = Duration.between(startTime, endTime).toMillis();
			logger.debug("Duration of sunset execution: " + elapsedTime + "ms");

			return result.trim();

		} catch(IOException e) {
			this.destroyProcess();
			logger.warn(SunsetGlobalMessages.IO_EXCEPTION);
			return SunsetGlobalMessages.IO_EXCEPTION;
		} catch(SizeLimitExceededException e) {
			return String.format(SunsetGlobalMessages.SIZE_LIMIT_EXCEEDED_EXCEPTION, this.MAXIMUM_RESULT_STRING_LENGTH)
					+ "\n" + result;
		}
	}
	
	@PreDestroy
	public void forciblyDestroyProcess() {
		System.out.println("(@PreDestroy) EXECUTOR: forcibly destroying process ...");
		this.process.destroyForcibly();
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

	private void closeBufferedReaders(BufferedReader in, BufferedWriter out) throws IOException {
		in.close();
		out.close();
	}

}
