package sudoku;
import java.util.*;

/**
 *  
 * @author Ben Clark
 * The sudokuStrategies class has all the strategy implementations to solve a sudoku puzzle
 *
 */
public class SudokuPuzzle {

	Integer[] allPossValues = new Integer[] {1,2,3,4,5,6,7,8,9};
	private Square[][] sudokuSquares = new Square[9][9];

	private static final int SUDOKU_MAX_ROW_SQUARES = 9;
	private static final int SUDOKU_MAX_COLUMN_SQUARES = 9;


	/**
	 * This method is the default constructor
	 */
	public SudokuPuzzle() {


		
	}

	/**
	 * This converts the users sudoku numbers string into a sudoku 2 dimensional array of Square objects
	 * @param sudokuNumbersString String  This is the sudoku values with zeros for empty values.  The values should be input from left to right, top to bottom.
	 * @return Square[][]  This returns a 2d array of squares
	 */
	public Square[][] fillSudokuArray(String sudokuNumbersString) {

		int rowNum = 0;
		int columnNum = 0;


		for(int i = 0;i < sudokuNumbersString.length();i++) {
			sudokuSquares[columnNum][rowNum] = new Square(Character.getNumericValue(sudokuNumbersString.charAt(i)));

			//System.out.println("sudokuNumbers[" + columnNum + "][" + rowNum + "] = " + Character.getNumericValue(sudokuNumbersString.charAt(i)));
			//Jump to next row if we are at the end of a row.  If not then just go to next square.
			if(columnNum >= 8) {
				rowNum++;
				columnNum = 0;
			}
			else
				columnNum++;


		}

		return sudokuSquares;
	}

	/**
	 * This method adds in all possible values(1-9) to all squares that dont have a value
	 */
	public void addAllPossibilities() {

		//For every square in the row
		for(int columnNum = 0;columnNum < 9;columnNum++) {
			
			//For every square in the column
			for(int rowNum = 0;rowNum < 9;rowNum++) {
				
				//If square doesn't have a value then add all the possibilities
				if(sudokuSquares[columnNum][rowNum].getValue() == 0) {
					sudokuSquares[columnNum][rowNum].setPossibleValues(allPossValues);
				}

			}
		}
	}
	
	/**
	 * This prints all possible values for every square
	 */
	public void printAllPossibilities() {
		
		
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(sudokuSquares[j][i].getValue() == 0) {
					ArrayList<Integer> possValList = sudokuSquares[j][i].getPossibleValues();
					System.out.print("Possible values for [" + j + "][" + i + "]");
					for(Integer possVal : possValList )
						System.out.print(" " + possVal);

				}
			}
		}
	}
	


	
	/**
	 *
	 * @return Returns a string of all the current values of the sudoku going left to right top to bottom
	 */
	public String getSudokuValuesString() {
		//TODO: Change this to StringBuilder
		String sudokuNumbersString = "";
		
		for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES;rowNum++) {
			for(int columnNum = 0; columnNum < SUDOKU_MAX_COLUMN_SQUARES;columnNum++) {
				sudokuNumbersString += sudokuSquares[columnNum][rowNum].getValue();
			}
		}
		
		return sudokuNumbersString;
	}

	public Square[][] getSudokuSquares() {
		return sudokuSquares;
	}

	public void setSudokuSquares(Square[][] sudokuSquares) {
		this.sudokuSquares = sudokuSquares;
	}

}
