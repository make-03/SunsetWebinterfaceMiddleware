package com.syssec.sunsetmiddleware.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.Instant;

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

	/**
	 * Method used for executing sunset via commandline using the code sent by the
	 * user.
	 * 
	 * @param code - Code received by the user
	 * @return result of code execution as plain text (String)
	 */
	public String executeCommand(String receivedCode) {
		if (receivedCode.isEmpty()) {
			throw new IllegalArgumentException("Empty input code received!");
		}

		String code = receivedCode;
		String result = "EMPTY";

		if (!code.endsWith("END"))
			code = code + "\nEND\n";

		System.out.println("[INFO: Executing sunset in commandline with provided code!]");

		Instant startTime = Instant.now();

		try {
			this.process = Runtime.getRuntime().exec("java -jar " + sunsetPath + " --cmd");

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

			out.write(code);
			out.flush();

			in.readLine();
			result = "";
			String line = null;
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}

			this.destroyProcess();

			Instant endTime = Instant.now();

			long elapsedTime = Duration.between(startTime, endTime).toMillis();

			System.out.println("[INFO: Duration of sunset execution: " + elapsedTime + "ms]");

			return result.trim();
		} catch (IOException e) {
			this.destroyProcess();
			System.out.println("[ERROR: There was an exception when trying to execute sunset!]");
			System.out.println(e.getMessage());
			return e.getMessage();
		}
	}

	public void destroyProcess() {
		this.process.destroy();
	}

	public boolean isProcessAlive() {
		return this.process.isAlive();
	}

}
