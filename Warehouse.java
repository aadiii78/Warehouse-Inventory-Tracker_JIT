import java.io.*;
import java.util.*;

public class Warehouse {
    private Map<String, Product> products = new HashMap<>();
    private List<StockObserver> observers = new ArrayList<>();
    private final String inventoryFile = "inventory.txt";

    public Warehouse() {
        try {
            loadInventoryFromFile();
        } catch (Exception e) {
            System.out.println(" Error initializing warehouse: " + e.getMessage());
        }
    }

    // Add observer
    public void addObserver(StockObserver observer) {
        if (observer == null) {
            System.out.println(" Cannot add null observer!");
            return;
        }
        observers.add(observer);
    }

    // Add product
    public synchronized void addProduct(String id, String name, int quantity, int threshold) {
        try {
            if (id == null || id.trim().isEmpty() || name == null || name.trim().isEmpty()) {
                System.out.println("Product ID and name cannot be empty!");
                return;
            }
            if (quantity < 0 || threshold < 0) {
                System.out.println(" Quantity and threshold must be non-negative!");
                return;
            }
            if (products.containsKey(id)) {
                System.out.println(" Product ID already exists!");
                return;
            }

            Product product = new Product(id, name, quantity, threshold);
            products.put(id, product);
            System.out.println(" Product added successfully with ID -> '" + id + "'");
            saveInventoryToFile();
        } catch (Exception e) {
            System.out.println(" Error while adding product: " + e.getMessage());
        }
    }

    // Receive shipment
    public synchronized void receiveShipment(String id, int quantity) {
        try {
            if (id == null || id.trim().isEmpty()) {
                System.out.println(" Product ID cannot be empty!");
                return;
            }
            if (quantity <= 0) {
                System.out.println(" Quantity must be greater than zero!");
                return;
            }

            Product p = products.get(id);
            if (p == null) {
                System.out.println(" Invalid Product ID!");
                return;
            }

            p.increaseStock(quantity);
            System.out.println(" Shipment received! New Stock: " + p.getProductQauntity());
            saveInventoryToFile();
        } catch (Exception e) {
            System.out.println(" Error while receiving shipment: " + e.getMessage());
        }
    }

    // Fulfill order
    public synchronized void fulfillOrder(String id, int quantity) {
        try {
            if (id == null || id.trim().isEmpty()) {
                System.out.println(" Product ID cannot be empty!");
                return;
            }
            if (quantity <= 0) {
                System.out.println(" Order quantity must be greater than zero!");
                return;
            }

            Product p = products.get(id);
            if (p == null) {
                System.out.println(" Invalid Product ID!");
                return;
            }

            boolean success = false;
            try {
                success = p.decreaseStock(quantity);
            } catch (Exception ex) {
                System.out.println(" Error updating stock: " + ex.getMessage());
            }

            if (success) {
                System.out.println(" Order fulfilled! Remaining stock: " + p.getProductQauntity());
                if (p.getProductQauntity() < p.getProductThreshold()) {
                    notifyObservers(p);
                }
            } else {
                System.out.println(" Not enough stock to fulfill the order.");
            }

            saveInventoryToFile();
        } catch (Exception e) {
            System.out.println(" Error while fulfilling order: " + e.getMessage());
        }
    }

    // Notify observers on low stock
    private void notifyObservers(Product product) {
        if (product == null) return;
        for (StockObserver o : observers) {
            try {
                o.onLowStock(product);
            } catch (Exception e) {
                System.out.println(" Error notifying observer: " + e.getMessage());
            }
        }
    }

    // Show all products
    public void showAllProducts() {
        try {
            if (products.isEmpty()) {
                System.out.println("ℹ No products available!");
                return;
            }
            System.out.println("\n Current Inventory:");
            for (Product p : products.values()) {
                if (p == null) continue;
                System.out.println(
                    "--------------------------------------\n" +
                    "ID: " + p.getProductId() + "\n" +
                    "Name: " + p.getProductName() + "\n" +
                    "Quantity: " + p.getProductQauntity() + "\n" +
                    "Threshold: " + p.getProductThreshold()
                );
            }
        } catch (Exception e) {
            System.out.println(" Error displaying products: " + e.getMessage());
        }
    }

    // Save inventory to file
    private void saveInventoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFile))) {
            for (Product p : products.values()) {
                if (p != null)
                    writer.write(p.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(" Error saving inventory: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error while saving: " + e.getMessage());
        }
    }

    // Load inventory from file
    private void loadInventoryFromFile() {
        File file = new File(inventoryFile);
        if (!file.exists()) {
            return; // No data file yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.out.println(" Skipping invalid line " + lineCount + ": " + line);
                    continue;
                }

                try {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int quantity = Integer.parseInt(parts[2].trim());
                    int threshold = Integer.parseInt(parts[3].trim());
                    if (id.isEmpty() || name.isEmpty()) {
                        System.out.println(" Skipping invalid product data at line " + lineCount);
                        continue;
                    }
                    Product product = new Product(id, name, quantity, threshold);
                    products.put(id, product);
                } catch (NumberFormatException e) {
                    System.out.println(" Invalid number format in line " + lineCount + ": " + line);
                } catch (Exception e) {
                    System.out.println(" Error loading product at line " + lineCount + ": " + e.getMessage());
                }
            }
            System.out.println(" Inventory loaded successfully from file.");
        } catch (FileNotFoundException e) {
            System.out.println(" Inventory file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(" Error reading inventory file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(" Unexpected error loading inventory: " + e.getMessage());
        }
    }
}
