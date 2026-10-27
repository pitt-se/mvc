import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// Handles the requests: reads the form values, calls the model, and sends the page from the view
public class KitchenController implements HttpHandler {
    private final KitchenModel model;
    private final KitchenView view;

    public KitchenController(KitchenModel model, KitchenView view) {
        this.model = model;
        this.view = view;
    }

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getRawQuery();

        // TODO 2.1: handle /toggle (call model.toggle(...))
        // TODO 2.2: handle /temperature
        // TODO 2.3: handle /cycle
        // TODO 2.4: handle /alloff
        // Hint: read values from the form with param(query, "value") and number(...),
        // then redirect(exchange, "/"), or redirectWith(exchange, error) if the model returned an error
        // TODO 2.5 (check step 6): if the fridge is on and param(query, "confirmed") is not "yes", send view.confirmFridgeOff() instead

        send(exchange, view.page(model, param(query, "error")));
    }

    // NO NEED TO CHANGE ANYTHING BELOW
    
    private void redirectWith(HttpExchange exchange, String error) throws IOException {
        if (error == null) redirect(exchange, "/");
        else redirect(exchange, "/?error=" + URLEncoder.encode(error, StandardCharsets.UTF_8));
    }

    private int number(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String param(String query, String key) {
        if (query == null) return "";
        for (String pair : query.split("&")) {
            String[] parts = pair.split("=", 2);
            if (parts[0].equals(key) && parts.length == 2) return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
        }
        return "";
    }

    private void send(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }

    private void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(303, -1);
        exchange.close();
    }
}
