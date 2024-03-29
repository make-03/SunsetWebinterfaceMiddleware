package com.syssec.sunsetmiddleware.threadpool;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;
import com.syssec.sunsetmiddleware.main.App;
import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

import javafx.util.Pair;

/**
 * Class for managing the ThreadPoolTaskExecutor and handling other events that
 * might occur (e.g. Timeout, TaskRejection when all Threads and the queue are
 * full). Each thread (Future<String> object) starts its own process of sunset
 * to calculate the result.
 * 
 * @author Markus R.
 *
 */
public class SunsetThreadPool {
	private static final Logger LOGGER = LogManager.getLogger(SunsetThreadPool.class);

	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private Map<String, Pair<Future<String>, SunsetExecutor>> idToFuture = new ConcurrentHashMap<>();

	public SunsetThreadPool() {
		this.threadPoolTaskExecutor = App.threadPoolConfiguration.getThreadPoolTaskExecutor();
		LOGGER.info(SunsetGlobalMessages.THREAD_POOL_SUCCESSFULLY_INITIALIZED);
	}

	public String runSunsetThread(String code, String id) throws InterruptedException, ExecutionException {
		SunsetExecutor sunsetExecutor = new SunsetExecutor();

		LOGGER.debug(SunsetGlobalMessages.SUNSET_THREAD_RUN);

		String result;
		try {
			result = this.getFutureResult(sunsetExecutor, code, id);
		} catch (TimeoutException e) {
			result = String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION,
					threadPoolTaskExecutor.getKeepAliveSeconds());
			sunsetExecutor.destroyProcess();
			LOGGER.warn(String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION,
					threadPoolTaskExecutor.getKeepAliveSeconds()));
		} catch (TaskRejectedException e) {
			result = SunsetGlobalMessages.SERVER_IS_OVERLOADED;
			LOGGER.warn(SunsetGlobalMessages.SERVER_IS_OVERLOADED);
		}

		this.idToFuture.remove(id);

		return result;
	}

	public String getFutureResult(SunsetExecutor sunsetExecutor, String code, String id)
			throws InterruptedException, ExecutionException, TimeoutException, TaskRejectedException {
		Future<String> future = this.threadPoolTaskExecutor.submit(() -> {
			try {
				return sunsetExecutor.executeCommand(code);
			} catch (Exception e) {
				if (sunsetExecutor.isProcessAlive()) {
					sunsetExecutor.destroyProcess();
				}
				LOGGER.warn(e.getMessage());
			}
			return SunsetGlobalMessages.THREADPOOLTASKEXECUTOR_ERROR;
		});

		this.idToFuture.put(id, new Pair<Future<String>, SunsetExecutor>(future, sunsetExecutor));

		LOGGER.debug(String.format(SunsetGlobalMessages.THREAD_POOL_UTILIZATION_MESSAGE, this.getActiveCount(),
				this.getQueue().size()));

		return future.get(this.threadPoolTaskExecutor.getKeepAliveSeconds(), TimeUnit.SECONDS);
	}

	public boolean cancelExecutionOfSpecificThread(String id) {
		if (this.idToFuture.containsKey(id)) {
			if (this.idToFuture.get(id).getValue().isProcessAlive()) {
				this.idToFuture.get(id).getValue().destroyProcess();
			}
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

	/**
	 * Method for getting the queue capacity initially set when starting the
	 * application
	 * 
	 */
	public int getQueueCapacity() {
		return App.threadPoolConfiguration.getQueuecapacity();
	}

	public int getActiveCount() {
		return this.threadPoolTaskExecutor.getActiveCount();
	}

	public int getPoolSize() {
		return this.threadPoolTaskExecutor.getPoolSize();
	}

	/**
	 * Method for getting the queue of the underlying ThreadPoolExecutor.
	 * 
	 */
	public BlockingQueue<Runnable> getQueue() {
		return this.threadPoolTaskExecutor.getThreadPoolExecutor().getQueue();
	}

	/**
	 * Method for getting the approximate total task count of the underlying
	 * ThreadPoolExecutor that have ever been scheduled for execution.
	 * 
	 */
	public long getTaskCount() {
		return this.threadPoolTaskExecutor.getThreadPoolExecutor().getTaskCount();
	}

	/**
	 * Method for getting the approximate completed task count of the underlying
	 * ThreadPoolExecutor that have ever been scheduled for execution.
	 * 
	 */
	public long getCompletedTaskCount() {
		return this.threadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount();
	}

	@PreDestroy
	public void shutdownThreadPool() {
		System.out.println("(@PreDestroy) THREADPOOL: shutdown threadpool ...");
		this.shutdownExecutorService();
		this.idToFuture.values().forEach((pair) -> {
			System.out.println("(@PreDestroy) THREADPOOL: forcibly destroying process ...");
			pair.getValue().forciblyDestroyProcess();
		});
	}

}
