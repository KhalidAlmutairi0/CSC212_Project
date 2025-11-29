package csc212.copy;

public class Product {
    private int productId;
    private String name;
    private double price;
    private int stock;
    private MyLinkedList<Review> reviews;

    public Product(int productId, String name, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.reviews = new MyLinkedList<>();
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    // أضف هذا التابع ليُرجع السعر
    public double getPrice() {
        return price;
    }

    public void addReview(Review review) {
        reviews.insert(review);
    }

    public double getAverageRating() {
        if (reviews.isEmpty())
            return 0;

        double total = 0;
        int count = 0;

        Node<Review> current = reviews.head;
        while (current != null) {
            total += current.data.getRating();
            count++;
            current = current.next;
        }

        return total / count;
    }

    public boolean isOutOfStock() {
        return stock <= 0;
    }

    public void reduceStock(int amount) {
        stock -= amount;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId +
                ", Name: " + name +
                ", Price: $" + price +
                ", Stock: " + stock +
                ", Avg Rating: " + getAverageRating();
    }

    public MyLinkedList<Review> getReviews() {
        return reviews;
    }
        
    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    

}
