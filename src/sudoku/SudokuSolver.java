package sudoku;
import java.util.*;

/**
 * 
 * @author Ben Clark
 * This is the controller class for the sudoku application.  
 *
 */
public class SudokuSolver {

	Scanner scanner = new Scanner(System.in);
	private Square sudokuSquares[][] = new Square[9][9];
	
	/**
	 * This is the constructor.  It gets sudoku numbers from user and makes sure they are valid
	 * @param getSudokuFromUser  This is true if we want to get the sudoku values from the user
	 */
	public SudokuSolver(boolean getSudokuFromUser) {
		
		
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
			
			
			sudokuSquares = fillSudokuArray(sudokuNumbersString);

		}
	}
	
	/**
	 * This is a constructor.  It gets sudoku numbers from input and not the user
	 * @param sudokuNumbers String  This is the sudoku values with zeros for empty values.  The values should be input from left to right, top to bottom.
	 */
	public SudokuSolver(String sudokuNumbers) {
		
		

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
		
		
		sudokuSquares = fillSudokuArray(sudokuNumbersString);

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
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		boolean sudokuFinished = false;
		
		//Fill in all possible values for square that don't have a value
		sudokuStrategies.addAllPossibilities(sudokuSquares);
		
		//For each value we find, remove all possible values from squares in that row, column, and block
		sudokuStrategies.removePossibilities(sudokuSquares);
		
		int counter = 0;
		boolean madeProgress = false;
		
		//Keep going through the strategies of finding values as long as we aren't stuck or sudoku is finished
		do {
			counter++;
					
			//Find all the only choice values
			madeProgress = sudokuStrategies.findOnlyChoice(sudokuSquares);
			
			if(madeProgress && sudokuStrategies.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}
			
			//Find all the naked singles
			madeProgress = sudokuStrategies.findNakedSingle(sudokuSquares);
			
			if(madeProgress && sudokuStrategies.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}
			
			//Find all the hidden singles
			madeProgress = sudokuStrategies.findHiddenSingle(sudokuSquares);
			
			if(madeProgress && sudokuStrategies.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}
			
			//Find all the naked pairs
			madeProgress = sudokuStrategies.findNakedPairs(sudokuSquares);
			
			if(madeProgress && sudokuStrategies.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}
			
			//Find all the naked triples
			madeProgress = sudokuStrategies.findNakedTriples(sudokuSquares);
			
			if(madeProgress && sudokuStrategies.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}
			
			//Find all ywing combinations
			madeProgress = sudokuStrategies.findYWingRowColumn(sudokuSquares);
			
			if(madeProgress && sudokuStrategies.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}
			
			//Find all ywing combinations
			madeProgress = sudokuStrategies.findYWingBlock(sudokuSquares);
			
			if(madeProgress && sudokuStrategies.getSquaresBlank() == 0) {
				sudokuFinished = true;
				break;
			}

			
			//System.out.println("Still running");
		} while(!sudokuFinished && counter < 1000);
		System.out.println("Sudoku Is Finished!");
		printSudoku(sudokuSquares);
		
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
	 * This gets the sudoku squares
	 * @return Square[][] This is a 2d array of squares
	 */
	public Square[][] getSudokuSquares() {
		return sudokuSquares;
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
	
	/**
	 * This is the main function of the application
	 * @param args None
	 */
	public static void main(String args[]) {
		SudokuSolver game = new SudokuSolver(true);
		game.solveSudoku();
		System.exit(0);
		
	}
	

}
