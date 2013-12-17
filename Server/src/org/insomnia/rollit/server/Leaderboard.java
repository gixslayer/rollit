package org.insomnia.rollit.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.OperationNotSupportedException;

public final class Leaderboard<T extends Comparable<T>> {
	private final List<Score> scores;

	public Leaderboard() {
		this.scores = new ArrayList<Score>();
	}

	public void addScore(T score, String player) {
		addScore(score, player, new Date());
	}

	public void addScore(T score, String player, Date date) {
		scores.add(new Score(score, date, player));
	}

	public List<Score> getScoresAbove(T value) {
		return getScoresAbove(scores, value);
	}

	public List<Score> getScoresAbove(List<Score> argScores, T value) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getScore().compareTo(value) > 0) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresBelow(T value) {
		return getScoresBelow(scores, value);
	}

	public List<Score> getScoresBelow(List<Score> argScores, T value) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getScore().compareTo(value) < 0) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresAfter(Date date) {
		return getScoresAfter(scores, date);
	}

	public List<Score> getScoresAfter(List<Score> argScores, Date date) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getDate().after(date)) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresBefore(Date date) {
		return getScoresBefore(scores, date);
	}

	public List<Score> getScoresBefore(List<Score> argScores, Date date) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getDate().before(date)) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresBy(String player) {
		return getScoresBy(scores, player);
	}

	public List<Score> getScoresBy(List<Score> argScores, String player) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : argScores) {
			if (score.getPlayer().equals(player)) {
				result.add(score);
			}
		}

		return result;
	}

	public double getAverage() throws OperationNotSupportedException {
		return getAverage(scores);
	}

	public double getAverage(List<Score> argScores) throws OperationNotSupportedException {
		if (argScores.size() == 0 || !canBeAveraged(argScores.get(0).getScore())) {
			throw new OperationNotSupportedException(
					"Must have atleast one element of type Number to compure average");
		}

		double sum = 0;

		for (Score score : argScores) {
			// TODO: Verify this works.
			sum += double.class.cast(score);
		}

		return sum / argScores.size();
	}

	public List<Score> getScores() {
		return scores;
	}

	private boolean canBeAveraged(T value) {
		return value instanceof Integer || value instanceof Short || value instanceof Double
				|| value instanceof Float || value instanceof Long || value instanceof Byte;
	}

	public final class Score {
		private final T score;
		private final Date date;
		private final String player;

		public Score(T argScore, Date argDate, String argPlayer) {
			this.score = argScore;
			this.date = argDate;
			this.player = argPlayer;
		}

		public T getScore() {
			return score;
		}

		public Date getDate() {
			return date;
		}

		public String getPlayer() {
			return player;
		}
	}
}
