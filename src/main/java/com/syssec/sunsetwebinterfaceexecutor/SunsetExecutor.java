package com.syssec.sunsetwebinterfaceexecutor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.Duration;
import java.time.Instant;

public class SunsetExecutor {

	private String sunsetPath = "sunset.jar";
	private Process process;

	private final String SEP_LINE = "--------------------------------------------------------------------------------------------";

	public SunsetExecutor() {

	}

	public String execCommand(String _code) {
		String code = _code;
		String result = "EMPTY";

		if (!code.endsWith("END"))
			code = code + "\nEND\n";

		System.out.println("[INFO: Executing sunset in commandline provided code!]");
		System.out.println(code);
		System.out.println(this.SEP_LINE);

		Instant startTime = Instant.now();
		
		try {
	
			this.process = Runtime.getRuntime().exec("java -jar " + sunsetPath + " --cmd");

			if (process.isAlive()) {
				// Response and Request from the Process
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

				// finished getting result, terminating process
				process.destroy();
			}
			
			Instant endTime = Instant.now();
			
			long elapsedTime = Duration.between(startTime, endTime).toMillis();

			System.out.println("[INFO: Finished sunset execution in commandline! "
					+ "Duration: " + elapsedTime + "ms]");
			System.out.println(this.SEP_LINE);
			
			if(process.isAlive()) {
				killProcess();
			}

			return result;

		} catch (IOException e) {
			System.out.println("[ERROR: There was an exception when trying to execute sunset!]");
			System.out.println(e.getMessage());
			System.out.println(this.SEP_LINE);
			return e.getMessage();
		}
	}
	
	private void killProcess() {
		process.destroyForcibly();
	}
	
}
