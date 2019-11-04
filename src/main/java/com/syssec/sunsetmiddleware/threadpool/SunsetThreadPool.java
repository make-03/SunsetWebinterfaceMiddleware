package com.syssec.sunsetmiddleware.threadpool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;

import javafx.util.Pair;

/**
 * Class for managing the ExecutorService using a FixedThreadPool. Each thread
 * (Future<String> object) starts its own process of sunset to calculate the
 * result.
 * 
 * @author Markus R.
 *
 */
public class SunsetThreadPool {

	private static final int DEFAULT_NUMBER_OF_THREADS = 8;

	private int maxNumberOfThreads = SunsetThreadPool.DEFAULT_NUMBER_OF_THREADS;
	private int defaultTimeoutSeconds = 5;

	private Map<String, Pair<Future<String>, SunsetExecutor>> idToFuture = new ConcurrentHashMap<>();

	/**
	 * Service to manage all the threads running. For this program a fixed thread
	 * pool is used to limit the maximum number of concurrent calculations being
	 * performed.
	 */
	private ExecutorService executorService;

	public SunsetThreadPool() {
		this.executorService = Executors.newFixedThreadPool(SunsetThreadPool.DEFAULT_NUMBER_OF_THREADS);
	}

	public SunsetThreadPool(int max_threads, int timeout_seconds) {
		if (max_threads <= 0) {
			throw new IllegalArgumentException("Maximum number of threads must be >0!");
		}
		if (timeout_seconds <= 0) {
			throw new IllegalArgumentException("Timeout in seconds must be >0!");
		}

		this.maxNumberOfThreads = max_threads;
		this.executorService = Executors.newFixedThreadPool(this.maxNumberOfThreads);
		this.defaultTimeoutSeconds = timeout_seconds;
	}

	public String runSunsetExecutor(String code, String id) throws InterruptedException, ExecutionException {
		SunsetExecutor sunsetExecutor = new SunsetExecutor();

		String result;
		try {
			result = this.getFutureResult(sunsetExecutor, code, id);
		} catch (TimeoutException e) {
			result = "Timeout occurred after " + this.defaultTimeoutSeconds + " seconds!";
			sunsetExecutor.destroyProcess();
			e.printStackTrace();
		}

		this.idToFuture.remove(id);

		return result;
	}

	public String getFutureResult(SunsetExecutor sunsetExecutor, String code, String id)
			throws InterruptedException, ExecutionException, TimeoutException {
		Future<String> future = executorService.submit(() -> {
			try {
				return sunsetExecutor.executeCommand(code);
			} catch (Exception e) {
				sunsetExecutor.destroyProcess();
				e.printStackTrace();
			}
			return "Executor Service failed!";
		});

		this.idToFuture.put(id, new Pair<Future<String>, SunsetExecutor>(future, sunsetExecutor));

		return future.get(this.defaultTimeoutSeconds, TimeUnit.SECONDS);
	}

	public boolean cancelExecutionOfSpecificThread(String id) {
		if (this.idToFuture.containsKey(id)) {
			this.idToFuture.get(id).getValue().destroyProcess();
			this.idToFuture.get(id).getKey().cancel(true);
			this.idToFuture.remove(id);
			return true;
		}

		return false;
	}

	public void shutdownExecutorService() {
		this.executorService.shutdown();
	}

	public ExecutorService getExecutorService() {
		return this.executorService;
	}

	public int getTimeoutSeconds() {
		return this.defaultTimeoutSeconds;
	}

	public int getMaxNumberOfThreads() {
		return this.maxNumberOfThreads;
	}

	public void setThreadPoolProperties(int timeoutSeconds, int maxNumberOfThreads) {
		if (maxNumberOfThreads <= 0) {
			throw new IllegalArgumentException("Maximum number of threads must be >0!");
		}
		if (timeoutSeconds <= 0) {
			throw new IllegalArgumentException("Timeout in seconds must be >0!");
		}
		this.defaultTimeoutSeconds = timeoutSeconds;
		this.maxNumberOfThreads = maxNumberOfThreads;
	}

}
