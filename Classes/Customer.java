/**
 * Represents an individual customer with a unique identifier and name
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class Customer {
    private final int customerId;
    private final String name;

    /**
     * Constructs a new customer object and initializes fields
     * @param customerId
     *        the customer's unique ID
     * @param name
     *        the customer's name
     * @throws IllegalArgumentException if name is null or blank
     */
    public Customer(int customerId, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        this.customerId = customerId;
        this.name = name;
    }

    /**
     * Returns the customer's ID
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Returns the customer's name
     * @return name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (#" + customerId + ")";
    }
}
