// a bunch of import statements here

import Classes.Order;
// import other classes

/**
 * OrderTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class OrderTest {
    // variables

    // setUp

    /**
     * Tests markPrepared()
     */
    public void testMarkPrepared() {

        // PLACED order changes to PREPARED

        // Already-served order throws IllegalStateException

    }

    /**
     * Tests markServed()
     */
    public void testMarkServed() {

        // PREPARED order changes to SERVED

        // Unprepared order throws IllegalStateException

    }

    /**
     * Tests getTotalPrice()
     */
    public void testGetTotalPrice() {

        // Returns correct total based on prices and quantities

        // Empty order created for isolated testing returns 0;
        // Game rejects empty orders
    }
}