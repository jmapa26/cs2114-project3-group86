/**
 * Implements a singly linked FIFO queue of customers waiting at a register
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class LQueue {
    private LQueueNode front;
    private LQueueNode back;
    private int size;

    /**
     * Creates a new singly-linked LQueue
     */
    public LQueue() {
        // TODO
    }

    /**
     * Adds a Customer to the rear
     * @param customer
     *        the Customer added to the LQueue
     * @throws IllegalArgumentException if customer is null
     */
    public void enqueue(Customer customer) {
        // TODO
    }

    /**
     * Removes and returns the front Customer
     * @return the Customer removed from the LQueue
     * @throws NoSuchElementException if the LQueue is empty
     */
    public Customer dequeue() {
        // TODO
    }

    /**
     * Returns the front Customer without removing them
     * @return the Customer at the front of the LQueue
     * @throws NoSuchElementException if the LQueue is empty
     */
    public Customer peek() {
        // TODO
    }

    /**
     * Returns true when the LQueue has no Customers
     * @return true if size is 0
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of Customers in the LQueue
     * @return number of Customers in the LQueue
     */
    public int size() {
        return size;
    }
}