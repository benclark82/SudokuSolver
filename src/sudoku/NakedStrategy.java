package sudoku;

import java.util.ArrayList;
import java.util.HashMap;

public class NakedStrategy extends Strategy{

    /**
     * This finds all the naked pairs in a sudoku puzzle
     * @param sudokuSquares Square This is a 2D array of squares
     * @return boolean This method returns true if any possible number is removed due to finding a naked pair
     */
    public Square[][] findNakedPairs(Square sudokuSquares[][]) {
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
                                //System.out.println("found naked pair at [" + k + "][" + i + "] and [" + j + "][" + i + "] - Removed");
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

        return sudokuSquares;
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
                        System.out.println("Naked Pair(row): Removed possible number " + nakedPairValue + " from [" + i + "][" + rowNum + "]");
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
                        System.out.println("Naked Pair(column): Removed possible number " + nakedPairValue + " from [" + columnNum + "][" + i + "]");
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
                        System.out.println("Naked Pair(block): Removed possible number " + nakedPairValue + " from [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "]");					}
                }
            }
        }
        return foundNakedValue;
    }

    //TODO Need to put in code to find naked triples
    public boolean findNakedTriples(Square sudokuSquares[][]) {
        boolean foundNakedTriple = false;

        //for each block
        //for each square in the block
        //find first naked double square
        //find second naked double square that has one of same possibility in same row/column
        //check 3rd square in row/column to see if it is naked triple
        //if naked triple found in row of block
        //remove all 3 possibilities in block except the 3 squares from before
        //remove all 3 possibilities from all square in row except the 3 squares from before
        //if naked triple found in column of block
        //remove all 3 possibilities in block except the 3 squares from before
        //remove all 3 possibilities from all square in column except the 3 squares from before


        return foundNakedTriple;

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
            if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0 && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().size() == 2)
                numNakedPairs++;

        }

        return numNakedPairs;
    }

    /**
     * This method finds naked singles in the sudoku squares
     * @param sudokuSquares This is a 2D array of squares
     * @return boolean This method returns true if any possible number is removed due to finding a naked single
     */
    public Square[][] findNakedSingle(Square sudokuSquares[][]) {

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

                    //TODO This should only return true if a possible number was removed
                    foundNakedSingle = true;
                }
            }
        }

        return sudokuSquares;

    }
}
