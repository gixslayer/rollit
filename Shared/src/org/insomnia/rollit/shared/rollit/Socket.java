package org.insomnia.rollit.shared.rollit;

final class Socket {
	private Ball ball;
	
	Socket() {
		this.ball = Ball.None;
	}
	
	public boolean isEmpty() {
		return ball == Ball.None;
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	
}
