package com.github.npaetzel.Sudoku;
import java.io.Serializable;

public class Block extends Position implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private int number;
	private Field field;
	private int fieldIndex;
	private Grid grid;
	/**
	 * Places a block into its new row and column and deletes it from its old ones.
	 * Is automatically called by setX and setY.
	 * @param oldX
	 * @param newX
	 * @param oldY
	 * @param newY
	 */
	public void setPosition(int oldX, int newX, int oldY, int newY) {
			if(oldX != -1 && oldY != -1) {
				getGrid().getRow(oldY).setBlock(oldX, null);
				getGrid().getColumn(oldX).setBlock(oldY, null);
			}
			if(newX != -1 && newY != -1) {
				getGrid().getRow(newY).setBlock(newX, this);
				getGrid().getColumn(newX).setBlock(newY, this);
				setField();
			}
	}
	
	@Override
	public boolean setX(int newX) {
		int oldX = getX();
		int oldY, newY;
		oldY = newY = getY();
		if(super.setX(newX)) {
			setPosition(oldX, newX, oldY, newY);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean setY(int newY) {
		int oldY = getY();
		int oldX, newX;
		oldX = newX = getX();
		if(super.setY(newY)) {
			setPosition(oldX, newX, oldY, newY);
			return true;
		}
		return false;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public Field getField() {
		return field;
	}
	
	public void setField() {
		field = Field.getField(getX(), getY());
		setFieldIndex();
	}

	public int getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex() {
		fieldIndex = Field.getIndex(getX(), getY());
		getField().setBlock(getFieldIndex(), this);
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}
	@Override
	public Block clone() {
		Block clone = null;
		try {
			clone = (Block) super.clone();
		} catch(CloneNotSupportedException e) {
		}
		return clone;
	}
	public Block(Grid grid) {
		setGrid(grid);
	}
}
