import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores an immutable copy of restaurant information
 * that the Controller can use to update the View
 * 
 * @author David Solyom (dsolyom)
 * @version November 12, 2026
 */
public final class RestaurantSnapshot {
    private final Map<Integer, Integer> registerLines;
    private final Map<Integer, String> registerNames;
    private final List<String> activeOrderSummaries;
    private final double money;
    private final int completedOrders;
    private final double customerSatisfaction;
    private final boolean shiftActive;

    /**
     * Creates a snapshot. All collections are copied so later game
     * changes don't affect it.
     * @param registerLines register ID mapped to line length
     * @param registerNames register ID mapped to name
     * @param activeOrderSummaries one line per unfinished order
     * @param money current earnings
     * @param completedOrders number of served orders
     * @param customerSatisfaction satisfaction from 0 to 100
     * @param shiftActive whether a shift is running
     */
    public RestaurantSnapshot(Map<Integer, Integer> registerLines,
        Map<Integer, String> registerNames, List<String> activeOrderSummaries,
        double money, int completedOrders, double customerSatisfaction,
        boolean shiftActive) {
        this.registerLines = Collections.unmodifiableMap(
            new LinkedHashMap<>(registerLines));
        this.registerNames = Collections.unmodifiableMap(
            new LinkedHashMap<>(registerNames));
        this.activeOrderSummaries = List.copyOf(activeOrderSummaries);
        this.money = money;
        this.completedOrders = completedOrders;
        this.customerSatisfaction = customerSatisfaction;
        this.shiftActive = shiftActive;
    }

    /**
     * @return register ID mapped to line length (read-only)
     */
    public Map<Integer, Integer> getRegisterLines() {
        return registerLines;
    }

    /**
     * @return register ID mapped to name (read-only)
     */
    public Map<Integer, String> getRegisterNames() {
        return registerNames;
    }

    /**
     * @return summaries of unfinished orders (read-only)
     */
    public List<String> getActiveOrderSummaries() {
        return activeOrderSummaries;
    }

    /**
     * @return current money
     */
    public double getMoney() {
        return money;
    }

    /**
     * @return number of completed orders
     */
    public int getCompletedOrders() {
        return completedOrders;
    }

    /**
     * @return customer satisfaction
     */
    public double getCustomerSatisfaction() {
        return customerSatisfaction;
    }

    /**
     * @return whether a shift is active
     */
    public boolean isShiftActive() {
        return shiftActive;
    }

    /**
     * Returns a multi-line text version used by both Views
     * @return formatted restaurant status
     */
    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
            "Shift: %s | Money: $%.2f | Completed: %d | Satisfaction: %.0f%%%n",
            shiftActive ? "ACTIVE" : "inactive", money, completedOrders,
            customerSatisfaction));
        sb.append("Registers:\n");
        for (Map.Entry<Integer, Integer> e : registerLines.entrySet()) {
            sb.append(String.format("  [%d] %-12s line: %d%n", e.getKey(),
                registerNames.get(e.getKey()), e.getValue()));
        }
        sb.append("Active orders:\n");
        if (activeOrderSummaries.isEmpty()) {
            sb.append("  (none)\n");
        }
        for (String s : activeOrderSummaries) {
            sb.append("  ").append(s).append('\n');
        }
        return sb.toString();
    }
}
