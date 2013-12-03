package org.insomnia.rollit.shared.rollit;

public enum Ball {
	None((byte)0),
	Blue((byte)1),
	Red((byte)2),
	Yellow((byte)3),
	Green((byte)4);
	
	private byte value;
	
	Ball(byte value) {
		this.value = value;
	}
	
	public byte getValue() {
		return value;
	}
	
	public static Ball fromByte(byte value) {
		Ball ball = null;
		
		if(value == Blue.getValue()) {
			ball = Blue;
		} else if(value == Red.getValue()) {
			ball = Red;
		} else if(value == Yellow.getValue()) {
			ball = Yellow;
		} else if(value == Green.getValue()) {
			ball = Green;
		} else if(value == None.getValue()) {
			ball = None;
		}
		
		return ball;
	}
}
