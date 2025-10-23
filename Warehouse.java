import java.util.*;

public class Warehouse {
    private Map<String,Product> products = new HashMap<>();
    private List<StockObserver> observers = new ArrayList<>();

    public void addObserver(StockObserver observer)
    {
        observers.add(observer);
    }

    //add product 
    public void addProduct(String id,String name, int quantity,int threshold)
    {
        if(products.containsKey(id))
        {
            System.out.println("Product ID already exists!");
            return;
        }
        
        Product product = new Product(id, name, quantity, threshold);
        products.put(id, product);
        System.out.println("Adding product with ID -> '" + id + "'");
    }
    // recieve shipment
    public void receiveShipment (String id , int quantity) {
        Product p= products.get(id);
        if(p== null)
        {
            System.out.println("Invalid Product ID!");
            return;
        }
        p.increaseStock(quantity);
        System.out.println("shipment received! New Stock "+ p.getProductQauntity());
    }
     // order 
     public void fulfillOrder(String id,int quantity)
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
}
