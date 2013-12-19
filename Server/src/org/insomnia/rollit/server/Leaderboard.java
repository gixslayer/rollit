package org.insomnia.rollit.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a leader board which can store and manipulate scores.
 * 
 * @author Ciske
 * 
 * @param <T> The type of a score.
 */
public final class Leaderboard<T extends Comparable<T>> {
	private final List<Score> scores;

	/**
	 * Creates a new leader board without any scores.
	 */
	public Leaderboard() {
		this.scores = new ArrayList<Score>();
	}

	/**
	 * Adds a score to the database with the current date and time.
	 * 
	 * @param score The value of the score.
	 * @param player The player that got the score.
	 */
	public void addScore(T score, String player) {
		addScore(score, player, new Date());
	}

	/**
	 * Adds a score to the database.
	 * 
	 * @param score The value of the score.
	 * @param player The player that got the score.
	 * @param date The date and time of the score.
	 */
	public void addScore(T score, String player, Date date) {
		scores.add(new Score(score, date, player));
	}

	/**
	 * Returns a list of all scores above a certain value.
	 * 
	 * @param value The value to compare against.
	 */
	public List<Score> getScoresAbove(T value) {
		return getScoresAbove(scores, value);
	}

	/**
	 * Returns a list of all scores above a certain value.
	 * 
	 * @param argScores The source list to operate on.
	 * @param value The value to compare against.
	 */
	public List<Score> getScoresAbove(List<Score> argScores, T value) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getScore().compareTo(value) > 0) {
				result.add(score);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all scores below a certain value.
	 * 
	 * @param value The value to compare against.
	 */
	public List<Score> getScoresBelow(T value) {
		return getScoresBelow(scores, value);
	}

	/**
	 * Returns a list of all scores below a certain value.
	 * 
	 * @param argScores The source list to operate on.
	 * @param value The value to compare against.
	 */
	public List<Score> getScoresBelow(List<Score> argScores, T value) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getScore().compareTo(value) < 0) {
				result.add(score);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all scores after a certain date and time.
	 * 
	 * @param date The date and time to compare against.
	 */
	public List<Score> getScoresAfter(Date date) {
		return getScoresAfter(scores, date);
	}

	/**
	 * Returns a list of all scores after a certain date and time.
	 * 
	 * @param argScores The source list to operate on.
	 * @param date The date and time to compare against.
	 */
	public List<Score> getScoresAfter(List<Score> argScores, Date date) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getDate().after(date)) {
				result.add(score);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all scores before a certain date and time.
	 * 
	 * @param date The date and time to compare against.
	 */
	public List<Score> getScoresBefore(Date date) {
		return getScoresBefore(scores, date);
	}

	/**
	 * Returns a list of all scores before a certain date and time.
	 * 
	 * @param argScores The source list to operate on.
	 * @param date The date and time to compare against.
	 */
	public List<Score> getScoresBefore(List<Score> argScores, Date date) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getDate().before(date)) {
				result.add(score);
			}
		}

		return result;
	}

	/**
	 * Returns a list of all scores by a certain player.
	 * 
	 * @param player The name of the player to retrieve scores from.
	 */
	public List<Score> getScoresBy(String player) {
		return getScoresBy(scores, player);
	}

	/**
	 * Returns a list of all scores by a certain player.
	 * 
	 * @param argScores The source list to operate on.
	 * @param player The name of the player to retrieve scores from.
	 * @return
	 */
	public List<Score> getScoresBy(List<Score> argScores, String player) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getPlayer().equals(player)) {
				result.add(score);
			}
		}

		return result;
	}

	/**
	 * Returns the average value of all scores. If the score database did not contain any scores or
	 * if the score type isn't part of the <code>Number</code> class an
	 * <code>IllegalArgumentException</code> is thrown.
	 */
	public double getAverage() {
		return getAverage(scores);
	}

	/**
	 * Returns the average of all scores. If the score list did not contain any scores or if the
	 * score type isn't part of the <code>Number</code> class an
	 * <code>IllegalArgumentException</code> is thrown.
	 * 
	 * @param argScores The source list to operate on.
	 */
	public double getAverage(List<Score> argScores) {
		if (argScores.size() == 0) {
			throw new IllegalArgumentException("Must have atleast one element to compure average");
		}

		Class<?> scoreClass = scores.get(0).getScore().getClass();

		if (!Number.class.isAssignableFrom(scoreClass)) {
			throw new IllegalArgumentException("Score type must be assignable to a Number");
		}

		double sum = 0;

		for (Score score : argScores) {
			double valueToAdd = 0;

			T scoreValue = score.getScore();

			if (scoreValue instanceof Integer) {
				valueToAdd += (Integer) scoreValue;
			} else if (scoreValue instanceof Short) {
				valueToAdd += (Short) scoreValue;
			} else if (scoreValue instanceof Byte) {
				valueToAdd += (Byte) scoreValue;
			} else if (scoreValue instanceof Long) {
				valueToAdd += (Long) scoreValue;
			} else if (scoreValue instanceof Float) {
				valueToAdd += (Float) scoreValue;
			} else if (scoreValue instanceof Double) {
				valueToAdd += (Double) scoreValue;
			}

			sum += valueToAdd;
		}

		return sum / argScores.size();
	}

	/**
	 * Returns the current list of scores.
	 */
	public List<Score> getScores() {
		return scores;
	}

	/**
	 * Sorts the scores incrementally on their score value and returns a copy of the list.
	 */
	public List<Score> sortIncremental() {
		return sortIncremental(scores);
	}

	/**
	 * Sorts the scores incrementally on their score value and returns a copy of the list.
	 * 
	 * @param argScores The source list to operate on.
	 */
	public List<Score> sortIncremental(List<Score> argScores) {
		int lastSwap = argScores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = argScores.get(i);
				Score b = argScores.get(i + 1);

				if (a.getScore().compareTo(b.getScore()) > 0) {
					argScores.set(i, b);
					argScores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(argScores);

		return result;
	}

	/**
	 * Sorts the scores decrementally on their score value and returns a copy of the list.
	 */
	public List<Score> sortDecremental() {
		return sortDecremental(scores);
	}

	/**
	 * Sorts the scores decrementally on their score value and returns a copy of the list.
	 * 
	 * @param argScores The source list to operate on.
	 */
	public List<Score> sortDecremental(List<Score> argScores) {
		int lastSwap = argScores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = argScores.get(i);
				Score b = argScores.get(i + 1);

				if (a.getScore().compareTo(b.getScore()) < 0) {
					argScores.set(i, b);
					argScores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(argScores);

		return result;
	}

	/**
	 * Sorts the scores incrementally on their date value and returns a copy of the list.
	 */
	public List<Score> sortDateIncremental() {
		return sortDateIncremental(scores);
	}

	/**
	 * Sorts the scores incrementally on their date value and returns a copy of the list.
	 * 
	 * @param argScores The source list to operate on.
	 */
	public List<Score> sortDateIncremental(List<Score> argScores) {
		int lastSwap = argScores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = argScores.get(i);
				Score b = argScores.get(i + 1);

				if (a.getDate().compareTo(b.getDate()) > 0) {
					argScores.set(i, b);
					argScores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(argScores);

		return result;
	}

	/**
	 * Sorts the scores decrementally on their date value and returns a copy of the list.
	 */
	public List<Score> sortDateDecremental() {
		return sortDateDecremental(scores);
	}

	/**
	 * Sorts the scores decrementally on their date value and returns a copy of the list.
	 * 
	 * @param argScores The source list to operate on.
	 */
	public List<Score> sortDateDecremental(List<Score> argScores) {
		int lastSwap = argScores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = argScores.get(i);
				Score b = argScores.get(i + 1);

				if (a.getDate().compareTo(b.getDate()) < 0) {
					argScores.set(i, b);
					argScores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(argScores);

		return result;
	}

	/**
	 * Represents a score entry.
	 * 
	 * @author Ciske
	 * 
	 */
	public final class Score {
		private final T score;
		private final Date date;
		private final String player;

		/**
		 * Creates a new score instance.
		 * 
		 * @param argScore The value of the score.
		 * @param argDate The date and time the score was achieved.
		 * @param argPlayer The player that achieved the score.
		 */
		public Score(T argScore, Date argDate, String argPlayer) {
			this.score = argScore;
			this.date = argDate;
			this.player = argPlayer;
		}

		/**
		 * Returns the value of the score.
		 */
		public T getScore() {
			return score;
		}

		/**
		 * Returns the date and time at which the score was achieved.
		 */
		public Date getDate() {
			return date;
		}

		/**
		 * Returns the name of the player that achieved the score.
		 */
		public String getPlayer() {
			return player;
		}
	}
}
