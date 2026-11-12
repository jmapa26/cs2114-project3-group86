// a bunch of import statements here

import Classes.LQueue;
// import other classes

/**
 * LQueueTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class LQueueTest {
    // variables
    
    // setUp

    /**
     * Tests enqueue()
     */
    public void testEnqueue() {

        // Add two customers; size becomes 2 and insertion order is preserved

        // Enqueue null; IllegalArgumentException is thrown and size is unchanged

    }

    /**
     * Tests dequeue()
     */
    public void testDequeue() {
        
        // Remove first of two customers; returns first customer and leaves second

        // Empty queue throws NoSuchElementException

    }

    /**
     * Tests peek()
     */
    public void testPeek() {

        // Returns first customer without changing size

        // Empty queue throws NoSuchElementException

    }

    /**
     * Tests isEmpty()
     */
    public void testIsEmpty() {

        // Queue containing one customer returns false

        // Empty queue returns true

    }

    /**
     * Tests size()
     */
    public void testSize() {

        // Queue with three customers returns 3

        // Empty queue returns 0

    }
}