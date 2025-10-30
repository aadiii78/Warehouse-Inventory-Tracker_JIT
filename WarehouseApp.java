import java.util.*;
import java.util.concurrent.*;

public class WarehouseApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Warehouse warehouse = new Warehouse();
        warehouse.addObserver(new AlertService());
        ExecutorService executor = Executors.newFixedThreadPool(3);

        while (true) {
            System.out.println("\n=== WAREHOUSE MENU ===");
            System.out.println("1. Add Product");
            System.out.println("2. Receive Shipment");
            System.out.println("3. Fulfill Order");
            System.out.println("4. Show All Products");
            System.out.println("5. Exit");

            int choice = readChoiceSafe(sc, 1, 5);

            switch (choice) {
                case 1:
                    addProduct(sc, warehouse);
                    break;

                case 2:
                    receiveShipment(sc, warehouse, executor);
                    break;

                case 3:
                    fulfillOrder(sc, warehouse, executor);
                    break;

                case 4:
                    warehouse.showAllProducts();
                    break;

                case 5:
                    System.out.println("Exiting Warehouse System...");
                    executor.shutdown();
                    sc.close();
                    System.exit(0);
                    break;
            }
        }
    }

    // === Helper for choice with range validation ===
    private static int readChoiceSafe(Scanner sc, int min, int max) {
        while (true) {
            System.out.print("Enter choice (" + min + "-" + max + "): ");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("❌ Choice cannot be empty!");
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                if (value < min || value > max) {
                    System.out.println("❌ Invalid choice! Please enter a number between " + min + " and " + max + ".");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input! Please enter a valid number.");
            }
        }
    }

    // === Add Product ===
    private static void addProduct(Scanner sc, Warehouse warehouse) {
        String id = readNonEmptyString(sc, "Enter ID: ");
        String name = readNonEmptyString(sc, "Enter Name: ");
        int qty = readPositiveIntSafe(sc, "Enter Quantity: ");
        int th = readPositiveIntSafe(sc, "Enter Threshold: ");
        warehouse.addProduct(id, name, qty, th);
    }

    // === Receive Shipment ===
    private static void receiveShipment(Scanner sc, Warehouse warehouse, ExecutorService executor) {
        try {
            String id = readNonEmptyString(sc, "Enter Product ID: ");
            int addQty = readPositiveIntSafe(sc, "Enter Shipment Quantity: ");
            executor.submit(() -> warehouse.receiveShipment(id, addQty)).get();
        } catch (Exception e) {
            System.out.println("⚠️ Error while receiving shipment: " + e.getMessage());
        }
    }

    // === Fulfill Order ===
    private static void fulfillOrder(Scanner sc, Warehouse warehouse, ExecutorService executor) {
        try {
            String id = readNonEmptyString(sc, "Enter Product ID: ");
            int orderQty = readPositiveIntSafe(sc, "Enter Order Quantity: ");
            executor.submit(() -> warehouse.fulfillOrder(id, orderQty)).get();
        } catch (Exception e) {
            System.out.println("⚠️ Error while fulfilling order: " + e.getMessage());
        }
    }

    // === Read non-empty strings safely ===
    private static String readNonEmptyString(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ Input cannot be empty!");
                continue;
            }
            return input;
        }
    }

    // === Read positive integers safely ===
    private static int readPositiveIntSafe(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("❌ Input cannot be empty!");
                continue;
            }

            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("❌ Please enter a positive number (0 or above).");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number! Please enter a valid integer.");
            }
        }
    }
}
