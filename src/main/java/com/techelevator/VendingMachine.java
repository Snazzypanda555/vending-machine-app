package com.techelevator;

import com.techelevator.Item;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

// VendingMachine class responsible for managing vending machine operations
class VendingMachine {

    //Logging object


    // Map to store items in the vending machine, keyed by slot location
    private Map<String, Item> items;
    // Current balance in the vending machine
    private double currentBalance;

    // Constructor to initialize the vending machine
    public VendingMachine() {
        // Initialize the map and current balance
        items = new HashMap<>();
        currentBalance = 0;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public Map<String, Item> getItemsMap() {
        return new HashMap<>(items);
    }

    // Method to stock items in the vending machine from a file
    public void stockItems(String filename) {

        try (Scanner scanner = new Scanner(new File(filename))) {
            // Loop through each line in the file
            while (scanner.hasNextLine()) {
                // Split each line by the pipe (|) delimiter
                String[] parts = scanner.nextLine().split("\\|");
                // Extract slot location, product name, price, and type from the line
                String slotLocation = parts[0];
                String productName = parts[1];
                double price = Double.parseDouble(parts[2]);
                String type = parts[3];
                // Create and add a new Item object to the map
                items.put(slotLocation, new Item(slotLocation, productName, price, type));
            }
        } catch (FileNotFoundException e) {
            // Print stack trace if file not found
            e.printStackTrace();
        }
    }

    // Method to display the available items in the vending machine

    public void displayItems() {
    // Create a list to store items sorted by slot location
    List<Item> sortedItems = new ArrayList<>(items.values());

    // Sort the items by slot location
    sortedItems.sort(Comparator.comparing(Item::getSlotLocation));

    // Loop through each item in the sorted list
    for (Item item : sortedItems) {
        // Check if the item is sold out
        if (item.getQuantity() == 0) {
            System.out.println(item.getSlotLocation() + " " + item.getProductName() + " $" + item.getPrice() + " SOLD OUT");
        } else {
            // Calculate the remaining quantity and display the item
            int remainingQuantity = item.getQuantity();
            System.out.println(item.getSlotLocation() + " " + item.getProductName() + " $" + item.getPrice() + " (" + remainingQuantity + " remaining)");
        }
    }
}

    // Method to handle purchasing items from the vending machine
    public boolean purchaseItem(String slotLocation, int quantity) {
        // Get the item corresponding to the provided slot location
        Item item = items.get(slotLocation);
        // Check if item exists
        //update after capstone, throw exceptions here
        if (item == null) {
            // Print error message if item not found
            System.out.println("Invalid product code!");
            return false;
        }
        // Check if customer has enough balance to purchase the item
        if (item.getPrice() * quantity > currentBalance) {
            // Print error message if insufficient funds
            System.out.println("Insufficient funds!");
            return false;
        }
        // Check if there is enough quantity of the item in stock
        if (item.getQuantity() < quantity) {
            // Print error message if item is sold out
            System.out.println("Product is sold out!");
            return false;
        }

        // Dispense the item, update balance, and log the purchase
        item.saleItem();
        updateBalance(-item.getPrice() * quantity);
        //jorge update to logger
        updateLogFile(item.getProductName(), (item.getPrice() * quantity), currentBalance);


        switch (item.getType()) {
            case "Chip":
                System.out.println("Crunch Crunch, Yum!");
                break;
            case "Candy":
                System.out.println("Munch Munch, Yum!");
                break;
            case "Drink":
                System.out.println("Glug Glug, Yum!");
                break;
            case "Gum":
                System.out.println("Chew Chew, Yum!");
                break;
            default:
                System.out.println("Enjoy your purchase!");
        }

        return true;
    }


    // Method to add money to the current balance
    public void feedMoney(double amount) {
        // Add the provided amount to the current balance
        currentBalance += amount;
        // Log the money fed into the vending machine
        //jorge update to logger 4/13
        updateLogFile("FEED MONEY", amount, currentBalance);
    }

    // Method to finish the transaction and return change to the customer
    public void finishTransaction() {
        // Log giving change and give change to the customer
        //jorge update to logger 4/13
        updateLogFile("GIVE CHANGE", currentBalance,currentBalance);
        giveChange();
        // Reset the current balance to zero
        currentBalance = 0;
    }

    // Private method to calculate and return change to the customer
    private void giveChange() {
        // Convert current balance to cents
        int cents = (int) (currentBalance * 100);
        // Calculate the number of quarters
        int quarters = cents / 25;
        cents %= 25;
        // Calculate the number of dimes
        int dimes = cents / 10;
        cents %= 10;
        // Calculate the number of nickels
        int nickels = cents / 5;

        // Print the change returned
        System.out.println("Change returned: " + quarters + " quarters, " + dimes + " dimes, " + nickels + " nickels");
    }

    // Private method to update the current balance
    private void updateBalance(double amount) {
        // Update the current balance by adding the provided amount
        currentBalance += amount;
    }
    //*----------------------------------------------------------------------------------------------
    /**
     * @Author Jorge
     * @Description Will log all transactions to prevent theft from the vending machine.
     * Note: I created a logs folder, and a 'log.txt' file within that folder to simplify things.
     **/
    private static void updateLogFile(String productName, double amount, Double currentBalance) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("logs/log.txt", true))) {
            //adding the 'a' at end will get AM/PM
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            String timestamp = formatter.format(new Date());
            //jorge updated logger code 4/13
            //create formatted string -22s and -20 are blank spaces to separate timestamp & product, move to far left (looks clean)
            String newLogLine = String.format("%-22s %-20s : $%6.2f     $%6.2f", timestamp, productName, amount, currentBalance);
            writer.println(newLogLine);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //*----------------------------------------------------------------------------------------------
    //Sales report
    public void report(Map<String, Item> itemMap) {

        if (itemMap == null || itemMap.isEmpty()) {
            System.out.println("**TOTAL SALES** $0");
            return;
        }
        //create item object
        Item itemObj = new Item();
        //create variable "maxQuantity to get current stock of items
        Integer maxQuantity = itemObj.getQuantity();
        Double balance = Double.valueOf(0);
        //get entry set from "itemMap parameter"
        for (Map.Entry<String, Item> map : itemMap.entrySet()) {
            Item item = map.getValue();
            //subtract the maxQuantity allowed from the current Quantity to get the amount of items sold
            Integer quantityOfSold = maxQuantity - item.getQuantity();
            System.out.println(String.format("%-20s | %d", item.getProductName(), quantityOfSold));
            balance += quantityOfSold * item.getPrice();
        }
        //finally, prints the total balance at the end of the report.
        System.out.println(String.format("\n**TOTAL SALES**  $%.2f", balance));

    }


}

