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

public class ThreadPoolTest {

	private SunsetThreadPool sunsetThreadPool;

	@Before
	public void setUp() {
		this.sunsetThreadPool = new SunsetThreadPool();
	}

	@After
	public void tearDown() {
		this.sunsetThreadPool.shutdownExecutorService();
	}

	@Test
	public void testSunsetThreadPoolWithDefaultConstructor() {
		assertThat(this.sunsetThreadPool.getMaxNumberOfThreads()).isNotNull().isNotNegative().isEqualTo(8);
		assertThat(this.sunsetThreadPool.getTimeoutSeconds()).isNotNull().isNotNegative().isEqualTo(5);

		this.sunsetThreadPool.shutdownExecutorService();

		assertThat(this.sunsetThreadPool.getExecutorService().isShutdown()).isTrue();
		assertThat(this.sunsetThreadPool.getExecutorService().isTerminated()).isTrue();
	}

	@Test
	public void testSunsetThreadPoolWithUserDefinedConstructor() {
		int maxNumberOfThreads = 16;
		int timeoutSeconds = 60;

		SunsetThreadPool sunsetThreadPoolCustomConstructor = new SunsetThreadPool(maxNumberOfThreads, timeoutSeconds);

		assertThat(sunsetThreadPoolCustomConstructor.getMaxNumberOfThreads()).isNotNull().isNotNegative().isEqualTo(16);
		assertThat(sunsetThreadPoolCustomConstructor.getTimeoutSeconds()).isNotNull().isNotNegative().isEqualTo(60);

		sunsetThreadPoolCustomConstructor.shutdownExecutorService();

		assertThat(sunsetThreadPoolCustomConstructor.getExecutorService().isShutdown()).isTrue();
		assertThat(sunsetThreadPoolCustomConstructor.getExecutorService().isTerminated()).isTrue();
	}

	@Test
	public void testHelloWorldCodeReturnsValidResult() throws InterruptedException, ExecutionException {
		String code = "program HelloWorld {\n" + "\t println(\"test\");\n" + "}";
		String id = UUID.randomUUID().toString();

		String result = this.sunsetThreadPool.runSunsetExecutor(code, id);

		assertThat(result).isNotNull().isNotEmpty().isEqualTo("test");
	}

	@Test
	public void testStandardElGamalCodeReturnsValidResult() throws InterruptedException, ExecutionException {
		String codeStandardElGamal = this.getStandardElGamalCode();
		String idRSA = UUID.randomUUID().toString();

		String result = this.sunsetThreadPool.runSunsetExecutor(codeStandardElGamal, idRSA);

		assertThat(result).isNotNull().isNotEmpty().startsWith("message: 123\n\nciphertext: {")
				.endsWith("decrypted into: 123");
	}

	@Test
	public void testCodeWithEndlessLoopCausesTimeoutException() {
		this.sunsetThreadPool.setTimeoutSeconds(10);

		System.out.println("Testing TimeoutException with a timeout of 10 seconds ...");
		
		String code = "program EndlessTest {\n" + "\t while(true) {}\n" + "}";
		String id = UUID.randomUUID().toString();

		SunsetExecutor sunsetExecutor = new SunsetExecutor();

		assertThatThrownBy(() -> {
			this.sunsetThreadPool.getFutureResult(sunsetExecutor, code, id);
		}).isInstanceOf(TimeoutException.class);

		sunsetExecutor.destroyProcess(); // process has to be destroyed manually here!
	}

	@Test
	public void testIllegalArgumentExceptionIsThrown() {
		assertThatThrownBy(() -> {
			this.sunsetThreadPool.setMaxNumberOfThreads(0);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Maximum number of threads must be >0!");

		assertThatThrownBy(() -> {
			this.sunsetThreadPool.setMaxNumberOfThreads(-1);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Maximum number of threads must be >0!");

		assertThatThrownBy(() -> {
			this.sunsetThreadPool.setTimeoutSeconds(0);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Timeout in seconds must be >0!");

		assertThatThrownBy(() -> {
			this.sunsetThreadPool.setTimeoutSeconds(-1);
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Timeout in seconds must be >0!");

		assertThatThrownBy(() -> {
			SunsetThreadPool customSunsetThreadPool = new SunsetThreadPool(0, 60);
			customSunsetThreadPool.shutdownExecutorService();
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Maximum number of threads must be >0!");

		assertThatThrownBy(() -> {
			SunsetThreadPool customSunsetThreadPool = new SunsetThreadPool(16, 0);
			customSunsetThreadPool.shutdownExecutorService();
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Timeout in seconds must be >0!");

		assertThatThrownBy(() -> {
			SunsetThreadPool customSunsetThreadPool = new SunsetThreadPool(0, 0);
			customSunsetThreadPool.shutdownExecutorService();
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching("Maximum number of threads must be >0!");
	}

	private String getStandardElGamalCode() {
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

}
