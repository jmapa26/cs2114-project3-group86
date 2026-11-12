import java.util.NoSuchElementException;
import junit.framework.TestCase;

/**
 * LQueueTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class LQueueTest extends TestCase {
    private LQueue queue;
    private Customer a;
    private Customer b;

    /**
     * Sets up an empty queue and two customers
     */
    public void setUp() {
        queue = new LQueue();
        a = new Customer(1, "Ava");
        b = new Customer(2, "Ben");
    }

    /**
     * Tests enqueue()
     */
    public void testEnqueue() {
        // Add two customers; size becomes 2 and insertion order is preserved
        queue.enqueue(a);
        queue.enqueue(b);
        assertEquals(2, queue.size());
        assertSame(a, queue.dequeue());
        assertSame(b, queue.dequeue());

        // Enqueue null; IllegalArgumentException is thrown and size is unchanged
        try {
            queue.enqueue(null);
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertEquals(0, queue.size());
        }
    }

    /**
     * Tests dequeue()
     */
    public void testDequeue() {
        // Remove first of two customers; returns first customer and leaves second
        queue.enqueue(a);
        queue.enqueue(b);
        assertSame(a, queue.dequeue());
        assertEquals(1, queue.size());
        assertSame(b, queue.peek());

        // Empty queue throws NoSuchElementException
        queue.dequeue();
        try {
            queue.dequeue();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertTrue(queue.isEmpty());
        }

        // Queue still works after being emptied
        queue.enqueue(a);
        assertSame(a, queue.peek());
    }

    /**
     * Tests peek()
     */
    public void testPeek() {
        // Returns first customer without changing size
        queue.enqueue(a);
        assertSame(a, queue.peek());
        assertEquals(1, queue.size());

        // Empty queue throws NoSuchElementException
        queue.dequeue();
        try {
            queue.peek();
            fail("Expected NoSuchElementException");
        }
        catch (NoSuchElementException e) {
            assertEquals(0, queue.size());
        }
    }

    /**
     * Tests isEmpty()
     */
    public void testIsEmpty() {
        // Queue containing one customer returns false
        queue.enqueue(a);
        assertFalse(queue.isEmpty());

        // Empty queue returns true
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    /**
     * Tests size()
     */
    public void testSize() {
        // Queue with three customers returns 3
        queue.enqueue(a);
        queue.enqueue(b);
        queue.enqueue(new Customer(3, "Chloe"));
        assertEquals(3, queue.size());

        // Empty queue returns 0
        assertEquals(0, new LQueue().size());
    }
}
