import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class SuperGroceryMart {
static Scanner sc = new Scanner(System.in);
static ArrayList<Product> products = new ArrayList<>();
static ArrayList<BundleOffer> bundles = new ArrayList<>();
static ArrayList<Sale> sales = new ArrayList<>();
static HashMap<String, User> users = new HashMap<>();
static ArrayList<Customer> customers = new ArrayList<>();
static ArrayList<Customer> customers = new ArrayList<>();
static final String PRODUCT_FILE = "product.txt";
static final String SALES_FILE = "sales.txt";
static final String USER_FILE = "users.txt";
static final String CUSTOMER_FILE = "customers.txt";
static int invoiceCounter = 1000;

public static void main(String[] args) {
loadUsers();
loadProducts();
loadCustomers();
loadSales();
login();
}
static void loadUsers() {
users.put("admin", new User("admin", "admin123", "Admin"));
users.put("cashier", new User("cashier", "1234", "Cashier"));
try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
String line;
while ((line = br.readLine()) != null) {
String[] u = line.split(",");
users.put(u[0], new User(u[0], u[1], u[2]));
}
} catch (Exception e) {}
}

static void saveUsers() {
try (PrintWriter pw = new PrintWriter(new FileWriter(USER_FILE))) {
for (User u : users.values()) {
pw.println(u.username, ",", u.password, ",", u.role);
}
} catch (Exception e) {}
}
static void login() {
System.out.println("=== SUPER GROCERY MART SYSTEM ===");
System.out.print("Username: ");
String uname = sc.nextLine();
System.out.print("Password: ");
String pwd = sc.nextLine();
if (!users.containsKey(uname) || !users.get(uname).password.equals(pwd)) {
System.out.println("Invalid credentials");
return;
}
User user = users.get(uname);
if (user.role.equalsIgnoreCase("Admin")) adminMenu(user);
else cashierMenu(user);
}
static void adminMenu(User user) {
while (true) {
System.out.println("\n--- ADMIN DASHBOARD ---");
System.out.println("1. Products 2. Bundles 3. Customers 4. Sales Reports 5. Users 6. Save & Exit");
int ch = sc.nextInt();
sc.nextLine();
switch (ch) {
case 1:
productMenu();
break;
case 2:
bundleMenu();
break;
case 3:
customerMenu();
break;
case 4:
salesReportMenu();
break;
case 5:
userMenu();
break;
case 6:
saveAll();
return;
default:
System.out.println("Invalid choice");
}
}
}
static void cashierMenu(User user) {
while (true) {
System.out.println("\n--- CASHIER DASHBOARD ---");
System.out.println("1. Products 2. Search Product 3. Sell Product 4. Change Password 5. Exit");
int ch = sc.nextInt();
sc.nextLine();
switch (ch) {
case 1:
viewProducts();
break;
case 2:
searchProduct();
break;
case 3:
sellProduct(user);
break;
case 4:
changePassword(user);
break;
case 5:
saveAll();
return;
default:
System.out.println("Invalid choice");
}
}
}
static void loadProducts() {
try (BufferedReader br = new BufferedReader(new FileReader(PRODUCT_FILE))) {
String line;
while ((line = br.readLine()) != null) {
String[] p = line.split(",");
LocalDate exp = p[5].equals("NA") ? null : LocalDate.parse(p[5]);
products.add(new Product(Integer.parseInt(p[0]), p[1], Double.parseDouble(p[2]), Integer.parseInt(p[3]), p[4], exp));
}
} catch (Exception e) {}
}

static void saveProducts() {
try (PrintWriter pw = new PrintWriter(new FileWriter(PRODUCT_FILE))) {
for (Product p : products) pw.println(p.toString());
} catch (Exception e) {}
}

static void viewProducts() {
System.out.println("ID\tName\tPrice\tQty\tCategory\tExpiry");
for (Product p : products) System.out.println(p.display());
}
static void searchProduct() {
System.out.print("Search by name/category: ");
String key = sc.nextLine().toLowerCase();
for (Product p : products)
if (p.name.toLowerCase().contains(key) || p.category.toLowerCase().contains(key))
System.out.println(p.display());
}
static void productMenu() {
while (true) {
System.out.println("\n1. View All 2. Add 3. Update 4. Delete 5. Back");
int ch = sc.nextInt();
sc.nextLine();
switch (ch) {
case 1:
viewProducts();
break;
case 2:
addProduct();
break;
case 3:
updateStock();
break;
case 4:
deleteProduct();
break;
case 5:
return;
default:
System.out.println("Invalid choice");
}
}
}
static void addProduct() {
System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
System.out.print("Name: "); String name = sc.nextLine();
System.out.print("Price: "); double price = sc.nextDouble();
System.out.print("Qty: "); int qty = sc.nextInt(); sc.nextLine();
System.out.print("Category: "); String cat = sc.nextLine();
System.out.print("Expiry (yyyy-mm-dd or NA): "); String exp = sc.nextLine();
LocalDate expiry = exp.equals("NA") ? null : LocalDate.parse(exp);
products.add(new Product(id, name, price, qty, cat, expiry));
System.out.println("Product added.");
}

static void updateStock() {
System.out.print("Product ID: "); int id = sc.nextInt(); sc.nextLine();
for (Product p : products) {
if (p.id == id) {
System.out.print("New Qty: "); p.quantity = sc.nextInt(); sc.nextLine();
System.out.println("Updated.");
return;
}
}
System.out.println("Not found.");
}

static void deleteProduct() {
System.out.print("Product ID: "); int id = sc.nextInt(); sc.nextLine();
products.removeIf(p -> p.id == id);
System.out.println("Deleted if existed.");
}
static void bundleMenu() {
while (true) {
System.out.println("\n1.View Bundles 2.Add Bundle 3.Back");
int ch = sc.nextInt(); sc.nextLine();
switch (ch) {
case 1:
viewBundles();
break;
case 2:
addBundle();
break;
case 3:
return;
default:
System.out.println("Invalid choice");
}
}
}

static void viewBundles() {
for (BundleOffer b : bundles) System.out.println(b.toString());
}
static void addBundle() {
System.out.print("Bundle Name: "); String name = sc.nextLine();
System.out.print("Number of products in bundle: "); int n = sc.nextInt(); sc.nextLine();
ArrayList<Integer> prodIds = new ArrayList<>();
for (int i = 0; i < n; i++) {
System.out.print("Enter product ID: "); int pid = sc.nextInt(); sc.nextLine();
prodIds.add(pid);
}
System.out.print("Bundle price: "); double price = sc.nextDouble(); sc.nextLine();
bundles.add(new BundleOffer(name, prodIds, price));
System.out.println("Bundle added.");
}
static void loadCustomers() {
try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
String line;
while ((line = br.readLine()) != null) {
String[] c = line.split(",");
customers.add(new Customer(c[0], c[1], c[2], Integer.parseInt(c[3])));
}
} catch (Exception e) {
// Preload demo customers
customers.add(new Customer("Alice", "01711111111", "alice@gmail.com", 50));
customers.add(new Customer("Bob", "01722222222", "bob@gmail.com", 30));
customers.add(new Customer("Charlie", "01733333333", "charlie@gmail.com", 10));
for (int i = 4; i <= 20; i++) {
customers.add(new Customer("Cust" + i, "0170000000" + i, "cust" + i + "@mail.com", i * 5));
}
}
static void saveCustomers() {
try (PrintWriter pw = new PrintWriter(new FileWriter(CUSTOMER_FILE))) {
for (Customer c : customers) pw.println(c.toString());
} catch (Exception e) {}
}

static void customerMenu() {
while (true) {
System.out.println("\n1. View 2. Add 3. Edit 4. Delete 5. Back");
int ch = sc.nextInt(); sc.nextLine();
switch (ch) {
case 1:
viewCustomers();
break;
case 2:
addCustomer();
break;
case 3:
editCustomer();
break;
case 4:
deleteCustomer();
break;
case 5:
return;
default:
System.out.println("Invalid choice");
}
}
}
static void viewCustomers() {
System.out.println("Name\tPhone\tEmail\tPoints");
for (Customer c : customers) System.out.println(c.toString());
}

static void addCustomer() {
System.out.print("Name: "); String n = sc.nextLine();
System.out.print("Phone: "); String ph = sc.nextLine();
System.out.print("Email: "); String em = sc.nextLine();
customers.add(new Customer(n, ph, em, 0));
System.out.println("Customer added.");
}

static void editCustomer() {
System.out.print("Enter customer name to edit: "); String name = sc.nextLine();
for (Customer c : customers) {
if (c.name.equalsIgnoreCase(name)) {
System.out.print("New phone: "); c.phone = sc.nextLine();
System.out.print("New email: "); c.email = sc.nextLine();
System.out.println("Updated.");
return;
}
}
System.out.println("Not found.");
}
static void deleteCustomer() {
System.out.print("Customer name: "); String name = sc.nextLine();
customers.removeIf(c -> c.name.equalsIgnoreCase(name));
System.out.println("Deleted if existed.");
}

static void loadSales() {}

static void saveSale(String data) {
try (PrintWriter pw = new PrintWriter(new FileWriter(SALES_FILE, true))) {
pw.println(data);
pw.println("======= ====");
} catch (Exception e) {}
}
static void sellProduct(User user) {
double total = 0;
System.out.print("Enter Customer Name: "); String custName = sc.nextLine();
Customer customer = null;
for (Customer c : customers) if (c.name.equalsIgnoreCase(custName)) customer = c;
if (customer == null) System.out.println("Customer not found. Will continue without loyalty.");
StringBuilder bill = new StringBuilder();
int invoiceNo = invoiceCounter++;
bill.append("Invoice#: ").append(invoiceNo).append("\n");
bill.append("Cashier: ").append(user.username).append("\n");
bill.append("Date: ").append(LocalDateTime.now()).append("\n");
bill.append("Item\tQty\tPrice\tTotal\n");
while (true) {
System.out.print("Product ID (0 to finish): "); int id = sc.nextInt();
if (id == 0) break;
Product p = null;
for (Product pr : products) if (pr.id == id) p = pr;
if (p == null) { System.out.println("Not found"); continue; }
System.out.print("Qty: "); int q = sc.nextInt();
if (q > p.quantity) { System.out.println("Insufficient stock"); continue; }
double cost = q * p.price;
p.quantity -= q;
total += cost;
bill.append(p.name).append("\t").append(q).append("\t").append(p.price).append("\t").append(cost).append("\n");
}
static void userMenu() {
while (true) {
System.out.println("\n1.View Users 2.Add 3.Delete 4.Back");
int ch = sc.nextInt(); sc.nextLine();
switch (ch) {
case 1:
for (User u : users.values()) System.out.println(u.toString());
break;
case 2:
addUser();
break;
case 3:
deleteUser();
break;
case 4:
saveUsers();
return;
default:
System.out.println("Invalid choice");
}
}
}
static void addUser() {
System.out.print("Username: "); String u = sc.nextLine();
if (users.containsKey(u)) { System.out.println("Exists"); return; }
System.out.print("Password: "); String p = sc.nextLine();
System.out.print("Role: "); String r = sc.nextLine();
users.put(u, new User(u, p, r));
System.out.println("User added.");
}

static void deleteUser() {
System.out.print("Username: "); String u = sc.nextLine();
if (users.containsKey(u)) { users.remove(u); System.out.println("Removed"); return; }
System.out.println("Not found");
}

static void changePassword(User user) {
System.out.print("New Password: "); String np = sc.nextLine();
user.password = np;
System.out.println("Changed.");
}
static void salesReportMenu() {
while (true) {
System.out.println("\n1. Daily Sales 2. Monthly Sales 3. Back");
int ch = sc.nextInt(); sc.nextLine();
switch (ch) {
case 1:
System.out.println("Daily sales feature placeholder");
break;
case 2:
System.out.println("Monthly sales feature placeholder");
break;
case 3:
return;
default:
System.out.println("Invalid choice");
}
}
}

static void saveAll() {
saveProducts(); saveUsers(); saveCustomers();
System.out.println("All data saved.");
}
static class Product {
int id; String name; double price; int quantity; String category; LocalDate expiry;
Product(int id, String n, double p, int q, String c, LocalDate e) {
this.id = id; name = n; price = p; quantity = q;
category = c; expiry = e;
}
public String toString() {
return id + "," + name + "," + price + "," + quantity + "," + category + "," + (expiry == null ? "NA" : expiry.toString());
}
public String display() {
return id + "\t" + name + "\t" + price + "\t" + quantity + "\t" + category + "\t" + (expiry == null ? "NA" : expiry.toString());
}
}

static class BundleOffer {
String name; ArrayList<Integer> productIds; double price;
BundleOffer(String n, ArrayList<Integer> pids, double price) {
this.name = n; this.productIds = pids;
this.price = price;
}
public String toString() {
return "Bundle: " + name + " | Price: " + price + " | Products: " + productIds;
}
}

static class Sale {
int invoiceNo; String cashier; LocalDateTime datetime; double amount;
Sale(int i, String c, LocalDateTime dt, double amt) {
invoiceNo = i; cashier = c; datetime = dt; amount = amt;
}
public String toString() {
return "Invoice#" + invoiceNo + " | " + cashier + " | " + datetime + " | " + amount;
}
}
static class User {
String username, password, role;
User(String u, String p, String r) {
username = u; password = p; role = r;
}
public String toString() {
return username + " | " + role;
}
}

static class Customer {
String name, phone, email; int points;
Customer(String n, String ph, String em, int pts) {
name = n; phone = ph; email = em; points = pts;
}
public String toString() {
return name + "\t" + phone + "\t" + email + "\t" + points;
}
}
