package org.insomnia.rollit.shared;

public enum LogLevel {
	Low(), Medium(), High();

	public boolean shouldLog(LogLevel level) {
		return this == LogLevel.High || this == LogLevel.Medium && level != LogLevel.High
				|| this == LogLevel.Low && level == LogLevel.Low;
	}
}
