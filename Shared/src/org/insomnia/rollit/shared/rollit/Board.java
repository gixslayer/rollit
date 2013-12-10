package org.insomnia.rollit.shared.rollit;

/**
 * Represents the rollit game board.
 * @author ciske
 * 
 */
public final class Board {
	/**
	 * The width of the game board.
	 */
	public static final int BOARD_WIDTH = 8;
	/**
	 * The height of the game board.
	 */
	public static final int BOARD_HEIGHT = 8;

	private final Socket[][] sockets;

	/**
	 * Creates a new rollit game board and initializes all sockets to be empty.
	 */
	public Board() {
		this.sockets = new Socket[BOARD_WIDTH][BOARD_HEIGHT];

		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				// The constructor of socket initializes to Ball.None so we don't have to explicitly
				// set it to be empty.
				this.sockets[x][y] = new Socket();
			}
		}
	}

	/**
	 * Clears the game board by setting each socket to be empty.
	 */
	public void clear() {
		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				setSocket(x, y, Ball.None);
			}
		}
	}

	/**
	 * Returns the ball currently contained by the socket.
	 * @param x The x position of the socket.
	 * @param y The y position of the socket.
	 */
	public Ball getSocket(int x, int y) {
		return sockets[x][y].getBall();
	}

	/**
	 * Checks if the socket is empty.
	 * @param x The x position of the socket.
	 * @param y The y position of the socket.
	 */
	public boolean isSocketEmpty(int x, int y) {
		return sockets[x][y].isEmpty();
	}

	/**
	 * Sets the ball contained by the socket.
	 * @param x The x position of the socket.
	 * @param y The y position of the socket.
	 * @param ball The ball to set in the socket.
	 */
	public void setSocket(int x, int y, Ball ball) {
		sockets[x][y].setBall(ball);
	}

	/**
	 * Synchronizes the state of the board.
	 * @param board The board instance to synchronize to.
	 */
	public void synchronize(Board board) {
		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				setSocket(x, y, board.getSocket(x, y));
			}
		}
	}

	/**
	 * Counts all balls of a given type and returns the result. This method can also be used to
	 * count empty sockets by calling it with <code>Ball.None</code>.
	 * @param ball The type of the ball to count.
	 */
	public int countBalls(Ball ball) {
		int result = 0;

		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				if (getSocket(x, y) == ball) {
					result++;
				}
			}
		}

		return result;
	}

	/**
	 * Checks if no empty sockets are available on the game board.
	 */
	public boolean isFull() {
		boolean result = true;

		for (int x = 0; x < BOARD_WIDTH && result; x++) {
			for (int y = 0; y < BOARD_HEIGHT && result; y++) {
				if (isSocketEmpty(x, y)) {
					result = false;
				}
			}
		}

		return result;
	}
}
