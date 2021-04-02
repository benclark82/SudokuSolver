/*
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import sudoku.SudokuSolver;
import sudoku.Square;
import sudoku.SudokuStrategies;

class UnitTests {

	@Test
	void testExpertSudoku() {
		
		System.out.println("testExpertSudoku1 test running........");
		SudokuSolver sudokuSolver = new SudokuSolver("900008400403000050000079200050000040000600001000000000307000020100006000002045080");
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		
		sudokuSolver.solveSudoku();
		System.out.println(sudokuStrategies.getSudokuValuesString(sudokuSolver.getSudokuSquares()));
		System.out.println("921538476473162859568479213856913742234687591719254368347891625185726934692345187");
		assertTrue(sudokuStrategies.getSudokuValuesString(sudokuSolver.getSudokuSquares()).equals("921538476473162859568479213856913742234687591719254368347891625185726934692345187"));

	}

	@Test
	void testNakedSingle() {
		
		Square sudokuSquaresTest[][] = new Square[9][9];
		Square sudokuSquaresCorrect[][] = new Square[9][9];
		SudokuSolver sudokuSolver = new SudokuSolver(false);
		SudokuSolver sudokuSolver2 = new SudokuSolver(false);
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		
		//Test the code
		sudokuSquaresTest = sudokuSolver.fillSudokuArray("800739006370465000040182009000600040054300610060500000400853070000271064100940002");
		sudokuStrategies.addAllPossibilities(sudokuSquaresTest);
		sudokuStrategies.removePossibilities(sudokuSquaresTest);

		System.out.println("Starting naked singles test.......");
		sudokuStrategies.findNakedSingle(sudokuSquaresTest);
		
		//Get the correct sudokuSquares with filled in naked singles
		sudokuSquaresCorrect = sudokuSolver2.fillSudokuArray("800739006370465000040182009000600040054300610060500000400853071000271064100946002");
		sudokuStrategies.addAllPossibilities(sudokuSquaresCorrect);
		sudokuStrategies.removePossibilities(sudokuSquaresCorrect);
		
		//Compare every square possible values to what
		boolean isEquals = true;
		for(int i = 0;i < 9;i++) {
			for (int j = 0;j < 9;j++) {
				if(sudokuSquaresTest[i][j].compareTo(sudokuSquaresCorrect[i][j]) != 0) {
					System.out.println("FAILED: For [" + i + "][" + j + "] is different. sudokuSquaresTest: " + sudokuSquaresTest[i][j].getValue() + " sudokuSquaresCorrect: " + sudokuSquaresCorrect[i][j].getValue());
					isEquals = false;
				}
			}
		}

		assertTrue(isEquals);
		System.out.println("Finished naked singles candidates test");
	}
	
	@Test
	void testOnlyChoice() {

		Square sudokuSquaresTest[][] = new Square[9][9];
		Square sudokuSquaresCorrect[][] = new Square[9][9];
		SudokuSolver sudokuSolver = new SudokuSolver(false);
		SudokuSolver sudokuSolver2 = new SudokuSolver(false);
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		
		//Setup the sudoku for the test
		sudokuSquaresTest = sudokuSolver.fillSudokuArray("000000500160900000009064000000000004400020100000300050002089000010250030700100009");
		sudokuStrategies.addAllPossibilities(sudokuSquaresTest);
		sudokuStrategies.removePossibilities(sudokuSquaresTest);

		//Test the code
		System.out.println("Starting testOnlyChoice test......");
		sudokuStrategies.findOnlyChoice(sudokuSquaresTest);
		
		//Get the correct sudokuSquares
		sudokuSquaresCorrect = sudokuSolver2.fillSudokuArray("000000596160900000009064000000000004400020100000340050002489005910257030700106009");
		sudokuStrategies.addAllPossibilities(sudokuSquaresCorrect);
		sudokuStrategies.removePossibilities(sudokuSquaresCorrect);
		
		//Check every sudoku square to make sure it has the correct value
		boolean isEquals = true;
		for(int i = 0;i < 9;i++) {
			for (int j = 0;j < 9;j++) {
				if(sudokuSquaresTest[i][j].compareTo(sudokuSquaresCorrect[i][j]) != 0) {
					isEquals = false;
				}
			}
		}

		assertTrue(isEquals);
		System.out.println("Finished findOnlyChoice test");
	}
	
	@Test
	void testHiddenSingle() {

		boolean isEquals = true;
		Square sudokuSquaresTest[][] = new Square[9][9];
		Square sudokuSquaresCorrect[][] = new Square[9][9];
		SudokuSolver sudokuSolver = new SudokuSolver(false);
		SudokuSolver sudokuSolver2 = new SudokuSolver(false);
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		
		//Setup sudoku for test
		sudokuSquaresTest = sudokuSolver.fillSudokuArray("000000500160900000009064000000000004400020100000300050002089000010250030700100009");
		sudokuStrategies.addAllPossibilities(sudokuSquaresTest);
		sudokuStrategies.removePossibilities(sudokuSquaresTest);

		//Test the code
		System.out.println("Starting testHiddenSingle test.......");
		sudokuStrategies.findHiddenSingle(sudokuSquaresTest);
		//sudokuStrategies.printAllPossibilities(sudokuSquaresTest);
		
		//Get the correct sudokuSquares
		sudokuSquaresCorrect = sudokuSolver2.fillSudokuArray("000000596160900000009064000000090004490020100000340950002489715910257030700100009");
		sudokuStrategies.addAllPossibilities(sudokuSquaresCorrect);
		sudokuStrategies.removePossibilities(sudokuSquaresCorrect);
		
		//Check to make sure every square matches the correct sudoku square values
		for(int i = 0;i < 9;i++) {
			for (int j = 0;j < 9;j++) {
				if(sudokuSquaresTest[i][j].compareTo(sudokuSquaresCorrect[i][j]) != 0) {
					//System.out.println("FAILED");
					isEquals = false;
				}
			}
		}

		assertTrue(isEquals);
		System.out.println("Finished findHiddenSingle test");
	}
	
	@Test
	void testNakedPairs() {

		Square sudokuSquaresTest[][] = new Square[9][9];
		SudokuSolver sudokuSolver2 = new SudokuSolver(false);
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		ArrayList<Integer> allPossTestList = new ArrayList<Integer>();
		ArrayList<Integer> testList = new ArrayList<Integer>();
		ArrayList<Integer> nakedPairList = new ArrayList<Integer>();
		boolean passed = true;
		
		//Create a test list that has all possible values
		allPossTestList.add(1);
		allPossTestList.add(2);
		allPossTestList.add(3);
		allPossTestList.add(4);
		allPossTestList.add(5);
		allPossTestList.add(6);
		allPossTestList.add(7);
		allPossTestList.add(8);
		allPossTestList.add(9);
		
		//Create a test list for the expected values 
		testList.add(1);
		testList.add(2);
		testList.add(3);
		testList.add(4);
		testList.add(5);
		testList.add(6);
		testList.add(7);
		
		//Create a list for naked pairs
		nakedPairList.add(8);
		nakedPairList.add(9);
		
		//Test the code
		System.out.println("Starting nakedPairs test.......");
		sudokuSquaresTest = sudokuSolver2.fillSudokuArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		sudokuStrategies.addAllPossibilities(sudokuSquaresTest);
		
		//Test naked pairs in a row 
		sudokuSquaresTest[4][5].setPossibleValues(nakedPairList);
		sudokuSquaresTest[6][5].setPossibleValues(nakedPairList);
		
		//Run test
		sudokuStrategies.findNakedPairs(sudokuSquaresTest);		

		//Go through and check all squares to make sure the ones in the row only have the naked pair values
		//and all the rest will have 1-9 possible values
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(i==5)
				{
					//Don't check the naked pair squares
					if(j !=6 && j != 4) {
						if(!sudokuSquaresTest[j][i].getPossibleValues().equals(testList)) {
							passed = false;
							System.out.println("Naked Pair(row) FAIL: Should only have test values for square [" + j + "][" + i + "] " );
							sudokuSquaresTest[j][i].printPossibleValues();
						}
					}
				}
				else if(!sudokuSquaresTest[j][i].getPossibleValues().equals(allPossTestList)) {
					passed = false;
					System.out.println("Naked Pair(row) FAIL: Should have test possible values for square [" + j + "][" + i + "]");
					sudokuSquaresTest[j][i].printPossibleValues();
				}
				
			}
		}
		

		//Test naked pairs in a same column and square.  Possibilities should be removed for column and block
		sudokuSquaresTest = new Square[9][9];
		sudokuSquaresTest = sudokuSolver2.fillSudokuArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		sudokuStrategies.addAllPossibilities(sudokuSquaresTest);
		sudokuSquaresTest[4][3].setPossibleValues(nakedPairList);
		sudokuSquaresTest[4][5].setPossibleValues(nakedPairList);
		sudokuStrategies.findNakedPairs(sudokuSquaresTest);
		
		//Check squares that should have possible values removed in block and column
		if(!sudokuSquaresTest[3][3].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [3][3] should only have test values for block");
		}
		if(!sudokuSquaresTest[5][3].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [5][3] should only have test values for block");
		}
		if(!sudokuSquaresTest[3][5].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [4][3] should only have test values for block");
		}
		if(!sudokuSquaresTest[5][5].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [4][5] should only have test values for block");
		}
		if(!sudokuSquaresTest[3][4].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [3][4] should only have test values for block");
		}
		if(!sudokuSquaresTest[5][4].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [5][4] should only have test values for block");
		}
		if(!sudokuSquaresTest[3][5].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [3][5] should only have test values for block");
		}
		if(!sudokuSquaresTest[5][5].getPossibleValues().equals(testList)) {
			passed = false;
			System.out.println("Naked Pairs FAILED: [5][5] should only have test values for block");
		}
		
		//Check all other squares to make sure nothing was removed that wasn't supposed to be
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(i <= 3 && i >= 5 && j <= 3 && j >= 5) {
					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(allPossTestList)) {
						System.out.println("Naked Pairs FAILED:  should have all poss test values for block");
						passed = false;
					}
				}
				
			}
		}
		
		assertTrue(passed);
		System.out.println("Finished nakedPairs test");
	}
	
	@Test
	void testYWingColumnRow() {

		Square sudokuSquaresTest[][] = new Square[9][9];
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		SudokuSolver sudokuSolver = new SudokuSolver(false);
		boolean passed = true;
		
		
		//Test naked pairs in a column AND squares in block should be removed
		sudokuSquaresTest = new Square[9][9];
		sudokuSquaresTest = sudokuSolver.fillSudokuArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		sudokuStrategies.addAllPossibilities(sudokuSquaresTest);
		
		//Create 3 YWing squares
		ArrayList<Integer> onePossValList = new ArrayList<Integer>();
		onePossValList.add(4);
		onePossValList.add(7);
		sudokuSquaresTest[0][1].setPossibleValues(onePossValList);
		
		ArrayList<Integer> twoPossValList = new ArrayList<Integer>();
		twoPossValList.add(7);
		twoPossValList.add(8);
		sudokuSquaresTest[5][1].setPossibleValues(twoPossValList);
		
		ArrayList<Integer> threePossValList = new ArrayList<Integer>();
		threePossValList.add(4);
		threePossValList.add(8);
		sudokuSquaresTest[5][3].setPossibleValues(threePossValList);
		
		//Create 4th YWing square that will be changed
		ArrayList<Integer> fourPossValList = new ArrayList<Integer>();
		fourPossValList.add(1);
		fourPossValList.add(4);
		fourPossValList.add(6);
		fourPossValList.add(8);
		sudokuSquaresTest[0][3].setPossibleValues(fourPossValList);
		
		//Expected result of 4th square if everything goes correct
		ArrayList<Integer> endPossValList = new ArrayList<Integer>();
		endPossValList.add(1);
		endPossValList.add(6);
		endPossValList.add(8);
		
		//Test YWing
		System.out.println("Starting YWing ColumnRow test.......");
		sudokuSquaresTest[0][3].printPossibleValues();
		sudokuStrategies.findYWingRowColumn(sudokuSquaresTest);
		

		//Go through and check all squares to make sure the ones in the row only have the naked pair values
		//and all the rest will have 1-9 possible values
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(j==0 && i==1)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(onePossValList)) {
						passed = false;
						System.out.println("YWing FAIL: onePossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				else if(j==5 && i==1)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(twoPossValList)) {
						passed = false;
						System.out.println("YWing FAIL: twoPossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				else if(j==5 && i==3)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(threePossValList)) {
						passed = false;
						System.out.println("YWing FAIL: threePossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				else if(j==0 && i==3)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(endPossValList)) {
						passed = false;
						System.out.println("YWing FAIL: endPossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				
			}
		}
		
		assertTrue(passed);
		
		
	}
	
	
	@Test
	void testYWingBlock() {

		Square sudokuSquaresTest[][] = new Square[9][9];
		SudokuStrategies sudokuStrategies = new SudokuStrategies();
		SudokuSolver sudokuSolver = new SudokuSolver(false);
		boolean passed = true;
		
		sudokuSquaresTest = sudokuSolver.fillSudokuArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		sudokuStrategies.addAllPossibilities(sudokuSquaresTest);
		
		//Create 3 YWing squares
		ArrayList<Integer> onePossValList = new ArrayList<Integer>();
		onePossValList.add(3);
		onePossValList.add(8);
		sudokuSquaresTest[1][0].setPossibleValues(onePossValList);
		
		ArrayList<Integer> twoPossValList = new ArrayList<Integer>();
		twoPossValList.add(4);
		twoPossValList.add(8);
		sudokuSquaresTest[2][1].setPossibleValues(twoPossValList);
		
		ArrayList<Integer> threePossValList = new ArrayList<Integer>();
		threePossValList.add(3);
		threePossValList.add(4);
		sudokuSquaresTest[1][8].setPossibleValues(threePossValList);
		
		//Create 4th YWing square that will be changed
		ArrayList<Integer> fourPossValList = new ArrayList<Integer>();
		fourPossValList.add(3);
		fourPossValList.add(4);
		fourPossValList.add(8);
		sudokuSquaresTest[2][7].setPossibleValues(fourPossValList);
		
		//Expected result of 4th square if everything goes correct
		ArrayList<Integer> endPossValList = new ArrayList<Integer>();
		endPossValList.add(3);
		endPossValList.add(8);
		
		//Test YWing
		System.out.println("Starting YWingBlock test.......");
		sudokuSquaresTest[0][3].printPossibleValues();
		sudokuStrategies.findYWingBlock(sudokuSquaresTest);
		

		//Go through and check all squares to make sure the ones in the row only have the naked pair values
		//and all the rest will have 1-9 possible values
		for(int i = 0;i < 9;i++) {
			for(int j = 0;j < 9;j++) {
				if(j==1 && i==0)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(onePossValList)) {
						passed = false;
						System.out.println("YWing FAIL: onePossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				else if(j==2 && i==1)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(twoPossValList)) {
						passed = false;
						System.out.println("YWing FAIL: twoPossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				else if(j==1 && i==8)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(threePossValList)) {
						passed = false;
						System.out.println("YWing FAIL: threePossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				else if(j==2 && i==7)
				{

					if(!sudokuSquaresTest[j][i].getPossibleValues().equals(endPossValList)) {
						passed = false;
						System.out.println("YWing FAIL: endPossValList was not correct" );
						sudokuSquaresTest[j][i].printPossibleValues();
					}
				}
				
			}
		}
		
		assertTrue(passed);
		
		
	}

}
*/
