package sudoku;

import java.util.ArrayList;
import java.util.HashMap;

public class PointingPairStrategy extends Strategy{

    /**
     * This method finds pointing pairs in a block and removes possible numbers in respect to it
     * @param sudokuSquares 2d array of squares
     * @return Returns true if possibilities were removed due to finding pointing pairs
     */
    public Square[][] findPointingPairsInBlock(Square sudokuSquares[][]) {

        boolean foundNewPointingPair = false;

        //For each possible number
        for(int possNum = 1; possNum <= 9; possNum++) {

            //For each block
            for(int blockNum = 0; blockNum < SUDOKU_MAX_BLOCKS; blockNum++) {
                int firstColumnNum = -1;
                int firstRowNum = -1;
                int secondColumnNum = -1;
                int secondRowNum = -1;
                int possNumsFound = 0;
                ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);

                //For each square in a block
                for(SquareCoordinate squareCoord : squareNumbersList) {
                    //Search remaining squares in block for a square that has one identical possible value

                    //If square value is 0 and has possible number
                    if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0 && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].hasPossibleValue(possNum)) {
                        //Add 1 to possible number count
                        possNumsFound++;

                        //Record rows and columns to use later if we found a pointing pair
                        if(possNumsFound == 1) {
                            firstColumnNum = squareCoord.columnNum;
                            firstRowNum = squareCoord.rowNum;
                        }
                        else if (possNumsFound == 2 ) {
                            secondColumnNum = squareCoord.columnNum;
                            secondRowNum = squareCoord.rowNum;
                        }
                    }
                }

                //If possible number count equals 2
                if(possNumsFound == 2) {

                    //If rows were same
                    if(firstRowNum == secondRowNum) {

                        //For each square in row
                        for(int columnNum = 0; columnNum < SUDOKU_MAX_COLUMN_SQUARES; columnNum++) {
                            Square square = sudokuSquares[columnNum][firstRowNum];

                            //If square value is = 0 and has possible number
                            if(square.getValue() == 0 && square.hasPossibleValue(possNum) && !square.equals(sudokuSquares[firstColumnNum][firstRowNum]) && !square.equals(sudokuSquares[secondColumnNum][secondRowNum]) ) {
                                //Remove possible number
                                square.removePossibleValue(possNum);
                                System.out.println("Found Pointing Pair at [" + firstColumnNum + "][" + firstRowNum + "] [" + secondColumnNum + "][" + secondRowNum + "].  Removed " + possNum + " from [" + columnNum + "][" + firstRowNum + "]");
                                //Set found new pointing pair to true
                                foundNewPointingPair = true;
                            }
                        }
                    }

                    //If columns were same
                    if(firstColumnNum == secondColumnNum) {

                        //For each square in column
                        for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
                            Square square = sudokuSquares[firstColumnNum][rowNum];

                            //If square value is = 0 and has possible number
                            if(square.getValue() == 0 && square.hasPossibleValue(possNum) && !square.equals(sudokuSquares[firstColumnNum][firstRowNum]) && !square.equals(sudokuSquares[secondColumnNum][secondRowNum]) ) {
                                //Remove possible number
                                square.removePossibleValue(possNum);
                                System.out.println("Found Pointing Pair at [" + firstColumnNum + "][" + firstRowNum + "] [" + secondColumnNum + "][" + secondRowNum + "].  Removed " + possNum + " from [" + firstColumnNum + "][" + rowNum + "]");
                                //Set found new pointing pair to true
                                foundNewPointingPair = true;
                            }
                        }
                    }
                }


            }
        }

        return sudokuSquares;
    }

    /**
     * This method finds pointing pairs in rows and removes possible numbers in respect to it
     * @param sudokuSquares 2d array of squares
     * @return Returns true if possibilities were removed due to finding pointing pairs
     */
    public Square[][] findPointingPairsInRow(Square sudokuSquares[][]) {
        boolean foundNewPointingPair = false;

        //For each possible number
        for(int possNum = 1; possNum <= 9; possNum++) {

            //For each row
            for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
                int firstColumnNum = -1;
                int firstRowNum = -1;
                int secondColumnNum = -1;
                int secondRowNum = -1;
                int possNumsFound = 0;

                //For each square in a row
                for(int columnNum = 0; columnNum < SUDOKU_MAX_COLUMN_SQUARES; columnNum++) {
                    //Search remaining squares in block for a square that has one identical possible value

                    //If square value is 0 and has possible number
                    if(sudokuSquares[columnNum][rowNum].getValue() == 0 && sudokuSquares[columnNum][rowNum].hasPossibleValue(possNum)) {
                        //Add 1 to possible number count
                        possNumsFound++;

                        //Record rows and columns to use later if we found a pointing pair
                        if(possNumsFound == 1) {
                            firstColumnNum = columnNum;
                            firstRowNum = rowNum;
                        }
                        else if (possNumsFound == 2 ) {
                            secondColumnNum = columnNum;
                            secondRowNum = rowNum;
                        }
                    }
                }

                //If possible number count equals 2
                if(possNumsFound == 2) {
                    int square1BlockNum = getBlockNum(firstColumnNum, firstRowNum);
                    int square2BlockNum = getBlockNum(secondColumnNum, secondRowNum);

                    //If rows were same
                    if(square1BlockNum == square2BlockNum) {

                        ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(square1BlockNum);

                        //For each square in a block
                        for(SquareCoordinate squareCoord : squareNumbersList) {
                            Square square = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum];

                            //If square value is = 0 and has possible number
                            if(square.getValue() == 0 && square.hasPossibleValue(possNum) && !square.equals(sudokuSquares[firstColumnNum][firstRowNum]) && !square.equals(sudokuSquares[secondColumnNum][secondRowNum]) ) {
                                //Remove possible number
                                square.removePossibleValue(possNum);
                                System.out.println("Found Pointing Pair at [" + firstColumnNum + "][" + firstRowNum + "] [" + secondColumnNum + "][" + secondRowNum + "].  Removed " + possNum + " from [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "]");
                                //Set found new pointing pair to true
                                foundNewPointingPair = true;
                            }
                        }
                    }


                }


            }
        }

        return sudokuSquares;
    }
}
