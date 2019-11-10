package com.syssec.sunsetmiddleware.threadpool;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

public class SunsetThreadPoolConfiguration {
	public static final int CORE_POOL_SIZE_DEFAULT = 8;
	public static final int MAX_POOL_SIZE_DEFAULT = 16;
	public static final int QUEUE_CAPACITY_DEFAULT = 8;
	public static final int KEEP_ALIVE_SECONDS_DEFAULT = 10;

	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	public SunsetThreadPoolConfiguration() {
		threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE_DEFAULT);
		threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE_DEFAULT);
		threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY_DEFAULT);
		threadPoolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS_DEFAULT);
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		threadPoolTaskExecutor.setThreadNamePrefix(SunsetGlobalMessages.THREAD_NAME_PREFIX);
		threadPoolTaskExecutor.initialize();
	}

	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return threadPoolTaskExecutor;
	}

	public void resetThreadPoolValuesToDefault() {
		threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE_DEFAULT);
		threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE_DEFAULT);
		threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY_DEFAULT);
		threadPoolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS_DEFAULT);
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		threadPoolTaskExecutor.setThreadNamePrefix(SunsetGlobalMessages.THREAD_NAME_PREFIX);
		threadPoolTaskExecutor.initialize();
	}
}
