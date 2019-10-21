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
 * command line).
 * 
 * @author Markus R.
 *
 */
public class SunsetExecutor {

	private String sunsetPath = "sunset.jar";
	private Process process;

	private final String SEP_LINE = "------------------------------------------------";

	public SunsetExecutor() {

	}

	/**
	 * Method used for executing sunset via commandline using the code sent by the
	 * user.
	 * 
	 * @param code - Code received by the user
	 * @return result of code execution as plain text (String)
	 */
	public String executeCommand(String receivedCode) {
		String code = receivedCode;
		String result = "EMPTY";

		if (!code.endsWith("END"))
			code = code + "\nEND\n";

		System.out.println("[INFO: Executing sunset in commandline with provided code!]");
		// System.out.println(code);
		System.out.println(this.SEP_LINE);

		Instant startTime = Instant.now();

		try {

			// start the process to execute sunset
			this.process = Runtime.getRuntime().exec("java -jar " + sunsetPath + " --cmd");

			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

			out.write(code);
			out.flush();

			in.readLine();
			result = "";
			// returnResult += in.readLine();
			String line = null;
			while ((line = in.readLine()) != null)
				result += line + "\n";

			this.destroyProcess();

			Instant endTime = Instant.now();

			long elapsedTime = Duration.between(startTime, endTime).toMillis();

			System.out
					.println("[INFO: Duration of sunset execution: " + elapsedTime + "ms]");
			System.out.println(this.SEP_LINE);

			return result;

		} catch (IOException e) {
			System.out.println("[ERROR: There was an exception when trying to execute sunset!]");
			System.out.println(e.getMessage());
			System.out.println(this.SEP_LINE);
			return e.getMessage();
		}
	}
	
	public void destroyProcess() {
		this.process.destroy();
	}

}
