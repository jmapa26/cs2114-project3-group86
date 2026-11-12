/**
 * Receives player commands, calls the appropriate Model methods,
 * handles errors, and updates the View after changes occur
 * 
 * @author Rohail Amjad (ramjad)
 * @version November 12, 2026
 */
public class GameController {
    private final Game game;
    private final ConsoleView consoleView;
    private final WindowView windowView;

    public static void main(String[] args) {
        // TODO
    }

    /**
     * Initializes the game and displays the starting restaurant state
     */
    public void startGame() {
        // TODO
    }

    /**
     * Interprets player commands and calls appropriate Model methods
     * @param command
     */
    public void handleCommand(String command) {
        // TODO
    }

    /**
     * Selects a register and displays its waiting customers
     */
    public void handleRegisterSelection(int registerId) {
        // TODO
    }

    /**
     * Places an order for the selected register and displays the result
     * @param registerId
     * @param items
     */
    public void handleOrder(int registerId, Map<String, Integer> items) {
        // TODO
    }

    /**
     * Prepares the next pending order and updates the display
     */
    public void handlePrepare() {
        // TODO
    }

    /**
     * Attempts to serve the selected order and displays the result
     * @param orderId
     */
    public void handleServe(int orderId) {
        // TODO
    }

    /**
     * Handles an optional upgrade purchase
     * @param cost
     */
    public void handleUpgrade(double cost) {

    }
    
    /**
     * Retrieves a RestaurantSnapshot and updates the active View
     */
    public void refreshViews() {

    }
}