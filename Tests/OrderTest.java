import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 * OrderTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class OrderTest extends TestCase {
    private Order order;
    private Customer customer;
    private Menu menu;

    /**
     * Sets up an order for 2 Burgers and 1 Soda ($14.75)
     */
    public void setUp() {
        menu = new Menu();
        customer = new Customer(1, "Ava");
        Map<FoodItem, Integer> items = new HashMap<>();
        items.put(menu.getItem("Burger"), 2);
        items.put(menu.getItem("Soda"), 1);
        order = new Order(1, customer, items);
    }

    /**
     * Tests markPrepared()
     */
    public void testMarkPrepared() {
        // PLACED order changes to PREPARED
        assertEquals(OrderStatus.PLACED, order.getStatus());
        order.markPrepared();
        assertEquals(OrderStatus.PREPARED, order.getStatus());

        // Already-served order throws IllegalStateException
        order.markServed();
        try {
            order.markPrepared();
            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException e) {
            assertEquals(OrderStatus.SERVED, order.getStatus());
        }
    }

    /**
     * Tests markServed()
     */
    public void testMarkServed() {
        // Unprepared order throws IllegalStateException
        try {
            order.markServed();
            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException e) {
            assertEquals(OrderStatus.PLACED, order.getStatus());
        }

        // PREPARED order changes to SERVED
        order.markPrepared();
        order.markServed();
        assertEquals(OrderStatus.SERVED, order.getStatus());
    }

    /**
     * Tests getTotalPrice()
     */
    public void testGetTotalPrice() {
        // Returns correct total based on prices and quantities
        assertEquals(14.75, order.getTotalPrice(), 0.001);

        // Empty order created for isolated testing returns 0;
        // Game rejects empty orders
        Order empty = new Order(2, customer, new HashMap<>());
        assertEquals(0.0, empty.getTotalPrice(), 0.001);
    }
}
