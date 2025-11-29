package phase2; 

import java.util.Scanner;

public class Main {

    // Define AVL Trees for data storage (Single Source of Truth)
    static AVLTree<Product> productsTree = new AVLTree<>();
    static AVLTree<Customer> customersTree = new AVLTree<>();
    static AVLTree<Order> ordersTree = new AVLTree<>();
    
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(">>> Loading Data into AVL Trees...");
        
        // Load data using the Data helper class
        Data.loadProducts(productsTree);
        Data.loadCustomers(customersTree);
        Data.loadOrders(ordersTree, customersTree, productsTree);
        Data.loadReviews(productsTree, customersTree);
        
        System.out.println("We Are Ready\n");

        while (true) {
            printMenu();
            int choice = getIntInput();

            switch (choice) {
                // 1-3: View All (AVL prints in-order automatically)
                case 1: productsTree.inOrderPrint(); break;
                case 2: customersTree.inOrderPrint(); break;
                case 3: ordersTree.inOrderPrint(); break;
                
                // 4: Report using the new 'traverse' method
                case 4: 
                    productsTree.traverse(p -> System.out.printf("%s | Rating: %.2f\n", p.getName(), p.getAverageRating())); 
                    break;

                // Edit and Remove operations
                case 5: editProduct(); break;
                case 6: removeProduct(); break;
                case 7: editCustomer(); break;
                case 8: removeCustomer(); break;
                case 9: editOrder(); break;
                case 10: removeOrder(); break;
                case 11: editReview(); break;

                // Add operations
                case 12: addProduct(); break;
                case 13: addCustomer(); break;
                case 14: placeOrder(); break;
                case 15: addReview(); break;

                case 16: viewCustomerHistory(); break;
                
                // 17: Out of Stock Report
                case 17: 
                    System.out.println("--- Out of Stock ---");
                    productsTree.traverse(p -> {
                        if (p.isOutOfStock()) System.out.println(p);
                    });
                    break;

                case 18: printTopRatedProducts(); break;
                
                // 19: Filter orders by date range
                case 19: 
                     System.out.print("Start Date: "); String start = scanner.nextLine();
                     System.out.print("End Date: "); String end = scanner.nextLine();
                     ordersTree.traverse(o -> {
                         if (o.isWithinDateRange(start, end)) System.out.println(o);
                     });
                     break;

                case 20: printCommonHighRatedProducts(); break;

                case 21: 
                    System.out.println("Goodbye!"); 
                    return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    // --- Helper Methods ---

    private static void printMenu() {
        System.out.println("\n===== AVL E-Commerce System =====");
        System.out.println("1. View Products       2. View Customers       3. View Orders");
        System.out.println("4. View Prod w/Rating  5. Edit Product         6. Remove Product");
        System.out.println("7. Edit Customer       8. Remove Customer      9. Edit Order");
        System.out.println("10. Remove Order       11. Edit Review         12. Add Product");
        System.out.println("13. Add Customer       14. Place Order         15. Add Review");
        System.out.println("16. Cust History       17. Out-of-Stock        18. Top 3 Rated");
        System.out.println("19. Order By Date      20. Common High Rated   21. Exit");
        System.out.print("Enter choice: ");
    }

    private static void editProduct() {
        System.out.print("Product ID: ");
        Product p = productsTree.search(getIntInput());
        if (p != null) {
            System.out.print("New Name: "); p.setName(scanner.nextLine());
            System.out.print("New Price: "); p.setPrice(getDoubleInput());
            System.out.print("New Stock: "); p.setStock(getIntInput());
            System.out.println("Updated.");
        } else System.out.println("Not found.");
    }

    private static void removeProduct() {
        System.out.print("Product ID: ");
        int id = getIntInput();
        if (productsTree.search(id) != null) {
            productsTree.delete(id);
            System.out.println("Removed.");
        } else System.out.println("Not found.");
    }

    private static void editCustomer() {
        System.out.print("Customer ID: ");
        Customer c = customersTree.search(getIntInput());
        if (c != null) {
            System.out.print("New Name: "); c.setName(scanner.nextLine());
            System.out.print("New Email: "); c.setEmail(scanner.nextLine());
            System.out.println("Updated.");
        } else System.out.println("Not found.");
    }

    private static void removeCustomer() {
        System.out.print("Customer ID: ");
        int id = getIntInput();
        if (customersTree.search(id) != null) {
            customersTree.delete(id);
            System.out.println("Removed.");
        } else System.out.println("Not found.");
    }

    private static void editOrder() {
        System.out.print("Order ID: ");
        Order o = ordersTree.search(getIntInput());
        if (o != null) {
            System.out.print("New Status: ");
            o.updateStatus(scanner.nextLine());
            System.out.println("Updated.");
        } else System.out.println("Not found.");
    }

    private static void removeOrder() {
        System.out.print("Order ID: ");
        int id = getIntInput();
        if (ordersTree.search(id) != null) {
            ordersTree.delete(id);
            System.out.println("Removed.");
        } else System.out.println("Not found.");
    }

    private static void addProduct() {
        System.out.print("ID: "); int id = getIntInput();
        if (productsTree.search(id) != null) { System.out.println("Exists!"); return; }
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Price: "); double price = getDoubleInput();
        System.out.print("Stock: "); int stock = getIntInput();
        productsTree.insert(id, new Product(id, name, price, stock));
        System.out.println("Added.");
    }

    private static void addCustomer() {
        System.out.print("ID: "); int id = getIntInput();
        if (customersTree.search(id) != null) { System.out.println("Exists!"); return; }
        System.out.print("Name: "); String name = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        customersTree.insert(id, new Customer(id, name, email));
        System.out.println("Added.");
    }

    private static void placeOrder() {
        System.out.print("Order ID: "); int oid = getIntInput();
        if (ordersTree.search(oid) != null) { System.out.println("Exists!"); return; }
        
        System.out.print("Cust ID: "); Customer c = customersTree.search(getIntInput());
        if (c == null) { System.out.println("Cust Not Found."); return; }

        MyLinkedList<Product> cart = new MyLinkedList<>();
        double total = 0;
        while(true) {
            System.out.print("Prod ID (-1 end): "); int pid = getIntInput();
            if (pid == -1) break;
            Product p = productsTree.search(pid);
            if (p != null && !p.isOutOfStock()) {
                cart.insert(p);
                p.reduceStock(1);
                total += p.getPrice();
                System.out.println("Added.");
            } else System.out.println("Error/No Stock.");
        }
        if (total > 0) {
            Order o = new Order(oid, c, cart, total, "2025-11-29", "Pending");
            ordersTree.insert(oid, o);
            c.placeOrder(o);
            System.out.println("Done. Total: " + total);
        }
    }

    private static void addReview() {
        System.out.print("Cust ID: "); Customer c = customersTree.search(getIntInput());
        System.out.print("Prod ID: "); Product p = productsTree.search(getIntInput());
        if (c!=null && p!=null) {
            System.out.print("Rate (1-5): "); int r = getIntInput();
            System.out.print("Comment: "); String m = scanner.nextLine();
            p.addReview(new Review(c, p, r, m));
            System.out.println("Added.");
        } else System.out.println("Not found.");
    }

    private static void editReview() {
        System.out.print("Prod ID: "); Product p = productsTree.search(getIntInput());
        if (p == null) return;
        System.out.print("Cust ID: "); int cid = getIntInput();
        
        Node<Review> curr = p.getReviews().head;
        while(curr != null) {
            if (curr.data.getCustomerId() == cid) {
                System.out.print("New Rate: "); curr.data.setRating(getIntInput());
                System.out.print("New Comment: "); curr.data.setComment(scanner.nextLine());
                System.out.println("Updated.");
                return;
            }
            curr = curr.next;
        }
        System.out.println("Review not found.");
    }

    private static void viewCustomerHistory() {
        System.out.print("Cust ID: "); Customer c = customersTree.search(getIntInput());
        if (c != null) c.printOrderHistory();
        else System.out.println("Not found.");
    }

    // --- Top 3 (Requires sorting logic) ---
    private static void printTopRatedProducts() {
        // Collect all products into a temporary list using traverse
        MyLinkedList<Product> temp = new MyLinkedList<>();
        productsTree.traverse(p -> temp.insert(p));

        System.out.println("--- Top 3 Products ---");
        // Find top 3 and remove them from temp list
        for(int i=0; i<3; i++) {
            Product best = null;
            double maxR = -1;
            Node<Product> curr = temp.head;
            while(curr != null) {
                if (curr.data.getAverageRating() > maxR) {
                    maxR = curr.data.getAverageRating();
                    best = curr.data;
                }
                curr = curr.next;
            }
            if (best != null) {
                System.out.printf("#%d %s (%.2f)\n", i+1, best.getName(), maxR);
                // Manual removal from temp list
                if (temp.head.data == best) temp.head = temp.head.next;
                else {
                    Node<Product> c = temp.head;
                    while(c.next != null) {
                        if (c.next.data == best) { c.next = c.next.next; break; }
                        c = c.next;
                    }
                }
            }
        }
    }

    private static void printCommonHighRatedProducts() {
        System.out.print("Cust 1: "); int id1 = getIntInput();
        System.out.print("Cust 2: "); int id2 = getIntInput();
        
        productsTree.traverse(p -> {
            boolean c1 = false, c2 = false;
            Node<Review> r = p.getReviews().head;
            while(r != null) {
                if(r.data.getCustomerId() == id1) c1 = true;
                if(r.data.getCustomerId() == id2) c2 = true;
                r = r.next;
            }
            if (c1 && c2 && p.getAverageRating() > 4.0)
                System.out.println("Common High Rated: " + p.getName());
        });
    }

    private static int getIntInput() {
        try { return Integer.parseInt(scanner.nextLine()); } catch (Exception e) { return -1; }
    }
    private static double getDoubleInput() {
        try { return Double.parseDouble(scanner.nextLine()); } catch (Exception e) { return 0.0; }
    }
}
