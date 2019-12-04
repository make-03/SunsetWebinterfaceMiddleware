package com.syssec.sunsetmiddleware;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.task.TaskRejectedException;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;
import com.syssec.sunsetmiddleware.main.App;
import com.syssec.sunsetmiddleware.threadpool.SunsetThreadPool;

public class SunsetThreadPoolTests {
	private final Logger logger = Logger.getLogger(SunsetThreadPoolTests.class);
	private final int TIMEOUT_SECONDS = 5;

	private SunsetThreadPool sunsetThreadPool;
	private String id;

	@Before
	public void setUp() {
		this.sunsetThreadPool = new SunsetThreadPool();
		this.id = UUID.randomUUID().toString();
		this.resetThreadPoolConfigToDefaultValues();
		
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setKeepAliveSeconds(this.TIMEOUT_SECONDS);
		this.sunsetThreadPool.getThreadPoolTaskExecutor().initialize();
	}
	
	private void resetThreadPoolConfigToDefaultValues() {
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setCorePoolSize(App.threadPoolConfiguration.getCorepoolsize());
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setMaxPoolSize(App.threadPoolConfiguration.getMaxpoolsize());
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setQueueCapacity(App.threadPoolConfiguration.getQueuecapacity());
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setKeepAliveSeconds(App.threadPoolConfiguration.getKeepaliveseconds());
		this.sunsetThreadPool.getThreadPoolTaskExecutor().initialize();
	}

	@After
	public void tearDown() {
		this.sunsetThreadPool.shutdownExecutorService();
	}

	@Test
	public void testSunsetThreadPoolWithDefaultValues() {
		assertThat(this.sunsetThreadPool.getCorePoolSize()).isNotNull().isNotNegative()
				.isEqualTo(App.threadPoolConfiguration.getCorepoolsize());
		assertThat(this.sunsetThreadPool.getMaxPoolSize()).isNotNull().isNotNegative()
				.isEqualTo(App.threadPoolConfiguration.getMaxpoolsize());
		assertThat(this.sunsetThreadPool.getQueueCapacity()).isNotNull().isNotNegative()
				.isEqualTo(App.threadPoolConfiguration.getQueuecapacity());
		assertThat(this.sunsetThreadPool.getTimeoutSeconds()).isNotNull().isNotNegative()
				.isEqualTo(this.TIMEOUT_SECONDS);

		this.sunsetThreadPool.shutdownExecutorService();

		assertThat(this.sunsetThreadPool.getThreadPoolTaskExecutor().getActiveCount()).isEqualTo(0);
	}

	@Test
	public void testHelloWorldCodeReturnsValidResult() throws InterruptedException, ExecutionException {
		String result = this.sunsetThreadPool.startSunsetThread(this.getHelloWorldTestCode(), this.id);

		assertThat(result).isNotNull().isNotEmpty().isEqualTo("Hello World");
	}

	private String getHelloWorldTestCode() {
		return "program HelloWorld {\n\tprintln(\"Hello World\");\n}";
	}

	@Test
	public void testStandardElGamalCodeReturnsValidResult() throws InterruptedException, ExecutionException {
		String result = this.sunsetThreadPool.startSunsetThread(this.getStandardElGamalTestCode(), this.id);

		assertThat(result).isNotNull().isNotEmpty().startsWith("message: 123\n\nciphertext: {")
				.endsWith("decrypted into: 123");
	}

	private String getStandardElGamalTestCode() {
		return "program StandardElGamal{\r\n" + "	const p : Prime := 2063;\r\n"
				+ "	const g : Integer := 607; //Generator \r\n"
				+ "	function encrypt(m: Integer; pk: Integer) : Z()[] {\r\n" + "		c: Z(p)[];\r\n"
				+ "		X: RandomGenerator(1:p-1);\r\n" + "		r: Integer;\r\n" + "		c := new Z()[2];\r\n"
				+ "		r := X;\r\n" + "		c[0] := g^r;\r\n" + "		c[1] := m * pk^r;\r\n"
				+ "		return c;\r\n" + "	}\r\n" + "	function decrypt(c: Z()[]; sk: Integer) : Z() {\r\n"
				+ "		return c[1] * c[0]^(-sk);\r\n" + "	}\r\n" + "	\r\n" + "	X: RandomGenerator(1:(p-1));\r\n"
				+ "	z: Integer;\r\n" + "	h: Integer;\r\n" + "	message: Integer;\r\n" + "	ciphertext: Z(p)[];\r\n"
				+ "	message := 123;\r\n" + "	z := X;\r\n" + "	h := g^z;\r\n"
				+ "	ciphertext := encrypt(message,h);\r\n" + "	println(\"message: \" + message);\r\n"
				+ "	println(\"ciphertext: \" + ciphertext);\r\n"
				+ "	println(\"decrypted into: \" + str(decrypt(ciphertext,z)));\r\n" + "}";
	}

	@Test
	public void testTaskCountIsCorrectAfterSuccessfullExecution() throws InterruptedException, ExecutionException {
		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(0);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(0);

		this.sunsetThreadPool.startSunsetThread("test", this.id);

		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(1);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(1);
	}

	@Test
	public void testTaskCountIsCorrectAfterTimeout() throws InterruptedException, ExecutionException {
		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(0);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(0);

		logger.debug("[Test] Executing sunset code with endless loop using a timeout of " + this.TIMEOUT_SECONDS
				+ "seconds ...");
		this.sunsetThreadPool.startSunsetThread(this.getEndlessLoopTestCode(), this.id);

		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(1);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(0);
	}

	@Test
	public void testCodeWithEndlessLoopCausesTimeoutException() {
		SunsetExecutor sunsetExecutor = new SunsetExecutor();

		logger.debug("[Test] Executing sunset code with endless loop using a timeout of " + this.TIMEOUT_SECONDS
				+ " seconds ...");
		assertThatThrownBy(() -> {
			this.sunsetThreadPool.getFutureResult(sunsetExecutor, this.getEndlessLoopTestCode(), this.id);
		}).isInstanceOf(TimeoutException.class);

		sunsetExecutor.destroyProcess(); // process has to be manually destroyed here!
	}

	private String getEndlessLoopTestCode() {
		return "program Endless {\n\twhile(true) {}\n}";
	}

	@Test
	public void testAbortPolicyOfThreadPoolThrowsTaskRejectedExceptionIfThreadPoolAndQueueAreFull()
			throws InterruptedException {
		final int CORE_POOL_SIZE_TEST = 1;
		final int MAX_POOL_SIZE_TEST = 1;
		final int QUEUE_CAPACITY_TEST = 1;

		this.sunsetThreadPool.getThreadPoolTaskExecutor().setCorePoolSize(CORE_POOL_SIZE_TEST);
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setMaxPoolSize(MAX_POOL_SIZE_TEST);
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setQueueCapacity(QUEUE_CAPACITY_TEST);
		this.sunsetThreadPool.getThreadPoolTaskExecutor().initialize();

		this.threadRunHelloWorldCode1.start();
		Thread.sleep(50);
		this.threadRunHelloWorldCode2.start();
		Thread.sleep(50);
		this.threadRunHelloWorldCode3CausingTaskRejectionException.start();

		this.threadRunHelloWorldCode3CausingTaskRejectionException.join();
		this.threadRunHelloWorldCode1.join();
		this.threadRunHelloWorldCode2.join();

		assertThat(this.resultThread1).isNotNull().isNotEmpty().isEqualTo("Hello World");
		assertThat(this.resultThread2).isNotNull().isNotEmpty().isEqualTo("Hello World");
		assertThat(this.resultThread3).isNotNull().isEmpty();
		assertThat(this.resultThread3ExceptionClassSimpleName).isNotNull().isNotEmpty()
				.isEqualTo("TaskRejectedException");
		assertThat(this.resultThread3ExceptionMessage).isNotNull().isNotEmpty()
				.startsWith("Executor [java.util.concurrent.ThreadPoolExecutor@")
				.contains("[Running, pool size = 1, active threads = 1, queued tasks = 1, completed tasks = 0]] "
						+ "did not accept task: com.syssec.sunsetmiddleware.threadpool.SunsetThreadPool");
	}

	private String resultThread1 = "";
	private Thread threadRunHelloWorldCode1 = new Thread() {
		public void run() {
			SunsetExecutor sunsetExecutor = new SunsetExecutor();
			try {
				resultThread1 = sunsetThreadPool.getFutureResult(sunsetExecutor, getHelloWorldTestCode(),
						UUID.randomUUID().toString());
			} catch (TaskRejectedException | InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	};

	private String resultThread2 = "";
	private Thread threadRunHelloWorldCode2 = new Thread() {
		public void run() {
			SunsetExecutor sunsetExecutor = new SunsetExecutor();
			try {
				resultThread2 = sunsetThreadPool.getFutureResult(sunsetExecutor, getHelloWorldTestCode(),
						UUID.randomUUID().toString());
			} catch (TaskRejectedException | InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	};

	private String resultThread3 = "";
	private String resultThread3ExceptionClassSimpleName = "";
	private String resultThread3ExceptionMessage = "";
	private Thread threadRunHelloWorldCode3CausingTaskRejectionException = new Thread() {
		public void run() {
			SunsetExecutor sunsetExecutor = new SunsetExecutor();
			try {
				resultThread3 = sunsetThreadPool.getFutureResult(sunsetExecutor, getHelloWorldTestCode(),
						UUID.randomUUID().toString());
			} catch (TaskRejectedException e) {
				resultThread3ExceptionMessage = e.getMessage();
				resultThread3ExceptionClassSimpleName = e.getClass().getSimpleName();
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	};

}
