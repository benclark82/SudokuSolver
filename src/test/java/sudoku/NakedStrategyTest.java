package sudoku;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class NakedStrategyTest extends Strategy {

    /**
     * This test puts one possibile number into each square and then test to make sure
     * findOnlyChoice finds the choice for each square.
     */
    @Test
    void findNakedSingle() {
        SudokuPuzzle sudokuPuzzle = new SudokuPuzzle();
        NakedStrategy nakedStrategy = new NakedStrategy();
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

        System.out.println("Starting findNakedSingle test......");

        //Run findOnlyChoice on puzzle
        sudokuPuzzle.setSudokuSquares(nakedStrategy.findNakedSingle(sudokuPuzzle.getSudokuSquares()));

        assertEquals(solution, sudokuPuzzle.getSudokuValuesString());
        System.out.println(sudokuPuzzle.getSudokuValuesString());
        System.out.println("Finished findOnlyChoice test");
    }

}