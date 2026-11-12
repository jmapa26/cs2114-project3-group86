import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Stores a customer's selected food items,
 * quantities, total price, and completion status
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class Order {
    private final int orderId;
    private final Customer customer;
    private final Map<FoodItem, Integer> items;
    private final double totalPrice;
    private OrderStatus status;

    /**
     * Creates a PLACED Order and calculates its total
     * @param orderId
     *        the order's unique ID
     * @param customer
     *        the Customer who placed it
     * @param items
     *        each FoodItem mapped to its quantity
     * @throws IllegalArgumentException if customer/items are null or a
     *         quantity is not positive
     */
    public Order(int orderId, Customer customer, Map<FoodItem, Integer> items) {
        if (customer == null || items == null) {
            throw new IllegalArgumentException(
                "Customer and items cannot be null");
        }
        double total = 0;
        for (Map.Entry<FoodItem, Integer> e : items.entrySet()) {
            if (e.getKey() == null || e.getValue() == null
                || e.getValue() <= 0) {
                throw new IllegalArgumentException("Invalid order item");
            }
            total += e.getKey().getPrice() * e.getValue();
        }
        this.orderId = orderId;
        this.customer = customer;
        this.items = Collections.unmodifiableMap(new LinkedHashMap<>(items));
        this.totalPrice = total;
        this.status = OrderStatus.PLACED;
    }

    /**
     * Returns the Order ID
     * @return orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Returns the Customer associated with the order
     * @return customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns a read-only map of FoodItems to quantities
     * @return items
     */
    public Map<FoodItem, Integer> getItems() {
        return items;
    }

    /**
     * Returns the calculated total without modifying the order
     * @return the calculated order total
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Returns the order's current status
     * @return status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Changes an order from PLACED to PREPARED
     * @throws IllegalStateException if status is not PLACED
     */
    public void markPrepared() {
        if (status != OrderStatus.PLACED) {
            throw new IllegalStateException(
                "Only placed orders can be prepared");
        }
        status = OrderStatus.PREPARED;
    }

    /**
     * Changes an order from PREPARED to SERVED
     * @throws IllegalStateException if status is not PREPARED
     */
    public void markServed() {
        if (status != OrderStatus.PREPARED) {
            throw new IllegalStateException(
                "Only prepared orders can be served");
        }
        status = OrderStatus.SERVED;
    }

    /**
     * Returns a one-line summary for the View
     * @return e.g. Order #1 [PLACED] Ava: 2x Burger ($13.00)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<FoodItem, Integer> e : items.entrySet()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(e.getValue()).append("x ").append(e.getKey().getName());
        }
        return String.format("Order #%d [%s] %s: %s ($%.2f)", orderId,
            status, customer.getName(), sb, totalPrice);
    }
}
