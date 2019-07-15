package com.syssec.sunsetthreadpool;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class for creating/managing the thread pool and the threads currently
 * running. Threads are stored
 * 
 * @author Markus
 *
 */
public class SunsetThreadPool implements Runnable {

	/**
	 * Data Structure used to store "Key-Value Pairs" for storing information about
	 * currently running threads ("user-ID" maps to "thread-ID" associated with this user). The main reason for this is to keep track of users
	 * manually cancelling calculations via the Web Interface.
	 */
	private HashMap<String, Integer> execution_threads;

	/**
	 * Service to manage all the threads running. For this program a fixed thread
	 * pool is used to limit the maximum number of concurrent calculations being
	 * performed.
	 */
	private ExecutorService threadPool;

	public SunsetThreadPool() {
		this.execution_threads = new HashMap<String, Integer>();
		this.threadPool = Executors.newFixedThreadPool(8); // default value if no value is given
	}

	public SunsetThreadPool(int max_processes) {
		this.execution_threads = new HashMap<String, Integer>();
		this.threadPool = Executors.newFixedThreadPool(max_processes);
	}

	@Override
	public void run() {
		//TODO execute thread code
	}
	
	public void addThread(String user_id) {
		int thread_id = 1; // for testing
		this.execution_threads.put(user_id, thread_id);
	}
	
	public String getThreads() {
		return this.execution_threads.toString();
	}

}
