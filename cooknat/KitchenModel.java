import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class KitchenModel {
    public static final List<String> CYCLES = List.of("Normal", "Eco", "Heavy"); // used by the view

    // Note: Approach the TODOs in order (2.1 to 2.5, same numbers in KitchenController)
    // Make sure to test and run the app after each one!

    private boolean ovenOn = false;
    private int ovenTemperature = 350;
    private boolean dishwasherOn = false;
    private String dishwasherCycle = "Normal";
    private boolean fridgeOn = true;
    private int fridgeTemperature = 37;
    private final List<String> notifications = new ArrayList<>();
    private final Timer timer = new Timer(true);

    // Used by the view
    public synchronized boolean isOvenOn() { return ovenOn; }
    public synchronized int getOvenTemperature() { return ovenTemperature; }
    public synchronized boolean isDishwasherOn() { return dishwasherOn; }
    public synchronized String getDishwasherCycle() { return dishwasherCycle; }
    public synchronized boolean isFridgeOn() { return fridgeOn; }
    public synchronized int getFridgeTemperature() { return fridgeTemperature; }
    public synchronized List<String> getNotifications() { return new ArrayList<>(notifications); } // a copy

    // Turns "oven", "dishwasher", or "fridge" on or off, incl. your rule from Pt. 1 and the timers
    // Hint: one private method per appliance (e.g., toggleOven())
    public synchronized void toggle(String name) {
        // TODO 2.1 (check steps 1 and 3)
    }

    // Returns an error message, or null if the temperature was set
    public synchronized String setOvenTemperature(int value) {
        return null; // TODO 2.2 (check step 2)
    }

    // Returns an error message, or null if the cycle was set
    public synchronized String setDishwasherCycle(String cycle) {
        return null; // TODO 2.3 (check steps 4 and 5)
    }

    // Turns off everything except the fridge
    public synchronized void allOff() {
        // TODO 2.4 (check step 7)
    }

    // Runs an action after a delay (for the timers), e.g., later(1500, () -> notifications.add("..."));
    private void later(long millis, Runnable action) {
        timer.schedule(new TimerTask() {
            public void run() {
                synchronized (KitchenModel.this) {
                    action.run();
                }
            }
        }, millis);
    }
}
