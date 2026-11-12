import java.util.NoSuchElementException;

/**
 * Represents an individual cash register and manages its customer waiting line
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class Register {
    private final int registerId;
    private final String registerName;
    private final LQueue waitingCustomers;
    private boolean isOpen;

    /**
     * Creates an open Register with an empty line
     * @param registerId
     *        the register's ID
     * @param registerName
     *        the register's display name
     * @throws IllegalArgumentException if registerName is null or blank
     */
    public Register(int registerId, String registerName) {
        if (registerName == null || registerName.isBlank()) {
            throw new IllegalArgumentException("Register name cannot be empty");
        }
        this.registerId = registerId;
        this.registerName = registerName;
        this.waitingCustomers = new LQueue();
        this.isOpen = true;
    }

    /**
     * Adds a Customer to this Register's queue
     * @param customer
     *        the Customer joining the line
     * @throws IllegalArgumentException if customer is null
     * @throws IllegalStateException if the Register is closed
     */
    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (!isOpen) {
            throw new IllegalStateException(registerName + " is closed");
        }
        waitingCustomers.enqueue(customer);
    }

    /**
     * Removes and returns the first waiting Customer
     * @return the first waiting Customer
     * @throws NoSuchElementException if LQueue is empty
     */
    public Customer nextCustomer() {
        return waitingCustomers.dequeue();
    }

    /**
     * Returns the first waiting Customer without removing them
     * @return the first waiting Customer
     * @throws NoSuchElementException if LQueue is empty
     */
    public Customer peekCustomer() {
        return waitingCustomers.peek();
    }

    /**
     * Returns the number of waiting Customers
     * @return line length
     */
    public int getLineLength() {
        return waitingCustomers.size();
    }

    /**
     * Returns whether no Customers are waiting
     * @return true if the line is empty
     */
    public boolean isEmpty() {
        return waitingCustomers.isEmpty();
    }

    /**
     * Returns whether the Register accepts Customers.
     * @return true if isOpen is true
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Opens or closes the Register
     * @param open
     *        true to open, false to close
     */
    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    /**
     * Returns the Register's ID
     * @return registerId
     */
    public int getRegisterId() {
        return registerId;
    }

    /**
     * Returns the Register's name
     * @return registerName
     */
    public String getRegisterName() {
        return registerName;
    }
}
