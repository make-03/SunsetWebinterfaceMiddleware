package com.syssec.sunsetmiddleware.threadpool;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private Map<String, Pair<Future<String>, SunsetExecutor>> idToFuture = new ConcurrentHashMap<>();
	
	public SunsetThreadPool() {
		this.threadPoolTaskExecutor = SunsetThreadPoolConfiguration.getThreadPoolTaskExecutor();
	}

	public String runSunsetExecutor(String code, String id) throws InterruptedException, ExecutionException {
		SunsetExecutor sunsetExecutor = new SunsetExecutor();
		
		String result;
		try {
			result = this.getFutureResult(sunsetExecutor, code, id);
		} catch (TimeoutException e) {
			result = "Timeout occurred after " + SunsetThreadPoolConfiguration.getKeepAliveSeconds() + " seconds!";
			sunsetExecutor.destroyProcess();
			e.printStackTrace();
		}

		this.idToFuture.remove(id);

		return result;
	}

	public String getFutureResult(SunsetExecutor sunsetExecutor, String code, String id)
			throws InterruptedException, ExecutionException, TimeoutException {
		Future<String> future = this.threadPoolTaskExecutor.submit(() -> {
			try {
				return sunsetExecutor.executeCommand(code);
			} catch (Exception e) {
				sunsetExecutor.destroyProcess();
				e.printStackTrace();
			}
			return "Executor Service failed!";
		});

		this.idToFuture.put(id, new Pair<Future<String>, SunsetExecutor>(future, sunsetExecutor));

		return future.get(SunsetThreadPoolConfiguration.getKeepAliveSeconds(), TimeUnit.SECONDS);
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
		this.threadPoolTaskExecutor.shutdown();
	}

	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return this.threadPoolTaskExecutor;
	}
	
	public int getCorePoolSize() {
		return this.threadPoolTaskExecutor.getCorePoolSize();
	}
	
	public int getMaxPoolSize() {
		return this.threadPoolTaskExecutor.getMaxPoolSize();
	}
	
	public int getTimeoutSeconds() {
		return this.threadPoolTaskExecutor.getKeepAliveSeconds();
	}
	
	public int getQueueCapacity() {
		return SunsetThreadPoolConfiguration.getQueueCapacity();
	}
	
	public int getActiveCount() {
		return this.threadPoolTaskExecutor.getActiveCount();
	}
	
	public int getPoolSize() {
		return this.threadPoolTaskExecutor.getPoolSize();
	}
	
	public BlockingQueue<Runnable> getQueue() {
		return this.threadPoolTaskExecutor.getThreadPoolExecutor().getQueue();
	}
	
	public long getTaskCount() {
		return this.threadPoolTaskExecutor.getThreadPoolExecutor().getTaskCount();
	}
	
	public long getCompletedTaskCount() {
		return this.threadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount();
	}

}
