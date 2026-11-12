import junit.framework.TestCase;

/**
 * MenuTest
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class MenuTest extends TestCase {
    private Menu menu;

    /**
     * Sets up the default menu
     */
    public void setUp() {
        menu = new Menu();
    }

    /**
     * Tests containsItem()
     */
    public void testContainsItem() {
        // Existing food name returns true (case doesn't matter)
        assertTrue(menu.containsItem("Burger"));
        assertTrue(menu.containsItem("burger"));

        // Unknown food name returns false
        assertFalse(menu.containsItem("Pizza"));
        assertFalse(menu.containsItem(null));
    }

    /**
     * Tests getItem()
     */
    public void testGetItem() {
        // Valid name returns correct FoodItem
        FoodItem fries = menu.getItem("Fries");
        assertEquals("Fries", fries.getName());
        assertEquals(2.75, fries.getPrice(), 0.001);

        // Unknown name throws IllegalArgumentException
        try {
            menu.getItem("Pizza");
            fail("Expected IllegalArgumentException");
        }
        catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Pizza"));
        }
    }
}
