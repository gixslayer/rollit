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

	public List<Score> getScoresAbove(List<Score> scores, T value) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : scores) {
			if (score.getScore().compareTo(value) > 0) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresBelow(T value) {
		return getScoresBelow(scores, value);
	}

	public List<Score> getScoresBelow(List<Score> scores, T value) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : scores) {
			if (score.getScore().compareTo(value) < 0) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresAfter(Date date) {
		return getScoresAfter(scores, date);
	}

	public List<Score> getScoresAfter(List<Score> scores, Date date) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : scores) {
			if (score.getDate().after(date)) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresBefore(Date date) {
		return getScoresBefore(scores, date);
	}

	public List<Score> getScoresBefore(List<Score> scores, Date date) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : scores) {
			if (score.getDate().before(date)) {
				result.add(score);
			}
		}

		return result;
	}

	public List<Score> getScoresBy(String player) {
		return getScoresBy(scores, player);
	}

	public List<Score> getScoresBy(List<Score> scores, String player) {
		List<Score> result = new ArrayList<Score>();

		for (Score score : scores) {
			if (score.getPlayer().equals(player)) {
				result.add(score);
			}
		}

		return result;
	}

	public double getAverage() throws OperationNotSupportedException {
		return getAverage(scores);
	}

	public double getAverage(List<Score> scores) throws OperationNotSupportedException {
		if (scores.size() == 0) {
			throw new OperationNotSupportedException(
					"Must have atleast one element to compure average");
		}

		Class<?> scoreClass = scores.get(0).getScore().getClass();

		if (!Number.class.isAssignableFrom(scoreClass)) {
			throw new OperationNotSupportedException("Score type must be assignable to a Double");
		}

		double sum = 0;

		for (Score score : scores) {
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

		return sum / scores.size();
	}

	public List<Score> getScores() {
		return scores;
	}

	public List<Score> sortIncremental() {
		return sortIncremental(scores);
	}

	public List<Score> sortIncremental(List<Score> scores) {
		int lastSwap = scores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = scores.get(i);
				Score b = scores.get(i + 1);

				if (a.getScore().compareTo(b.getScore()) > 0) {
					scores.set(i, b);
					scores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(scores);

		return result;
	}

	public List<Score> sortDecremental() {
		return sortDecremental(scores);
	}

	public List<Score> sortDecremental(List<Score> scores) {
		int lastSwap = scores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = scores.get(i);
				Score b = scores.get(i + 1);

				if (a.getScore().compareTo(b.getScore()) < 0) {
					scores.set(i, b);
					scores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(scores);

		return result;
	}

	public List<Score> sortDateIncremental() {
		return sortDateIncremental(scores);
	}

	public List<Score> sortDateIncremental(List<Score> scores) {
		int lastSwap = scores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = scores.get(i);
				Score b = scores.get(i + 1);

				if (a.getDate().compareTo(b.getDate()) > 0) {
					scores.set(i, b);
					scores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(scores);

		return result;
	}

	public List<Score> sortDateDecremental() {
		return sortDateDecremental(scores);
	}

	public List<Score> sortDateDecremental(List<Score> scores) {
		int lastSwap = scores.size() - 1;

		while (lastSwap != 0) {
			int currentLastSwap = 0;

			for (int i = 0; i < lastSwap; i++) {
				Score a = scores.get(i);
				Score b = scores.get(i + 1);

				if (a.getDate().compareTo(b.getDate()) < 0) {
					scores.set(i, b);
					scores.set(i + 1, a);

					currentLastSwap = i;
				}
			}

			lastSwap = currentLastSwap;
		}

		List<Score> result = new ArrayList<Score>();

		result.addAll(scores);

		return result;
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
