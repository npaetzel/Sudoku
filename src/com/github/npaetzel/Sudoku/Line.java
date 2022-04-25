package com.github.npaetzel.Sudoku;
import java.io.Serializable;

public class Line implements Serializable {
	private static final long serialVersionUID = 1L;
	private Block[] blocks = new Block[9];
	
	public Block getBlock(int index) {
		return blocks[index];
	}
	
	public void setBlock(int index, Block number) {
		this.blocks[index] = number;
	}
	/**
	 * Deletes all blocks with the specified value from the line.
	 * @param value blocks with this value will deleted.
	 */
	public void deleteBlocks(int value) {
		for(int i=0; i<9; i++) {
			if(getBlock(i) != null && getBlock(i).getNumber() == value) {
				setBlock(i, null);
			}
		}
	}
	/**
	 * Checks if the specified position on the line is already taken by a block.
	 * @param index the index on the line.
	 * @return returns true if the position is already taken.
	 */
	public boolean isSet(int index) {
			if(getBlock(index) == null) {
				return false;
			}
			return true;
	}
	/**
	 * Checks if the specified value is already set on another index apart from the index that has
	 * been specified.
	 * @param index the index that is not checked.
	 * @param value the value that is been searched.
	 * @return returns true if the value is found on another index.
	 */
	public boolean hasDoubles(int index, int value) {
		for(int i=0; i<9; i++) {
			if(getBlock(i) != null 
					&& value == getBlock(i).getNumber()
					&& i != index) {
				return true;
			}
		}
		return false;
	} 
}
