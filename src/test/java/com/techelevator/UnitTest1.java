package com.techelevator;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.util.Scanner;


public class UnitTest1 {
	private static VendingMachineCLI machineObj;
	private static VendingMachine vendingMachine;
	private Scanner scanner;
	@Test
	public void createAndFulfillPurchase(){
		/* Will create a sample purchase.
		*  Amount that is fed is 10 dollars
		* will print current stock, and sales report */


		this.machineObj = new VendingMachineCLI();
		//String will store the steps for Vending Machine
		String inputSteps = "1\n2\n1\n10\n2\na1\n3\n4\n3\n3\n";
		// Create a ByteArrayInputStream, that will read the input steps String
		System.setIn(new ByteArrayInputStream(inputSteps.getBytes()));
		//will call main to display all the steps in console
		String[] args = {};
		machineObj.main(args);
	}



}
