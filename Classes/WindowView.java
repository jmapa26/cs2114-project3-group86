import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Displays a graphical interface using Java Swing,
 * including register selection, customer lines,
 * available orders, and restaurant statistics
 *
 * @author Jacob Mapa (jmapa26)
 * @version November 12, 2026
 */
public class WindowView {
    private GameController controller;
    private JFrame frame;
    private JTextArea statusArea;
    private JTextArea messageArea;
    private JTextField commandField;
    private JComboBox<Integer> registerSelector;
    private String pendingStatus;
    private final StringBuilder pendingMessages = new StringBuilder();

    /**
     * Connects this view to the controller that handles player actions.
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
     * Creates and displays the restaurant window.
     * @param registerCount number of registers to make available
     * @throws IllegalArgumentException if registerCount is negative
     * @throws IllegalStateException if no controller is connected or the
     *         window cannot be created
     */
    public void show(int registerCount) {
        if (registerCount < 0) {
            throw new IllegalArgumentException(
                "Register count cannot be negative");
        }
        if (controller == null) {
            throw new IllegalStateException(
                "WindowView must be connected to a controller before showing");
        }

        Runnable createWindow = () -> createWindow(registerCount);
        if (SwingUtilities.isEventDispatchThread()) {
            createWindow.run();
        }
        else {
            try {
                SwingUtilities.invokeAndWait(createWindow);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(
                    "Interrupted while opening the restaurant window", e);
            }
            catch (InvocationTargetException e) {
                throw new IllegalStateException(
                    "Could not open the restaurant window", e.getCause());
            }
        }
    }

    /**
     * Displays the latest restaurant status snapshot.
     * @param status formatted restaurant status
     */
    public void displayStatus(String status) {
        updateOnEventThread(() -> {
            if (statusArea != null) {
                statusArea.setText(status);
                statusArea.setCaretPosition(0);
            }
            else {
                pendingStatus = status;
            }
        });
    }

    /**
     * Adds a controller message to the message history.
     * @param message the message to display
     */
    public void displayMessage(String message) {
        updateOnEventThread(() -> {
            String entry = message + System.lineSeparator();
            if (messageArea != null) {
                messageArea.append(entry);
                messageArea.setCaretPosition(
                    messageArea.getDocument().getLength());
            }
            else {
                pendingMessages.append(entry);
            }
        });
    }

    private void createWindow(int registerCount) {
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        statusArea = new JTextArea(14, 60);
        statusArea.setEditable(false);
        statusArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statusArea.setText(pendingStatus == null
            ? "Restaurant status will appear here." : pendingStatus);
        JScrollPane statusScrollPane = new JScrollPane(statusArea);
        statusScrollPane.setBorder(
            BorderFactory.createTitledBorder("Restaurant status"));

        messageArea = new JTextArea(4, 60);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setText(pendingMessages.toString());
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setBorder(
            BorderFactory.createTitledBorder("Messages"));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton startButton = new JButton("Start shift");
        startButton.addActionListener(
            event -> controller.handleCommand("start"));
        actions.add(startButton);

        JButton endButton = new JButton("End shift");
        endButton.addActionListener(
            event -> controller.handleCommand("end"));
        actions.add(endButton);

        JButton prepareButton = new JButton("Prepare next order");
        prepareButton.addActionListener(event -> controller.handlePrepare());
        actions.add(prepareButton);

        actions.add(new JLabel("Register:"));
        registerSelector = new JComboBox<>();
        for (int id = 1; id <= registerCount; id++) {
            registerSelector.addItem(id);
        }
        actions.add(registerSelector);

        JButton arriveButton = new JButton("Customer arrives");
        arriveButton.addActionListener(event -> {
            Integer registerId = (Integer)registerSelector.getSelectedItem();
            if (registerId != null) {
                controller.handleArrival(registerId);
            }
        });
        actions.add(arriveButton);

        JButton selectButton = new JButton("Select register");
        selectButton.addActionListener(event -> {
            Integer registerId = (Integer)registerSelector.getSelectedItem();
            if (registerId != null) {
                controller.handleRegisterSelection(registerId);
            }
        });
        actions.add(selectButton);

        if (registerCount == 0) {
            arriveButton.setEnabled(false);
            selectButton.setEnabled(false);
        }

        commandField = new JTextField();
        commandField.setToolTipText(
            "For example: order 1 Burger=2, serve 1, upgrade 5, or help");
        commandField.addActionListener(event -> submitCommand());
        JButton sendButton = new JButton("Send command");
        sendButton.addActionListener(event -> submitCommand());
        JPanel commandPanel = new JPanel(new BorderLayout(6, 0));
        commandPanel.add(new JLabel("Command:"), BorderLayout.WEST);
        commandPanel.add(commandField, BorderLayout.CENTER);
        commandPanel.add(sendButton, BorderLayout.EAST);

        JPanel bottom = new JPanel(new BorderLayout(0, 6));
        bottom.add(messageScrollPane, BorderLayout.CENTER);
        bottom.add(commandPanel, BorderLayout.SOUTH);

        JPanel content = new JPanel(new BorderLayout(0, 8));
        content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        content.add(actions, BorderLayout.NORTH);
        content.add(statusScrollPane, BorderLayout.CENTER);
        content.add(bottom, BorderLayout.SOUTH);

        frame = new JFrame("Restaurant Simulator");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(content);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    private void submitCommand() {
        String command = commandField.getText();
        commandField.setText("");
        if ("quit".equalsIgnoreCase(command.trim())) {
            displayMessage("Goodbye!");
            frame.dispose();
        }
        else {
            controller.handleCommand(command);
        }
    }

    private static void updateOnEventThread(Runnable update) {
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        }
        else {
            SwingUtilities.invokeLater(update);
        }
    }
}