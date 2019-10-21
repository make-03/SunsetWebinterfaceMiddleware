package com.syssec.sunsetmiddleware.threadpool;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;

import javafx.util.Pair;

/**
 * Class for managing the thread pool and the threads currently
 * running. Each thread exectutes its own process of sunset to calculate the result.
 * 
 * @author Markus R.
 *
 */
public class SunsetThreadPool {

	private static final int DEFAULT_NUMBER_OF_THREADS = 8;
	private static final int DEFAULT_TIMEOUT_SECONDS = 5;
	
	private HashMap<String, Pair<Future<String>, SunsetExecutor>> idToFutureMap = new HashMap<>();
	
	/**
	 * Service to manage all the threads running. For this program a fixed thread
	 * pool is used to limit the maximum number of concurrent calculations being
	 * performed.
	 */
	private ExecutorService executorService;

	public SunsetThreadPool() {
		this.executorService = Executors.newFixedThreadPool(SunsetThreadPool.DEFAULT_NUMBER_OF_THREADS); 
	}

	public SunsetThreadPool(short max_threads) {
		this.executorService = Executors.newFixedThreadPool(max_threads);
	}
	
	public String runSunsetExecutor(String code, String id) throws InterruptedException, ExecutionException {	
		SunsetExecutor sunsetExecutor = new SunsetExecutor();
		
		// run(), calls execCommand() in SunsetExecutor and stores result in result-varialble
		// if thread is interrupted by timeout, return string "Timeout occured!"
		Future<String> future = executorService.submit(() -> {
			try {
				return sunsetExecutor.executeCommand(code);
			} catch (Exception e) {
				sunsetExecutor.destroyProcess();
				e.printStackTrace();
			}
			return "Executor Service failed!"; 
		});
		
		this.idToFutureMap.put(id, new Pair<Future<String>, SunsetExecutor>(future, sunsetExecutor));
		
		try {
			String result = future.get(SunsetThreadPool.DEFAULT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
			this.idToFutureMap.remove(id);
			return result;
		} catch (TimeoutException e) {
			sunsetExecutor.destroyProcess();
			e.printStackTrace();
		}
		
		this.idToFutureMap.remove(id);
		return "Timeout occurred after " + SunsetThreadPool.DEFAULT_TIMEOUT_SECONDS + " seconds!";
	}
	
	public boolean cancelExecutionOfSpecificThread(String id) {
		System.out.println("Map Entries: " + this.idToFutureMap.entrySet().toString());
		if(this.idToFutureMap.containsKey(id)) {
			this.idToFutureMap.get(id).getValue().destroyProcess();
			this.idToFutureMap.get(id).getKey().cancel(true);
			this.idToFutureMap.remove(id);
			return true;
		} else {
			return false;
		}
	}
	
	public void shutdownExecutorService() {
		this.executorService.shutdown();
	}

}
