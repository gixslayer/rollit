package org.insomnia.rollit.shared.rollit;

public final class Board {
	public static final int BOARD_WIDTH = 8;
	public static final int BOARD_HEIGHT = 8;
	
	private final Socket[][] sockets;
	
	public Board() {
		this.sockets = new Socket[BOARD_WIDTH][BOARD_HEIGHT];
		
		for(int x = 0; x < BOARD_WIDTH; x++) {
			for(int y = 0; y < BOARD_HEIGHT; y++) {
				this.sockets[x][y] = new Socket();
			}
		}
	}
	
	public void clear() {
		for(int x = 0; x < BOARD_WIDTH; x++) {
			for(int y = 0; y < BOARD_HEIGHT; y++) {
				setSocket(x, y, Ball.None);
			}
		}
	}
	
	//@ requires x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT;
	public Ball getSocket(int x, int y) {
		return sockets[x][y].getBall();
	}

	//@ requires x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT;
	public boolean isSocketEmpty(int x, int y) {
		return sockets[x][y].isEmpty();
	}

	//@ requires x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT;
	public void setSocket(int x, int y, Ball ball) {
		sockets[x][y].setBall(ball);
	}

	public void synchronize(Board board) {
		for(int x = 0; x < BOARD_WIDTH; x++) {
			for(int y = 0; y < BOARD_HEIGHT; y++) {
				setSocket(x, y, board.getSocket(x, y));
			}
		}
	}

	public int countBalls(Ball ball) {
		int result = 0;
		
		for(int x = 0; x < BOARD_WIDTH; x++) {
			for(int y = 0; y < BOARD_HEIGHT; y++) {
				if(getSocket(x,y) == ball) {
					result++;
				}
			}
		}
		
		return result;
	}

	public boolean isFull() {
		boolean result = true;
		
		for(int x = 0; x < BOARD_WIDTH && result; x++) {
			for(int y = 0; y < BOARD_HEIGHT && result; y++) {
				if(isSocketEmpty(x, y)) {
					result = false;
				}
			}
		}
		
		return result;
	}
}
