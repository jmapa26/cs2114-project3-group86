import java.util.Scanner;

/**
 * Displays restaurant information, register queues,
 * menus, orders, and statistics through the console
 *
 * @author Jacob Mapa (jmapa26)
 * @version November 12, 2026
 */
public class ConsoleView {
    private GameController controller;

    /**
     * Connects this view to the controller that handles player commands.
     * @param gameController the controller for this view
     * @throws IllegalArgumentException if the controller is null
     */
    public void setController(GameController gameController) {
        if (gameController == null) {
            throw new IllegalArgumentException("Controller cannot be null");
        }
        controller = gameController;
    }

    /**
     * Prints the current restaurant status.
     * @param status formatted restaurant status
     */
    public void displayStatus(String status) {
        System.out.print(status);
        if (!status.endsWith(System.lineSeparator())) {
            System.out.println();
        }
    }

    /**
     * Prints a message from the controller.
     * @param message the message to display
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Reads commands from standard input and sends them to the controller.
     * The loop ends when the player enters "quit" or input reaches EOF.
     * @throws IllegalStateException if no controller has been connected
     */
    public void run() {
        if (controller == null) {
            throw new IllegalStateException(
                "ConsoleView must be connected to a controller before running");
        }

        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            System.out.flush();
            if (!input.hasNextLine()) {
                break;
            }

            String command = input.nextLine();
            if ("quit".equalsIgnoreCase(command.trim())) {
                displayMessage("Goodbye!");
                break;
            }
            controller.handleCommand(command);
        }
    }
}