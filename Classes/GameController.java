import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Receives player commands, calls the appropriate Model methods,
 * handles errors, and updates the View after changes occur
 *
 * @author Rohail Amjad (ramjad)
 * @version November 12, 2026
 */
public class GameController {
    private static final String[] NAMES = {"Ava", "Ben", "Chloe", "Dev",
        "Ella", "Finn", "Grace", "Hassan", "Iris", "Jay", "Kai", "Lena"};

    private static final String HELP = "Commands:\n"
        + "  start                     start a shift\n"
        + "  end                       end the shift\n"
        + "  arrive <reg>              a customer joins a register line\n"
        + "  select <reg>              show who is next at a register\n"
        + "  order <reg> Item=qty ...  e.g. order 1 Burger=2 Soda=1\n"
        + "  prepare                   prepare the oldest pending order\n"
        + "  serve <orderId>           serve a prepared order\n"
        + "  upgrade <cost>            buy an upgrade\n"
        + "  status | help | quit";

    private final Game game;
    private final ConsoleView consoleView;
    private final WindowView windowView;
    private int nextCustomerId;

    /**
     * Run with no arguments for the console, or "gui" for the window.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Game game = new Game();
        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            WindowView window = new WindowView();
            GameController controller = new GameController(game, null, window);
            window.show(game.getRegisterCount());
            controller.startGame();
        }
        else {
            ConsoleView console = new ConsoleView();
            GameController controller = new GameController(game, console, null);
            controller.startGame();
            console.run();
        }
    }

    /**
     * Creates a controller. Either view may be null if it isn't used.
     * @param game the restaurant Model
     * @param consoleView the console interface
     * @param windowView the graphical interface
     */
    public GameController(Game game, ConsoleView consoleView,
        WindowView windowView) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        this.game = game;
        this.consoleView = consoleView;
        this.windowView = windowView;
        this.nextCustomerId = 1;
        if (consoleView != null) {
            consoleView.setController(this);
        }
        if (windowView != null) {
            windowView.setController(this);
        }
    }

    /**
     * Initializes the game and displays the starting restaurant state
     */
    public void startGame() {
        showMessage("Welcome to Restaurant Simulator!\n" + HELP);
        refreshViews();
    }

    /**
     * Interprets player commands and calls appropriate Model methods
     * @param command the text the player entered
     */
    public void handleCommand(String command) {
        if (command == null || command.isBlank()) {
            showMessage("Please enter a command. Type 'help' for options.");
            return;
        }
        String[] parts = command.trim().split("\\s+");
        try {
            switch (parts[0].toLowerCase()) {
                case "start":
                    showMessage(game.startShift() ? "Shift started."
                        : "A shift is already running.");
                    refreshViews();
                    break;
                case "end":
                    showMessage(game.endShift() ? "Shift ended."
                        : "Can't end: no active shift or orders unfinished.");
                    refreshViews();
                    break;
                case "arrive":
                    handleArrival(parseInt(parts, 1, "register ID"));
                    break;
                case "select":
                    handleRegisterSelection(parseInt(parts, 1, "register ID"));
                    break;
                case "order":
                    handleOrder(parseInt(parts, 1, "register ID"),
                        parseItems(parts));
                    break;
                case "prepare":
                    handlePrepare();
                    break;
                case "serve":
                    handleServe(parseInt(parts, 1, "order ID"));
                    break;
                case "upgrade":
                    handleUpgrade(parseDouble(parts, 1, "cost"));
                    break;
                case "status":
                    refreshViews();
                    break;
                case "help":
                    showMessage(HELP);
                    break;
                default:
                    showMessage("Unknown command '" + parts[0]
                        + "'. Type 'help' for options.");
            }
        }
        catch (IllegalArgumentException e) {
            showMessage("Error: " + e.getMessage());
        }
    }

    /**
     * A new customer arrives at a register
     * @param registerId the register they join
     */
    public void handleArrival(int registerId) {
        try {
            String name = NAMES[(nextCustomerId - 1) % NAMES.length];
            Customer customer = new Customer(nextCustomerId, name);
            game.addCustomer(registerId, customer);
            nextCustomerId++;
            showMessage(name + " joined "
                + game.getRegister(registerId).getRegisterName() + ".");
        }
        catch (IllegalArgumentException | IllegalStateException e) {
            showMessage("Error: " + e.getMessage());
        }
        refreshViews();
    }

    /**
     * Selects a register and displays its waiting customers
     * @param registerId the selected register
     */
    public void handleRegisterSelection(int registerId) {
        try {
            Register register = game.getRegister(registerId);
            Customer first = register.peekCustomer();
            showMessage(register.getRegisterName() + ": " + first.getName()
                + " is next (" + register.getLineLength() + " waiting).");
        }
        catch (IllegalArgumentException e) {
            showMessage("Error: " + e.getMessage()
                + ". Pick an existing register.");
        }
        catch (NoSuchElementException e) {
            showMessage("No customers are waiting there. "
                + "Try another register.");
        }
    }

    /**
     * Places an order for the selected register and displays the result
     * @param registerId the register to take the order from
     * @param items item names mapped to quantities
     */
    public void handleOrder(int registerId, Map<String, Integer> items) {
        try {
            Order order = game.placeOrder(registerId, items);
            showMessage(String.format("Placed order #%d for %s ($%.2f).",
                order.getOrderId(), order.getCustomer().getName(),
                order.getTotalPrice()));
        }
        catch (IllegalArgumentException | IllegalStateException
            | NoSuchElementException e) {
            showMessage("Error: " + e.getMessage());
        }
        refreshViews();
    }

    /**
     * Prepares the next pending order and updates the display
     */
    public void handlePrepare() {
        try {
            Order order = game.prepareNextOrder();
            showMessage("Prepared order #" + order.getOrderId()
                + ". Serve it with: serve " + order.getOrderId());
        }
        catch (NoSuchElementException e) {
            showMessage("No orders are waiting to be prepared.");
        }
        refreshViews();
    }

    /**
     * Attempts to serve the selected order and displays the result
     * @param orderId the order to serve
     */
    public void handleServe(int orderId) {
        showMessage(game.serveOrder(orderId) ? "Served order #" + orderId + "!"
            : "Order #" + orderId + " doesn't exist or isn't prepared yet.");
        refreshViews();
    }

    /**
     * Handles an optional upgrade purchase
     * @param cost the upgrade's price
     */
    public void handleUpgrade(double cost) {
        try {
            showMessage(game.purchaseUpgrade(cost) ? "Upgrade purchased!"
                : "Not enough money for that upgrade.");
        }
        catch (IllegalArgumentException e) {
            showMessage("Error: " + e.getMessage());
        }
        refreshViews();
    }

    /**
     * Retrieves a RestaurantSnapshot and updates the active View
     */
    public void refreshViews() {
        String status = game.getRestaurantSnapshot().format();
        if (consoleView != null) {
            consoleView.displayStatus(status);
        }
        if (windowView != null) {
            windowView.displayStatus(status);
        }
    }

    private void showMessage(String message) {
        if (consoleView != null) {
            consoleView.displayMessage(message);
        }
        if (windowView != null) {
            windowView.displayMessage(message);
        }
    }

    private static int parseInt(String[] parts, int index, String what) {
        if (parts.length <= index) {
            throw new IllegalArgumentException("Missing " + what);
        }
        try {
            return Integer.parseInt(parts[index]);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "'" + parts[index] + "' is not a valid " + what);
        }
    }

    private static double parseDouble(String[] parts, int index,
        String what) {
        if (parts.length <= index) {
            throw new IllegalArgumentException("Missing " + what);
        }
        try {
            return Double.parseDouble(parts[index]);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "'" + parts[index] + "' is not a valid " + what);
        }
    }

    /** Turns "order 1 Burger=2 Soda" into {Burger=2, Soda=1}. */
    private static Map<String, Integer> parseItems(String[] parts) {
        if (parts.length < 3) {
            throw new IllegalArgumentException(
                "List items like: order 1 Burger=2 Soda=1");
        }
        Map<String, Integer> items = new LinkedHashMap<>();
        for (int i = 2; i < parts.length; i++) {
            String[] pair = parts[i].split("=");
            if (pair.length > 2) {
                throw new IllegalArgumentException("Bad item: " + parts[i]);
            }
            int qty = 1;
            if (pair.length == 2) {
                try {
                    qty = Integer.parseInt(pair[1]);
                }
                catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                        "'" + pair[1] + "' is not a valid quantity");
                }
            }
            items.merge(pair[0], qty, Integer::sum);
        }
        return items;
    }
}
