package org.insomnia.rollit.server.test;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.insomnia.rollit.server.Leaderboard;

public class Tests {

	public static void main(String[] args) {
		Leaderboard<Integer> leaderboard = new Leaderboard<Integer>();

		leaderboard.addScore(50, "2");
		leaderboard.addScore(10, "2");
		leaderboard.addScore(20, "1");
		leaderboard.addScore(26, "1");
		leaderboard.addScore(166, "1");

		List<Leaderboard<Integer>.Score> scores = leaderboard.sortIncremental();
		scores = leaderboard.getScoresBy("2");

		for (Leaderboard<Integer>.Score score : scores) {
			System.out.println(score.getScore() + " > " + score.getPlayer() + " @ "
					+ score.getDate());
		}

		try {
			double average = leaderboard.getAverage(scores);

			System.out.println("Average: " + average);
		} catch (OperationNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
