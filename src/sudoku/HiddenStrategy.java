package sudoku;

import java.util.ArrayList;

public class HiddenStrategy extends Strategy{

    /**
     * This method finds hidden singles in a sudoku
     * @param sudokuSquares  This is a 2D array of squares
     * @return boolean This method returns true if any possible number is removed due to finding a hidden single
     */
    public Square[][] findHiddenSingle(Square[][] sudokuSquares, Integer[] allPossValues) {

        //Go through each row and set hidden single candidates
        for(int i = 0;i < SUDOKU_MAX_ROW_SQUARES;i++) {
            //for every possible value check each row to see if there is only one square that has the value
            for(Integer possValue : allPossValues)
                sudokuSquares = setHiddenSingleInRow(possValue, i, sudokuSquares);
        }

        //Go through each column and set hidden candidates
        for(int i = 0;i < SUDOKU_MAX_COLUMN_SQUARES; i++) {
            //for every possible value check each column to see if there is only one square that has the value
            for(Integer possValue : allPossValues)
                sudokuSquares = setHiddenSingleInColumn(possValue, i, sudokuSquares);
        }

        //Go through each block and set hidden candidates
        for(int i = 0;i < SUDOKU_MAX_BLOCKS; i++) {
            //for every possible value check each block to see if there is only one square that has the value
            for(Integer possValue : allPossValues)
                sudokuSquares = setHiddenSingleInBlock(possValue, i, sudokuSquares);
        }

        return sudokuSquares;
    }

    /**
     * Sets values for squares where a hidden singles is found
     * @param possNum int This is the possible number to look for
     * @param rowNum This is the row number of the squares
     * @param sudokuSquares This is a 2D array of squares
     * @return boolean Returns true if a hidden single was found
     */
    public Square[][] setHiddenSingleInRow(int possNum, int rowNum, Square[][] sudokuSquares ) {

        int numPossibilities = 0;
        int tempX = 0;
        int tempY = 0;

        //Search every square for hidden single
        for(int i = 0; i < sudokuSquares.length; i++) {
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

        }


        return sudokuSquares;
    }

    /**
     * Go through every square and find if a square has only one possible value and set it
     * @param sudokuSquares  This is a 2D array of squares
     * @return boolean  This is if we found a new only choice square or not
     */
    public Square[][] findOnlyChoice(Square[][] sudokuSquares) {

        //Go through every square and if number is found, remove that possibility from squares in same row,
        //column, and 9 square section
        for(int columnNum = 0;columnNum < 9;columnNum++) {

            for(int rowNum = 0;rowNum < 9;rowNum++) {

                if(sudokuSquares[columnNum][rowNum].getValue() == 0) {
                    ArrayList<Integer> possNumsList = sudokuSquares[columnNum][rowNum].getPossibleValues();
                    for(Integer possNum : possNumsList) {


                        if(getNumOnlyChoiceInRow(possNum, rowNum, sudokuSquares) == 1) {
                            sudokuSquares[columnNum][rowNum].setValue(possNum);

                            //Remove possibilities for the number we just set
                            removePossibilitiesFromRow(columnNum, rowNum, sudokuSquares);
                            removePossibilitiesFromColumn(columnNum, rowNum, sudokuSquares);
                            removePossibilitiesFromBlock(columnNum, rowNum, sudokuSquares);
                            System.out.println("OnlyChoiceCandidate(row):  Setting value " + possNum + " for [" + columnNum + "][" + rowNum + "]");
                            break;
                        }

                        if(getNumOnlyChoiceInColumn(possNum, columnNum, sudokuSquares) == 1) {
                            sudokuSquares[columnNum][rowNum].setValue(possNum);

                            //Remove possibilities for the number we just set
                            removePossibilitiesFromRow(columnNum, rowNum, sudokuSquares);
                            removePossibilitiesFromColumn(columnNum, rowNum, sudokuSquares);
                            removePossibilitiesFromBlock(columnNum, rowNum, sudokuSquares);
                            System.out.println("OnlyChoiceCandidate(column):  Setting value " + possNum + " for [" + columnNum + "][" + rowNum + "]");
                            break;
                        }

                        if(getNumOnlyChoiceInBlock(possNum, rowNum, columnNum, sudokuSquares) == 1) {
                            sudokuSquares[columnNum][rowNum].setValue(possNum);

                            //Remove possibilities for the number we just set
                            removePossibilitiesFromRow(columnNum, rowNum, sudokuSquares);
                            removePossibilitiesFromColumn(columnNum, rowNum, sudokuSquares);
                            removePossibilitiesFromBlock(columnNum, rowNum, sudokuSquares);
                            System.out.println("OnlyChoiceCandidate(block):  Setting value " + possNum + " for [" + columnNum + "][" + rowNum + "]");
                            break;
                        }

                    }
                }
            }
        }

        return sudokuSquares;

    }

    /**
     * This sets any hidden singles for a possible number in a column
     * @param possNum int This is the possible number to look for
     * @param columnNum This is the column number of the squares
     * @param sudokuSquares This is a 2D array of squares
     * @return boolean This returns true if a hidden single was found in a column
     */
    public Square[][] setHiddenSingleInColumn(int possNum, int columnNum, Square[][] sudokuSquares) {
        int numPossibilities = 0;
        int tempX = 0;
        int tempY = 0;

        for(int i = 0; i < sudokuSquares.length; i++) {
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
            sudokuSquares = removePossibilitiesFromRow(tempX, tempY, sudokuSquares);
            sudokuSquares = removePossibilitiesFromColumn(tempX, tempY, sudokuSquares);
            sudokuSquares = removePossibilitiesFromBlock(tempX, tempY, sudokuSquares);

        }


        return sudokuSquares;
    }


    /**
     * This sets any hidden singles for a possible number in a block
     * @param possNum int This is the possible number to look for
     * @param blockNum This is the block number of the squares
     * @param sudokuSquares This is a 2D array of squares
     * @return boolean This returns true if a hidden single was found in a block
     */
    public Square[][] setHiddenSingleInBlock(int possNum, int blockNum, Square[][] sudokuSquares) {
        int numPossibilities = 0;
        int tempX = 0;
        int tempY = 0;

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
        }


        return sudokuSquares;
    }
}
