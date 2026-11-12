import java.util.NoSuchElementException;
import junit.framework.TestCase;

/**
 * RegisterTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class RegisterTest extends TestCase {
    private Register register;
    private Customer a;
    private Customer b;

    /**
     * Sets up an empty register and two customers
     */
    public void setUp() {
        register = new Register(1, "Register 1");
        a = new Customer(1, "Ava");
        b = new Customer(2, "Ben");
    }

    /**
     * Tests addCustomer()
     */
    public void testAddCustomer() {
        // Adding a customer increases the selected register's line length by one
        register.addCustomer(a);
        assertEquals(1, register.getLineLength());

        // Adding null throws IllegalArgumentException and leaves the line unchanged
        try {
            register.addCustomer(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(1, register.getLineLength());
        }

        // Closed register rejects customers
        register.setOpen(false);
        try {
            register.addCustomer(b);
            fail("Expected IllegalStateException");
        }
        catch (IllegalStateException e) {
            assertEquals(1, register.getLineLength());
        }
    }

    /**
     * Tests nextCustomer()
     */
    public void testNextCustomer() {
        // Returns and removes the first waiting customer
        register.addCustomer(a);
        register.addCustomer(b);
        assertSame(a, register.nextCustomer());
        assertEquals(1, register.getLineLength());

        // Empty register throws NoSuchElementException
        register.nextCustomer();
        try {
            register.nextCustomer();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertTrue(register.isEmpty());
        }
    }

    /**
     * Tests peekCustomer()
     */
    public void testPeekCustomer() {
        // Returns first customer without removal
        register.addCustomer(a);
        assertSame(a, register.peekCustomer());
        assertEquals(1, register.getLineLength());

        // Empty register throws NoSuchElementException
        register.nextCustomer();
        try {
            register.peekCustomer();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertEquals(0, register.getLineLength());
        }
    }

    /**
     * Tests getLineLength()
     */
    public void testGetLineLength() {
        // Empty register returns 0
        assertEquals(0, register.getLineLength());

        // Three waiting customers returns 3
        register.addCustomer(a);
        register.addCustomer(b);
        register.addCustomer(new Customer(3, "Chloe"));
        assertEquals(3, register.getLineLength());
    }

    /**
     * Tests isEmpty()
     */
    public void testIsEmpty() {
        // Empty register returns true
        assertTrue(register.isEmpty());

        // Occupied register returns false
        register.addCustomer(a);
        assertFalse(register.isEmpty());
    }

    /**
     * Tests isOpen()
     */
    public void testIsOpen() {
        // Open register returns true
        assertTrue(register.isOpen());

        // Closed register returns false
        register.setOpen(false);
        assertFalse(register.isOpen());
    }
}
