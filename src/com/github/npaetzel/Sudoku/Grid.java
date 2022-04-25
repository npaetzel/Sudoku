package com.github.npaetzel.Sudoku;
import java.io.Serializable;

public class Grid implements Serializable {
	private static final long serialVersionUID = 1L;
	private Line[] rows = new Line[9];
	private Line[] columns = new Line[9];
	private Block[] clearedBlocks;
	private Block[] guessedBlocks;
	private int toClear;
	private boolean win = false;
	
	public Line getRow(int index) {
		return rows[index];
	}
	public Line getColumn(int index) {
		return columns[index];
	}
	public Block getClearedBlock(int index) {
		return clearedBlocks[index];
	}
	public Block getClearedBlock(int x, int y) {
		for(int i=0; i<getToClear(); i++) {
			if(getClearedBlock(i).getX() == x && getClearedBlock(i).getY() == y) {
				return getClearedBlock(i);
			}
		}
		return null;
	}
	public void setClearedBlock(int index, Block block) {
		clearedBlocks[index] = block;
	}
	public Block getGuessedBlock(int index) {
		return guessedBlocks[index];
	}
	public Block getGuessedBlock(int x, int y) {
		for(int i=0; i<getToClear(); i++) {
			if(getGuessedBlock(i).getX() == x && getGuessedBlock(i).getY() == y) {
				return getGuessedBlock(i);
			}
		}
		return null;
	}
	public void setGuessedBlock(int index, Block block) {
		guessedBlocks[index] = block;
	}
	public int getToClear() {
		return toClear;
	}
	public void setToClear(int toClear) {
		this.toClear = toClear;
	}
	public boolean getWin() {
		return win;
	}
	public void setWin(boolean win) {
		this.win = win;
	}
	/**
	 * Deletes a single block by its coordinates from its row and column.
	 * @param x x-coordinate of a block to be deleted
	 * @param y y-coordinate of a block to be deleted
	 */
	public void deleteBlock(int x, int y) {
		getRow(y).setBlock(x, null);
		getColumn(x).setBlock(y, null);
		Field.getField(x, y).setBlock(Field.getIndex(x,y), null);
	}
	/**
	 * Deletes all blocks with the given value from their row and column.
	 * @param values an array containing the values to be deleted
	 */
	public void deleteBlocks(int... values) {
		for(int value: values) {
			for(int i=0; i<9; i++) {
				getRow(i).deleteBlocks(value);
				getColumn(i).deleteBlocks(value);
				Field.values()[i].deleteBlocks(value);
			}
		}
	}
	/**
	 * Checks if a block(x, y) is already taken or if the value already exists in the same
	 * row, column or (optional) field.  
	 * 
	 * @param x x-coordinate of a block to be checked
	 * @param y y-coordinate of a block to be checked
	 * @param value the value to be checked if it is already set somewhere
	 * @param checkField set true if you need to check if the value is already set in the same field.
	 * @return true if the value can be set on block(x, y).
	 */
	public boolean checkNumber(int x, int y, int value, boolean checkField) {
		if(getRow(y).isSet(x) || getRow(y).hasDoubles(x, value) || getColumn(x).hasDoubles(y, value)) {
			return false;
		}
		if(checkField) {
			Field field = Field.getField(x, y);
			for(int i=field.getFirstRow(); i<field.getLastRow(); i++) {
				for(int j=field.getFirstColumn(); j<field.getLastColumn(); j++) {
					if(getRow(i).getBlock(j) != null && getRow(i).getBlock(j).getNumber() == value) {
						return false;
					}
				}
			}
		}
		return true;
	}
	public boolean checkWin() {
		for(int i=0; i<getToClear(); i++) {
			if(getGuessedBlock(i).getNumber() != getClearedBlock(i).getNumber()) {
				return false;
			}
		}
		setWin(true);
		return true;
	}
	/**
	 * Fills the grid with numbers from 1 to 9. At first it tries to set nine different ones into
	 * nine different fields. Then it goes on with two, three, four...to nine. If it can't set a number,
	 * it goes back to the former one and tries again.
	 */
	public void fillGrid() {
		int x, y;
		Field field;
		Block block;
		int innerCounter;
		int outerCounter = 1;
		for(int j=1; j<10; j++) {
			for(int i=0; i<Field.values().length; i++) {
				innerCounter = 1;
				field = Field.values()[i];
				block = new Block(this);
				
				do {
					x = (int) (Math.random() * 3 + field.getFirstColumn());
					y = (int) (Math.random() * 3 + field.getFirstRow());
				} while(++innerCounter % 1000 != 0 && !checkNumber(x, y, j, false));
	
				if(outerCounter % 1000 == 0) {
				int[] deletedValues = new int[9];
					int backtrack = Math.round(outerCounter/1000);
					for(int k=0; k<backtrack && k<9; k++) {
						deletedValues[k] = j-k;
					}
					
					deleteBlocks(deletedValues);
					outerCounter++;
					j = Math.max(j-backtrack-1, 1);
					i=-1;
					continue;
				}
				if(innerCounter % 1000 == 0) {
					deleteBlocks(j);
					outerCounter++;
					i=-1;
					continue;
				}
				block.setX(x);
				block.setY(y);
				block.setNumber(j);
			}
		}
	}
	/**
	 * Recursive algorithm that clears numbers in a way that only a single solution is possible
	 * from the grid and saves them into an array.	
	 * @param numbers for recursion purposes. Always start with a nine.
	 * @return returns true if there is only a single possible solution.
	 */
	public boolean clearGrid(int numbers) {
		if(numbers>0) {
			setClearedBlock(numbers-1, new Block(this));
			int x = -1;
			int y = -1;
			int solutions = 0;
			int counter = 0;
			do {
				getClearedBlock(numbers-1).setPosition(-1, x, -1, y);
				solutions = 0;
				do {
				x = (int) (Math.random()*9);
				y = (int) (Math.random()*9);
				} while (getRow(y).getBlock(x) == null);
				setClearedBlock(numbers-1, getRow(y).getBlock(x));
				deleteBlock(x, y);
				for(int j=1; j<10; j++) {
					if(checkNumber(x, y, j, true)) {
						if(clearGrid(numbers-1)) {
							solutions++;
							break;
						}
					}
				}
			} while(counter++ < 1000 && solutions>1);
			if(counter > 1000) {
				getClearedBlock(numbers-1).setPosition(-1, x, -1, y);
				return false;
			} else {
				
			}
		}
		return true;
	}
	
	public Grid(int clear) {
		for(int i=0; i<9; i++) {
			rows[i] = new Line();
			columns[i] = new Line();
		}
		
		setToClear(clear);
		clearedBlocks = new Block[getToClear()];
		guessedBlocks = new Block[getToClear()];
		
		fillGrid();
		clearGrid(getToClear());

		for(int i=0; i<getToClear(); i++) {
			setGuessedBlock(i, getClearedBlock(i).clone());
			getGuessedBlock(i).setNumber(0);
		}
	}	
}
