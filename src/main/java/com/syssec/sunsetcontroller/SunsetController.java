package com.syssec.sunsetcontroller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syssec.sunsetexecutor.SunsetExecutor;

@Controller
public class SunsetController {

	private final String SEP_LINE = "--------------------------------------------------------------------------------------------";
	
	public SunsetController() {
		System.out.println("[INFO: Sunset Controller successfully loaded!]");
		// TODO initialize logger
	}

	@RequestMapping(value = { "/result" }, method = RequestMethod.POST)
	//@ResponseBody //DEPRECATED
	public ModelAndView getCode(@RequestParam("code") String code) {
		SunsetExecutor exec = new SunsetExecutor();

		// if textarea for code is empty, no execution of sunset needed!
		if (code.length() == 0) {
			System.out.println("[INFO: EMPTY CODE RECEIVED; NO INPUT CODE!]");
			System.out.println(this.SEP_LINE);
			//return String.format("NO INPUT CODE!");
			ModelAndView modelAndViewEmptyCode = new ModelAndView("/index");
		    modelAndViewEmptyCode.addObject("codeOriginal", "");
		    modelAndViewEmptyCode.addObject("codeResult", "NO CODE RECEIVED! PLEASE ENTER SOME CODE FOR EXECUTION!");
		    return modelAndViewEmptyCode;
		}

		System.out.println("[INFO: Code received!]\n" + code);
		System.out.println(this.SEP_LINE);

		// execute code with sunset via the commandline
		String result = exec.execCommand(code);

		System.out.println("[INFO: Result of sunset execution:]\n" + result);
		System.out.println(this.SEP_LINE);

		// DEPRECATED - was used for testing functionality
		// return String.format("[Code:] %s\n[Result:] %s", code, result);
		
		ModelAndView modelAndView = new ModelAndView("/index");
		modelAndView.addObject("codeOriginal", code);
	    modelAndView.addObject("codeResult", result);
	    return modelAndView;
	}

	@RequestMapping(value = { "/cancelled" }, method = RequestMethod.POST)
	//@ResponseBody // DEPRECATED
	public ModelAndView stopExecution() {
		try {
			/*
			 * currently kills the main java process (including all sub processes, so other
			 * parallel users have their calculations cancelled as well) 
			 * AND if program is executed in command line (spring boot *.jar file) the corresponding program is stopped as well!
			 * TODO only kill corresponding sub-process of user (get PID?)
			 */
			Runtime.getRuntime().exec("taskkill /IM java.exe /F");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[INFO: Sunset execution was cancelled by the user!]");
		System.out.println(this.SEP_LINE);
		
		// DEPRECATED - was used for testing functionality
		// return String.format("[INFO: Calculation was cancelled by the user!]");
		ModelAndView modelAndView = new ModelAndView("/index");
	    modelAndView.addObject("codeResult", "EXECUTION WAS CANCELLED BY THE USER!");
	    return modelAndView;
	}

}
