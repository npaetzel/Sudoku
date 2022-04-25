package com.github.npaetzel.Sudoku;
import java.io.Serializable;

public abstract class Position implements Serializable {
	private static final long serialVersionUID = 1L;
	private int x = -1;
	private int y = -1;
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean setX(int x) {
		if(x >= 0) {
			this.x = x;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean setY(int y) {
		if(y >= 0) {
			this.y = y;
			return true;
		} else {
			return false;
		}
		
	}
	
	public void setPosition(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
}
