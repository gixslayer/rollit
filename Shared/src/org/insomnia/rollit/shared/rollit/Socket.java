package org.insomnia.rollit.shared.rollit;

/**
 * Represents a socket in the game board.
 * @author ciske
 * 
 */
final class Socket {
	private Ball ball;

	/**
	 * Creates a new empty socket.
	 */
	Socket() {
		this.ball = Ball.None;
	}

	/**
	 * Checks if the socket currently contains no ball.
	 */
	public boolean isEmpty() {
		return ball == Ball.None;
	}

	/**
	 * Returns the ball that is currently in the socket.
	 */
	public Ball getBall() {
		return ball;
	}

	/**
	 * Sets the ball that is currently in the socket.
	 * @param ball The ball to set in the socket. The value can be <code>Ball.None</code> to make
	 * the socket empty.
	 */
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	/**
	 * Checks if both sockets contain the same ball (which can be <code>Ball.None</code>).
	 * @param socket The socket to compare against.
	 */
	public boolean equals(Socket socket) {
		return this.ball == socket.ball;
	}
}
