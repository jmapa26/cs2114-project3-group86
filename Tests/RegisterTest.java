// a bunch of import statements here

import Classes.Register;
// import other classes

/**
 * RegisterTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class RegisterTest {
    // variables

    // setUp

    /**
     * Tests addCustomer()
     */
    public void testAddCustomer() {

        // Adding a customer increases the selected register's line length by one

        // Adding null throws IllegalArgumentException and leaves the line unchanged

    }

    /**
     * Tests nextCustomer()
     */
    public void testNextCustomer() {

        // Returns and removes the first waiting customer

        // Empty register throws NoSuchElementException

    }

    /**
     * Tests peekCustomer()
     */
    public void testPeekCustomer() {

        // Returns first customer without removal

        // Empty register throws NoSuchElementException

    }

    /**
     * Tests getLineLength()
     */
    public void testGetLineLength() {

        // Three waiting customers returns 3

        // Empty register returns 0

    }

    /**
     * Tests isEmpty()
     */
    public void testIsEmpty() {

        // Occupied register returns false

        // Empty register returns true

    }

    /**
     * Tests isOpen()
     */
    public void testIsOpen() {

        // Open register returns true

        // Closed register returns false

    }
}