package com.syssec.sunsetmiddleware.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;
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

	private SunsetThreadPool sunsetThreadPool;

	private Map<String, String> idToCode = new ConcurrentHashMap<>();

	public SunsetController() {
		this.sunsetThreadPool = new SunsetThreadPool();
		System.out.println("[INFO:] " + SunsetGlobalMessages.CONTROLLER_SUCCESSFULLY_LOADED);
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
			throws InterruptedException, ExecutionException, TimeoutException, TaskRejectedException {
		System.out.println("[INFO:] User-ID for this request: " + id);

		if (code.isEmpty()) {
			throw new IllegalArgumentException(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
		}

		this.idToCode.put(id, code);

		System.out.println("[INFO:] {ID = " + id + "}: Code received!\n" + code);

		String result = this.sunsetThreadPool.runSunsetExecutor(code, id);

		System.out.println("[INFO:] {ID = " + id + "}: Result of sunset execution:\n" + result);

		this.idToCode.remove(id);

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
	public ModelAndView cancelExecution(@RequestParam("uniqueId2") String id) {
		// TODO: error when passing parameter code for stopExecution ->
		// workaround, map data structure which stores code for each unique user (id)
		boolean wasCancelled = this.sunsetThreadPool.cancelExecutionOfSpecificThread(id);

		if (wasCancelled) {
			System.out.println(SunsetGlobalMessages.THREAD_CANCELLED);
		} else {
			System.out.println(SunsetGlobalMessages.THREAD_CANCELLED_FAILED);
		}

		System.out.println("[INFO:] {ID = " + id + "}: " + SunsetGlobalMessages.EXECUTION_CANCELLED_BY_USER);

		String code = this.idToCode.get(id);

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("codeOriginal", code);
		modelAndView.addObject("codeResult", "Execution was cancelled by the user!");

		this.idToCode.remove(id);

		return modelAndView;
	}

	public SunsetThreadPool getSunsetThreadPool() {
		return this.sunsetThreadPool;
	}

}
