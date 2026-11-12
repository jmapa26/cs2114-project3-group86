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
    // TODO: Map<FoodItem, Integer> items
    private final double totalPrice;
    private OrderStatus status;

    public Order() {
        // TODO
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

    // Some get method for the items Map

    /**
     * Returns the calculated total without modifying the order
     * @return the calculated order total
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    // Something for status

    /**
     * Changes an order from PLACED to PREPARED
     * @throws IllegalStateException if status is not PLACED
     */
    public void markPrepared() {
        // TODO
    }

    /**
     * Changes an order from PREPARED to SERVED
     * @throws IllegalStateException if status is not PREPARED
     */
    public void markServed() {
        // TODO
    }
}