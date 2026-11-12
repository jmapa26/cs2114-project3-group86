import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import junit.framework.TestCase;

/**
 * GameTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class GameTest extends TestCase {
    private static final double DELTA = 0.001;
    private Game game;
    private int nextId;

    /**
     * Sets up the default 3-register game
     */
    public void setUp() {
        game = new Game();
        nextId = 1;
    }

    private Customer newCustomer() {
        Customer c = new Customer(nextId, "C" + nextId);
        nextId++;
        return c;
    }

    private Order placeBurger(int registerId) {
        game.addCustomer(registerId, newCustomer());
        return game.placeOrder(registerId, Map.of("Burger", 1));
    }

    /**
     * Tests startShift()
     */
    public void testStartShift() {
        // Starts inactive shift and returns true
        assertTrue(game.startShift());
        assertTrue(game.isShiftActive());

        // Already-active shift returns false without changing state
        assertFalse(game.startShift());
        assertTrue(game.isShiftActive());
    }

    /**
     * Tests endShift()
     */
    public void testEndShift() {
        // Inactive shift returns false
        assertFalse(game.endShift());

        // Shift with unfinished orders returns false and stays active
        game.startShift();
        Order order = placeBurger(1);
        assertFalse(game.endShift());
        assertTrue(game.isShiftActive());

        // Ends active shift with no unfinished orders and returns true
        game.prepareNextOrder();
        game.serveOrder(order.getOrderId());
        assertTrue(game.endShift());
        assertFalse(game.isShiftActive());
    }

    /**
     * Tests addCustomer()
     */
    public void testAddCustomer() {
        game.startShift();

        // Adds customer to Register 2; only Register 2 line increases
        game.addCustomer(2, newCustomer());
        assertEquals(0, game.getRegister(1).getLineLength());
        assertEquals(1, game.getRegister(2).getLineLength());
        assertEquals(0, game.getRegister(3).getLineLength());

        // Invalid register ID throws IllegalArgumentException; no lines change
        try {
            game.addCustomer(99, newCustomer());
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(0, game.getRegister(1).getLineLength());
            assertEquals(1, game.getRegister(2).getLineLength());
            assertEquals(0, game.getRegister(3).getLineLength());
        }

        // No active shift throws IllegalStateException
        game.endShift();
        try {
            game.addCustomer(1, newCustomer());
            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException e) {
            assertEquals(0, game.getRegister(1).getLineLength());
        }
    }

    /**
     * Tests placeOrder()
     */
    public void testPlaceOrder() {
        game.startShift();

        // Valid order removes first customer from selected line
        // and adds an order to pendingOrders
        Customer first = newCustomer();
        game.addCustomer(1, first);
        game.addCustomer(1, newCustomer());
        Order order = game.placeOrder(1, Map.of("Burger", 2));
        assertSame(first, order.getCustomer());
        assertEquals(1, game.getRegister(1).getLineLength());
        assertEquals(1, game.getPendingOrderCount());
        assertEquals(13.00, order.getTotalPrice(), DELTA);

        // Unknown menu item throws IllegalArgumentException;
        // customer remains in line
        try {
            game.placeOrder(1, Map.of("Pizza", 1));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(1, game.getRegister(1).getLineLength());
            assertEquals(1, game.getPendingOrderCount());
        }

        // Zero quantity is rejected; customer remains in line
        try {
            game.placeOrder(1, Map.of("Burger", 0));
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(1, game.getRegister(1).getLineLength());
        }

        // Empty register throws NoSuchElementException
        try {
            game.placeOrder(2, Map.of("Burger", 1));
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertEquals(1, game.getPendingOrderCount());
        }
    }

    /**
     * Tests prepareNextOrder()
     */
    public void testPrepareNextOrder() {
        // Empty pending queue throws NoSuchElementException
        try {
            game.prepareNextOrder();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertEquals(0, game.getPendingOrderCount());
        }

        // First pending order becomes PREPARED (shared FIFO across registers)
        game.startShift();
        Order first = placeBurger(3);
        Order second = placeBurger(1);
        assertSame(first, game.prepareNextOrder());
        assertEquals(OrderStatus.PREPARED, first.getStatus());
        assertEquals(OrderStatus.PLACED, second.getStatus());
        assertEquals(1, game.getPendingOrderCount());
    }

    /**
     * Tests serveOrder()
     */
    public void testServeOrder() {
        // Invalid order ID returns false and statistics remain unchanged
        assertFalse(game.serveOrder(42));
        assertEquals(0, game.getMoney(), DELTA);
        assertEquals(0, game.getCompletedOrders());

        // Unprepared order returns false
        game.startShift();
        Order order = placeBurger(1);
        assertFalse(game.serveOrder(order.getOrderId()));
        assertEquals(0, game.getMoney(), DELTA);

        // Prepared order is served; money and completedOrders increase correctly
        game.prepareNextOrder();
        assertTrue(game.serveOrder(order.getOrderId()));
        assertEquals(6.50, game.getMoney(), DELTA);
        assertEquals(1, game.getCompletedOrders());
        assertEquals(Game.START_SATISFACTION + Game.SERVE_BONUS,
            game.getCustomerSatisfaction(), DELTA);
        assertEquals(OrderStatus.SERVED, order.getStatus());

        // Serving the same order twice returns false
        assertFalse(game.serveOrder(order.getOrderId()));
        assertEquals(1, game.getCompletedOrders());
    }

    /**
     * Tests getRegister()
     */
    public void testGetRegister() {
        // Existing ID returns correct Register
        assertEquals(2, game.getRegister(2).getRegisterId());
        assertEquals("Drive-Thru", game.getRegister(3).getRegisterName());

        // Unknown ID throws IllegalArgumentException
        try {
            game.getRegister(0);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(3, game.getRegisterCount());
        }
    }

    /**
     * Tests getRegisterCount()
     */
    public void testGetRegisterCount() {
        // Three initialized registers returns 3
        assertEquals(3, game.getRegisterCount());

        // Game configured with zero registers returns 0
        Game empty = new Game(new Menu(), List.of());
        assertEquals(0, empty.getRegisterCount());
    }

    /**
     * Tests getRestaurantSnapshot()
     */
    public void testGetSnapshot() {
        // Snapshot from an inactive game shows initial values without changing state
        RestaurantSnapshot initial = game.getRestaurantSnapshot();
        assertEquals(0, initial.getMoney(), DELTA);
        assertEquals(0, initial.getCompletedOrders());
        assertFalse(initial.isShiftActive());
        assertFalse(game.isShiftActive());

        // Returns current register lengths, money, and orders accurately
        game.startShift();
        game.addCustomer(1, newCustomer());
        game.addCustomer(1, newCustomer());
        placeBurger(3);
        RestaurantSnapshot snap = game.getRestaurantSnapshot();
        assertEquals(Integer.valueOf(2), snap.getRegisterLines().get(1));
        assertEquals(Integer.valueOf(0), snap.getRegisterLines().get(3));
        assertEquals(1, snap.getActiveOrderSummaries().size());
        assertTrue(snap.isShiftActive());

        // Snapshot can't be modified
        try {
            snap.getRegisterLines().put(1, 99);
            fail("Expected UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e) {
            assertEquals(2, game.getRegister(1).getLineLength());
        }
    }

    /**
     * Tests purchaseUpgrade()
     */
    public void testPurchaseUpgrade() {
        // Insufficient money returns false without deducting funds
        assertFalse(game.purchaseUpgrade(10));
        assertEquals(0, game.getMoney(), DELTA);

        // Affordable purchase deducts the correct amount and returns true
        game.startShift();
        Order order = placeBurger(1);
        game.prepareNextOrder();
        game.serveOrder(order.getOrderId());
        assertTrue(game.purchaseUpgrade(5.00));
        assertEquals(1.50, game.getMoney(), DELTA);

        // Negative cost throws IllegalArgumentException
        try {
            game.purchaseUpgrade(-1);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(1.50, game.getMoney(), DELTA);
        }
    }
}
