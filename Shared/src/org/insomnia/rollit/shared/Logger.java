package org.insomnia.rollit.shared;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A logging system that will log messages to one or more <code>PrintStream</code> instances.
 * 
 * @author Ciske
 * 
 */
public final class Logger {
	private static List<PrintStream> outputStreams = new ArrayList<PrintStream>();
	private static LogLevel logLevel = LogLevel.Low;

	/**
	 * Attaches a new stream to the output streams if it is not yet attached.
	 * 
	 * @param stream The stream instance to attach.
	 */
	public synchronized static void attachStream(PrintStream stream) {
		if (!outputStreams.contains(stream)) {
			outputStreams.add(stream);
		}
	}

	/**
	 * Detaches a stream from the output streams and closes it.
	 * 
	 * @param stream The stream instance to detach.
	 */
	public synchronized static void detachStream(PrintStream stream) {
		outputStreams.remove(stream);

		stream.close();
	}

	/**
	 * Detaches and closes all attached output streams.
	 */
	public synchronized static void detachStreams() {
		for (PrintStream stream : outputStreams) {
			detachStream(stream);
		}
	}

	/**
	 * Sets the current log level.
	 * 
	 * @param level The new log level.
	 */
	public synchronized static void setLogLevel(LogLevel level) {
		logLevel = level;
	}

	/**
	 * Returns the current log level.
	 */
	public synchronized static LogLevel getLogLevel() {
		return logLevel;
	}

	/**
	 * Prints a message followed by a new line to all attached output streams if the current log
	 * level is equal or greater than the log level of the message.
	 * 
	 * @param level The log level of the message.
	 * @param s The message.
	 */
	public synchronized static void println(LogLevel level, String s) {
		if (logLevel.shouldLog(level)) {
			for (PrintStream stream : outputStreams) {
				stream.println(s);
			}
		}
	}

	/**
	 * Prints a message to all attached output streams if the current log level is equal or greather
	 * than the log level of the message.
	 * 
	 * @param level The log level of the message.
	 * @param s The message.
	 */
	public synchronized static void print(LogLevel level, String s) {
		if (logLevel.shouldLog(level)) {
			for (PrintStream stream : outputStreams) {
				stream.print(s);
			}
		}
	}

}
