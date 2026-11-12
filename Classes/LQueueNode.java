/**
 * Represents a node in the linked queue, storing a
 * customer reference and a reference to the next node
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class LQueueNode {
    private Customer data;
    private LQueueNode next;

    /**
     * Creates a new LQueueNode
     * @param data
     *        this node's Customer object
     * @param next
     *        the node after this node in the LQueue
     */
    public LQueueNode(Customer data, LQueueNode next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Creates a new LQueueNode with no next node
     * @param data
     */
    public LQueueNode(Customer data) {
        LQueueNode(data, null);
    }

    /**
     * Returns the Customer's information
     * @return this node's Customer object
     */
    public Customer getData() {
        return data;
    }

    /**
     * Sets the Customer information to the Customer argument
     * @param data
     *        the Customer object to be assigned to this node
     */
    public void setData(Customer data) {
        this.data = data;
    }

    /**
     * Returns the next node
     * @return the node after this node in the LQueue
     */
    public LQueueNode getNext() {
        return next;
    }

    /**
     * Sets the next node to the LQueueNode argument
     * @param next
     */
    public void setNext(LQueueNode next) {
        this.next = next;
    }
}