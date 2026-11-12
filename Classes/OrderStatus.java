/**
 * Defines whether an order is PLACED, PREPARED, or SERVED
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public enum OrderStatus {
    /** Order taken, waiting to be prepared */
    PLACED,
    /** Order prepared, waiting to be served */
    PREPARED,
    /** Order given to the customer */
    SERVED
}
