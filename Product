public class Product {
    private String productId;
    private String productName;
    private int productQuantity;
    private int productThreshold;

    public Product( String productId,String productName,int productQuantity,int productThreshold)
    {
        this.productId=productId;
        this.productName=productName;
        this.productQuantity=productQuantity;
        this.productThreshold=productThreshold;
    }

    //getting and setters method

    public String getProductId()
    {
        return productId;
    }
    
     public String getProductName()
    {
        return productName;
    }

     public int getProductQauntity()
    {
        return productQuantity;
    }

     public int getProductThreshold()
    {
        return productThreshold;
    }

    public void setProductId(String productId)
    {
        this.productId=productId;
    }

    public void setProductName(String productName)
    {
        this.productName=productName;
    }

    public void setProductQuantity(int productQuantity)
    {
        this.productQuantity=productQuantity;
    }

     public void setProductThreshold(int productThreshold)
    {
        this.productThreshold=productThreshold;
    }

    // increase and decrease stock 
    public void increaseStock (int amount)
    {
        productQuantity += amount;
    }

    public boolean decreaseStock(int amount)
    {
        if(amount>productQuantity)
        {
            System.out.println("not enough stock to fulfill order!");
            return false;
        }
        productQuantity -= amount;
        return true;
    }
}
