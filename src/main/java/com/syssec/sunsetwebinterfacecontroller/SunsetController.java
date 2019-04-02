package com.syssec.sunsetwebinterfacecontroller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.syssec.sunsetwebinterfaceexecutor.SunsetExecutor;

@Controller
public class SunsetController {

	private final String SEP_LINE = "--------------------------------------------------------------------------------------------";
	private boolean manCancel = false;
	
	public SunsetController() {
		this.manCancel = false;
		System.out.println("[INFO: Sunset Controller successfully loaded!]");
		// TODO initialize logger
		// TODO initialize thread pools ???
	}

	@RequestMapping(value = { "/result" }, method = RequestMethod.POST)
	@ResponseBody
	public String getCode(@RequestParam("code") String code) {
		this.manCancel = false;
		SunsetExecutor exec = new SunsetExecutor();

		if (code.length() == 0) {
			System.out.println("[INFO: EMPTY CODE RECEIVED; NO INPUT CODE!]");
			System.out.println(this.SEP_LINE);
			return String.format("NO INPUT CODE!");
		}

		System.out.println("[INFO: Code received!]\n" + code);
		System.out.println(this.SEP_LINE);

		// execute code with sunset via the commandline
		String result = exec.execCommand(code);
		
		if(this.manCancel == true) {
			System.out.println("[INFO: Calculation was cancelled by user!]\n" + result);
			System.out.println(this.SEP_LINE);
			return String.format("CALCULATION WAS CANCELLED BY USER!");
		}

		System.out.println("[INFO: Result of sunset execution:]\n" + result);
		System.out.println(this.SEP_LINE);

		// TODO return result inside a HTML-Page
		return String.format("[Code:] %s\n[Result:] %s", code, result);
	}

	@RequestMapping(value = { "/cancelled" }, method = RequestMethod.POST)
	@ResponseBody
	public String stopExecution() {
		try {
			/*
			 * currently kills the main java process (including all sub processes, so other
			 * parallel users have their calculations cancelled as well) 
			 * AND if program is executed in command line (spring boot *.jar file) the corresponding program is stopped as well!
			 * TODO only kill corresponding sub-process of user (get PID?)
			 */
			Runtime.getRuntime().exec("taskkill /IM java.exe /F");
			this.manCancel = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[INFO: Sunset execution was cancelled by the user!]");
		System.out.println(this.SEP_LINE);

		return String.format("[INFO: Calculation was cancelled by the user!]");
	}

}
