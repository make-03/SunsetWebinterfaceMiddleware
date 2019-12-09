package com.syssec.sunsetmiddleware;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.syssec.sunsetmiddleware.executor.SunsetExecutor;
import com.syssec.sunsetmiddleware.messages.SunsetGlobalMessages;

public class SunsetExecutorTests {
	private static final Logger LOGGER = Logger.getLogger(SunsetExecutorTests.class);
	
	private final int TIMEOUT_SECONDS = 5;

	private SunsetExecutor sunsetExecutor;

	@Before
	public void setUp() {
		this.sunsetExecutor = new SunsetExecutor();
		this.sunsetExecutor.setTimeoutSeconds(this.TIMEOUT_SECONDS);
	}

	@After
	public void tearDown() {
		if (sunsetExecutor.isProcessAlive()) {
			this.sunsetExecutor.destroyProcess();
		}
	}

	@Test
	public void testIllegalArgumentExceptionIsThrown() {
		assertThatThrownBy(() -> {
			this.sunsetExecutor.executeCommand("");
		}).isInstanceOf(IllegalArgumentException.class).hasMessageMatching(SunsetGlobalMessages.EMPTY_CODE_RECEIVED);
	}

	@Test
	public void testHelloWorldCodeIsSuccessfullyExecuted() throws TimeoutException, InterruptedException {
		String result = this.sunsetExecutor.executeCommand(this.getHelloWorldTestCode());

		assertThat(result).isNotNull().isNotEmpty().isEqualTo("Hello World");
	}

	private String getHelloWorldTestCode() {
		return "program HelloWorld {\n\tprintln(\"Hello World\");\n}";
	}

	@Test
	public void testStandardElGamalCodeIsSuccessfullyExecuted() throws TimeoutException, InterruptedException {
		String result = this.sunsetExecutor.executeCommand(this.getStandardElGamalTestCode());

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
	public void testProcessExecutionOfEndlessLoopThrowsInterruptedException() {
		LOGGER.debug("[Test] Executing sunset code with endless loop using a timeout of " + this.TIMEOUT_SECONDS
				+ " seconds ...");
		assertThatThrownBy(() -> {
			this.sunsetExecutor.executeCommand(this.getEndlessLoopTestCode());
		}).isInstanceOf(TimeoutException.class)
				.hasMessageMatching(String.format(SunsetGlobalMessages.TIMEOUT_EXCEPTION, this.TIMEOUT_SECONDS));
	}

	private String getEndlessLoopTestCode() {
		return "program Endless { \n\twhile(true) {}\n}";
	}

}
