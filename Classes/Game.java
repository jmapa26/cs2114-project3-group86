import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Stores the restaurant's overall state, including registers, pending
 * orders, money, completed orders, satisfaction, and shift status
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class Game {
    /** Satisfaction at the start of the game */
    public static final double START_SATISFACTION = 50.0;
    /** Satisfaction gained per served order */
    public static final double SERVE_BONUS = 5.0;
    /** Highest possible satisfaction */
    public static final double MAX_SATISFACTION = 100.0;

    private final ArrayList<Register> registers;
    private Queue<Order> pendingOrders;
    private ArrayList<Order> activeOrders;
    private final Menu menu;
    private double money;
    private int completedOrders;
    private double customerSatisfaction;
    private boolean shiftActive;
    private int nextOrderId;

    /**
     * Creates the default restaurant: two indoor registers and a
     * drive-through
     */
    public Game() {
        this(new Menu(), List.of("Register 1", "Register 2", "Drive-Thru"));
    }

    /**
     * Creates a restaurant with custom registers. Registers get IDs 1..n
     * in the order given.
     * @param menu
     *        the restaurant's menu
     * @param registerNames
     *        names of the registers to create
     * @throws IllegalArgumentException if either argument is null
     */
    public Game(Menu menu, List<String> registerNames) {
        if (menu == null || registerNames == null) {
            throw new IllegalArgumentException(
                "Menu and register names cannot be null");
        }
        this.menu = menu;
        this.registers = new ArrayList<>();
        for (int i = 0; i < registerNames.size(); i++) {
            registers.add(new Register(i + 1, registerNames.get(i)));
        }
        this.pendingOrders = new ArrayDeque<>();
        this.activeOrders = new ArrayList<>();
        this.money = 0;
        this.completedOrders = 0;
        this.customerSatisfaction = START_SATISFACTION;
        this.shiftActive = false;
        this.nextOrderId = 1;
    }

    /**
     * Starts an inactive shift
     * @return false if already active
     */
    public boolean startShift() {
        if (shiftActive) {
            return false;
        }
        shiftActive = true;
        return true;
    }

    /**
     * Ends an active shift when no unfinished orders remain
     * @return false if already inactive or unfinished orders remain
     */
    public boolean endShift() {
        if (!shiftActive || !activeOrders.isEmpty()) {
            return false;
        }
        shiftActive = false;
        return true;
    }

    /**
     * Adds a customer to the specified register
     * @param registerId
     *        the register to join
     * @param customer
     *        the arriving customer
     * @throws IllegalStateException if no shift is active
     * @throws IllegalArgumentException if the register ID is invalid or
     *         customer is null
     */
    public void addCustomer(int registerId, Customer customer) {
        requireShift();
        getRegister(registerId).addCustomer(customer);
    }

    /**
     * Takes an order from the first customer in the
     * selected line and adds it to the pending order queue.
     * Everything is validated before the customer is removed,
     * so a bad order changes nothing.
     * @param registerId
     *        the register to take the order from
     * @param items
     *        item names mapped to quantities
     * @return the new Order
     * @throws IllegalStateException if no shift is active
     * @throws IllegalArgumentException for a bad register, empty order,
     *         unknown item, or non-positive quantity
     * @throws NoSuchElementException if nobody is waiting at the register
     */
    public Order placeOrder(int registerId, Map<String, Integer> items) {
        requireShift();
        Register register = getRegister(registerId);
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain items");
        }
        Map<FoodItem, Integer> resolved = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            Integer qty = e.getValue();
            if (qty == null || qty <= 0) {
                throw new IllegalArgumentException(
                    "Quantity must be positive for " + e.getKey());
            }
            resolved.merge(menu.getItem(e.getKey()), qty, Integer::sum);
        }
        if (register.isEmpty()) {
            throw new NoSuchElementException(
                "No customers waiting at " + register.getRegisterName());
        }
        Customer customer = register.nextCustomer();
        Order order = new Order(nextOrderId++, customer, resolved);
        pendingOrders.add(order);
        activeOrders.add(order);
        return order;
    }

    /**
     * Removes the oldest pending order and marks it prepared
     * @return oldest pending order
     * @throws NoSuchElementException if no orders are pending
     */
    public Order prepareNextOrder() {
        Order order = pendingOrders.poll();
        if (order == null) {
            throw new NoSuchElementException("No orders are pending");
        }
        order.markPrepared();
        return order;
    }

    /**
     * Serves a prepared order and updates earnings,
     * satisfaction, and completed orders
     * @param orderId
     *        the order to serve
     * @return false if order is invalid or unprepared
     */
    public boolean serveOrder(int orderId) {
        for (Order order : activeOrders) {
            if (order.getOrderId() == orderId) {
                if (order.getStatus() != OrderStatus.PREPARED) {
                    return false;
                }
                order.markServed();
                activeOrders.remove(order);
                money += order.getTotalPrice();
                completedOrders++;
                customerSatisfaction = Math.min(MAX_SATISFACTION,
                    customerSatisfaction + SERVE_BONUS);
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a register by ID
     * @param registerId
     *        the ID to look up
     * @return register
     * @throws IllegalArgumentException if register ID does not exist
     */
    public Register getRegister(int registerId) {
        for (Register r : registers) {
            if (r.getRegisterId() == registerId) {
                return r;
            }
        }
        throw new IllegalArgumentException(
            "No register with ID " + registerId);
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
        Map<Integer, Integer> lines = new LinkedHashMap<>();
        Map<Integer, String> names = new LinkedHashMap<>();
        for (Register r : registers) {
            lines.put(r.getRegisterId(), r.getLineLength());
            names.put(r.getRegisterId(), r.getRegisterName());
        }
        List<String> summaries = new ArrayList<>();
        for (Order o : activeOrders) {
            summaries.add(o.toString());
        }
        return new RestaurantSnapshot(lines, names, summaries, money,
            completedOrders, customerSatisfaction, shiftActive);
    }

    /**
     * Optional stretch-goal method that purchases an upgrade
     * when sufficient money is available.
     * @param cost
     *        the upgrade's price
     * @return true if purchased, false if funds are insufficient
     * @throws IllegalArgumentException if cost is negative
     */
    public boolean purchaseUpgrade(double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        if (cost > money) {
            return false;
        }
        money -= cost;
        return true;
    }

    /**
     * @return the menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * @return current money
     */
    public double getMoney() {
        return money;
    }

    /**
     * @return number of completed orders
     */
    public int getCompletedOrders() {
        return completedOrders;
    }

    /**
     * @return customer satisfaction
     */
    public double getCustomerSatisfaction() {
        return customerSatisfaction;
    }

    /**
     * @return whether a shift is active
     */
    public boolean isShiftActive() {
        return shiftActive;
    }

    /**
     * @return number of orders waiting to be prepared
     */
    public int getPendingOrderCount() {
        return pendingOrders.size();
    }

    /**
     * @return number of orders not yet served
     */
    public int getActiveOrderCount() {
        return activeOrders.size();
    }

    private void requireShift() {
        if (!shiftActive) {
            throw new IllegalStateException("Start a shift first");
        }
    }
}
