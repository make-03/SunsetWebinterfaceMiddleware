package com.syssec.sunsetmiddleware.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syssec.sunsetmiddleware.threadpool.SunsetThreadPool;

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

	private final String SEP_LINE = "------------------------------------------------";

	private SunsetThreadPool threadPool;

	private Map<String, String> idToCodeMap = new HashMap<>();

	public SunsetController() {
		this.threadPool = new SunsetThreadPool();
		System.out.println("[INFO: Sunset Controller successfully loaded!]");
	}

	/**
	 * Method used for receiving code from user (browser) and calling method
	 * {@link com.syssec.sunsetmiddleware.threadpool.SunsetThreadPool#runSunsetExecutor(String code, String id)}.
	 * Result is then added as an object to the modelAndView and returned to the
	 * user.
	 * 
	 * @param code - plain text code entered by the user
	 * @param id   - generated ID (in browser) to identify which user sent which
	 *             code/request
	 * @return modelAndView that contains name of template and additional objects
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@RequestMapping(value = { "/result" }, method = RequestMethod.POST)
	public ModelAndView executeCode(@RequestParam("code") String code, @RequestParam("uniqueId") String id)
			throws InterruptedException, ExecutionException, TimeoutException {
		System.out.println("[INFO: User-ID for this request: " + id + "]");

		// if textarea for code is empty, no execution of sunset needed!
		if (code.length() == 0) {
			System.out.println("[INFO {ID = " + id + "}: NO INPUT CODE RECEIVED!]");
			System.out.println(this.SEP_LINE);
			// return String.format("NO INPUT CODE!");
			ModelAndView modelAndViewEmptyCode = new ModelAndView("index");
			modelAndViewEmptyCode.addObject("codeOriginal", "");
			modelAndViewEmptyCode.addObject("codeResult",
					"NO CODE RECEIVED! PLEASE ENTER CODE FOR EXECUTION AND TRY AGAIN!");
			return modelAndViewEmptyCode;
		}

		this.idToCodeMap.put(id, code);

		System.out.println("[INFO {ID = " + id + "}: Code received!]\n" + code);
		System.out.println(this.SEP_LINE);

		// execute code with sunset via the commandline
		// String result = exec.execCommand(code); // TODO: change this code to call
		// thread pool class with specific parameters
		// if timeout occurs, return different string
		String result = threadPool.runSunsetExecutor(code, id);

		// TESTING thead pool functionality
		// threadPool.addThread(id);
		// System.out.println("THREADS IN THREAD POOL: " + threadPool.getThreads());

		System.out.println("[INFO {ID = " + id + "}: Result of sunset execution:]\n" + result);
		System.out.println(this.SEP_LINE);

		this.idToCodeMap.remove(id);

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("codeOriginal", code);
		modelAndView.addObject("codeResult", result);

		return modelAndView;
	}

	/**
	 * Method used for manually cancelling execution of sunset by the user via the
	 * browser.
	 * 
	 * @param id - generated ID (in browser) to identify which user sent which
	 *           code/request
	 * @return modelAndView that contains name of template and additional objects
	 */
	@RequestMapping(value = { "/cancelled" }, method = RequestMethod.POST)
	public ModelAndView stopExecution(@RequestParam("uniqueId2") String id) {
		// TODO: error when passing parameter code for stopExecution ->
		// workaround, map data structure which stores code for each unique user (id)
		boolean wasCancelled = this.threadPool.cancelExecutionOfSpecificThread(id);

		if (wasCancelled) {
			System.out.println("Thread successfully cancelled!");
		} else {
			System.out.println("Something went wrong when cancelling!");
		}

		System.out.println("[INFO {ID = " + id + "}: Sunset execution was cancelled by the user!]");
		System.out.println(this.SEP_LINE);

		String code = this.idToCodeMap.get(id);

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("codeOriginal", code);
		modelAndView.addObject("codeResult", "Execution was cancelled by the user!");

		this.idToCodeMap.remove(id);

		return modelAndView;
	}

}
