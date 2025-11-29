package csc212.copy;

public class Review {
    private Customer customer;
    private Product product;
    private int rating;     // مثلا من 1 إلى 5
    private String comment;

    public Review(Customer customer, Product product, int rating, String comment) {
        this.customer = customer;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getCustomerId() {
        return customer.getCustomerId();
    }

    public int getProductId() {
        return product.getProductId();
    }

    @Override
    public String toString() {
        return "Customer ID: " + customer.getCustomerId() +
                " rated " + rating +
                " - " + comment;
    }
     public void setRating(int rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
