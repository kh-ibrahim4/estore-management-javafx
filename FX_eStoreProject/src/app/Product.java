package app;

public class Product {
    private String code;
    private String name;
    private double price;
    private int quantity;
    private String currency;
    private String date;
    private String imageUrl;

    public Product(String code, String name, double price, int quantity, String currency, String date, String imageUrl) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.currency = currency;
        this.date = date;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}