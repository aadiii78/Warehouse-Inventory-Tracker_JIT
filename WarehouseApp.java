import java.util.*;
import java.util.concurrent.*;
public class WarehouseApp {

    public static void main(String[] args) throws Exception{
        Scanner sc = new Scanner(System.in);
        Warehouse warehouse = new Warehouse();
        warehouse.addObserver(new AlertService());
        ExecutorService executor = Executors.newFixedThreadPool(3);

        while(true){
            System.out.println("\n=== WAREHOUSE MENU ===");
            System.out.println("1. Add Product");
            System.out.println("2. Receive Shipment");
            System.out.println("3. Fulfill Order");
            System.out.println("4. Show All Products");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
             
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                 System.out.print("Enter ID: ");
                    String id = sc.nextLine().trim();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    System.out.print("Enter Threshold: ");
                    int th = sc.nextInt();
                    warehouse.addProduct(id, name, qty, th);
                    break;

                    case 2:
                    System.out.print("Enter Product ID: ");
                    String id2 = sc.nextLine().trim();
                    System.out.print("Enter Shipment Quantity: ");
                    int addQty = sc.nextInt();
                    executor.submit(()->warehouse.receiveShipment(id2, addQty)).get();
                    break;

                    case 3:
                    System.out.print("Enter Product ID: ");
                    String id3 = sc.nextLine().trim();
                    System.out.print("Enter Order Quantity: ");
                    int orderQty = sc.nextInt();
                    executor.submit(()->warehouse.fulfillOrder(id3, orderQty)).get();
                    break;
                     
                    case 4:
                    warehouse.showAllProducts();
                    break;

                    case 5:
                    System.out.println(" Exiting Warehouse System...");
                    sc.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println(" Invalid choice! Try again.");
            }
        }
    }
}
