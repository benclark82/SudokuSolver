package com.sudoku.sudokusolver.model;

import java.util.*;

/**
 * 
 * @author Ben Clark
 *
 */
public class SudokuSolver {

	Scanner scanner = new Scanner(System.in);
	SudokuPuzzle sudokuPuzzle;
	HiddenStrategy hiddenStrategy;
	NakedStrategy nakedStrategy;
	PointingPairStrategy pointingPairStrategy;
	WingStrategy wingStrategy;

	/**
	 * Constructor.  It gets sudoku numbers from user and makes sure they are valid
	 * @param getSudokuFromUser  This is true if we want to get the sudoku values from the user
	 */
	public SudokuSolver(boolean getSudokuFromUser) {

		sudokuPuzzle = new SudokuPuzzle();
		hiddenStrategy = new HiddenStrategy();
		nakedStrategy = new NakedStrategy();
		pointingPairStrategy = new PointingPairStrategy();
		wingStrategy = new WingStrategy();

		if(getSudokuFromUser) {
			//Get sudoku numbers from user
			String sudokuNumbersString = getSudokuNumbers();
			
			//Check to make sure sudoku is valid before we start
			boolean sudokuIsValid = false;
			while(!sudokuIsValid) {
				if(checkIfSudokuValid(sudokuNumbersString))
					sudokuIsValid = true;
				else
					sudokuNumbersString = getSudokuNumbers();
			}


			sudokuPuzzle.fillSudokuArray(sudokuNumbersString);

		}
	}
	
	/**
	 * This is a constructor.  It gets sudoku numbers from input and not the user
	 * @param sudokuNumbers String  This is the sudoku values with zeros for empty values.  The values should be input from left to right, top to bottom.
	 */
	public SudokuSolver(String sudokuNumbers) {

		hiddenStrategy = new HiddenStrategy();
		nakedStrategy = new NakedStrategy();
		pointingPairStrategy = new PointingPairStrategy();
		wingStrategy = new WingStrategy();

		//Get sudoku numbers from input
		String sudokuNumbersString = sudokuNumbers;
		
		//Check to make sure sudoku is valid before we start
		boolean sudokuIsValid = false;
		while(!sudokuIsValid) {
			if(checkIfSudokuValid(sudokuNumbersString))
				sudokuIsValid = true;
			else
				sudokuNumbersString = getSudokuNumbers();
		}


		sudokuPuzzle.fillSudokuArray(sudokuNumbersString);

	}
	
	

	
	/**
	 * This gets sudoku values from the user
	 * @return String This is the sudoku values with zeros for empty values.  The values should be input from left to right, top to bottom.
	 */
	public String getSudokuNumbers() {
				
		System.out.println("Enter in sudoku chart from left to right, with 0s instead of blanks, and no returns: ");
		String sudokuNumbersString = scanner.nextLine();

		
		return sudokuNumbersString;
		
	}
	
	/**
	 * This goes through and uses sudoku strategies to solve the sudoku puzzle
	 */
	public void solveSudoku() {
		boolean sudokuFinished = false;
		
		//Fill in all possible values for square that don't have a value
		sudokuPuzzle.addAllPossibilities();
		
		//For each value we find, remove all possible values from squares in that row, column, and block
		hiddenStrategy.removePossibilities(sudokuPuzzle.getSudokuSquares());
		
		int counter = 0;
		boolean madeProgress = false;
		
		//Keep going through the strategies of finding values as long as we aren't stuck or sudoku is finished
		do {
			counter++;
					
			//Find all the only choice values
			sudokuPuzzle.setSudokuSquares(hiddenStrategy.findOnlyChoice(sudokuPuzzle.getSudokuSquares()));
			
			//Find all the naked singles
			sudokuPuzzle.setSudokuSquares(nakedStrategy.findNakedSingle(sudokuPuzzle.getSudokuSquares()));

			
			//Find all the hidden singles
			sudokuPuzzle.setSudokuSquares(hiddenStrategy.findHiddenSingle(sudokuPuzzle.getSudokuSquares(), sudokuPuzzle.allPossValues));

			
			//Find all the naked pairs
			sudokuPuzzle.setSudokuSquares(nakedStrategy.findNakedPairs(sudokuPuzzle.getSudokuSquares()));
			
			//Find all pointing pairs combinations in rows
			sudokuPuzzle.setSudokuSquares(pointingPairStrategy.findPointingPairsInRow(sudokuPuzzle.getSudokuSquares()));

			
			//Find all pointing pairs combinations in blocks
			sudokuPuzzle.setSudokuSquares(pointingPairStrategy.findPointingPairsInBlock(sudokuPuzzle.getSudokuSquares()));
			
			//Find all the naked triples
			/**madeProgress = sudokuPuzzle.findNakedTriples(sudokuSquares);
			
			if(madeProgress && sudokuPuzzle.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}*/
			
			//Find all ywing combinations
			sudokuPuzzle.setSudokuSquares(wingStrategy.findYWingRowColumn(sudokuPuzzle.getSudokuSquares()));

			
			//Find all ywing combinations
			sudokuPuzzle.setSudokuSquares(wingStrategy.findYWingBlock(sudokuPuzzle.getSudokuSquares()));


			//Find all xwing combinations
			sudokuPuzzle.setSudokuSquares(wingStrategy.findXWingInRows(sudokuPuzzle.getSudokuSquares()));

			
			//Find all xwing combinations
			sudokuPuzzle.setSudokuSquares(wingStrategy.findXWingInColumns(sudokuPuzzle.getSudokuSquares()));


			
			//System.out.println("Still running");
		} while(!sudokuFinished && counter < 1000);
		System.out.println("Sudoku Is Finished!");
		//System.out.println(sudokuPuzzle.getSudokuValuesString(sudokuSquares));
		sudokuPuzzle.printAllPossibilities();
		printSudoku(sudokuPuzzle.getSudokuSquares());
		
	}
	

	/**
	 * This checks if the sudoku string is valid.  It needs to have 81 numbers, and only numbers 0-9
	 * @param sudokuNumbersString String This is the sudoku values with zeros for empty values.  The values should be input from left to right, top to bottom.
	 * @return boolean This returns true if the sudoku string is valid
	 */
	public boolean checkIfSudokuValid(String sudokuNumbersString) {
		if(sudokuNumbersString.length() != 81) {
			System.out.println("Error: Invalid number of sudoku numbers");
			return false;
		}
		else if (sudokuNumbersString.contains(" "))  {
			System.out.println("Error: Sudoku numbers can't have any spaces");
			return false;
		}
		else {
			for(int i = 0;i < sudokuNumbersString.length();i++) {
				int num = Character.getNumericValue(sudokuNumbersString.charAt(i));
				if(num < 0 || num > 9) {
					System.out.println("Error: You've entered an invalid character(" + num + ") at location " + i+1 + ".  Please try again.");
					return false;
				}
			}

		}
		
		return true;
	}
	
	/**
	 * This prints the sudoku values in the shape of a sudoku puzzle
	 * @param sudokuSquares This is a 2d array of squares
	 */
	public void printSudoku(Square sudokuSquares[][]) {
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				System.out.print(sudokuSquares[j][i].getValue() + " ");
			}
			System.out.println("");
		}
	}
	

}
