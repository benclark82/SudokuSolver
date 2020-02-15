package sudoku;
import java.util.*;

/**
 * 
 * @author Ben Clark
 * The sudokuStrategies class has all the strategy implementations to solve a sudoku puzzle
 *
 */
public class SudokuStrategies {

	int squaresBlank = 0;
	Integer allPossValues[] = new Integer[] {1,2,3,4,5,6,7,8,9};
	
	class SquareCoordinate {
		
		public int columnNum;
		public int rowNum;
		
		SquareCoordinate(int col, int row) {
			
			columnNum = col;
			rowNum = row;
		}
	}
	
	private static final int SUDOKU_MAX_ROW_SQUARES = 9;
	private static final int SUDOKU_MAX_COLUMN_SQUARES = 9;
	private static final int SUDOKU_MAX_BLOCKS = 9;
	private HashMap<Integer, ArrayList<SquareCoordinate>> blockNumSquareNumbers = new HashMap<Integer, ArrayList<SquareCoordinate>>();

	/**
	 * This method is the default constructor
	 */
	public SudokuStrategies() {
		
		//Setup a hash with block number as key and the square numbers for each block as the values
		setBlockNumSquareNumbers();
		
	}
	
	/**
	 * This method sets all the data for all of the coordinates of each block in the sudoku puzzle.
	 * Block numbers go left to right, top to bottom
	 */
	private void setBlockNumSquareNumbers() {
		
		int blockRowIncrement = 0;
		int blockColumnIncrement = 0;
		int rowNum = 0;
		int columnNum = 0;
		
		//Build all the blocks and square numbers that go in those blocks
		for(int i=0; i < SUDOKU_MAX_BLOCKS; i++) {
			
			ArrayList <SquareCoordinate> tempCoordinates = new ArrayList<SquareCoordinate>();
			
			//For every row num in block
			for(int blockRowNum=0; blockRowNum < 3; blockRowNum++) {
				
				//For every column num in block
				for(int blockColumnNum=0; blockColumnNum < 3; blockColumnNum++) {
					
					columnNum = blockColumnNum+blockColumnIncrement;
					rowNum = blockRowNum+blockRowIncrement;
					
					//Add coordinates to block
					tempCoordinates.add( new SquareCoordinate(columnNum, rowNum));
					
				}
			}
			
			blockNumSquareNumbers.put(i, tempCoordinates);
			
			
			//This will allow to jump to the first square of the next block. If it's the 3rd block in the row then
			//it will reset to 0 to start at beginning block of the next row
			if(blockColumnIncrement >= 6)
				blockColumnIncrement = 0;
			else
				blockColumnIncrement += 3;
			
			//This allows us to jump down to the first square of the next block of the next row after we are finished with a row
			if(i == 2) 
				blockRowIncrement = 3;
			else if(i == 5)
				blockRowIncrement = 6;

		}
	}
	
	/**
	 * This method adds in all possible values(1-9) to all squares that dont have a value
	 * @param sudokuSquares This is a 2D array of squares
	 */
	public void addAllPossibilities(Square sudokuSquares[][]) {

		//For every square in the row
		for(int columnNum = 0;columnNum < 9;columnNum++) {
			
			//For every square in the column
			for(int rowNum = 0;rowNum < 9;rowNum++) {
				
				//If square doesn't have a value then add all the possibilities
				if(sudokuSquares[columnNum][rowNum].getValue() == 0) {
					sudokuSquares[columnNum][rowNum].setPossibleValues(allPossValues);
					squaresBlank++;
				}

			}
		}
	}
	
	/**
	 * This prints all possible values for every square
	 * @param sudokuSquares This is a 2D array of squares
	 */
	public void printAllPossibilities(Square sudokuSquares[][]) {
		
		
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(sudokuSquares[j][i].getValue() == 0) {
					ArrayList<Integer> possValList = sudokuSquares[j][i].getPossibleValues();;
					
					System.out.print("Possible values for [" + j + "][" + i + "]");
					for(Integer possVal : possValList )
						System.out.print(" " + possVal);

				}
			}
		}
	}
	
	/**
	 * This will go through all known numbers and remove any possibilities of any of the squares in the same row, column, and block
	 * @param sudokuSquares This is a 2D array of squares
	 */
	public void removePossibilities(Square sudokuSquares[][]) {
		//go through ever square and if number is found, remove that possibility from squares in same row,
		//column, and 9 square section
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(sudokuSquares[i][j].getValue() != 0) {
					//System.out.println("Removing possibilities for value: " + sudokuSquares[i][j].getValue() + "for [" + i + "][" + j + "]");
					removePossibilitiesFromRow(i, j, sudokuSquares);
					removePossibilitiesFromColumn(i, j, sudokuSquares);
					removePossibilitiesFromBlock(i, j, sudokuSquares);
				}
			}
		}
	}
	/**
	 * This method finds naked singles in the sudoku squares
	 * @param sudokuSquares This is a 2D array of squares
	 * @return boolean This method returns true if any possible number is removed due to finding a naked single
	 */
	public boolean findNakedSingle(Square sudokuSquares[][]) {
	
	    boolean foundNakedSingle = false;
	    
		//for ever square in sudoku 
		for(int i = 0;i < SUDOKU_MAX_COLUMN_SQUARES; i++) {
			for(int j = 0;j < SUDOKU_MAX_ROW_SQUARES; j++) {
	
				if(sudokuSquares[i][j].getValue() == 0 && sudokuSquares[i][j].getPossibleValues().size() == 1) {
					System.out.println("NakedSingle: setting value " + sudokuSquares[i][j].getPossibleValues().get(0) + " for square [" + i + "][" + j + "]");
					sudokuSquares[i][j].setValue(sudokuSquares[i][j].getPossibleValues().get(0));
					
					//Remove possibilities for the number we just set
					removePossibilitiesFromRow(i, j, sudokuSquares);
					removePossibilitiesFromColumn(i, j, sudokuSquares);
					removePossibilitiesFromBlock(i, j, sudokuSquares);
					
					squaresBlank--;
					//TODO This should only return true if a possible number was removed
					foundNakedSingle = true;
				}
			}
		}
		
		return foundNakedSingle;

	}
		
	/**
	 * This method removes possibilities for all squares in the same row that do not already have a value
	 * @param columnNum  This is the row number of the square
	 * @param rowNum  This is the row number of the square
	 * @param sudokuSquares  This is a 2D array of squares
	 */
	public void removePossibilitiesFromRow(int columnNum, int rowNum, Square sudokuSquares[][]) {
		//For every square in the row
		for(int checkColumnNum = 0; checkColumnNum < SUDOKU_MAX_ROW_SQUARES; checkColumnNum++) {
			
			//If it doesn't have a value
			if(sudokuSquares[checkColumnNum][rowNum].getValue() == 0) {
				sudokuSquares[checkColumnNum][rowNum].removePossibleValue(sudokuSquares[columnNum][rowNum].getValue());
			}
		}
	}
	
	/**
	 * Removes possibilities for all squares in the same column that do not already have a value
	 * @param columnNum  This is the row number of the square
	 * @param rowNum  This is the row number of the square
	 * @param sudokuSquares  This is a 2D array of squares
	 */
	public void removePossibilitiesFromColumn(int columnNum, int rowNum, Square sudokuSquares[][]) {

		for(int checkRowNum = 0; checkRowNum < SUDOKU_MAX_COLUMN_SQUARES; checkRowNum++) {
		
			if(sudokuSquares[columnNum][checkRowNum].getValue() == 0) {
			
				sudokuSquares[columnNum][checkRowNum].removePossibleValue(sudokuSquares[columnNum][rowNum].getValue());
			
			}
		}
	}
	
	/**
	 * Removes possibilities for all squares in the same block that do not already have a value
	 * @param columnNum  This is the row number of the square
	 * @param rowNum  This is the row number of the square
	 * @param sudokuSquares  This is a 2D array of squares
	 */
	public void removePossibilitiesFromBlock(int columnNum, int rowNum, Square sudokuSquares[][]) {
		
		int blockNum = 0;
		blockNum = getBlockNum(columnNum, rowNum);
		ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
		
		//Remove possibilities from every square in block that doesn't have a value
		for(SquareCoordinate squareCoord : squareNumbersList) {
			
			//If square doesn't have a value
			if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0) {
				sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].removePossibleValue(sudokuSquares[columnNum][rowNum].getValue());
			}
		}
	}
	
	/**
	 * Go through every square and find if a square has only one possible value and set it
	 * @param sudokuSquares  This is a 2D array of squares
	 * @return boolean  This is if we found a new only choice square or not
	 */
	public boolean findOnlyChoice(Square sudokuSquares[][]) {
		
		boolean foundOnlyChoice = false;
		
		//Go through every square and if number is found, remove that possibility from squares in same row,
		//column, and 9 square section
		for(int columnNum = 0;columnNum < 9;columnNum++) {
		
			for(int rowNum = 0;rowNum < 9;rowNum++) {
			
				if(sudokuSquares[columnNum][rowNum].getValue() == 0) {
					ArrayList<Integer> possNumsList = sudokuSquares[columnNum][rowNum].getPossibleValues();
					for(Integer possNum : possNumsList) {
						
					
						if(getNumOnlyChoiceInRow(possNum, rowNum, sudokuSquares) == 1) {
							sudokuSquares[columnNum][rowNum].setValue(possNum);
							foundOnlyChoice = true;
							
							//Remove possibilities for the number we just set
							removePossibilitiesFromRow(columnNum, rowNum, sudokuSquares);
							removePossibilitiesFromColumn(columnNum, rowNum, sudokuSquares);
							removePossibilitiesFromBlock(columnNum, rowNum, sudokuSquares);
							squaresBlank--;
							System.out.println("OnlyChoiceCandidate(row):  Setting value " + possNum + " for [" + columnNum + "][" + rowNum + "]");
							break;
						}
						
						if(getNumOnlyChoiceInColumn(possNum, columnNum, sudokuSquares) == 1) {
							sudokuSquares[columnNum][rowNum].setValue(possNum);
							foundOnlyChoice = true;
							
							//Remove possibilities for the number we just set
							removePossibilitiesFromRow(columnNum, rowNum, sudokuSquares);
							removePossibilitiesFromColumn(columnNum, rowNum, sudokuSquares);
							removePossibilitiesFromBlock(columnNum, rowNum, sudokuSquares);
							squaresBlank--;
							System.out.println("OnlyChoiceCandidate(column):  Setting value " + possNum + " for [" + columnNum + "][" + rowNum + "]");
							break;
						}
						
						if(getNumOnlyChoiceInBlock(possNum, rowNum, columnNum, sudokuSquares) == 1) {
							sudokuSquares[columnNum][rowNum].setValue(possNum);
							foundOnlyChoice = true;
							
							//Remove possibilities for the number we just set
							removePossibilitiesFromRow(columnNum, rowNum, sudokuSquares);
							removePossibilitiesFromColumn(columnNum, rowNum, sudokuSquares);
							removePossibilitiesFromBlock(columnNum, rowNum, sudokuSquares);
							squaresBlank--;
							System.out.println("OnlyChoiceCandidate(block):  Setting value " + possNum + " for [" + columnNum + "][" + rowNum + "]");
							break;
						}
						
					}
				}
			}
		}
		
		return foundOnlyChoice;
				
	}
	
	/**
	 * Gets number of a specific possible number for all squares in the same row that do not already have a value
	 * @param possNum  This is the possible number we are checking for
	 * @param rowNum  This is the row number of the square
	 * @param sudokuSquares  This is a 2D array of squares
	 * @return int  This is the number of possibilities we find in the row
	 */
	public int getNumOnlyChoiceInRow(int possNum, int rowNum, Square sudokuSquares[][]) {
		
		int numPossibilities = 0;
		
		//For every square in the row
		for(int columnNum = 0; columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {
			
			//If it doesn't have a value
			if(sudokuSquares[columnNum][rowNum].getValue() == 0) {
				
				ArrayList<Integer> possNumList = sudokuSquares[columnNum][rowNum].getPossibleValues();
				
				//For every possible number in this square
				for(Integer possNumInSquare : possNumList) {
					
					//If possible number is same as what we are looking for
					if(possNum == possNumInSquare)
						numPossibilities++;
				
				}
			}
		}
			
		return numPossibilities;
	}
	
	/**
	 * Gets number of a specific possible number for all squares in the same column that do not already have a value
	 * @param possNum  This is the possible number we are checking for
	 * @param columnNum  This is the row number of the square
	 * @param sudokuSquares  This is a 2D array of squares
	 * @return int  This is the number of possibilities we find in the column
	 */
	public int getNumOnlyChoiceInColumn(int possNum, int columnNum, Square sudokuSquares[][]) {
		
		int numPossibilities = 0;
		
		//For every square in the column
		for(int rowNum = 0; rowNum < SUDOKU_MAX_COLUMN_SQUARES; rowNum++) {
			
			//If it doesn't have a value
			if(sudokuSquares[columnNum][rowNum].getValue() == 0) {
				
				ArrayList<Integer> possNumList = sudokuSquares[columnNum][rowNum].getPossibleValues();
				
				//For every possible number in this square
				for(Integer possNumInSquare : possNumList) {
					
					//If possible number is same as what we are looking for
					if(possNum == possNumInSquare)
						numPossibilities++;
				
				}
			}
		}

		return numPossibilities;
	}
	
	/**
	 * Gets number of a specific possible number for all squares in the same block that do not already have a value
	 * @param possNum  This is the possible number we are checking for
	 * @param rowNum  This is the row number of the square
	 * @param columnNum  This is the column number of the square
	 * @param sudokuSquares  This is a 2D array of squares
	 * @return int  This is the number of possibilities we find in the block
	 */
	public int getNumOnlyChoiceInBlock(int possNum, int rowNum, int columnNum, Square sudokuSquares[][]) {
		int numPossibilities = 0;
		int blockNum = 0;
		
		//Get what block number this square resides in
		blockNum = getBlockNum(columnNum, rowNum);
		
		ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
		
		//For ever square in the block
		for(SquareCoordinate squareCoord : squareNumbersList) {
			
			Square square = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum];
			
			//If square doesn't have a value
			if(square.getValue() == 0) {
				
				ArrayList<Integer> possNumList = square.getPossibleValues();
				
				//For every possible number in this square
				for(Integer possNumInSquare : possNumList) {
					
					//If possible number is the same as the one we are looking for
					if(possNum == possNumInSquare)
						numPossibilities++;
	
					}
			}
		}
		
		return numPossibilities;
	}
	
	/**
	 * This method finds hidden singles in a sudoku
	 * @param sudokuSquares  This is a 2D array of squares
	 * @return boolean This method returns true if any possible number is removed due to finding a hidden single
	 */
	public boolean findHiddenSingle(Square sudokuSquares[][]) {
		boolean foundHiddenSingleCandidate = false;
		
		//Go through each row and set hidden single candidates
		for(int i = 0;i < SUDOKU_MAX_ROW_SQUARES;i++) {
			//for every possible value check each row to see if there is only one square that has the value
			for(Integer possValue : allPossValues) {
				if(setHiddenSingleInRow(possValue, i, sudokuSquares))
					foundHiddenSingleCandidate = true;
				
			}
		}
		
		//Go through each column and set hidden candidates
		for(int i = 0;i < SUDOKU_MAX_COLUMN_SQUARES; i++) {
			//for every possible value check each column to see if there is only one square that has the value
			for(Integer possValue : allPossValues) {
				if(setHiddenSingleInColumn(possValue, i, sudokuSquares))
					foundHiddenSingleCandidate = true;
			}
		}
		
		//Go through each block and set hidden candidates
		for(int i = 0;i < SUDOKU_MAX_BLOCKS; i++) {
			//for every possible value check each block to see if there is only one square that has the value
			for(Integer possValue : allPossValues) {
				if(setHiddenSingleInBlock(possValue, i, sudokuSquares))
					foundHiddenSingleCandidate = true;
			}
		}
		
		return foundHiddenSingleCandidate;
	}
	
	/**
	 * Sets values for squares where a hidden singles is found
	 * @param possNum int This is the possible number to look for 
	 * @param rowNum This is the row number of the squares
	 * @param sudokuSquares This is a 2D array of squares
	 * @return boolean Returns true if a hidden single was found
	 */
	public boolean setHiddenSingleInRow(int possNum, int rowNum, Square sudokuSquares[][]) {

		int numPossibilities = 0;
		int tempX = 0;
		int tempY = 0;
		boolean foundHiddenSingle = false;
		
		//Search every square for hidden single
		for(int i = 0; i < SUDOKU_MAX_ROW_SQUARES; i++) {
			if(sudokuSquares[i][rowNum].getValue() == 0) {
				ArrayList<Integer> possNumList = sudokuSquares[i][rowNum].getPossibleValues();
				for(Integer possNumCheck : possNumList) {
					if(possNum == possNumCheck) {
						numPossibilities++;
						tempX = i;
						tempY = rowNum;
					}
				
				}
			}
			
		}
		
		if(numPossibilities == 1) {
			System.out.println("HiddenSingleCandidate(row): setting value " + possNum + " for square [" + tempX + "][" + tempY + "]");
			sudokuSquares[tempX][tempY].setValue(possNum);
			
			//Remove possibilities for the number we just set
			removePossibilitiesFromRow(tempX, tempY, sudokuSquares);
			removePossibilitiesFromColumn(tempX, tempY, sudokuSquares);
			removePossibilitiesFromBlock(tempX, tempY, sudokuSquares);
			
			squaresBlank--;
			foundHiddenSingle = true;
		}
	
			
		return foundHiddenSingle;
	}
	
	/**
	 * This sets any hidden singles for a possible number in a column
	 * @param possNum int This is the possible number to look for 
	 * @param columnNum This is the column number of the squares
	 * @param sudokuSquares This is a 2D array of squares
	 * @return boolean This returns true if a hidden single was found in a column
	 */
	public boolean setHiddenSingleInColumn(int possNum, int columnNum, Square sudokuSquares[][]) {
		int numPossibilities = 0;
		int tempX = 0;
		int tempY = 0;
		boolean foundHiddenSingle = false;
		
		for(int i = 0; i < SUDOKU_MAX_ROW_SQUARES; i++) {
			if(sudokuSquares[columnNum][i].getValue() == 0) {
				ArrayList<Integer> possNumList = sudokuSquares[columnNum][i].getPossibleValues();
				for(Integer possNumCheck : possNumList) {
					if(possNum == possNumCheck) {
						numPossibilities++;
						tempX = columnNum;
						tempY = i;
					}
				
				}
			}
			
		}
		
		if(numPossibilities == 1) {
			System.out.println("HiddenSingleCandidate(row): setting value " + possNum + " for square [" + tempX + "][" + tempY + "]");
			sudokuSquares[tempX][tempY].setValue(possNum);
			
			//Remove possibilities for the number we just set
			removePossibilitiesFromRow(tempX, tempY, sudokuSquares);
			removePossibilitiesFromColumn(tempX, tempY, sudokuSquares);
			removePossibilitiesFromBlock(tempX, tempY, sudokuSquares);
			
			squaresBlank--;
			foundHiddenSingle = true;
		}
	
			
		return foundHiddenSingle;
	}
	

	/**
	 * This sets any hidden singles for a possible number in a block
	 * @param possNum int This is the possible number to look for 
	 * @param blockNum This is the block number of the squares
	 * @param sudokuSquares This is a 2D array of squares
	 * @return boolean This returns true if a hidden single was found in a block
	 */
	public boolean setHiddenSingleInBlock(int possNum, int blockNum, Square sudokuSquares[][]) {
		int numPossibilities = 0;
		int tempX = 0;
		int tempY = 0;
		boolean foundHiddenSingle = false;
		
		ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
		
		for(SquareCoordinate squareCoord : squareNumbersList) {
			
			Square square = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum];
			if(square.getValue() == 0) {
				ArrayList<Integer> possNumList = square.getPossibleValues();
				
				for(Integer possNumCheck : possNumList) {
					if(possNum == possNumCheck) {
						numPossibilities++;
						tempX = squareCoord.columnNum;
						tempY = squareCoord.rowNum;
					}
					
				}
			}
		}

		
		if(numPossibilities == 1) {
			System.out.println("HiddenSingleCandidate(block): setting value " + possNum + " for square [" + tempX + "][" + tempY + "]");
			sudokuSquares[tempX][tempY].setValue(possNum);
			
			//Remove possibilities for the number we just set
			removePossibilitiesFromRow(tempX, tempY, sudokuSquares);
			removePossibilitiesFromColumn(tempX, tempY, sudokuSquares);
			removePossibilitiesFromBlock(tempX, tempY, sudokuSquares);
			
			squaresBlank--;
			foundHiddenSingle = true;
		}
	
			
		return foundHiddenSingle;
	}
	
	/**
	 * This finds all the naked pairs in a sudoku puzzle
	 * @param sudokuSquares Square This is a 2D array of squares
	 * @return boolean This method returns true if any possible number is removed due to finding a naked pair
	 */
	public boolean findNakedPairs(Square sudokuSquares[][]) {
		boolean foundNakedPair = false;
		
		//TODO: Should be using removePossibilitiesFromRow/Column/Block instead of rewriting the code
		//Search through every row
		for(int i = 0;i < SUDOKU_MAX_ROW_SQUARES;i++) {
			
			//Search through every column
			for(int j = 0;j < SUDOKU_MAX_COLUMN_SQUARES;j++) {
				
				
				ArrayList<Integer> square1PossVals = sudokuSquares[j][i].getPossibleValues();
				//If we find a square that has hidden pair
				if(square1PossVals.size() == 2 && sudokuSquares[j][i].getValue() == 0) {
					
					//Search every square in the row and it has the same hidden pair, remove possibilities from every other square
					for(int k = j;k < SUDOKU_MAX_ROW_SQUARES;k++) {
						
						//Make sure it's not the same square
						if(j != k) {
							
							ArrayList<Integer> square2PossVals = sudokuSquares[k][i].getPossibleValues();
							
							if(square2PossVals.size() == 2 && square1PossVals.equals(square2PossVals)) {
								//System.out.println("found naked pair at [" + k + "][" + i + "] and [" + j + "][" + i + "]");
								sudokuSquares[k][i].setIsNakedPair(true);
								sudokuSquares[j][i].setIsNakedPair(true);
								foundNakedPair = removeNakedPairValuesInRow(square1PossVals, i, sudokuSquares);
							}
						}
					}
					
					//Search every square in the column and it has the same hidden pair, remove possibilities from every other square
					for(int k = j;k < SUDOKU_MAX_COLUMN_SQUARES;k++) {
						
						//Make sure it's not the same square
						if(i != k) {
							
							ArrayList<Integer> square2PossVals = sudokuSquares[j][k].getPossibleValues();
							
							if(square2PossVals.size() == 2 && square1PossVals.equals(square2PossVals)) {
								sudokuSquares[j][k].setIsNakedPair(true);
								sudokuSquares[j][i].setIsNakedPair(true);
								foundNakedPair = removeNakedPairValuesInColumn(square1PossVals, j, sudokuSquares);

							}
						}
					}
					
					//Search every square in block and if it has the same hidden pair, remove possibilities from every other square
					int blockNum = 0;
					blockNum = getBlockNum(j, i);
					ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
					
					//Go through every square in the row and remove all hidden pair values from possible values from
					//the other squares
					for(SquareCoordinate squareCoord : squareNumbersList) {

						//Make sure it's not the same square
						if(!(squareCoord.columnNum == j && squareCoord.rowNum == i) && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0 ) {
						
							ArrayList<Integer> square2PossVals = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues();
							
							if(square2PossVals.size() == 2 && square1PossVals.equals(square2PossVals) && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0) {
								sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].setIsNakedPair(true);
								sudokuSquares[j][i].setIsNakedPair(true);
								foundNakedPair = removeNakedPairValuesInBlock(square1PossVals, squareCoord.rowNum, squareCoord.columnNum, sudokuSquares);

							}
						}
					}
				}	
			}
		}
			
		return foundNakedPair;
	}
	
	/**
	 * This method removes all naked pair values in a row except for naked pair squares
	 * @param nakedPairValues ArrayList<Integer> This are the naked pair values
	 * @param rowNum int This is the row number to search through
	 * @param sudokuSquares Square[][] This is a 2D array of squares
	 * @return boolean This returns true if a naked pair value was removed from a square in the row
	 */
	public boolean removeNakedPairValuesInRow(ArrayList<Integer> nakedPairValues, int rowNum, Square sudokuSquares[][]) {
		
		boolean foundNakedValue = false;
		
		//Go through every square in the row and remove all hidden pair values from possible values from
		//the other squares
		for(int i = 0;i < SUDOKU_MAX_ROW_SQUARES;i++) {

			//If it's not the hidden pair and value is 0 lets remove the hidden pairs from possible values
			if(!sudokuSquares[i][rowNum].getPossibleValues().equals(nakedPairValues) && sudokuSquares[i][rowNum].getValue() == 0) {
				
				for(Integer nakedPairValue : nakedPairValues) {
					if(sudokuSquares[i][rowNum].hasPossibleValue(nakedPairValue)) {
						sudokuSquares[i][rowNum].removePossibleValue(nakedPairValue);
						foundNakedValue = true;
					}
				}		
			}
		}
		
		return foundNakedValue;
	}
	
	/**
	 * This method removes all naked pair values in a row except for naked pair squares
	 * @param nakedPairValues ArrayList<Integer> This are the naked pair values
	 * @param columnNum int This is the column number to search through
	 * @param sudokuSquares Square[][] This is a 2D array of squares 
	 * @return boolean This returns true if a naked pair value was removed from a square in the column
	 */
	public boolean removeNakedPairValuesInColumn(ArrayList<Integer> nakedPairValues, int columnNum, Square sudokuSquares[][]) {
		
		boolean foundNakedValue = false;
		
		//Go through every square in the column and remove all hidden pair values from possible values from
		//the other squares
		for(int i = 0;i < SUDOKU_MAX_COLUMN_SQUARES;i++) {
			
			//If it's not the hidden pair and value is 0 lets remove the hidden pairs from possible values
			if(!sudokuSquares[columnNum][i].getPossibleValues().equals(nakedPairValues) && sudokuSquares[columnNum][i].getValue() == 0) {

				for(Integer nakedPairValue : nakedPairValues) {
					if(sudokuSquares[columnNum][i].hasPossibleValue(nakedPairValue)) {
						sudokuSquares[columnNum][i].removePossibleValue(nakedPairValue);
						foundNakedValue = true;
					}
				}
			}
		}
		
		return foundNakedValue;
	}
	
	/**
	 * This method removes all naked pair values in a row except for naked pair squares
	 * @param nakedPairValues ArrayList<Integer>  This are the naked pair values
	 * @param rowNum int  This is the block number to search through
	 * @param columnNum int  This is the column number to search through
	 * @param sudokuSquares Square[][]  This is a 2D array of squares 
	 * @return boolean  This returns true if a naked pair value was removed from a square in the block
	 */
	public boolean removeNakedPairValuesInBlock(ArrayList<Integer> nakedPairValues, int rowNum, int columnNum, Square sudokuSquares[][]) {
		boolean foundNakedValue = false;
		int blockNum = 0;
		blockNum = getBlockNum(columnNum, rowNum);
		
		ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
		
		//Go through every square in the row and remove all hidden pair values from possible values from
		//the other squares
		for(SquareCoordinate squareCoord : squareNumbersList) {

			//If it's not the hidden pair and value is 0 lets remove the hidden pairs from possible values
			if(!sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().equals(nakedPairValues) && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0) {
				
				for(Integer nakedPairValue : nakedPairValues) {
					if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].hasPossibleValue(nakedPairValue)) {
						sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].removePossibleValue(nakedPairValue);
						foundNakedValue = true;
						//System.out.println("Remove(block) " + hiddenPairValue + " from [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "]" );
					}
				}				
			}
		}
		return foundNakedValue;
	}
	
	/**
	 * This finds all the naked triples in a sudoku puzzle
	 * @param sudokuSquares Square This is a 2D array of squares
	 * @return boolean This method returns true if any possible number is removed due to finding a naked triple
	 */
	public boolean findNakedTriples(Square sudokuSquares[][]) {
		boolean foundNakedTriple = false;
		final int SUDOKU_MAX_ROWS_COLUMNS_BLOCKS = 9;
		
		for(int num = 0;num < SUDOKU_MAX_ROWS_COLUMNS_BLOCKS;num++) {

			if(findNakedTriplesInRow(num, sudokuSquares))
				foundNakedTriple = true;

			if(findNakedTriplesInColumn(num, sudokuSquares))
				foundNakedTriple = true;

			if(findNakedTriplesInBlock(num, sudokuSquares))
				foundNakedTriple = true;

		}
		
				
		return foundNakedTriple;
	}
	
	/**
	 * Finds naked triples in a row and removes possibilities from other squares from the naked triples
	 * @param rowNum
	 * @param sudokuSquares Square[][] This is a 2D array of squares
	 * @return boolean This returns true if a possible number was removed from a naked triple in a row
	 */
	public boolean findNakedTriplesInRow(int rowNum, Square sudokuSquares[][]) {
		boolean foundNakedTriple = false;
		Map<Integer,Integer> allPossNumsMap = new HashMap<Integer,Integer>();
		ArrayList<Integer[]> possibleNumCombinations = new ArrayList<Integer[]>();
		ArrayList<Integer> allPossNumsList = new ArrayList<Integer>();
		Integer[] nakedTripleNums = new Integer[3];
		
		//if there are not more than 2 naked doubles in the row then there are no naked triples
		if(getNumNakedPairsInRow(rowNum, sudokuSquares) < 3)
			return false;
		
		//get all square coordinates that have naked doubles that aren't naked pairs
		for(int columnNum = 0; columnNum < SUDOKU_MAX_COLUMN_SQUARES; columnNum++) {
			
			if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2 &&
					sudokuSquares[columnNum][rowNum].getValue() == 0) {
				Integer possNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
				Integer possNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
				
				if(!allPossNumsMap.containsKey(possNum1))
					allPossNumsMap.put(possNum1, 1);
				
				if(!allPossNumsMap.containsKey(possNum2))
					allPossNumsMap.put(possNum2, 1);
				
			}
		}
		
		for(Integer possNumKey : allPossNumsMap.keySet()) {
			allPossNumsList.add(possNumKey);
		}
		
		if(allPossNumsList.size() < 3)
			return false;
		else if(allPossNumsList.size() > 3) {
			possibleNumCombinations = getPossNumCombinations(allPossNumsList);
			
			//For every possible num combination
			for(Integer[] possNumCombination : possibleNumCombinations) {
				int squareHasBothPossNums = 0;
				
				//Go through every square in the row
				for(int columnNum = 0; columnNum < SUDOKU_MAX_COLUMN_SQUARES; columnNum++) {
					if(sudokuSquares[columnNum][rowNum].getValue() == 0 && sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2) {
						Integer possNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
						Integer possNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
						boolean possNum1Found = false;
						boolean possNum2Found = false;
						
						//Go through and see if both of square's possible numbers are in naked triple combination
						for(int possNumIndex = 0;possNumIndex < possNumCombination.length;possNumIndex++) {
							if(possNum1 == possNumCombination[possNumIndex])
								possNum1Found = true;
							else if(possNum2 == possNumCombination[possNumIndex])
								possNum2Found = true;
						}
						
						//If square is possible naked triple
						if(possNum1Found && possNum2Found) {
							squareHasBothPossNums++;
						}
					}
				}
				
				if(squareHasBothPossNums > 3)
					System.out.println("ERROR: nakedTriple(row) has more than 3 matches");
				else if(squareHasBothPossNums == 3) {
					nakedTripleNums = possNumCombination;
				}
			}
		}
		else {
			//we have our 3 naked triple nums
			for(int nakedTripNumIndex = 0;nakedTripNumIndex < allPossNumsList.size();nakedTripNumIndex++)
				nakedTripleNums[nakedTripNumIndex] = allPossNumsList.get(nakedTripNumIndex);
		}
		
		//for each square if both possible numbers don't exist in naked triple numbers then remove 
		//naked triple numbers from that squares possible nums
		if(nakedTripleNums[0] != null && nakedTripleNums[1] != null && nakedTripleNums[2] != null ) {
			for(int columnNum = 0; columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {
				//If square doens't have a value
				if(sudokuSquares[columnNum][rowNum].getValue() == 0 && sudokuSquares[columnNum][rowNum].getPossibleValues().size() > 1) {
					Integer possNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
					Integer possNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
					boolean foundPossNum1 = false;
					boolean foundPossNum2 = false;
					
					if(possNum1 == nakedTripleNums[0] || possNum1 == nakedTripleNums[1] || possNum1 == nakedTripleNums[2])
						foundPossNum1 = true;
					
					if(possNum2 == nakedTripleNums[0] || possNum2 == nakedTripleNums[1] || possNum2 == nakedTripleNums[2])
						foundPossNum2 = true;
					
					//If square isn't a naked triple remove all naked triple numbers from it's possible numbers
					if(!foundPossNum1 || !foundPossNum2) {
						System.out.println("Removing naked triple possibilities(row): " + nakedTripleNums[0] + nakedTripleNums[1] + nakedTripleNums[2]);
						sudokuSquares[columnNum][rowNum].removePossibleValue(nakedTripleNums[0]);
						sudokuSquares[columnNum][rowNum].removePossibleValue(nakedTripleNums[1]);
						sudokuSquares[columnNum][rowNum].removePossibleValue(nakedTripleNums[2]);
						foundNakedTriple = true;
					}
				}
			}
		}
			
		return foundNakedTriple;
	}
	
	/**
	 * This finds naked triples in a column and removes possibilities from other squares from the naked triples
	 * @param columnNum  The column number of the squares to search through
	 * @param sudokuSquares Square[][]  This is a 2d array of squares
	 * @return boolean  This returns true if we remove a possible number from a naked triple we find
	 */
	public boolean findNakedTriplesInColumn(int columnNum, Square sudokuSquares[][]) {
		boolean foundNakedTriple = false;
		Map<Integer,Integer> allPossNumsMap = new HashMap<Integer,Integer>();
		ArrayList<Integer[]> possibleNumCombinations = new ArrayList<Integer[]>();
		ArrayList<Integer> allPossNumsList = new ArrayList<Integer>();
		Integer[] nakedTripleNums = new Integer[3];
		
		//if there are not more than 2 naked doubles in the row then there are no naked triples
		if(getNumNakedPairsInColumn(columnNum, sudokuSquares) < 3)
			return false;
		
		//get all square coordinates that have naked doubles that aren't naked pairs
		for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
			
			if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2 &&
					sudokuSquares[columnNum][rowNum].getValue() == 0) {
				Integer possNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
				Integer possNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
				
				if(!allPossNumsMap.containsKey(possNum1))
					allPossNumsMap.put(possNum1, 1);
				
				if(!allPossNumsMap.containsKey(possNum2))
					allPossNumsMap.put(possNum2, 1);
				
			}
		}
		
		for(Integer possNumKey : allPossNumsMap.keySet()) {
			allPossNumsList.add(possNumKey);
		}
		
		if(allPossNumsList.size() < 3)
			return false;
		else if(allPossNumsList.size() > 3) {
			possibleNumCombinations = getPossNumCombinations(allPossNumsList);
			
			//for each possible combination
			  //for each square in the column
			    //if we find 3 squares that both possible numbers are in the combination then we found our naked triple
			
			//For every possible num combination
			for(Integer[] possNumCombination : possibleNumCombinations) {
				int squareHasBothPossNums = 0;
				
				//Go through every square in the column
				for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
					//System.out.println("possNumCombination[" + rowNum + "][" + columnNum + "] : " + possNumCombination[0] + possNumCombination[1] + possNumCombination[2]);
					if(sudokuSquares[columnNum][rowNum].getValue() == 0 && sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2) {
						Integer possNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
						Integer possNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
						boolean possNum1Found = false;
						boolean possNum2Found = false;
						
						//Go through and see if both of square's possible numbers are in naked triple combination
						for(int possNumIndex = 0;possNumIndex < possNumCombination.length;possNumIndex++) {
							if(possNum1 == possNumCombination[possNumIndex])
								possNum1Found = true;
							else if(possNum2 == possNumCombination[possNumIndex])
								possNum2Found = true;
						}
						
						//If square is possible naked triple for this combination
						if(possNum1Found && possNum2Found) {
							squareHasBothPossNums++;
						}
					}
				}
				
				if(squareHasBothPossNums > 3)
					System.out.println("ERROR: nakedTriple has more than 3 matches for combination " + possNumCombination);
				else if(squareHasBothPossNums == 3) {
					nakedTripleNums = possNumCombination;
				}
			}
		}
		else {
			//we have our 3 naked triple nums
			for(int nakedTripNumIndex = 0;nakedTripNumIndex < allPossNumsList.size();nakedTripNumIndex++)
				nakedTripleNums[nakedTripNumIndex] = allPossNumsList.get(nakedTripNumIndex);
		}
		
		//for each square if both possible numbers don't exist in naked triple numbers then remove 
		//naked triple numbers from that squares possible nums
		if(nakedTripleNums[0] != null && nakedTripleNums[1] != null && nakedTripleNums[2] != null ) {
			for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
				//If square doens't have a value
				if(sudokuSquares[columnNum][rowNum].getValue() == 0 && sudokuSquares[columnNum][rowNum].getPossibleValues().size() > 1) {
					Integer possNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
					Integer possNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
					boolean foundPossNum1 = false;
					boolean foundPossNum2 = false;
					
					if(possNum1 == nakedTripleNums[0] || possNum1 == nakedTripleNums[1] || possNum1 == nakedTripleNums[2])
						foundPossNum1 = true;
					
					if(possNum2 == nakedTripleNums[0] || possNum2 == nakedTripleNums[1] || possNum2 == nakedTripleNums[2])
						foundPossNum2 = true;
					
					//If square isn't a naked triple remove all naked triple numbers from it's possible numbers
					if(!foundPossNum1 || !foundPossNum2) {

						System.out.println("Removing naked triple possibilities(column): " + nakedTripleNums[0] + nakedTripleNums[1] + nakedTripleNums[2]);
						sudokuSquares[columnNum][rowNum].removePossibleValue(nakedTripleNums[0]);
						sudokuSquares[columnNum][rowNum].removePossibleValue(nakedTripleNums[1]);
						sudokuSquares[columnNum][rowNum].removePossibleValue(nakedTripleNums[2]);
						foundNakedTriple = true;
					}
				}
			}
		}
			
		return foundNakedTriple;
	}
	
	/**
	 * This finds naked triples in a block and removes possibilities from other squares from the naked triples
	 * @param blockNum  The block number of the squares to search through
	 * @param sudokuSquares Square[][]  This is a 2d array of squares
	 * @return boolean  This returns true if we remove a possible number from a naked triple we find
	 */
	public boolean findNakedTriplesInBlock(int blockNum, Square sudokuSquares[][]) {
		
		boolean foundNakedTriple = false;
		Map<Integer,Integer> allPossNumsMap = new HashMap<Integer,Integer>();
		ArrayList<Integer[]> possibleNumCombinations = new ArrayList<Integer[]>();
		ArrayList<Integer> allPossNumsList = new ArrayList<Integer>();
		ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
		Integer[] nakedTripleNums = new Integer[3];
		
		//if there are not more than 2 naked doubles in the block then there are no naked triples
		if(getNumNakedPairsInBlock(blockNum, sudokuSquares) < 3)
			return false;
		
		//get all square coordinates that have naked doubles that aren't naked pairs
		for(SquareCoordinate squareCoord : squareNumbersList) {
			
			if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().size() == 2 &&
					sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0) {
				Integer possNum1 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0);
				Integer possNum2 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1);
				
				if(!allPossNumsMap.containsKey(possNum1))
					allPossNumsMap.put(possNum1, 1);
				
				if(!allPossNumsMap.containsKey(possNum2))
					allPossNumsMap.put(possNum2, 1);
				
			}
		}
		
		for(Integer possNumKey : allPossNumsMap.keySet()) {
			allPossNumsList.add(possNumKey);
		}
		
		if(allPossNumsList.size() < 3)
			return false;
		else if(allPossNumsList.size() > 3) {
			possibleNumCombinations = getPossNumCombinations(allPossNumsList);
			
			//for each possible combination
			  //for each square in the column
			    //if we find 3 squares that both possible numbers are in the combination then we found our naked triple
			
			//For every possible num combination
			for(Integer[] possNumCombination : possibleNumCombinations) {
				int squareHasBothPossNums = 0;
				
				//Go through every square in the column
				for(SquareCoordinate squareCoord : squareNumbersList) {
					//System.out.println("possNumCombination(block)[" + squareCoord.rowNum + "][" + squareCoord.columnNum + "] : " + possNumCombination[0] + possNumCombination[1] + possNumCombination[2]);
					if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0 && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().size() == 2) {
						Integer possNum1 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0);
						Integer possNum2 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1);
						boolean possNum1Found = false;
						boolean possNum2Found = false;
						
						//Go through and see if both of square's possible numbers are in naked triple combination
						for(int possNumIndex = 0;possNumIndex < possNumCombination.length;possNumIndex++) {
							if(possNum1 == possNumCombination[possNumIndex])
								possNum1Found = true;
							else if(possNum2 == possNumCombination[possNumIndex])
								possNum2Found = true;
						}
						
						//If square is possible naked triple
						if(possNum1Found && possNum2Found) {
							squareHasBothPossNums++;
						}
					}
				}
				
				if(squareHasBothPossNums > 3)
					System.out.println("ERROR: nakedTriple(block) has more than 3 matches");
				else if(squareHasBothPossNums == 3) {
					nakedTripleNums = possNumCombination;
				}
			}
		}
		else {
			//we have our 3 naked triple nums
			for(int nakedTripNumIndex = 0;nakedTripNumIndex < allPossNumsList.size();nakedTripNumIndex++)
				nakedTripleNums[nakedTripNumIndex] = allPossNumsList.get(nakedTripNumIndex);
		}
		
		//for each square if both possible numbers don't exist in naked triple numbers then remove 
		//naked triple numbers from that squares possible nums
		if(nakedTripleNums[0] != null && nakedTripleNums[1] != null && nakedTripleNums[2] != null ) {
			for(SquareCoordinate squareCoord : squareNumbersList) {
				//If square doens't have a value
				if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0 && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().size() > 1) {
					Integer possNum1 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0);
					Integer possNum2 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1);
					boolean foundPossNum1 = false;
					boolean foundPossNum2 = false;
					
					if(possNum1 == nakedTripleNums[0] || possNum1 == nakedTripleNums[1] || possNum1 == nakedTripleNums[2])
						foundPossNum1 = true;
					
					if(possNum2 == nakedTripleNums[0] || possNum2 == nakedTripleNums[1] || possNum2 == nakedTripleNums[2])
						foundPossNum2 = true;
					
					//If square isn't a naked triple remove all naked triple numbers from it's possible numbers
					if(!foundPossNum1 || !foundPossNum2) {
						System.out.println("Removing naked triple possible numbers(block): " + nakedTripleNums[0] + nakedTripleNums[1] + nakedTripleNums[2]);
						sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].removePossibleValue(nakedTripleNums[0]);
						sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].removePossibleValue(nakedTripleNums[1]);
						sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].removePossibleValue(nakedTripleNums[2]);
						foundNakedTriple = true;
					}
				}
			}
		}
			
		return foundNakedTriple;
	}
	
	/**
	 * Finds y-wing patterns and removes possible numbers
	 * @param sudokuSquares Square[][]  This is a 2d array of squares
	 * @return boolean  This method returns true if any possible number is removed due to finding a YWing
	 */
	public boolean findYWingRowColumn(Square sudokuSquares[][]) {
		//Go through each square
		  //If square is naked double
		    //Set this square as the pivot
		    //Search remaining squares in row
		      //If square is naked double and only has one of the same possibilities
		        //Search all squares in the column of the first square and this square
		          //If it has the remaining combination 
		            //Remove the possibility that is common to the squares that are not the pivot
		    //Search remaining squares in the block
		      //If square is naked double and only has one of the same possibilities
		        //Search all squares in the column of the first square
					//If it has the remaining combination
		                //Remove the possibility that is common to the squares that are not the pivot
		        //Search all squares in the column of the second square
					//If it has the remaining combination
		                //Remove the possibility that is common to the squares that are not the pivot
		        //Search all the squares in the row of the first square
		        	//If it has the remaining combination
		                //Remove the possibility that is common to the squares that are not the pivot
		        //Search all the squares in the row of the second square for the remaining combination
					//If it has the remaining combination
						//Remove the possibility that is common to the squares that are not the pivot
		
		boolean foundYWing = false;
		
		//Go through each square
		for(int rowNum = 0;rowNum < SUDOKU_MAX_COLUMN_SQUARES; rowNum++) {
			for(int columnNum = 0;columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {

				//If square is naked double and doesn't have a value
				if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][rowNum].getValue() == 0) {
					
					Map<Integer, Integer> possNumsMap = new HashMap<Integer,Integer>();
					Integer pivotPossNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
					Integer pivotPossNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
					
					possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(0), 1);
					possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(1), 1);
					
					//Search remaining squares in row for a square that has one identical possible value
					for(int secondRemSquareColumnNum = 0; secondRemSquareColumnNum < SUDOKU_MAX_ROW_SQUARES; secondRemSquareColumnNum++) {
						
						//If square we are checking doesn't have a value, has at least 2 possible values, has at least one common possible value, and isn't the same square
						if(sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[secondRemSquareColumnNum][rowNum].getValue() == 0 &&
								(pivotPossNum1 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(0) || pivotPossNum1 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(1) ||
								 pivotPossNum2 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(0) || pivotPossNum2 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(1)) &&
								secondRemSquareColumnNum != columnNum) {
							
							ArrayList<Integer> nextPossibleNums = new ArrayList<Integer>();
							Integer keyToRemove = 0;
							Integer secondRemPossNum1 = sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(0);
							Integer secondRemPossNum2 = sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(1);
							
							//Add new possible numbers to map and find the one that already exists so we can remove it
							if(possNumsMap.containsKey(secondRemPossNum1))
								keyToRemove = secondRemPossNum1;
							else
								possNumsMap.put(secondRemPossNum1, 1);
							
							if(possNumsMap.containsKey(secondRemPossNum2))
								keyToRemove = secondRemPossNum2;
							else
								possNumsMap.put(secondRemPossNum2, 1);
							
							//Remove common possible number so we can find the remaining 3rd square possible numbers
							if(keyToRemove == 0)
								System.out.println("ERROR findYWingColumnNum: keyToRemove = 0.  This should equal 1-9");
							else
								possNumsMap.remove(keyToRemove);
					
							//Add all possible numbers to ArrayList
							for(Integer possNumKey : possNumsMap.keySet()) 
								nextPossibleNums.add(possNumKey);
							
							//If we found the same possible numbers break
							if(nextPossibleNums.size() < 2)
								break;
							
							//Sort the possible numbers from least to greatest
							if(nextPossibleNums.get(1) < nextPossibleNums.get(0)) {
								Integer tempNum = nextPossibleNums.get(1);
								nextPossibleNums.remove(1);
								nextPossibleNums.add(0, tempNum);
							}
																					
							//Search all squares in the column of the first square and this square
							for(int thirdRemSquareRowNum = 0; thirdRemSquareRowNum < SUDOKU_MAX_COLUMN_SQUARES; thirdRemSquareRowNum++) {
								if(sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 && sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2) {
									Integer thirdPossNum1 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(0);
									Integer thirdPossNum2 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(1);
									
									Integer commonPossNum = 0;
	
									
									//Set a variable to the common number we found and add uncommon numbers to a list to find the third square
									if(thirdPossNum1 == secondRemPossNum1) {
										commonPossNum = thirdPossNum1;
									}
									else if(thirdPossNum1 == secondRemPossNum2){
										commonPossNum = thirdPossNum1;
									}
									else if(thirdPossNum2 == secondRemPossNum1) {
										commonPossNum = thirdPossNum2;
									}
									else if(thirdPossNum2 == secondRemPossNum2) {
										commonPossNum = thirdPossNum2;
									}
									
									//If it has the remaining combination
									if(sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 &&
											thirdPossNum1 == nextPossibleNums.get(0) && thirdPossNum2 == nextPossibleNums.get(1) &&
											thirdRemSquareRowNum != rowNum) {
										if(sudokuSquares[secondRemSquareColumnNum][thirdRemSquareRowNum].hasPossibleValue(commonPossNum)) {
											sudokuSquares[secondRemSquareColumnNum][thirdRemSquareRowNum].removePossibleValue(commonPossNum);
											foundYWing = true;
											System.out.println("Found Y Wing at [" + columnNum + "][" + rowNum + "] [" + secondRemSquareColumnNum + "][" + rowNum + "] [" + columnNum + "][" + thirdRemSquareRowNum + "]" + 
													"removed value: " + commonPossNum + " from [" + secondRemSquareColumnNum + "][" + thirdRemSquareRowNum + "]");
										}
										
									
			            				//Remove the possibility that is common to the squares that are not the pivot
									}
								}
							}	
						}	
					}
				}
			}
		}
		return foundYWing;
	}
		
	
	/**
	 * This finds all the YWings in a sudoku puzzle with 2 of the numbers being in the same block
	 * @param sudokuSquares Square This is a 2D array of squares
	 * @return boolean This method returns true if any possible number is removed due to finding a naked triple
	 */
	public boolean findYWingBlock(Square sudokuSquares[][]) {
		//Go through each square
		  //If square is naked double
		    //Set this square as the pivot
		    //Search remaining squares in row
		      //If square is naked double and only has one of the same possibilities
		        //Search all squares in the column of the first square and this square
		          //If it has the remaining combination 
		            //Remove the possibility that is common to the squares that are not the pivot
		    //Search remaining squares in the block
		      //If square is naked double and only has one of the same possibilities
		        //Search all squares in the column of the first square
					//If it has the remaining combination
		                //Remove the possibility that is common to the squares that are not the pivot
		        //Search all squares in the column of the second square
					//If it has the remaining combination
		                //Remove the possibility that is common to the squares that are not the pivot
		        //Search all the squares in the row of the first square
		        	//If it has the remaining combination
		                //Remove the possibility that is common to the squares that are not the pivot
		        //Search all the squares in the row of the second square for the remaining combination
					//If it has the remaining combination
						//Remove the possibility that is common to the squares that are not the pivot
		
		boolean foundYWing = false;
		
		//Go through each square
		for(int rowNum = 0;rowNum < SUDOKU_MAX_COLUMN_SQUARES; rowNum++) {
			for(int columnNum = 0;columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {

				//If square is naked double and doesn't have a value
				if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][rowNum].getValue() == 0) {
					
					int blockNum = 0;
					Map<Integer, Integer> possNumsMap = new HashMap<Integer,Integer>();
					Integer pivotPossNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
					Integer pivotPossNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);
					
					possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(0), 1);
					possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(1), 1);
					
					blockNum = getBlockNum(columnNum, rowNum);
					
					ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
					
					//Search remaining squares in block for a square that has one identical possible value
					for(SquareCoordinate squareCoord : squareNumbersList) {
						
						//If square we are checking doesn't have a value, has at least 2 possible values, has at least one common possible value, and isn't the same square
						if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().size() == 2 && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0 &&
								(pivotPossNum1 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0) || pivotPossNum1 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1) ||
								 pivotPossNum2 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0) || pivotPossNum2 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1)) &&
								(squareCoord.rowNum != rowNum && squareCoord.columnNum != columnNum)) {
							
							ArrayList<Integer> nextPossibleNums = new ArrayList<Integer>();
							Integer keyToRemove = 0;
							Integer secondRemPossNum1 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0);
							Integer secondRemPossNum2 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1);
							
							//Add new possible numbers to map and find the one that already exists so we can remove it
							if(possNumsMap.containsKey(secondRemPossNum1))
								keyToRemove = secondRemPossNum1;
							else
								possNumsMap.put(secondRemPossNum1, 1);
							
							if(possNumsMap.containsKey(secondRemPossNum2))
								keyToRemove = secondRemPossNum2;
							else
								possNumsMap.put(secondRemPossNum2, 1);
							
							//Remove common possible number so we can find the remaining 3rd square possible numbers
							if(keyToRemove == 0)
								System.out.println("ERROR findYWingBlock: keyToRemove = 0.  This should equal 1-9");
							else
								possNumsMap.remove(keyToRemove);
					
							//Add all possible numbers to ArrayList
							for(Integer possNumKey : possNumsMap.keySet()) 
								nextPossibleNums.add(possNumKey);
							
							//If we found the same possible numbers break
							if(nextPossibleNums.size() < 2)
								break;
							
							//Sort the possible numbers from least to greatest
							if(nextPossibleNums.get(1) < nextPossibleNums.get(0)) {
								Integer tempNum = nextPossibleNums.get(1);
								nextPossibleNums.remove(1);
								nextPossibleNums.add(0, tempNum);
							}
																					
							//Search all squares in the column of the first square and this square
							for(int thirdRemSquareRowNum = 0; thirdRemSquareRowNum < SUDOKU_MAX_COLUMN_SQUARES; thirdRemSquareRowNum++) {
								if(sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 && sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2) {
									Integer thirdPossNum1 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(0);
									Integer thirdPossNum2 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(1);
									
									Integer commonPossNum = 0;
	
									
									//Set a variable to the common number we found and add uncommon numbers to a list to find the third square
									if(thirdPossNum1 == secondRemPossNum1) {
										commonPossNum = thirdPossNum1;
									}
									else if(thirdPossNum1 == secondRemPossNum2){
										commonPossNum = thirdPossNum1;
									}
									else if(thirdPossNum2 == secondRemPossNum1) {
										commonPossNum = thirdPossNum2;
									}
									else if(thirdPossNum2 == secondRemPossNum2) {
										commonPossNum = thirdPossNum2;
									}
									
									//If it has the remaining combination
									if(sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 &&
											thirdPossNum1 == nextPossibleNums.get(0) && thirdPossNum2 == nextPossibleNums.get(1) &&
											thirdRemSquareRowNum != rowNum) {
										
										int lastBlockNum = getBlockNum(columnNum, thirdRemSquareRowNum);
										
										ArrayList<SquareCoordinate> lastSquareNumbersList = blockNumSquareNumbers.get(lastBlockNum);
										
										for(SquareCoordinate lastSquareCoord : lastSquareNumbersList) {
											if(squareCoord.columnNum == lastSquareCoord.columnNum) {
												if(sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].hasPossibleValue(commonPossNum) &&
														sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].getValue() == 0) {
													sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].removePossibleValue(commonPossNum);
													foundYWing = true;
													System.out.println("Found Y Wing (1) at [" + columnNum + "][" + rowNum + "] [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "] [" + columnNum + "][" + thirdRemSquareRowNum + "]" + 
															"removed value: " + commonPossNum + " from [" + lastSquareCoord.columnNum + "][" + lastSquareCoord.rowNum + "]");
												}
												
											
					            				//Remove the possibility that is common to the squares that are not the pivot
											}
										}
									}
								}
							}	
							
							//Search all squares in the row of the first square and this square
							for(int thirdRemSquareColumnNum = 0; thirdRemSquareColumnNum < SUDOKU_MAX_COLUMN_SQUARES; thirdRemSquareColumnNum++) {
								if(sudokuSquares[thirdRemSquareColumnNum][rowNum].getValue() == 0 && sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().size() == 2) {
									Integer thirdPossNum1 = sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().get(0);
									Integer thirdPossNum2 = sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().get(1);
									
									Integer commonPossNum = 0;
	
									
									//Set a variable to the common number we found and add uncommon numbers to a list to find the third square
									if(thirdPossNum1 == secondRemPossNum1) {
										commonPossNum = thirdPossNum1;
									}
									else if(thirdPossNum1 == secondRemPossNum2){
										commonPossNum = thirdPossNum1;
									}
									else if(thirdPossNum2 == secondRemPossNum1) {
										commonPossNum = thirdPossNum2;
									}
									else if(thirdPossNum2 == secondRemPossNum2) {
										commonPossNum = thirdPossNum2;
									}
									
									//If it has the remaining combination
									if(sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][thirdRemSquareColumnNum].getValue() == 0 &&
											thirdPossNum1 == nextPossibleNums.get(0) && thirdPossNum2 == nextPossibleNums.get(1) &&
											thirdRemSquareColumnNum != rowNum) {
										if(sudokuSquares[thirdRemSquareColumnNum][squareCoord.rowNum].hasPossibleValue(commonPossNum)) {
											sudokuSquares[thirdRemSquareColumnNum][squareCoord.rowNum].removePossibleValue(commonPossNum);
											int lastBlockNum = getBlockNum(thirdRemSquareColumnNum, rowNum);
											
											ArrayList<SquareCoordinate> lastSquareNumbersList = blockNumSquareNumbers.get(lastBlockNum);
											
											for(SquareCoordinate lastSquareCoord : lastSquareNumbersList) {
												if(squareCoord.rowNum == lastSquareCoord.rowNum) {
													if(sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].hasPossibleValue(commonPossNum) &&
															sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].getValue() == 0) {
														sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].removePossibleValue(commonPossNum);
														foundYWing = true;
														System.out.println("Found Y Wing (1) at [" + columnNum + "][" + rowNum + "] [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "] [" + columnNum + "][" + thirdRemSquareColumnNum + "]" + 
																"removed value: " + commonPossNum + " from [" + lastSquareCoord.columnNum + "][" + lastSquareCoord.rowNum + "]");
													}
													
												
						            				//Remove the possibility that is common to the squares that are not the pivot
												}
											}
										}
									}
								}
							}	
							//TODO Search all Column squares
						}	
					}
				}
			}
		}
		return foundYWing;

	}
	

	public boolean findXWing(Square sudokuSquares[][]) {
		// Go through each square (x,y)
		  //If square has only 2 possible numbers and value is 0
		    //Search each square left in row
		      //If square2 has same 2 possible numbers
		        //Search remaining squares in column of square1
		          //If square3 has 2 possible numbers and one of them are the same as square1
		            //Search remaining squares in row
		              //If square4 has value of 0 and same possible numbers as square3
		                //Remove all possible numbers that match common number in column of square1 that is not square1 or square3
						//Remove all possible numbers that match common number in column of square2 that is not square2 or square4
						//Remove all possible numbers that match common number in row of square1 that is not square1 or square2
		                //Remove all possible numbers that match common number in row of square3 that is not square3 or square4
			//Search each square left in column
		      //If square2 has same 2 possible numbers
		        //Search remaining squares in row of square1
		          //If square3 has 2 possible numbers and one of them are the same as square1
		            //Search remaining squares in column
		              //If square4 has value of 0 and same possible numbers as square3
		                //Remove all possible numbers that match common number in column of square1 that is not square1 or square3
						//Remove all possible numbers that match common number in column of square2 that is not square2 or square4
						//Remove all possible numbers that match common number in row of square1 that is not square1 or square2
		                //Remove all possible numbers that match common number in row of square3 that is not square3 or square4
		
		//Go through each square
		for(int square1RowNum = 0;square1RowNum < SUDOKU_MAX_COLUMN_SQUARES; square1RowNum++) {
			for(int square1ColumnNum = 0;square1ColumnNum < SUDOKU_MAX_ROW_SQUARES; square1ColumnNum++) {
				//If square has only 2 possible numbers and value is 0
				if(squareHasTwoPossNums(sudokuSquares[square1ColumnNum][square1RowNum])) {
					Square square1 = sudokuSquares[square1ColumnNum][square1RowNum];
					Integer square1PossNum1 = square1.getPossibleValues().get(0);
				    Integer square1PossNum2 = square1.getPossibleValues().get(1);
					//Search each square left in row
					for(int square2ColumnNum = square1ColumnNum; square2ColumnNum < SUDOKU_MAX_ROW_SQUARES; square2ColumnNum++) {
					    Square square2 = sudokuSquares[square2ColumnNum][square1RowNum];
					    Integer square2PossNum1 = square2.getPossibleValues().get(0);
					    Integer square2PossNum2 = square2.getPossibleValues().get(1);
						
					    
						//If square2 has same 2 possible numbers as square 1
						if(squareHasTwoPossNums(sudokuSquares[square2ColumnNum][square1RowNum]) && square1.getPossibleValues().equals(square2.getPossibleValues())) {
					    	
							//Search remaining squares in column of square1
							for(int square3RowNum = square1RowNum; square3RowNum < SUDOKU_MAX_COLUMN_SQUARES; square3RowNum++) {
								
								Square square3 = sudokuSquares[square1ColumnNum][square3RowNum];
								
					          	//If square3 has 2 possible numbers and one of them are the same as square1
								if(squareHasTwoPossNums(square3) && getNumSamePossNums(square1, square3) == 1) {
									//Search remaining squares in row
					              		//If square4 has value of 0 and same possible numbers as square3
					                		//Remove all possible numbers that match common number in column of square1 that is not square1 or square3
											//Remove all possible numbers that match common number in column of square2 that is not square2 or square4
											//Remove all possible numbers that match common number in row of square1 that is not square1 or square2
							                //Remove all possible numbers that match common number in row of square3 that is not square3 or square4
							//Search each square left in column
								//If square2 has same 2 possible numbers
									//Search remaining squares in row of square1
										//If square3 has 2 possible numbers and one of them are the same as square1
											//Search remaining squares in column
												//If square4 has value of 0 and same possible numbers as square3
													//Remove all possible numbers that match common number in column of square1 that is not square1 or square3
													//Remove all possible numbers that match common number in column of square2 that is not square2 or square4
													//Remove all possible numbers that match common number in row of square1 that is not square1 or square2
													//Remove all possible numbers that match common number in row of square3 that is not square3 or square4
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * This gets the number of same possible numbers between two squares
	 * @param square1
	 * @param square2
	 * @return
	 */
	private int getNumSamePossNums(Square square1, Square square2) {
		int numSame = 0;
		
		for(int square1Num = 0; square1Num < square1.getPossibleValues().size(); square1Num++) {
			for(int square2Num = 0; square2Num < square2.getPossibleValues().size(); square2Num++) {
				if(square1.getPossibleValues().get(square1Num) == square2.getPossibleValues().get(square2Num))
					numSame++;
			}
		}
		
		return numSame;
	}
	//Gets all 3 number combinations with order not mattering and no same numbers
	//Input:  ArrayList of possible numbers
	ArrayList<Integer[]> getPossNumCombinations(ArrayList<Integer> possNums) {
		ArrayList<Integer[]> possNumCombinations = new ArrayList<Integer[]>();
		
		for(int i = 0; i < possNums.size(); i++) {
			for(int j = i+1;j < possNums.size(); j++) {
				for(int k = j+1;k < possNums.size(); k++) {
					possNumCombinations.add(new Integer[] {possNums.get(i),possNums.get(j),possNums.get(k)});
				}
			}
		}
		
		return possNumCombinations;
	}
	
	private boolean squareHasTwoPossNums(Square square) {
		if(square.getPossibleValues().size() == 2 && square.getValue() == 0)
			return true;
		
		return false;
	}
		
	/**
	 * Returns number of naked pairs in row
	 * @param rowNum int  This is the row number to search through
	 * @param sudokuSquares Square[][] This is a 2d array of squares
	 * @return int This returns the number of naked pairs found in row
	 */
	public int getNumNakedPairsInRow(int rowNum, Square sudokuSquares[][]) {
		int numNakedPairs = 0;
		
		for(int columnNum = 0; columnNum < SUDOKU_MAX_ROW_SQUARES;columnNum++) {
			//If square only has 2 possible values it's a naked pair
			if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2)
				numNakedPairs++;
			
		}
		
		return numNakedPairs;
	}
		
	/**
	 * Returns number of naked pairs in column
	 * @param columnNum int  This is the column number to search through
	 * @param sudokuSquares Square[][] This is a 2d array of squares
	 * @return int This returns the number of naked pairs found in row
	 */
	public int getNumNakedPairsInColumn(int columnNum, Square sudokuSquares[][]) {
		
		int numNakedPairs = 0;
		
		for(int rowNum = 0; rowNum < SUDOKU_MAX_COLUMN_SQUARES; rowNum++) {
			//If square only has 2 possible values it's a naked pair
			if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2)
				numNakedPairs++;
			
		}
		
		return numNakedPairs;
	}
	
		
	/**
	 * Returns number of naked pairs in block
	 * @param blockNum int  This is the row number to search through
	 * @param sudokuSquares Square[][] This is a 2d array of squares
	 * @return int This returns the number of naked pairs found in block
	 */
	public int getNumNakedPairsInBlock(int blockNum, Square sudokuSquares[][]) {
		
		int numNakedPairs = 0;
		
		ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);
		
		for(SquareCoordinate squareCoord : squareNumbersList) {
			//If square only has 2 possible values it's a naked pair
			if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().size() == 2)
				numNakedPairs++;
			
		}
		
		return numNakedPairs;
	}
	
	/**
	 * Returns the sudoku block number of an x/y square.  Block numbers are 1-9 going left to right,
	 * top to bottom
	 * @param columnNum int This is the column number of the square
	 * @param rowNum int This is the row number of the square
	 * @return
	 */
	private int getBlockNum(int columnNum, int rowNum) {
		int blockNum = 0;
		
		//Set the block number depending on what row number/column number we are in.
		if(columnNum < 3 && rowNum < 3) 
			blockNum = 0;
		else if(columnNum < 6 && rowNum < 3) 
			blockNum = 1;
		else if(columnNum < 9 && rowNum < 3) 
			blockNum = 2;
		else if(columnNum < 3 && rowNum < 6)
			blockNum = 3;
		else if(columnNum < 6 && rowNum < 6) 
			blockNum = 4;
		else if(columnNum < 9 && rowNum < 6)
			blockNum = 5;
		else if(columnNum < 3 && rowNum < 9) 
			blockNum = 6;
		else if(columnNum < 6 && rowNum < 9)
			blockNum = 7;
		else if(columnNum < 9 && rowNum < 9)
			blockNum = 8;
		else {
			System.out.println("ERROR: getBlockNum is returning invalid values for sudokuSquares[" + columnNum +"][" + rowNum + "]");
		}
		
		return blockNum;
	}
	
	/**
	 * Returns a string of all the current values of the sudoku going left to right top to bottom
	 * @param sudokuSquares Square[][]  This is a 2d array of squares
	 * @return
	 */
	public String getSudokuValuesString(Square sudokuSquares[][]) {
		String sudokuNumbersString = "";
		
		for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES;rowNum++) {
			for(int columnNum = 0; columnNum < SUDOKU_MAX_COLUMN_SQUARES;columnNum++) {
				sudokuNumbersString += sudokuSquares[columnNum][rowNum].getValue();
			}
		}
		
		return sudokuNumbersString;
	}
	
	/**
	 * Returns the number of squares that are still blank
	 * @return int This returns the number of squares that are still blank
	 */
	public int getSquaresBlank() {
		return squaresBlank;
	}

}
