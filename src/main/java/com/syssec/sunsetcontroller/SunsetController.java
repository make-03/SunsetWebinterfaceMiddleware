package com.syssec.sunsetcontroller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syssec.sunsetexecutor.SunsetExecutor;
import com.syssec.sunsetthreadpool.SunsetThreadPool;

/**
 * Controller class for communication between client (browser) and server.
 * Defines the behavior for execution of code and the user manaully cancelling
 * the excecution.
 * 
 * @author Markus R.
 *
 */
@Controller
public class SunsetController {

	private final String SEP_LINE = "--------------------------------------------------------------------------------------------";
	
	private SunsetThreadPool threadPool;

	public SunsetController() {
		this.threadPool = new SunsetThreadPool(10);
		System.out.println("[INFO: Sunset Controller successfully loaded!]");	
	}

	/**
	 * Method used for receiving code from user (browser) and calling method
	 * {@link com.syssec.sunsetexecutor.SunsetExecutor#execCommand(String)}. Result
	 * is then added as an object to the modelAndView and returned to the user.
	 * 
	 * @param code - plain text code entered by the user
	 * @param id - generated ID (in browser) to identify which user sent which code/request
	 * @return modelAndView that contains name of template and additional objects
	 */
	@RequestMapping(value = { "/result" }, method = RequestMethod.POST)
	public ModelAndView getCode(@RequestParam("code") String code, @RequestParam("uniqueId") String id) {
		SunsetExecutor exec = new SunsetExecutor();
		System.out.println("[INFO: User-ID for this request: "+id+"]");

		// if textarea for code is empty, no execution of sunset needed!
		if (code.length() == 0) {
			System.out.println("[INFO {ID = "+id+"}: NO INPUT CODE RECEIVED!]");
			System.out.println(this.SEP_LINE);
			// return String.format("NO INPUT CODE!");
			ModelAndView modelAndViewEmptyCode = new ModelAndView("index");
			modelAndViewEmptyCode.addObject("codeOriginal", "");
			modelAndViewEmptyCode.addObject("codeResult", "NO CODE RECEIVED! PLEASE ENTER CODE FOR EXECUTION AND TRY AGAIN!");
			return modelAndViewEmptyCode;
		}

		System.out.println("[INFO {ID = "+id+"}: Code received!]\n" + code);
		System.out.println(this.SEP_LINE);

		// execute code with sunset via the commandline
		String result = exec.execCommand(code);
		
		// TESTING thead pool functionality
		threadPool.addThread(id);
		System.out.println("THREADS IN THREAD POOL: " + threadPool.getThreads());

		System.out.println("[INFO {ID = "+id+"}: Result of sunset execution:]\n" + result);
		System.out.println(this.SEP_LINE);

		// DEPRECATED - was used for testing functionality
		// return String.format("[Code:] %s\n[Result:] %s", code, result);

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("codeOriginal", code);
		modelAndView.addObject("codeResult", result);
		return modelAndView;
	}

	/**
	 * Method used for manually cancelling execution of sunset by the user via the
	 * browser.
	 * 
	 * @param id - generated ID (in browser) to identify which user sent which code/request
	 * @return modelAndView that contains name of template and additional objects
	 */
	@RequestMapping(value = { "/cancelled" }, method = RequestMethod.POST)
	public ModelAndView stopExecution(@RequestParam("uniqueId2") String id) {
		try {
			/*
			 * currently kills the main java process (including all sub processes, so other
			 * parallel users have their calculations cancelled as well) AND if program is
			 * executed in command line (spring boot *.jar file) the corresponding program
			 * is stopped as well! TODO only kill corresponding sub-process of user (get
			 * PID?)
			 */
			Runtime.getRuntime().exec("taskkill /IM java.exe /F");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("[INFO {ID = "+id+"}: Sunset execution was cancelled by the user!]");
		System.out.println(this.SEP_LINE);

		// DEPRECATED - was used for testing functionality
		// return String.format("[INFO: Calculation was cancelled by the user!]");
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("codeResult", "EXECUTION WAS CANCELLED BY THE USER!");
		return modelAndView;
	}

}
