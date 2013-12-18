package org.insomnia.rollit.shared;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public final class Logger {
	private static List<PrintStream> outputStreams = new ArrayList<PrintStream>();
	private static LogLevel logLevel = LogLevel.Low;

	public synchronized static void attachStream(PrintStream stream) {
		if (!outputStreams.contains(stream)) {
			outputStreams.add(stream);
		}
	}

	public synchronized static void detachStream(PrintStream stream) {
		outputStreams.remove(stream);

		stream.close();
	}

	public synchronized static void detachStreams() {
		for (PrintStream stream : outputStreams) {
			detachStream(stream);
		}
	}

	public synchronized static void setLogLevel(LogLevel level) {
		logLevel = level;
	}

	public synchronized static LogLevel getLogLevel() {
		return logLevel;
	}

	public synchronized static void println(LogLevel level, String s) {
		if (logLevel.shouldLog(level)) {
			for (PrintStream stream : outputStreams) {
				stream.println(s);
			}
		}
	}

	public synchronized static void print(LogLevel level, String s) {
		if (logLevel.shouldLog(level)) {
			for (PrintStream stream : outputStreams) {
				stream.print(s);
			}
		}
	}

}
