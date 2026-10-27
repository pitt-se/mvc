import java.util.List;

// Builds HTML from the model
// NO NEED TO CHANGE ANYTHING: please review how page() reads the data with the model's getters
public class KitchenView {
    private static final String STYLE = """
        <style>
          * { box-sizing: border-box; }
            body { font-family: system-ui, -apple-system, "Segoe UI", sans-serif; background: #f3f1ec; color: #1c1c1c;
                max-width: 920px; margin: 0 auto; padding: 24px 16px; }
            header { background: #1c1c1c; border-radius: 16px; padding: 20px 24px; margin: 0 0 20px; }
            h1 { margin: 0; color: #ffb81c; letter-spacing: 0.5px; }
            .subtitle { color: #c9c4b8; margin: 4px 0 0; }
            h2 { margin: 28px 0 12px; }
            .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 16px; }
            .card { background: #fff; border-radius: 16px; padding: 18px 20px; }
            .card h3 { display: flex; justify-content: space-between; align-items: center; margin: 0 0 10px; }
            .status { display: flex; align-items: center; gap: 6px; font-size: 13px; font-weight: 600; color: #6b6b6b; }
            .dot { width: 12px; height: 12px; border-radius: 50%; background: #d64545; }
            .on .dot { background: #2fb36d; }
            .value { font-size: 30px; font-weight: 700; margin: 0 0 14px; }
            form { display: flex; gap: 8px; margin: 0 0 12px; }
            input, select { flex: 1; min-width: 0; padding: 8px 10px; border: none; border-radius: 10px; background: #f3f1ec;
                            color: #1c1c1c; font: inherit; }
            button, .btn { display: inline-block; padding: 8px 16px; border: none; border-radius: 10px; background: #ebe8e1;
                        color: #1c1c1c; font: inherit; font-weight: 600; text-decoration: none; cursor: pointer; }
            button:hover, .btn:hover { background: #dedad0; }
            .btn.primary { background: #ffb81c; }
            .btn.primary:hover { background: #f0a800; }
            .btn.dark { background: #1c1c1c; color: #ffb81c; }
            .btn.dark:hover { background: #333; }
            .toolbar { margin: 20px 0 0; }
            .error { display: flex; justify-content: space-between; align-items: center; gap: 12px; background: #fde4e4;
                    color: #8a1f1f; padding: 12px 16px; border-radius: 12px; margin: 0 0 16px; }
            .notifications { background: #fff; border-radius: 16px; padding: 6px 20px; list-style: none; margin: 0; }
            .notifications li { padding: 10px 0; }
            .notifications li + li { border-top: 1px solid #f0ede6; }
        </style>
        """;

    public String page(KitchenModel model, String error) {
        StringBuilder html = new StringBuilder();
        html.append(head());
        html.append("<header><h1>Cook N'At Kitchen</h1><p class='subtitle'>Your smart kitchen at a glance</p></header>");

        if (!error.isEmpty()) {
            html.append("<div class='error'><span>" + escape(error) + "</span><a class='btn' href='/'>OK</a></div>");
        }

        html.append("<div class='grid'>");
        html.append(card("Oven", "oven", model.isOvenOn(),
                "<p class='value'>" + model.getOvenTemperature() + " F</p>"
                + "<form action='/temperature'><input name='value' type='number' value='" + model.getOvenTemperature() + "'>"
                + "<button>Set</button></form>"));

        StringBuilder options = new StringBuilder();
        for (String cycle : KitchenModel.CYCLES) {
            options.append("<option" + (cycle.equals(model.getDishwasherCycle()) ? " selected" : "") + ">" + cycle + "</option>");
        }
        html.append(card("Dishwasher", "dishwasher", model.isDishwasherOn(),
                "<p class='value'>" + model.getDishwasherCycle() + "</p>"
                + "<form action='/cycle'><select name='value'>" + options + "</select><button>Set</button></form>"));

        html.append(card("Fridge", "fridge", model.isFridgeOn(),
                "<p class='value'>" + model.getFridgeTemperature() + " F</p>"));
        html.append("</div>");

        html.append("<p class='toolbar'><a class='btn dark' href='/alloff'>Turn everything off</a></p>");
        html.append("<h2>Notifications</h2><ul class='notifications'>");
        List<String> notifications = model.getNotifications();
        for (int i = notifications.size() - 1; i >= 0; i--) {
            html.append("<li>" + escape(notifications.get(i)) + "</li>");
        }
        html.append("</ul>");

        if (error.isEmpty()) {
            html.append("<script>setInterval(function () { if (document.activeElement == document.body) location.href = '/'; }, 1000);</script>");
        }
        html.append("</body></html>");
        return html.toString();
    }

    public String confirmFridgeOff() {
        return head()
                + "<div class='card'><h3>Turn off the fridge?</h3><p>Your food may spoil.</p>"
                + "<a class='btn primary' href='/toggle?name=fridge&confirmed=yes'>Yes, turn it off</a> "
                + "<a class='btn' href='/'>Cancel</a></div></body></html>";
    }

    private String head() {
        return "<!DOCTYPE html><html><head><meta charset='utf-8'><meta name='viewport' content='width=device-width, initial-scale=1'>"
                + "<title>Cook N'At Kitchen</title>" + STYLE + "</head><body>";
    }

    private String card(String title, String name, boolean on, String content) {
        return "<div class='card " + (on ? "on" : "off") + "'><h3>" + title + " <span class='status'><span class='dot'></span>" + (on ? "ON" : "OFF") + "</span></h3>"
                + content
                + "<a class='btn" + (on ? "" : " primary") + "' href='/toggle?name=" + name + "'>" + (on ? "Turn off" : "Turn on") + "</a></div>";
    }

    private String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;");
    }
}
