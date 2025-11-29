package csc212.copy;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private MyLinkedList<Order> orders;

    public Customer(int customerId, String name, String email) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.orders = new MyLinkedList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MyLinkedList<Order> getOrders() {
        return orders;
    }

    public void placeOrder(Order order) {
        orders.insert(order);
    }

    public void printOrderHistory() {
        if (orders.isEmpty()) {
            System.out.println("No order history for " + name);
            return;
        }
        System.out.println("Order history for customer: " + name);
        orders.printAll();
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId +
                ", Name: " + name +
                ", Email: " + email;
    }
     public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
