package com.sudoku.sudokusolver.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Strategy {

    public static class SquareCoordinate {

        public int columnNum;
        public int rowNum;

        SquareCoordinate(int col, int row) {

            columnNum = col;
            rowNum = row;
        }
    }
    
    public static final int SUDOKU_MAX_ROW_SQUARES = 9;
    public static final int SUDOKU_MAX_COLUMN_SQUARES = 9;
    public static final int SUDOKU_MAX_BLOCKS = 9;
    public static final int SUDOKU_MAX_POSS_NUMS = 9;

    public Strategy() {
        //Setup a hash with block number as key and the square numbers for each block as the values
        setBlockNumSquareNumbers();
    }

    public HashMap<Integer, ArrayList<SquareCoordinate>> blockNumSquareNumbers = new HashMap<>();
    
    /**
     * Gets number of a specific possible number for all squares in the same row that do not already have a value
     * @param possNum  This is the possible number we are checking for
     * @param rowNum  This is the row number of the square
     * @param sudokuSquares  This is a 2D array of squares
     * @return int  This is the number of possibilities we find in the row
     */
    public int getNumOnlyChoiceInRow(int possNum, int rowNum, Square[][] sudokuSquares) {

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
    public int getNumOnlyChoiceInColumn(int possNum, int columnNum, Square[][] sudokuSquares) {

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
    public int getNumOnlyChoiceInBlock(int possNum, int rowNum, int columnNum, Square[][] sudokuSquares) {
        int numPossibilities = 0;
        int blockNum;

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
     * This method sets all the data for all of the coordinates of each block in the sudoku puzzle.
     * Block numbers go left to right, top to bottom
     */
    public void setBlockNumSquareNumbers() {

        int blockRowIncrement = 0;
        int blockColumnIncrement = 0;
        int rowNum = 0;
        int columnNum = 0;

        //Build all the blocks and square numbers that go in those blocks
        for(int i=0; i < SUDOKU_MAX_BLOCKS; i++) {

            ArrayList <SquareCoordinate> tempCoordinates = new ArrayList<>();

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
     * Returns the sudoku block number of an x/y square.  Block numbers are 1-9 going left to right,
     * top to bottom
     * @param columnNum int This is the column number of the square
     * @param rowNum int This is the row number of the square
     * @return Returns the block number for the column/row pair
     */
    public int getBlockNum(int columnNum, int rowNum) {
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
     * This will go through all known numbers and remove any possibilities of any of the squares in the same row, column, and block
     * @param sudokuSquares This is a 2D array of squares
     */
    public Square[][] removePossibilities(Square[][] sudokuSquares) {
        //go through ever square and if number is found, remove that possibility from squares in same row,
        //column, and 9 square section
        for(int i = 0;i < 9;i++) {
            for(int j = 0;j < 9;j++) {
                if(sudokuSquares[i][j].getValue() != 0) {
                    //System.out.println("Removing possibilities for value: " + sudokuSquares[i][j].getValue() + "for [" + i + "][" + j + "]");
                    sudokuSquares = removePossibilitiesFromRow(i, j, sudokuSquares);
                    sudokuSquares = removePossibilitiesFromColumn(i, j, sudokuSquares);
                    sudokuSquares = removePossibilitiesFromBlock(i, j, sudokuSquares);
                }
            }
        }

        return sudokuSquares;
    }

    /**
     * This method removes possibilities for all squares in the same row that do not already have a value
     * @param columnNum  This is the row number of the square
     * @param rowNum  This is the row number of the square
     * @param sudokuSquares  This is a 2D array of squares
     */
    public Square[][] removePossibilitiesFromRow(int columnNum, int rowNum, Square[][] sudokuSquares) {
        //For every square in the row
        for(int checkColumnNum = 0; checkColumnNum < SUDOKU_MAX_ROW_SQUARES; checkColumnNum++) {

            //If it doesn't have a value
            if(sudokuSquares[checkColumnNum][rowNum].getValue() == 0) {
                sudokuSquares[checkColumnNum][rowNum].removePossibleValue(sudokuSquares[columnNum][rowNum].getValue());
                System.out.println("removePossibilitiesFromRow: Removed possible value " + sudokuSquares[columnNum][rowNum].getValue() + " from  [" + checkColumnNum + "][" + rowNum + "]");
            }
        }

        return sudokuSquares;
    }

    /**
     * Removes possibilities for all squares in the same column that do not already have a value
     * @param columnNum  This is the row number of the square
     * @param rowNum  This is the row number of the square
     * @param sudokuSquares  This is a 2D array of squares
     */
    public Square[][] removePossibilitiesFromColumn(int columnNum, int rowNum, Square[][] sudokuSquares) {

        for(int checkRowNum = 0; checkRowNum < SUDOKU_MAX_COLUMN_SQUARES; checkRowNum++) {

            if(sudokuSquares[columnNum][checkRowNum].getValue() == 0) {

                sudokuSquares[columnNum][checkRowNum].removePossibleValue(sudokuSquares[columnNum][rowNum].getValue());
                System.out.println("removePossibilitiesFromColumn: Removed possible value " + sudokuSquares[columnNum][rowNum].getValue() + " from  [" + columnNum + "][" + checkRowNum + "]");

            }
        }

        return sudokuSquares;
    }

    /**
     * Removes possibilities for all squares in the same block that do not already have a value
     * @param columnNum  This is the row number of the square
     * @param rowNum  This is the row number of the square
     * @param sudokuSquares  This is a 2D array of squares
     */
    public Square[][] removePossibilitiesFromBlock(int columnNum, int rowNum, Square[][] sudokuSquares) {

        int blockNum = getBlockNum(columnNum, rowNum);
        ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);

        //Remove possibilities from every square in block that doesn't have a value
        for(SquareCoordinate squareCoord : squareNumbersList) {

            //If square doesn't have a value
            if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0) {
                sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].removePossibleValue(sudokuSquares[columnNum][rowNum].getValue());
                System.out.println("removePossibilitiesFromBlock: Removed possible value " + sudokuSquares[columnNum][rowNum].getValue() + " from  [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "]");

            }
        }

        return sudokuSquares;
    }
}
