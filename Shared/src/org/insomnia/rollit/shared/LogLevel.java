package org.insomnia.rollit.shared;

/**
 * Indicates the possible log levels.
 * 
 * @author Ciske
 * 
 */
public enum LogLevel {
	/**
	 * Low log level. Indicates that the message contains important information. This level should
	 * be used for all vital information.
	 */
	Low(),
	/**
	 * Medium log level. Indicates that the message could contain important information. This level
	 * should be used for all optional information.
	 */
	Medium(),
	/**
	 * High log level. Indicates that the message probably contains less important information. This
	 * level should be used for debugging information only.
	 */
	High();

	/**
	 * Checks whether the given log level is equal or greater than the current log level.
	 * 
	 * @param level The log level to compare against.
	 */
	public boolean shouldLog(LogLevel level) {
		return this == LogLevel.High || this == LogLevel.Medium && level != LogLevel.High
				|| this == LogLevel.Low && level == LogLevel.Low;
	}
}
