/**
 * Register
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class Register {
    private final int registerId;
    private final String registerName;
    private final LQueue waitingCustomers;
    public boolean isOpen;

    public Register() {
        // TODO
    }
    
    /**
     * Adds a Customer to this Register's queue
     * @param customer
     * @throws NoSuchElementException if LQueue is empty
     */
    public void addCustomer(Customer customer) {
        // TODO
        waitingCustomers.enqueue(customer);
    }

    /**
     * Removes and returns the first waiting Customer
     * @return
     * @throws NoSuchElementException if LQueue is empty
     */
    public Customer nextCustomer() {
        // TODO
    }

    public Customer peekCustomer() {
        // TODO
    }

    public int getLineLength() {
        return waitingCustomers.size();
    }

    public boolean isEmpty() {
        // TODO
    }

    /**
     * Returns whether the Register accepts Customers.
     * @return true if isOpen is true
     */
    public boolean isOpen() {
        return isOpen;
    }
}