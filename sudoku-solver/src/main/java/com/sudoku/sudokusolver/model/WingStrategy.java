package com.sudoku.sudokusolver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WingStrategy extends Strategy {

    /**
     * Finds y-wing patterns and removes possible numbers
     * @param sudokuSquares Square[][]  This is a 2d array of squares
     * @return boolean  This method returns true if any possible number is removed due to finding a YWing
     */
    public Square[][] findYWingRowColumn(Square sudokuSquares[][]) {

        boolean foundYWing = false;

        //Go through each square
        for(int rowNum = 0;rowNum < SUDOKU_MAX_COLUMN_SQUARES; rowNum++) {
            for(int columnNum = 0;columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {

                //If square is naked double and doesn't have a value
                if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][rowNum].getValue() == 0) {

                    Map<Integer, Integer> possNumsMap = new HashMap<Integer,Integer>();
                    Integer pivotPossNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
                    Integer pivotPossNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);

                    possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(0), 1);
                    possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(1), 1);

                    //Search remaining squares in row for a square that has one identical possible value
                    for(int secondRemSquareColumnNum = 0; secondRemSquareColumnNum < SUDOKU_MAX_ROW_SQUARES; secondRemSquareColumnNum++) {

                        //If square we are checking doesn't have a value, has at least 2 possible values, has at least one common possible value, and isn't the same square
                        if(sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[secondRemSquareColumnNum][rowNum].getValue() == 0 &&
                                (pivotPossNum1 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(0) || pivotPossNum1 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(1) ||
                                        pivotPossNum2 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(0) || pivotPossNum2 == sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(1)) &&
                                secondRemSquareColumnNum != columnNum) {

                            ArrayList<Integer> nextPossibleNums = new ArrayList<Integer>();
                            Integer keyToRemove = 0;
                            Integer secondRemPossNum1 = sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(0);
                            Integer secondRemPossNum2 = sudokuSquares[secondRemSquareColumnNum][rowNum].getPossibleValues().get(1);

                            //Add new possible numbers to map and find the one that already exists so we can remove it
                            if(possNumsMap.containsKey(secondRemPossNum1))
                                keyToRemove = secondRemPossNum1;
                            else
                                possNumsMap.put(secondRemPossNum1, 1);

                            if(possNumsMap.containsKey(secondRemPossNum2))
                                keyToRemove = secondRemPossNum2;
                            else
                                possNumsMap.put(secondRemPossNum2, 1);

                            //Remove common possible number so we can find the remaining 3rd square possible numbers
                            if(keyToRemove == 0)
                                System.out.println("ERROR findYWingColumnNum: keyToRemove = 0.  This should equal 1-9");
                            else
                                possNumsMap.remove(keyToRemove);

                            //Add all possible numbers to ArrayList
                            for(Integer possNumKey : possNumsMap.keySet())
                                nextPossibleNums.add(possNumKey);

                            //If we found the same possible numbers break
                            if(nextPossibleNums.size() < 2)
                                break;

                            //Sort the possible numbers from least to greatest
                            if(nextPossibleNums.get(1) < nextPossibleNums.get(0)) {
                                Integer tempNum = nextPossibleNums.get(1);
                                nextPossibleNums.remove(1);
                                nextPossibleNums.add(0, tempNum);
                            }

                            //Search all squares in the column of the first square and this square
                            for(int thirdRemSquareRowNum = 0; thirdRemSquareRowNum < SUDOKU_MAX_COLUMN_SQUARES; thirdRemSquareRowNum++) {
                                if(sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 && sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2) {
                                    Integer thirdPossNum1 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(0);
                                    Integer thirdPossNum2 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(1);

                                    Integer commonPossNum = 0;


                                    //Set a variable to the common number we found and add uncommon numbers to a list to find the third square
                                    if(thirdPossNum1 == secondRemPossNum1) {
                                        commonPossNum = thirdPossNum1;
                                    }
                                    else if(thirdPossNum1 == secondRemPossNum2){
                                        commonPossNum = thirdPossNum1;
                                    }
                                    else if(thirdPossNum2 == secondRemPossNum1) {
                                        commonPossNum = thirdPossNum2;
                                    }
                                    else if(thirdPossNum2 == secondRemPossNum2) {
                                        commonPossNum = thirdPossNum2;
                                    }

                                    //If it has the remaining combination
                                    if(sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 &&
                                            thirdPossNum1 == nextPossibleNums.get(0) && thirdPossNum2 == nextPossibleNums.get(1) &&
                                            thirdRemSquareRowNum != rowNum) {
                                        if(sudokuSquares[secondRemSquareColumnNum][thirdRemSquareRowNum].hasPossibleValue(commonPossNum)) {
                                            sudokuSquares[secondRemSquareColumnNum][thirdRemSquareRowNum].removePossibleValue(commonPossNum);
                                            foundYWing = true;
                                            System.out.println("Found Y Wing at [" + columnNum + "][" + rowNum + "] [" + secondRemSquareColumnNum + "][" + rowNum + "] [" + columnNum + "][" + thirdRemSquareRowNum + "]" +
                                                    "removed value: " + commonPossNum + " from [" + secondRemSquareColumnNum + "][" + thirdRemSquareRowNum + "]");
                                        }


                                        //Remove the possibility that is common to the squares that are not the pivot
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return sudokuSquares;
    }


    /**
     * This finds all the YWings in a sudoku puzzle with 2 of the numbers being in the same block
     * @param sudokuSquares Square This is a 2D array of squares
     * @return boolean This method returns true if any possible number is removed due to finding a naked triple
     */
    //TODO Rewrite this.  It's a mess.
    public Square[][] findYWingBlock(Square sudokuSquares[][]) {

        boolean foundYWing = false;

        //Go through each square
        for(int rowNum = 0;rowNum < SUDOKU_MAX_COLUMN_SQUARES; rowNum++) {
            for(int columnNum = 0;columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {

                if(columnNum == 6 && rowNum == 2)
                    System.out.println("");
                //If square is naked double and doesn't have a value
                if(sudokuSquares[columnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][rowNum].getValue() == 0) {

                    int blockNum = 0;
                    Map<Integer, Integer> possNumsMap = new HashMap<Integer,Integer>();
                    Integer pivotPossNum1 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(0);
                    Integer pivotPossNum2 = sudokuSquares[columnNum][rowNum].getPossibleValues().get(1);

                    possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(0), 1);
                    possNumsMap.put(sudokuSquares[columnNum][rowNum].getPossibleValues().get(1), 1);

                    blockNum = getBlockNum(columnNum, rowNum);

                    ArrayList<SquareCoordinate> squareNumbersList = blockNumSquareNumbers.get(blockNum);

                    //Search remaining squares in block for a square that has one identical possible value
                    for(SquareCoordinate squareCoord : squareNumbersList) {

                        //If square we are checking doesn't have a value, has at least 2 possible values, has at least one common possible value, and isn't the same square
                        if(sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().size() == 2 && sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getValue() == 0 &&
                                (pivotPossNum1 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0) || pivotPossNum1 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1) ||
                                        pivotPossNum2 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0) || pivotPossNum2 == sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1)) &&
                                (squareCoord.rowNum != rowNum && squareCoord.columnNum != columnNum)) {

                            ArrayList<Integer> nextPossibleNums = new ArrayList<Integer>();
                            Integer keyToRemove = 0;
                            Integer secondRemPossNum1 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(0);
                            Integer secondRemPossNum2 = sudokuSquares[squareCoord.columnNum][squareCoord.rowNum].getPossibleValues().get(1);

                            //Add new possible numbers to map and find the one that already exists so we can remove it
                            if(possNumsMap.containsKey(secondRemPossNum1))
                                keyToRemove = secondRemPossNum1;
                            else
                                possNumsMap.put(secondRemPossNum1, 1);

                            if(possNumsMap.containsKey(secondRemPossNum2))
                                keyToRemove = secondRemPossNum2;
                            else
                                possNumsMap.put(secondRemPossNum2, 1);

                            //Remove common possible number so we can find the remaining 3rd square possible numbers
                            if(keyToRemove == 0)
                                System.out.println("ERROR findYWingBlock: keyToRemove = 0.  This should equal 1-9");
                            else
                                possNumsMap.remove(keyToRemove);

                            //Add all possible numbers to ArrayList
                            for(Integer possNumKey : possNumsMap.keySet())
                                nextPossibleNums.add(possNumKey);

                            //If we found the same possible numbers break
                            if(nextPossibleNums.size() < 2)
                                break;

                            //Sort the possible numbers from least to greatest
                            if(nextPossibleNums.get(1) < nextPossibleNums.get(0)) {
                                Integer tempNum = nextPossibleNums.get(1);
                                nextPossibleNums.remove(1);
                                nextPossibleNums.add(0, tempNum);
                            }

                            //Search all squares in the column of the first square and this square
                            for(int thirdRemSquareRowNum = 0; thirdRemSquareRowNum < SUDOKU_MAX_COLUMN_SQUARES; thirdRemSquareRowNum++) {
                                if(sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 && sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2) {
                                    Integer thirdPossNum1 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(0);
                                    Integer thirdPossNum2 = sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().get(1);

                                    Integer commonPossNum = 0;


                                    //Set a variable to the common number we found and add uncommon numbers to a list to find the third square
                                    if(thirdPossNum1 == secondRemPossNum1) {
                                        commonPossNum = thirdPossNum1;
                                    }
                                    else if(thirdPossNum1 == secondRemPossNum2){
                                        commonPossNum = thirdPossNum1;
                                    }
                                    else if(thirdPossNum2 == secondRemPossNum1) {
                                        commonPossNum = thirdPossNum2;
                                    }
                                    else if(thirdPossNum2 == secondRemPossNum2) {
                                        commonPossNum = thirdPossNum2;
                                    }

                                    //If it has the remaining combination
                                    if(sudokuSquares[columnNum][thirdRemSquareRowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][thirdRemSquareRowNum].getValue() == 0 &&
                                            thirdPossNum1 == nextPossibleNums.get(0) && thirdPossNum2 == nextPossibleNums.get(1) &&
                                            thirdRemSquareRowNum != rowNum) {

                                        int lastBlockNum = getBlockNum(columnNum, thirdRemSquareRowNum);

                                        ArrayList<SquareCoordinate> lastSquareNumbersList = blockNumSquareNumbers.get(lastBlockNum);

                                        for(SquareCoordinate lastSquareCoord : lastSquareNumbersList) {
                                            if(squareCoord.columnNum == lastSquareCoord.columnNum) {
                                                if(sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].hasPossibleValue(commonPossNum) &&
                                                        sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].getValue() == 0) {
                                                    sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].removePossibleValue(commonPossNum);
                                                    foundYWing = true;
                                                    System.out.println("Found Y Wing (1) at [" + columnNum + "][" + rowNum + "] [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "] [" + columnNum + "][" + thirdRemSquareRowNum + "]" +
                                                            "removed value: " + commonPossNum + " from [" + lastSquareCoord.columnNum + "][" + lastSquareCoord.rowNum + "]");
                                                }


                                                //Remove the possibility that is common to the squares that are not the pivot
                                            }
                                        }
                                    }
                                }
                            }

                            //Search all squares in the row of the first square and this square
                            for(int thirdRemSquareColumnNum = 0; thirdRemSquareColumnNum < SUDOKU_MAX_COLUMN_SQUARES; thirdRemSquareColumnNum++) {
                                if(sudokuSquares[thirdRemSquareColumnNum][rowNum].getValue() == 0 && sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().size() == 2) {
                                    Integer thirdPossNum1 = sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().get(0);
                                    Integer thirdPossNum2 = sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().get(1);

                                    Integer commonPossNum = 0;


                                    //Set a variable to the common number we found and add uncommon numbers to a list to find the third square
                                    if(thirdPossNum1 == secondRemPossNum1) {
                                        commonPossNum = thirdPossNum1;
                                    }
                                    else if(thirdPossNum1 == secondRemPossNum2){
                                        commonPossNum = thirdPossNum1;
                                    }
                                    else if(thirdPossNum2 == secondRemPossNum1) {
                                        commonPossNum = thirdPossNum2;
                                    }
                                    else if(thirdPossNum2 == secondRemPossNum2) {
                                        commonPossNum = thirdPossNum2;
                                    }

                                    //If it has the remaining combination
                                    if(sudokuSquares[thirdRemSquareColumnNum][rowNum].getPossibleValues().size() == 2 && sudokuSquares[columnNum][thirdRemSquareColumnNum].getValue() == 0 &&
                                            thirdPossNum1 == nextPossibleNums.get(0) && thirdPossNum2 == nextPossibleNums.get(1) &&
                                            thirdRemSquareColumnNum != rowNum) {
                                        if(sudokuSquares[thirdRemSquareColumnNum][squareCoord.rowNum].hasPossibleValue(commonPossNum)) {
                                            sudokuSquares[thirdRemSquareColumnNum][squareCoord.rowNum].removePossibleValue(commonPossNum);
                                            System.out.println("YWing [" + thirdRemSquareColumnNum + "][" + squareCoord.rowNum + "]: removed " + commonPossNum);
                                            int lastBlockNum = getBlockNum(thirdRemSquareColumnNum, rowNum);

                                            ArrayList<SquareCoordinate> lastSquareNumbersList = blockNumSquareNumbers.get(lastBlockNum);

                                            for(SquareCoordinate lastSquareCoord : lastSquareNumbersList) {
                                                if(squareCoord.rowNum == lastSquareCoord.rowNum) {
                                                    if(sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].hasPossibleValue(commonPossNum) &&
                                                            sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].getValue() == 0) {
                                                        sudokuSquares[lastSquareCoord.columnNum][lastSquareCoord.rowNum].removePossibleValue(commonPossNum);
                                                        foundYWing = true;
                                                        System.out.println("Found Y Wing (1) at [" + columnNum + "][" + rowNum + "] [" + squareCoord.columnNum + "][" + squareCoord.rowNum + "] [" + columnNum + "][" + thirdRemSquareColumnNum + "]" +
                                                                "removed value: " + commonPossNum + " from [" + lastSquareCoord.columnNum + "][" + lastSquareCoord.rowNum + "]");
                                                    }


                                                    //Remove the possibility that is common to the squares that are not the pivot
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //TODO Search all Column squares
                        }
                    }
                }
            }
        }
        return sudokuSquares;

    }

    /**
     * Finds an XWing and removes possible numbers appropriately
     * @param sudokuSquares 2D array of sudoku squares
     * @return This returns true if possible numbers were removed due to finding an XWing for the first time
     */
    public Square[][] findXWingInRows(Square sudokuSquares[][]) {
        boolean foundXWing = false;

        // For every possible square value
        for(int possSquareValue = 1; possSquareValue < SUDOKU_MAX_POSS_NUMS+1; possSquareValue++) {

            // For every column except last
            for(int row1Num = 0;row1Num < SUDOKU_MAX_COLUMN_SQUARES; row1Num++) {
                int row1PossNumColumnNums = 0;
                int row2PossNumColumnNums = 0;

                row1PossNumColumnNums = findPossibleNumInRow(possSquareValue, row1Num, sudokuSquares);

                //If possible value count equals 2
                if(row1PossNumColumnNums == 2) {
                    //For every column remaining
                    for(int row2Num = row1Num+1; row2Num < SUDOKU_MAX_COLUMN_SQUARES; row2Num++) {
                        row2PossNumColumnNums = findPossibleNumInRow(possSquareValue, row2Num, sudokuSquares);

                        if(row2PossNumColumnNums == 2  && xWingExistsInRows(possSquareValue, row1Num, row2Num, sudokuSquares))
                            removeXWingPossNumsInColumns(possSquareValue, row1Num, row2Num, sudokuSquares);

                    }

                }
            }
        }



        return sudokuSquares;
    }

    /**
     * This method removes possible numbers in rows from an XWing found.  Calling this requires that the XWing was already found to be true.
     * @param possSquareValue Possible square value from xwing
     * @param rowNum Column number to search
     * @param sudokuSquares 2D array of sudoku squares
     * @return This will return the number of possible values found in the column
     */
    private int findPossibleNumInRow(int possSquareValue, int rowNum, Square sudokuSquares[][]) {
        int possNumsColumnNums = 0;

        for(int columnNum = 0; columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {
            if(sudokuSquares[columnNum][rowNum].getValue() == 0 && sudokuSquares[columnNum][rowNum].hasPossibleValue(possSquareValue))
                possNumsColumnNums++;
        }

        return possNumsColumnNums;
    }

    /**
     * This method returns if there is an XWing
     * @param possSquareValue Possible square value from xwing
     * @param row1Num Column 1 of xwing
     * @param row2Num Column 2 of xwing
     * @param sudokuSquares 2D array of sudoku squares
     * @return This will return true if there was an XWing found for a possible number
     */
    private boolean xWingExistsInRows(int possSquareValue, int row1Num, int row2Num, Square sudokuSquares[][]) {
        int row1FirstColumn = -1;
        int row1SecondColumn = -1;
        int row2FirstColumn = -1;
        int row2SecondColumn = -1;
        boolean xWingExists = false;

        for(int columnNum = 0; columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {
            //First which rows possible values are in first column
            if(sudokuSquares[columnNum][row1Num].getValue() == 0 && sudokuSquares[columnNum][row1Num].hasPossibleValue(possSquareValue) && row1FirstColumn < 0)
                row1FirstColumn = columnNum;
            else if(sudokuSquares[columnNum][row1Num].getValue() == 0 && sudokuSquares[columnNum][row1Num].hasPossibleValue(possSquareValue) && row1SecondColumn < 0)
                row1SecondColumn = columnNum;

            //First which rows possible values are in second column
            if(sudokuSquares[columnNum][row2Num].getValue() == 0 && sudokuSquares[columnNum][row2Num].hasPossibleValue(possSquareValue) && row2FirstColumn < 0)
                row2FirstColumn = columnNum;
            else if(sudokuSquares[columnNum][row2Num].hasPossibleValue(possSquareValue) && row2SecondColumn < 0)
                row2SecondColumn = columnNum;
        }

        if( row1FirstColumn < 0 || row1SecondColumn < 0 || row2FirstColumn < 0 || row2SecondColumn < 0)
            System.out.println("Error xWingExists(): Did not find all 4 possible numbers for XWing.  XWing is not possible");
        else if( row1FirstColumn == row2FirstColumn && row1SecondColumn == row2SecondColumn)
            xWingExists = true;
        else
            xWingExists = false;

        return xWingExists;
    }

    /**
     * This method removes possible numbers in columns from an XWing found.  Calling this requires that the XWing was already found to be true.
     * @param possSquareValue Possible square value from xwing
     * @param row1Num Column 1 of xwing
     * @param row2Num Column 2 of xwing
     * @param sudokuSquares 2D array of sudoku squares
     * @return This will return true if we removed any possible values from the columns for the XWing
     */
    private boolean removeXWingPossNumsInColumns(int possSquareValue, int row1Num, int row2Num, Square sudokuSquares[][]) {
        int firstColumn = -1;
        int secondColumn = -1;
        boolean removedPossNums = false;

        for(int columnNum = 0; columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {
            //First which rows possible values are in first column
            if(sudokuSquares[columnNum][row1Num].getValue() == 0 && sudokuSquares[columnNum][row1Num].hasPossibleValue(possSquareValue) && firstColumn < 0)
                firstColumn = columnNum;
            else if(sudokuSquares[columnNum][row1Num].getValue() == 0 && sudokuSquares[columnNum][row1Num].hasPossibleValue(possSquareValue) && secondColumn < 0)
                secondColumn = columnNum;
        }

        //If we don't have both rows something is wrong and we need to quit
        if(firstColumn < 0 || secondColumn < 0)
            return false;

        //Go through each square on the two rows and remove the possible numbers from XWing
        for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {

            //If we aren't on an XWing square
            if(rowNum != row1Num && rowNum != row2Num) {
                if(sudokuSquares[firstColumn][rowNum].getValue() == 0 && sudokuSquares[firstColumn][rowNum].hasPossibleValue(possSquareValue)) {
                    System.out.println("XWingInRows: Removing possible number " + possSquareValue + " from [" + firstColumn + "][" + rowNum + "]");
                    sudokuSquares[firstColumn][rowNum].removePossibleValue(possSquareValue);
                    removedPossNums = true;
                }

                if(sudokuSquares[secondColumn][rowNum].getValue() == 0 && sudokuSquares[secondColumn][rowNum].hasPossibleValue(possSquareValue)) {
                    System.out.println("XWingInRows: Removing possible number " + possSquareValue + " from [" + secondColumn + "][" + rowNum + "]");
                    sudokuSquares[secondColumn][rowNum].removePossibleValue(possSquareValue);
                    removedPossNums = true;
                    System.out.println();
                }
            }

        }

        return removedPossNums;
    }

    /**
     * Finds an XWing and removes possible numbers appropriately
     * @param sudokuSquares 2D array of sudoku squares
     * @return This returns true if possible numbers were removed due to finding an XWing for the first time
     */
    public Square[][] findXWingInColumns(Square sudokuSquares[][]) {
        boolean foundXWing = false;

        // For every possible square value
        for(int possSquareValue = 1; possSquareValue < SUDOKU_MAX_POSS_NUMS+1; possSquareValue++) {

            // For every column except last
            for(int column1Num = 0;column1Num < SUDOKU_MAX_COLUMN_SQUARES; column1Num++) {
                int row1PossNumColumnNums = 0;
                int row2PossNumColumnNums = 0;

                row1PossNumColumnNums = findPossibleNumInColumn(possSquareValue, column1Num, sudokuSquares);

                //If possible value count equals 2
                if(row1PossNumColumnNums == 2) {
                    //For every column remaining
                    for(int column2Num = column1Num+1; column2Num < SUDOKU_MAX_COLUMN_SQUARES; column2Num++) {
                        row2PossNumColumnNums = findPossibleNumInColumn(possSquareValue, column2Num, sudokuSquares);

                        if(row2PossNumColumnNums == 2  && xWingExistsInColumn(possSquareValue, column1Num, column2Num, sudokuSquares))
                            removeXWingPossNumsInRows(possSquareValue, column1Num, column2Num, sudokuSquares);

                    }

                }
            }
        }


        return sudokuSquares;
    }

    /**
     * This method removes possible numbers in rows from an XWing found.  Calling this requires that the XWing was already found to be true.
     * @param possSquareValue Possible square value from xwing
     * @param columnNum Column number to search
     * @param sudokuSquares 2D array of sudoku squares
     * @return This will return the number of possible values found in the column
     */
    private int findPossibleNumInColumn(int possSquareValue, int columnNum, Square sudokuSquares[][]) {
        int possNumsRowNumsList = 0;

        for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
            if(sudokuSquares[columnNum][rowNum].getValue() == 0 && sudokuSquares[columnNum][rowNum].hasPossibleValue(possSquareValue))
                possNumsRowNumsList++;
        }

        return possNumsRowNumsList;
    }

    /**
     * This method returns if there is an XWing
     * @param possSquareValue Possible square value from xwing
     * @param column1Num Column 1 of xwing
     * @param column2Num Column 2 of xwing
     * @param sudokuSquares 2D array of sudoku squares
     * @return This will return true if there was an XWing found for a possible number
     */
    private boolean xWingExistsInColumn(int possSquareValue, int column1Num, int column2Num, Square sudokuSquares[][]) {
        int column1FirstRow = -1;
        int column1SecondRow = -1;
        int column2FirstRow = -1;
        int column2SecondRow = -1;
        boolean xWingExists = false;

        for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
            //First which rows possible values are in first column
            if(sudokuSquares[column1Num][rowNum].getValue() == 0 && sudokuSquares[column1Num][rowNum].hasPossibleValue(possSquareValue) && column1FirstRow < 0)
                column1FirstRow = rowNum;
            else if(sudokuSquares[column1Num][rowNum].hasPossibleValue(possSquareValue) && column1SecondRow < 0)
                column1SecondRow = rowNum;

            //First which rows possible values are in second column
            if(sudokuSquares[column2Num][rowNum].getValue() == 0 && sudokuSquares[column2Num][rowNum].hasPossibleValue(possSquareValue) && column2FirstRow < 0)
                column2FirstRow = rowNum;
            else if(sudokuSquares[column2Num][rowNum].getValue() == 0 && sudokuSquares[column2Num][rowNum].hasPossibleValue(possSquareValue) && column2SecondRow < 0)
                column2SecondRow = rowNum;
        }

        if( column1FirstRow < 0 || column1SecondRow < 0 || column2FirstRow < 0 || column2SecondRow < 0)
            System.out.println("Error xWingExists(): Did not find all 4 possible numbers for XWing.  XWing is not possible");
        else if( column1FirstRow == column2FirstRow && column1SecondRow == column2SecondRow)
            xWingExists = true;
        else
            xWingExists = false;

        return xWingExists;
    }

    /**
     * This method removes possible numbers in rows from an XWing found.  Calling this requires that the XWing was already found to be true.
     * @param possSquareValue Possible square value from xwing
     * @param column1Num Column 1 of xwing
     * @param column2Num Column 2 of xwing
     * @param sudokuSquares 2D array of sudoku squares
     * @return This will return true if we removed any possible values from the rows for the XWing
     */
    private boolean removeXWingPossNumsInRows(int possSquareValue, int column1Num, int column2Num, Square sudokuSquares[][]) {
        int firstRow = -1;
        int secondRow = -1;
        boolean removedPossNums = false;

        for(int rowNum = 0; rowNum < SUDOKU_MAX_ROW_SQUARES; rowNum++) {
            //First which rows possible values are in first column
            if(sudokuSquares[column1Num][rowNum].getValue() == 0 && sudokuSquares[column1Num][rowNum].hasPossibleValue(possSquareValue) && firstRow < 0)
                firstRow = rowNum;
            else if(sudokuSquares[column1Num][rowNum].getValue() == 0 && sudokuSquares[column1Num][rowNum].hasPossibleValue(possSquareValue) && secondRow < 0)
                secondRow = rowNum;
        }

        //If we don't have both rows something is wrong and we need to quit
        if(firstRow < 0 || secondRow < 0)
            return false;

        //Go through each square on the two rows and remove the possible numbers from XWing
        for(int columnNum = 0; columnNum < SUDOKU_MAX_ROW_SQUARES; columnNum++) {

            //If we aren't on an XWing square
            if(columnNum != column1Num && columnNum != column2Num) {
                if(sudokuSquares[columnNum][firstRow].getValue() == 0 && sudokuSquares[columnNum][firstRow].hasPossibleValue(possSquareValue)) {
                    sudokuSquares[columnNum][firstRow].removePossibleValue(possSquareValue);
                    removedPossNums = true;
                    System.out.println("XWingInColumns: Remove possible number " + possSquareValue + " from [" + columnNum + "][" + firstRow + "]");
                }

                if(sudokuSquares[columnNum][secondRow].getValue() == 0 && sudokuSquares[columnNum][secondRow].hasPossibleValue(possSquareValue)) {
                    sudokuSquares[columnNum][secondRow].removePossibleValue(possSquareValue);
                    removedPossNums = true;
                    System.out.println("XWingInColumns: Remove possible number " + possSquareValue + " from [" + columnNum + "][" + secondRow + "]");
                }
            }

        }

        return removedPossNums;
    }
}
