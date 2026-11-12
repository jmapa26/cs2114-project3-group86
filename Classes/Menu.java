import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores available food items and provides methods for looking up menu selections
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class Menu {
    private ArrayList<FoodItem> items;

    /**
     * Creates a Menu with the default food items
     */
    public Menu() {
        items = new ArrayList<>();
        items.add(new FoodItem("Burger", 6.50, 4));
        items.add(new FoodItem("Fries", 2.75, 2));
        items.add(new FoodItem("Nuggets", 4.25, 3));
        items.add(new FoodItem("Salad", 5.00, 2));
        items.add(new FoodItem("Soda", 1.75, 1));
        items.add(new FoodItem("Shake", 3.50, 2));
    }

    /**
     * Returns whether the Menu contains the FoodItem
     * with the name of the String argument (ignores case)
     * @param name
     *        the item name to look for
     * @return true if the item exists; false for null or unknown names
     */
    public boolean containsItem(String name) {
        return find(name) != null;
    }

    /**
     * Returns the FoodItem whose name matches that of the String argument
     * @param name
     *        the item name to look for
     * @return the matching FoodItem
     * @throws IllegalArgumentException if name is invalid
     */
    public FoodItem getItem(String name) {
        FoodItem item = find(name);
        if (item == null) {
            throw new IllegalArgumentException("Not on the menu: " + name);
        }
        return item;
    }

    /**
     * Returns a read-only list of every FoodItem
     * @return the menu items
     */
    public List<FoodItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private FoodItem find(String name) {
        if (name == null) {
            return null;
        }
        for (FoodItem item : items) {
            if (item.getName().equalsIgnoreCase(name.trim())) {
                return item;
            }
        }
        return null;
    }
}
