package sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class HiddenStrategyTest extends Strategy {

    /**
     * This test puts one possibile number into each square and then test to make sure
     * findOnlyChoice finds the choice for each square.
     */
    @Test
    void findOnlyChoice() {
        SudokuPuzzle sudokuPuzzle = new SudokuPuzzle();
        HiddenStrategy hiddenStrategy = new HiddenStrategy();
        //Used to set one possibility for each square
        String solution = "435269781682571493197834562826195347374682915951743628519326874248957136763418259";
        Integer possibilityNum;
        sudokuPuzzle.fillSudokuArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        //For each square only put in one possibility for the square
        for(int i = 0;i < 9;i++) {
            for(int j = 0;j < 9;j++) {
                //Get next possible value in solution string
                possibilityNum = Character.getNumericValue(solution.charAt(Integer.valueOf(i) + Integer.valueOf(j) * 9));
                sudokuPuzzle.setPossibleValues(i, j, new Integer[] {possibilityNum});
            }
        }

        System.out.println("Starting testOnlyChoice test......");

        //Run findOnlyChoice on puzzle
        sudokuPuzzle.setSudokuSquares(hiddenStrategy.findOnlyChoice(sudokuPuzzle.getSudokuSquares()));

        assertEquals(solution, sudokuPuzzle.getSudokuValuesString());
        System.out.println(sudokuPuzzle.getSudokuValuesString());
        System.out.println("Finished findOnlyChoice test");
    }



    /**
     * This test puts one possibile number into each square and then test to make sure
     * findOnlyChoice finds the choice for each square.
     */
    @Test
    void findHiddenSingle1() {
        SudokuPuzzle sudokuPuzzle = new SudokuPuzzle();
        HiddenStrategy hiddenStrategy = new HiddenStrategy();
        Square[][] sudokuSquares = sudokuPuzzle.getSudokuSquares();
        //Used to set one possibility for each square
        String solution = "000620851000000000000000000000000000000000000000000000000000000000000000000000000";

        sudokuPuzzle.fillSudokuArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        sudokuPuzzle.addAllPossibilities();

        //Fill in one column where square [3][0] has a hidden single of 6
        sudokuPuzzle.setPossibleValues(0, 0,new Integer[] {4,7,3,9});
        sudokuPuzzle.setPossibleValues(1, 0,new Integer[] {4,7});
        sudokuPuzzle.setPossibleValues(2, 0,new Integer[] {4,3,9});
        sudokuPuzzle.setPossibleValues(3, 0,new Integer[] {4,6,9});
        sudokuPuzzle.getSudokuSquares()[4][0].setValue(2);
        sudokuPuzzle.setPossibleValues(5, 0,new Integer[] {4,9});
        sudokuPuzzle.getSudokuSquares()[6][0].setValue(8);
        sudokuPuzzle.getSudokuSquares()[7][0].setValue(5);
        sudokuPuzzle.getSudokuSquares()[8][0].setValue(1);

        System.out.println("Starting findHiddenSingle test......");

        //Run findOnlyChoice on puzzle
        sudokuPuzzle.setSudokuSquares(hiddenStrategy.findHiddenSingle(sudokuPuzzle.getSudokuSquares(), new Integer[] {1,2,3,4,5,6,7,8,9}));

        assertEquals(solution, sudokuPuzzle.getSudokuValuesString());
        System.out.println(sudokuPuzzle.getSudokuValuesString());
        System.out.println("Finished findOnlyChoice test");
    }
}