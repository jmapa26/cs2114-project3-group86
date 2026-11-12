/**
 * Represents a menu item, including its name, price, and preparation time
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public class FoodItem {
    private String name;
    private double price;
    private int preparationTime;
    
    /**
     * Creates a FoodItem object and initializes fields
     * @param name
     * @param price
     * @param preparationTime
     */
    public FoodItem(String name, double price, int preparationTime) {
        this.name = name;
        this.price = price;
        this.preparationTime = preparationTime;
    }

    /**
     * Returns the name of the FoodItem
     * @return the name of the FoodItem
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }
}