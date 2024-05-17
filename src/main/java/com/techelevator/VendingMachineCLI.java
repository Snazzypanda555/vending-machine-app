package com.techelevator;

import java.util.Scanner;

public class VendingMachineCLI {
    public static void main(String[] args) {
        // Initialize the VendingMachine object and stock items
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.stockItems("vendingmachine.csv");

        // Create a scanner object for user input
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Main application loop
        while (running) {
            // Display main menu options
            System.out.println("Main Menu");
            System.out.println("1. Display Vending Machine Items");
            System.out.println("2. Purchase");
            System.out.println("3. Exit");
            System.out.print("Please choose an option: ");

            // Read user input
            String choice = scanner.nextLine();

            // Process user choice
            switch (choice) {
                case "1":
                    vendingMachine.displayItems();
                    break;
                case "2":
                    // Enter purchase menu
                    purchaseMenu(vendingMachine, scanner);
                    break;
                case "3":
                    // Exit the program
                    running = false;
                    break;
                case "4":
                    //Option for the "secret menu option", sales report
                    vendingMachine.report(vendingMachine.getItemsMap());
                    //once report is printed, return to purchase menu
                    purchaseMenu(vendingMachine, scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Method to display and handle the purchase menu
    private static void purchaseMenu(VendingMachine vendingMachine, Scanner scanner) {
        // Display purchase menu options
        // Display the current money provided

        while (true) {
            System.out.println("Purchase Menu");
            System.out.println("1. Feed Money");
            System.out.println("2. Select Product");
            System.out.println("3. Finish Transaction");
            System.out.print("Please choose an option: ");

            // Read user input
            String choice = scanner.nextLine();

            // Process user choice
            switch (choice) {
                case "1":
                    // Feed money option
                    System.out.print("Enter amount to feed: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    if (amount > 0){
                        vendingMachine.feedMoney(amount);
                        System.out.println("Current Money Provided: $" + String.format("%.2f", vendingMachine.getCurrentBalance()));
                        } else {
                        System.out.println("Invalid option. Please try again.");
                    }
                    break;
                case "2":
                    // Select product option
                    System.out.println("Available Products:");
                    // Display available products
                    vendingMachine.displayItems();
                    // Select product option
                    System.out.print("Enter product code: ");
                    // and turn it to uppercase
                    String productCode = scanner.nextLine().toUpperCase();
                    vendingMachine.purchaseItem(productCode,1);
                    break;
                case "3":
                    // Finish transaction option
                    vendingMachine.finishTransaction();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

}