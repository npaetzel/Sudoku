package com.github.npaetzel.Sudoku;
import java.io.Serializable;

public enum Field implements Serializable {
	TOP_LEFT		(0, 0),
	TOP_CENTER		(3, 0),
	TOP_RIGHT		(6, 0),
	CENTER_LEFT		(0, 3),
	CENTER			(3, 3),
	CENTER_RIGHT	(6, 3),
	BOTTOM_LEFT		(0, 6),
	BOTTOM_CENTER	(3, 6),
	BOTTOM_RIGHT	(6, 6);
	
	int firstColumn;
	int firstRow;
	Block[] blocks = new Block[9];
	/**
	 * 
	 * @return returns the first column of a field.
	 */
	public int getFirstColumn() {
		return firstColumn;
	}
	/**
	 * 
	 * @return returns the first row of a field.
	 */
	public int getFirstRow() {
		return firstRow;
	}
	/**
	 * 
	 * @return returns the last column of a field.
	 */
	public int getLastColumn() {
		return firstColumn+2;
	}
	/**
	 * 
	 * @return returns the last row of a field.
	 */
	public int getLastRow() {
		return firstRow+2;
	}
	public Block getBlock(int index) {
		return blocks[index];
	}
	public void setBlock(int index, Block block) {
		blocks[index] = block;
	}
	public void deleteBlocks(int value) {
		for(int i=0; i<9; i++) {
			if(getBlock(i) != null && getBlock(i).getNumber() == value) {
				setBlock(i, null);
			}
		}
	}
	public boolean hasDoubles(Block block) {
		for(int i=0; i<9; i++) {
			if(getBlock(i) != null && i != block.getFieldIndex() && block.getNumber() == getBlock(i).getNumber()) {
				return true;
			}
		}
		return false;
	}
	
	Field(int firstColumn, int firstRow) {
		this.firstColumn = firstColumn;
		this.firstRow = firstRow;
	}
	/**
	 * Searches for the field that contains the block(x, y).
	 * @param x x-coordinate of the block.
	 * @param y y-coordinate of the block.
	 * @return returns the found Field or null.
	 */
	public static Field getField(int x, int y) {
		for(int i=0; i<Field.values().length; i++) {
			Field field = Field.values()[i];
			if(x >= field.getFirstColumn() && x <= field.getLastColumn()) {
				if(y >= field.getFirstRow() && y <= field.getLastRow()) {
					return Field.values()[i];
				}
			}
		}
		return null;
	}
	public static int getIndex(int x, int y) {
		Field field = getField(x, y);
		return (y-field.getFirstRow())*3+x-field.getFirstColumn();
	}
	
}
