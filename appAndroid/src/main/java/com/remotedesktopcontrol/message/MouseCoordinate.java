package com.remotedesktopcontrol.message;

public class MouseCoordinate extends Message{

	private static final long serialVersionUID = -6535556422568075268L;
	private int x;
	private int y;
	
	public MouseCoordinate(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public MouseCoordinate minus(MouseCoordinate mC) {
		int x = this.x - mC.x;
		int y = this.y - mC.y;
		return new MouseCoordinate(x,y);
	}
	
	public MouseCoordinate add(MouseCoordinate mC) {
		int x = this.x + mC.x;
		int y = this.y + mC.y;
		return new MouseCoordinate(x,y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "[x : "+this.x+", y : "+this.y+"]";
	}

}
