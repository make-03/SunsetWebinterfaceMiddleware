package com.syssec.sunsetwebinterfacemiddleware;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;
import com.syssec.sunsetmiddleware.threadpool.SunsetThreadPool;
import com.syssec.sunsetmiddleware.threadpool.SunsetThreadPoolConfiguration;

public class SunsetThreadPoolTests {

	private SunsetThreadPool sunsetThreadPool;
	private String id;

	@Before
	public void setUp() {
		this.sunsetThreadPool = new SunsetThreadPool();
		this.id = UUID.randomUUID().toString();
	}

	@After
	public void tearDown() {
		this.sunsetThreadPool.shutdownExecutorService();
	}

	@Test
	public void testSunsetThreadPoolWithDefaultConstructor() {
		assertThat(this.sunsetThreadPool.getCorePoolSize()).isNotNull().isNotNegative()
				.isEqualTo(SunsetThreadPoolConfiguration.getCorePoolSize());
		assertThat(this.sunsetThreadPool.getMaxPoolSize()).isNotNull().isNotNegative()
				.isEqualTo(SunsetThreadPoolConfiguration.getMaxPoolSize());
		assertThat(this.sunsetThreadPool.getTimeoutSeconds()).isNotNull().isNotNegative()
				.isEqualTo(SunsetThreadPoolConfiguration.getKeepAliveSeconds());
		assertThat(this.sunsetThreadPool.getQueueCapacity()).isNotNull().isNotNegative()
				.isEqualTo(SunsetThreadPoolConfiguration.getQueueCapacity());

		this.sunsetThreadPool.shutdownExecutorService();

		assertThat(this.sunsetThreadPool.getThreadPoolTaskExecutor().getActiveCount()).isEqualTo(0);
	}

	@Test
	public void testSetKeepAliveSecondsThrowsIllegalArgumentException() {
		assertThatThrownBy(() -> {
			SunsetThreadPoolConfiguration.setKeepAliveSeconds(0);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Value for keepAliveSeconds must be >0");

		assertThatThrownBy(() -> {
			SunsetThreadPoolConfiguration.setKeepAliveSeconds(-1);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Value for keepAliveSeconds must be >0");
	}

	@Test
	public void testHelloWorldCodeReturnsValidResult() throws InterruptedException, ExecutionException {
		String code = this.getHelloWorldTestCode();

		String result = this.sunsetThreadPool.runSunsetExecutor(code, this.id);

		assertThat(result).isNotNull().isNotEmpty().isEqualTo("Hello World");
	}

	private String getHelloWorldTestCode() {
		return "program HelloWorld {\n\tprintln(\"Hello World\");\n}";
	}

	@Test
	public void testStandardElGamalCodeReturnsValidResult() throws InterruptedException, ExecutionException {
		String codeStandardElGamal = this.getStandardElGamalTestCode();

		String result = this.sunsetThreadPool.runSunsetExecutor(codeStandardElGamal, this.id);

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
	public void testCodeWithEndlessLoopCausesTimeoutException() {
		System.out.println("Testing TimeoutException with a timeout of 5 seconds ... ");
		int test_timeout_seconds = 5;
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setKeepAliveSeconds(test_timeout_seconds);
		assertThat(this.sunsetThreadPool.getTimeoutSeconds()).isNotNull().isNotNegative()
				.isEqualTo(test_timeout_seconds);

		String code = "program EndlessTest {\n" + "\t while(true) {}\n" + "}";

		SunsetExecutor sunsetExecutor = new SunsetExecutor();

		assertThatThrownBy(() -> {
			this.sunsetThreadPool.getFutureResult(sunsetExecutor, code, this.id);
		}).isInstanceOf(TimeoutException.class);

		this.sunsetThreadPool.getThreadPoolTaskExecutor()
				.setKeepAliveSeconds(SunsetThreadPoolConfiguration.getKeepAliveSecondsDefaultValue());
		assertThat(this.sunsetThreadPool.getTimeoutSeconds()).isNotNull().isNotNegative()
				.isEqualTo(SunsetThreadPoolConfiguration.getKeepAliveSecondsDefaultValue());

		sunsetExecutor.destroyProcess(); // process has to be manually destroyed here!
	}

	@Test
	public void testTaskCountIsCorrectAfterSuccessfullExecution() throws InterruptedException, ExecutionException {
		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(0);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(0);

		this.sunsetThreadPool.runSunsetExecutor("test", this.id);

		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(1);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(1);
	}

	@Test
	public void testTaskCountIsCorrectAfterTimeout() throws InterruptedException, ExecutionException {
		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(0);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(0);

		int test_timeout_seconds = 5;
		this.sunsetThreadPool.getThreadPoolTaskExecutor().setKeepAliveSeconds(test_timeout_seconds);

		String code = "program EndlessTest {\n" + "\t while(true) {}\n" + "}";

		this.sunsetThreadPool.runSunsetExecutor(code, this.id);

		this.sunsetThreadPool.getThreadPoolTaskExecutor()
				.setKeepAliveSeconds(SunsetThreadPoolConfiguration.getKeepAliveSecondsDefaultValue());

		assertThat(this.sunsetThreadPool.getTaskCount()).isNotNull().isNotNegative().isEqualTo(1);
		assertThat(this.sunsetThreadPool.getCompletedTaskCount()).isNotNull().isNotNegative().isEqualTo(0);
	}

}
