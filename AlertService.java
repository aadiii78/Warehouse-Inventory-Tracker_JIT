public class AlertService implements StockObserver{

    @Override
    public void onLowStock(Product product){
        System.out.println("ALERT: Low stock for "+product.getProductName()+" --only "+ 
        product.getProductQauntity() + " left!");
    }
    
}
