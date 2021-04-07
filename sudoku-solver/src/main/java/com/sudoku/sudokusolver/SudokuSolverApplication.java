package com.sudoku.sudokusolver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Combines @SpringBootConfiguration, @EnableAutoConfiguration, @ComponentScan
@SpringBootApplication
public class SudokuSolverApplication {

    /**
     * This is the main function of the application
     * @param args None
     */
    public static void main(String args[]) {
        SpringApplication.run(SudokuSolverApplication.class, args);
        //SudokuSolver game = new SudokuSolver(true);
        //game.solveSudoku();
        //System.exit(0);

    }
}
