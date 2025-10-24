import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Warehouse {
    private Map<String,Product> products = new HashMap<>();
    private List<StockObserver> observers = new ArrayList<>();
    private final String inventoryFile = "inventory.txt";

    public Warehouse()
    {
        loadInventoryFromFile();
    }
    // add observer
    public void addObserver(StockObserver observer)
    {
        observers.add(observer);
    }

    //add product 
    public synchronized void addProduct(String id,String name, int quantity,int threshold)
    {
        if(products.containsKey(id))
        {
            System.out.println("Product ID already exists!");
            return;
        }
        
        Product product = new Product(id, name, quantity, threshold);
        products.put(id, product);
        System.out.println("Adding product with ID -> '" + id + "'");
        saveInventoryToFile();
    }
    // recieve shipment
    public synchronized void receiveShipment (String id , int quantity) {
        Product p= products.get(id);
        if(p== null)
        {
            System.out.println("Invalid Product ID!");
            return;
        }
        p.increaseStock(quantity);
        System.out.println("shipment received! New Stock "+ p.getProductQauntity());
        saveInventoryToFile();
    }
     // order 
     public synchronized void fulfillOrder(String id,int quantity)
     {
        Product p = products.get(id);
        if(p == null)
        {
            System.out.println("Invalid Product ID!");
            return;
        }
        if(p.decreaseStock(quantity))
        {
            System.out.println("order fulfilled! Remaining stock: " + p.getProductQauntity());
            if(p.getProductQauntity()<p.getProductThreshold())
            {
                notifyObservers(p);
            }
        }
        saveInventoryToFile();
     }

     // notification on low stock
      private void notifyObservers(Product product)
      {
        for(StockObserver o: observers)
        {
            o.onLowStock(product);
        }
      }

      //show all product 
      public void showAllProducts()
      {
        if(products.isEmpty())
        {
            System.out.println("No products available!");
            return;
        }
        System.out.println("\n Current Inventory:");
        for(Product p: products.values())
        {
            System.out.println("ID: " + p.getProductId()+"\n"+
              "Name: "+p.getProductName()+"\n"+
              "Qty; " +p.getProductQauntity()+ "\n"+
              "Threshold: "+p.getProductThreshold() 
            );
        }
      }

      //save inventory to file
      private void saveInventoryToFile()
      {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryFile))) {
            for (Product p : products.values()) {
                writer.write(p.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
      }

      //load inventory from file
      private void loadInventoryFromFile()
      {
        File file = new File(inventoryFile);
        if (!file.exists()) {
            return; 
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(inventoryFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    int quantity = Integer.parseInt(parts[2]);
                    int threshold = Integer.parseInt(parts[3]);
                    Product product = new Product(id, name, quantity, threshold);
                    products.put(id, product);
                }
            }
            System.out.println("Inventory loaded from file.");
        }
         catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
      }
}
