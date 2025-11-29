package phase2;

import java.io.BufferedReader;
import java.io.FileReader;


public class Data {

    // 1. تحميل المنتجات إلى شجرة AVL
    public static void loadProducts(AVLTree<Product> products) {
        loadFile("products.csv", (parts) -> {
            try {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                int stock = Integer.parseInt(parts[3].trim());
                
                // الإضافة باستخدام (مفتاح، قيمة)
                products.insert(id, new Product(id, name, price, stock));
            } catch (Exception e) {
                System.out.println("Error parsing product: " + parts[0]);
            }
        });
        System.out.println("Products Loaded.");
    }

    // 2. تحميل العملاء إلى شجرة AVL
    public static void loadCustomers(AVLTree<Customer> customers) {
        loadFile("customers.csv", (parts) -> {
            try {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String email = parts[2].trim();
                
                customers.insert(id, new Customer(id, name, email));
            } catch (Exception e) {
                System.out.println("Error parsing customer: " + parts[0]);
            }
        });
        System.out.println("Customers Loaded.");
    }

    // 3. تحميل الطلبات (تستقبل أشجار للبحث السريع)
    public static void loadOrders(AVLTree<Order> orders, AVLTree<Customer> customers, AVLTree<Product> products) {
        loadFile("orders.csv", (parts) -> {
            try {
                int orderId = Integer.parseInt(parts[0].trim());
                int customerId = Integer.parseInt(parts[1].trim());
                String[] productIds = parts[2].split(";");
                double totalPrice = Double.parseDouble(parts[3].trim());
                String date = parts[4].trim();
                String status = parts[5].trim();

                // البحث السريع باستخدام AVL (search) بدلاً من اللوب
                Customer customer = customers.search(customerId);
                if (customer == null) return;

                // تجميع المنتجات داخل الطلب (تبقى LinkedList لأنها قائمة صغيرة داخل الطلب)
                MyLinkedList<Product> orderProducts = new MyLinkedList<>();
                for (String pid : productIds) {
                    Product p = products.search(Integer.parseInt(pid.trim()));
                    if (p != null) orderProducts.insert(p);
                }

                Order order = new Order(orderId, customer, orderProducts, totalPrice, date, status);
                
                // إضافة الطلب لهستوري العميل والشجرة العامة
                customer.getOrders().insert(order);
                orders.insert(orderId, order);
                
            } catch (Exception e) {
                System.out.println("Error parsing order: " + parts[0]);
            }
        });
        System.out.println("Orders Loaded.");
    }

    // 4. تحميل التقييمات
    public static void loadReviews(AVLTree<Product> products, AVLTree<Customer> customers) {
        loadFile("reviews.csv", (parts) -> {
            try {
                // int reviewId = Integer.parseInt(parts[0].trim()); // نتجاهل الـ ID إذا لم نكن نحتاجه
                int productId = Integer.parseInt(parts[1].trim());
                int customerId = Integer.parseInt(parts[2].trim());
                int rating = Integer.parseInt(parts[3].trim());
                String comment = parts[4].trim();

                Product p = products.search(productId);
                Customer c = customers.search(customerId);

                if (p != null && c != null) {
                    p.addReview(new Review(c, p, rating, comment));
                }
            } catch (Exception e) {
                System.out.println("Error parsing review.");
            }
        });
        System.out.println("Reviews Loaded.");
    }

    // دالة مساعدة لقراءة الملفات
    private static void loadFile(String fileName, LineProcessor processor) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean header = true; // لتخطي سطر العناوين
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                if (!line.trim().isEmpty()) {
                    processor.process(line.split(","));
                }
            }
        } catch (Exception e) {
            System.out.println("File not found: " + fileName);
        }
    }

    // Functional Interface للتعامل مع الأسطر
    interface LineProcessor {
        void process(String[] parts);
    }
}111
