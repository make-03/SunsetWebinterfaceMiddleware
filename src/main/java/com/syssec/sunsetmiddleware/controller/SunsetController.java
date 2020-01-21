package com.syssec.sunsetmiddleware.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
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
 * Defines the behavior for execution of code and the user manually canceling
 * the execution.
 * 
 * @author Markus R.
 *
 */
@Controller
public class SunsetController {
	private static final Logger LOGGER = Logger.getLogger(SunsetController.class);

	private SunsetThreadPool sunsetThreadPool;
	private Map<String, String> idToCode = new ConcurrentHashMap<>();

	public SunsetController() {
		this.sunsetThreadPool = new SunsetThreadPool();
		LOGGER.info(SunsetGlobalMessages.CONTROLLER_SUCCESSFULLY_LOADED);
	}

	@RequestMapping(value = { "/result" }, method = RequestMethod.POST)
	public ModelAndView executeCode(@RequestParam("code") String code, @RequestParam("uniqueId") String id)
			throws InterruptedException, ExecutionException, TimeoutException, TaskRejectedException {
		if (code.isEmpty()) {
			LOGGER.warn(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
			throw new IllegalArgumentException(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
		}

		LOGGER.info("Post Request received (User-ID: " + id + ")");

		this.idToCode.put(id, code);

		System.out.println("Code received (User-ID: " + id + "):\n" + code);

		String result = this.sunsetThreadPool.runSunsetThread(code, id);

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("codeOriginal", code);
		modelAndView.addObject("codeResult", result);

		this.idToCode.remove(id);

		System.out.println("Result of Execution (User-ID: " + id + "):\n" + result);
		LOGGER.info("Sunset Execution finished (User-ID: " + id + "), returning result ...");
		LOGGER.debug(String.format(SunsetGlobalMessages.THREAD_POOL_UTILIZATION_MESSAGE_AFTER_COMPLETION,
				this.sunsetThreadPool.getActiveCount(), this.sunsetThreadPool.getQueue().size()));

		return modelAndView;
	}

	@RequestMapping(value = { "/cancelled" }, method = RequestMethod.POST)
	public ModelAndView cancelExecution(@RequestParam("uniqueId2") String id) {
		boolean wasCancelled = this.sunsetThreadPool.cancelExecutionOfSpecificThread(id);

		LOGGER.info(SunsetGlobalMessages.EXECUTION_CANCELLED_BY_USER + " (User-ID: " + id + ")");

		if (wasCancelled) {
			LOGGER.debug(SunsetGlobalMessages.THREAD_CANCELLED);
		} else {
			LOGGER.debug(SunsetGlobalMessages.THREAD_CANCELLED_FAILED);
		}

		String code = this.idToCode.get(id);

		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("codeOriginal", code);
		modelAndView.addObject("codeResult", "Execution was cancelled by the user!");

		this.idToCode.remove(id);

		LOGGER.debug(String.format(SunsetGlobalMessages.THREAD_POOL_UTILIZATION_MESSAGE_AFTER_CANCELLING,
				this.sunsetThreadPool.getActiveCount(), this.sunsetThreadPool.getQueue().size()));

		return modelAndView;
	}

	public SunsetThreadPool getSunsetThreadPool() {
		return this.sunsetThreadPool;
	}

	@PreDestroy
	public void shutdownExecutorService() {
		System.out.println("(@PreDestroy) CONTROLLER: shutdown executor service ...");
		this.sunsetThreadPool.shutdownThreadPool();
	}

}
