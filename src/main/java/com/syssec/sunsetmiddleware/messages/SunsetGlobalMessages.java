package com.syssec.sunsetmiddleware.messages;

/**
 * Class containing a central location for storing all public global messages 
 * used by the other methods etc. of this program.
 * 
 */
public class SunsetGlobalMessages {
	/* Server status messages */
	public static final String WEBSERVER_SUCCESSFULLY_STARTED = "Sunset Webserver successfully started!";
	public static final String WEBSERVER_SHUTDOWN_ARGUMENT_REVEIVED = "--shutdown argument received! Shutting down server ...";
	public static final String WEBSERVER_RESTART_ARGUMENT_REVEIVED = "--restart argument received! Restarting server ...";
	public static final String CONTROLLER_SUCCESSFULLY_LOADED = "Sunset Controller successfully loaded!";
	public static final String THREAD_POOL_SUCCESSFULLY_INITIALIZED = "Sunset Thread Pool successfully initialized!";
	public static final String THREAD_POOL_DEFAULT_VALUES = 
			"Default values for ThreadPool: [Core Pool Size=%d, Max Pool Size=%d, Queue Capacity=%d, Keep Alive Seconds=%d]";
	public static final String SUNSET_INTERPRETER_VERSION_INFO = 
			"Using the [%s] interpreter file for executing code. Make sure your local Java installation is compatible with this file!";

	/* Messages about executing threads and processes */
	public static final String SUNSET_THREAD_RUN = "Starting Sunset Thread!";
	public static final String SUNSET_EXECUTION_VIA_COMMANDLINE = "Executing sunset via commandline with provided code!";

	/* Exception and Errors messages */
	public static final String ILLEGAL_ARGUMENTS_RECEIVED = "The argument(s) that were passed are not valid!";
	public static final String EMPTY_CODE_RECEIVED = "Empty input code received!";
	public static final String TIMEOUT_EXCEPTION = "Timeout occurred after %d seconds, cancelling execution!";
	public static final String SERVER_IS_OVERLOADED = "Server is overloaded right now!";
	public static final String IO_EXCEPTION = "IO Exception occurred!";
	public static final String SIZE_LIMIT_EXCEEDED_EXCEPTION = "Size of result String exceeded maximal length %d!";
	public static final String EXCEPTION = "Exception occurred!";
	public static final String THREADPOOLTASKEXECUTOR_ERROR = "ThreadPoolTaskExecutor error!";
	public static final String PID_IS_NOT_VALID = "Could not retrieve a valid PID from the file!";
	public static final String SUNSET_INTERPRETER_PATH_INVALID = "Sunset Interpreter path is invalid!";

	/* Messages about manually canceling a calculation */
	public static final String EXECUTION_CANCELLED_BY_USER = "Execution was cancelled by the user!";
	public static final String THREAD_CANCELLED = "Thread successfully cancelled!";
	public static final String THREAD_CANCELLED_FAILED = "Something went wrong when cancelling Thread!";

	/* Messages about utilization of ThreadPool */
	public static final String THREAD_POOL_UTILIZATION_MESSAGE = 
			"Current utilization of ThreadPool: %d Thread(s) active, %d Thread(s) waiting in Queue.";
	public static final String THREAD_POOL_UTILIZATION_MESSAGE_AFTER_COMPLETION = 
			"Current utilization of ThreadPool after completion: %d Thread(s) active, %d Thread(s) waiting in Queue.";
	public static final String THREAD_POOL_UTILIZATION_MESSAGE_AFTER_CANCELLING = 
			"Current utilization of ThreadPool after cancelling: %d Thread(s) active, %d Thread(s) waiting in Queue.";
}
