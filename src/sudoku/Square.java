package sudoku;

import java.util.*;

/**
 * 
 * @author Ben Clark
 * This class is a sudoku square
 */
 
public class Square implements Comparable<Square>{

	private int value;
	private ArrayList<Integer> possibleValues = new ArrayList<>();
	private boolean isNakedPair;
	
	/**
	 * This is the default constructor
	 */
	public Square() {
		value = 0;
		isNakedPair = false;
	}
	
	/**
	 * This is a square constructor that will set a value
	 * @param num int This is the value of the square
	 */
	public Square(int num) {
		value = num;
	}
	
	/**
	 * This sets the value of the square
	 * @param num int This is the value of the square
	 */
	public void setValue(int num) {
		value = num;
	}
	
	/**
	 * This gets the value of the square
	 * @return int  This returns the value of the square
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * This removes a possible sudoku number for square
	 * @param num int This is the possible number to remove for the square
	 */
	public void removePossibleValue(Integer num) {
		possibleValues.remove(num);
		if(possibleValues.size() == 0 && this.getValue() == 0) 
			System.out.println("ERROR: There are no possible values");
	}
	
	/**
	 * This sets the possible numbers of a square
	 * @param nums Integer[]  This is the possible numbers of the square
	 */
	public void setPossibleValues(Integer[] nums) {
		for(Integer num : nums) {
			possibleValues.add(num);
		}
	}
	
	/**
	 * This gets the possible values of the square
	 * @return ArrayList<Integer>  This returns an array of length 9 that holds the possible values that the square could be (1-9)
	 */
	public ArrayList<Integer> getPossibleValues() {
		return possibleValues;
	}
	
	/**
	 * This checks to see if square has a possible value 
	 * @param value Value to check for
	 * @return boolean  This returns true if square has possible value 
	 */
	public boolean hasPossibleValue(Integer value) {
		ArrayList<Integer> possibleValuesList = getPossibleValues();
		for(Integer possValue : possibleValuesList) {
			if(possValue.equals(value))
				return true;
		}
		
		return false;
	}
	
	/**
	 * This sets a square's possible values
	 * @param possValues ArrayList<Integer> These are the possible values of the square
	 */
	public void setPossibleValues(ArrayList<Integer> possValues) {
		possibleValues = possValues;
	}
	
	/**
	 * This prints the possible values of the square
	 */
	public void printPossibleValues() {
		for(Integer possVal : this.possibleValues)
			System.out.print(possVal + " ");
	}
	
	/**
	 * This gets if the square is a naked pair or not
	 * @return boolean  This returns true if the square is a naked pair
	 */
	public boolean getIsNakedPair() {
		return isNakedPair;
	}
	
	/**
	 * This sets if the square is a naked pair or not
	 * @param nakedPair  This is if it's a naked pair or not
	 */
	public void setIsNakedPair(boolean nakedPair) {
		isNakedPair = nakedPair;
	}
	
	/**
	 * This returns if the square is a naked pair or not
	 * @return boolean This returns if the square is a naked pair
	 */
	public boolean isNakedPair() {
		return isNakedPair;
	}
	
	/**
	 * This will compare the square to another square. This returns -1 if the value is different or if the possible numbers are different
	 */
	public int compareTo(Square square) {
				
		//TODO separate this method into 2 methods.  One for compareValueTo and one to comparePossibleNumsTo
		if(this.getValue() != square.getValue()) {
			System.out.println(this.getValue() + " " + square.getValue());
			return -1;
		}
		
		if(this.value == 0)
		{
			ArrayList<Integer> possValueList1 = this.getPossibleValues();
			ArrayList<Integer> possValueList2 = square.getPossibleValues();
			
			if(possValueList1.size() != possValueList2.size()) {
				System.out.println("possValueSize different");
				return -1;
			}
			
		
			
			for(int k = 0;k < possValueList1.size();k++) {
				if(!possValueList1.get(k).equals(possValueList2.get(k))) {
					System.out.println("possValue different");
					return -1;
				}
			}
		}

		
		return 0;
		
	}
}
