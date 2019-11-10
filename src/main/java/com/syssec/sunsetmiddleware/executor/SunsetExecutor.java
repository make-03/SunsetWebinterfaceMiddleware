package com.syssec.sunsetmiddleware.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeoutException;

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
	private String sunsetPath = "sunset.jar";
	private Process process;
	private int timeoutSeconds;

	public SunsetExecutor() {
		this.timeoutSeconds = SunsetThreadPoolConfiguration.KEEP_ALIVE_SECONDS_DEFAULT + 10;
		try {
			this.process = Runtime.getRuntime().exec("java -jar " + sunsetPath + " --cmd");
		} catch (IOException e) {
			this.destroyProcess();
			e.printStackTrace();
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

		String code = receivedCode;
		String result = "EMPTY";

		if (!code.endsWith("END"))
			code = code + "\nEND\n";

		System.out.println("[INFO:] " + SunsetGlobalMessages.SUNSET_EXECUTION_VIA_COMMANDLINE);

		try {
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
					throw new TimeoutException("Timeout occurred after " + this.timeoutSeconds + " seconds!");
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
			System.out.println("[INFO: Duration of sunset execution: " + elapsedTime + "ms]");

			return result.trim();

		} catch (IOException e) {
			this.destroyProcess();
			return e.getMessage();
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
