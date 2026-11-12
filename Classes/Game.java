/**
 * Stores the restaurant's overall state, including registers, pending
 * orders, money, completed orders, satisfaction, and shift status
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class Game {
    private final ArrayList<Register> registers;
    private Queue<Order> pendingOrders;
    private ArrayList<Order> activeOrders;
    private final Menu menu;
    private double money;
    private int completedOrders;
    private double customerSatisfaction;
    private boolean shiftActive;
    private int nextOrderId;

    public Game() {
        // TODO
    }

    /**
     * Starts an inactive shift
     * @return false if already active
     */
    public boolean startShift() {
        // TODO
    }

    /**
     * Ends an active shift when no unfinished orders remain
     * @return false if already inactive or unfinished orders remain
     */
    public boolean endShift() {
        // TODO
    }

    /**
     * Adds a customer to the specified register
     * @param registerId 
     * @param customer
     * @throws 
     */
    public void addCustomer(int registerId, Customer customer) {
        // TODO
    }

    /**
     * Takes an order from the first customer in the
     * selected line and adds it to the pending order queue
     * @param registerId
     * @param items
     * @return
     */
    public Order placeOrder(int registerId, Map<String, Integer> items) {
        // TODO
    }

    /**
     * Removes the oldest pending order and marks it prepared
     * @return oldest pending order
     * @throws NoSuchElementException if no orders are pending
     */
    public Order prepareNextOrder() {
        // TODO
    }

    /**
     * Serves a prepared order and updates earnings,
     * satisfaction, and completed orders
     * @param orderId
     * @return false if order is invalid or unprepared
     */
    public boolean serveOrder(int orderId) {
        // TODO
    }

    /**
     * Returns a register by ID
     * @param registerId
     * @return register
     * @throws IllegalArgumentException if register ID does not exist
     */
    public Register getRegister(int registerId) {
        // TODO
    }

    /**
     * Returns the number of restaurant registers
     * @return the number of restaurant registers
     */
    public int getRegisterCount() {
        return registers.size();
    }

    /**
     * Returns an immutable copy of current restaurant
     * information without modifying the game
     * @return an immutable copy of the current restaurant information
     */
    public RestaurantSnapshot getRestaurantSnapshot() {
        // TODO
    }

    /**
     * Optional stretch-goal method that purchases an upgrade
     * when sufficient money is available. Rejects negative
     * costs and returns false if funds are insufficient.
     * @param cost
     * @return
     */
    /*
    public boolean purchaseUpgrade(double cost) {

    } */
}