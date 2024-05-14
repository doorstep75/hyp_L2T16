import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Declare specialInstructions variable
        String specialInstructions;

        // Getting customer details
        System.out.println("Enter customer name:");
        String customerName = scanner.nextLine();

        // modified these to be integers but to use the method (line 110) to handle if non-integer entered
        System.out.println("Enter your order number:");
        int orderNumber = readIntInput(scanner);
        scanner.nextLine();

        // same as above and converted this to an integer value expected
        System.out.println("Enter customer contact number:");
        int contactNumber = readIntInput(scanner);
        scanner.nextLine();

        System.out.println("Enter customer address:");
        String customerAddress = scanner.nextLine();

        System.out.println("Enter customer city location:");
        String customerLocation = scanner.nextLine();

        System.out.println("Enter customer email:");
        String email = scanner.nextLine();

        // Creating customer object
        Customer customer = new Customer(customerName, orderNumber, contactNumber,
                customerAddress, customerLocation, email);

        // Getting restaurant details
        System.out.println("Enter restaurant name:");
        String restaurantName = scanner.nextLine();

        System.out.println("Enter restaurant location:");
        String restaurantLocation = scanner.nextLine();

        System.out.println("Enter restaurant contact number:");
        String restContactNumber = scanner.nextLine();

        System.out.println("Enter number of meal types to order:");
        int mealTypesNumber = readIntInput(scanner);

        // loop for entering meal details
        StringBuilder mealsDetailsBuilder = new StringBuilder();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (int i = 1; i <= mealTypesNumber; i++) {
            System.out.println("Enter quantity of meals of type " + i + " to order:");
            int quantity = readIntInput(scanner);

            scanner.nextLine();

            System.out.println("Enter the name of meal type " + i + ":");
            String mealType = scanner.nextLine();

            // added a try catch to ensure valid input used and to avoid program crash
            // loops whilst there's a false valid input
            BigDecimal costOfMeal = BigDecimal.ZERO;
            boolean validInput = false;
            while (!validInput) {
                try {
                    System.out.println("Enter the cost of meal type " + i + " per item:");
                    costOfMeal = scanner.nextBigDecimal();
                    scanner.nextLine(); // consume the newline character
                    validInput = true; // mark input as valid
                } catch (Exception e) {
                    System.out.println("Invalid input. Please use numbers and decimals only");
                    scanner.nextLine(); // consume the invalid input
                    // Loop continues until valid input is provided
                }
            }


            totalAmount = totalAmount.add(costOfMeal.multiply(BigDecimal.valueOf(quantity)));

            mealsDetailsBuilder.append(quantity).append(" x ").append(mealType).append(" (R").append(costOfMeal).append(")\n");
        }

        // Getting special instructions
        System.out.println("Enter special instructions:");
        specialInstructions = scanner.nextLine();

        // Creating restaurant object
        Restaurant restaurant = new Restaurant(restaurantName, restaurantLocation, restContactNumber, mealsDetailsBuilder.toString(), totalAmount);

        // Closing scanner
        scanner.close();

        // Reading driver details from file
        List<Driver> drivers = readDriversFromFile();

        if (drivers.isEmpty()) {
            System.out.println("No drivers available. Exiting...");
            return;
        }

        // Finding a suitable driver
        Driver selectedDriver = findSuitableDriver(drivers, restaurantLocation);
        if (selectedDriver == null) {
            System.out.println("Sorry! Our drivers are too far away from your location.");
            return;
        }

        // Generating invoice
        String invoiceContent = generateInvoice(customer, restaurant, selectedDriver, specialInstructions);

        // Writing invoice to file
        writeInvoiceToFile(invoiceContent);
    }

    // Method to handle integer input
    private static int readIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Please only use numbers here");
                scanner.nextLine(); // Clear the input buffer
            }
        }
    }

    // Method to find a suitable driver
    private static Driver findSuitableDriver(List<Driver> drivers, String restaurantLocation) {
        Driver selectedDriver = null;
        int minLoad = Integer.MAX_VALUE;

        // Loop through each driver in the list of drivers
        // checking if driver's load is less than current minimum load
        for (Driver driver : drivers) {
            if (driver.getLocation().equalsIgnoreCase(restaurantLocation) && driver.getLoad() < minLoad) {
                // if both conditions are met then update the selected driver and the minimum load
                selectedDriver = driver;
                minLoad = driver.getLoad();
            }
        }
        // return the selected driver, of null if no suitable one is found
        return selectedDriver;
    }

    // Method to generate invoice
    private static String generateInvoice(Customer customer, Restaurant restaurant, Driver driver, String specialInstructions) {
        StringBuilder invoiceBuilder = new StringBuilder();
        invoiceBuilder.append("Order number: ").append(customer.getOrderNumber()).append("\n");
        invoiceBuilder.append("Customer: ").append(customer.getName()).append("\n");
        invoiceBuilder.append("Email: ").append(customer.getEmail()).append("\n");
        invoiceBuilder.append("Phone number: ").append(customer.getContactNumber()).append("\n");
        invoiceBuilder.append("Location: ").append(customer.getLocation()).append("\n\n");

        invoiceBuilder.append("You have ordered the following from ").append(restaurant.getName())
                .append(" in ").append(customer.getLocation()).append(":\n\n");

        // Splitting meals details
        String[] meals = restaurant.getMealsDetails().split("\n");
        // start of the loop
        for (int i = 0; i < meals.length; i++) {
            String meal = meals[i].trim();
            if (!meal.isEmpty()) {
                invoiceBuilder.append(meal);
                // Add newline character if it's not the last non-empty meal
                if (i < meals.length - 1 && !meals[i + 1].trim().isEmpty()) {
                    invoiceBuilder.append("\n");
                }
            }
        }

        // Add special instructions directly after meals
        invoiceBuilder.append("\n\nSpecial instructions: ").append(specialInstructions).append("\n\n");
        // build the rest of the invoice
        invoiceBuilder.append("Total: R").append(String.format("%.2f", restaurant.getTotalAmount())).append("\n\n");
        invoiceBuilder.append(driver.getName()).append(" is the nearest to the restaurant and will be delivering your order to you at:\n\n")
                .append(customer.getAddress()).append("\n")
                .append(customer.getLocation()).append("\n\n")
                .append("If you need to contact the restaurant, their number is ").append(restaurant.getContactNumber()).append(".\n");
        return invoiceBuilder.toString();
    }

    /* Method to write invoice to file (updated to use 'try-with-resources' that will automatically close
        the FileWriter when I've researched */
    private static void writeInvoiceToFile(String content) {
        String fileName = "invoice.txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            System.out.println("Invoice has been successfully written to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the invoice: " + e.getMessage());
        }
    }

    // Method to read driver details from file
    private static List<Driver> readDriversFromFile() {
        List<Driver> drivers = new ArrayList<>();
        String fileName = "drivers.txt";
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(", ");
                if (parts.length == 3) {
                    String name = parts[0];
                    String location = parts[1];
                    int load = Integer.parseInt(parts[2]);
                    drivers.add(new Driver(name, location, load));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        return drivers;
    }
}
