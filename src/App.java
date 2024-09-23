import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.fastcgi.FCGIInterface;
import com.sun.tools.jconsole.JConsoleContext;

public class App {
    private static final String RESPONSE_TEMPLATE = "Content-Type: application/json\n" +
            "Content-Length: %d\n\n%s";

    public static void main (String args[]) {

        while(new FCGIInterface().FCGIaccept() >= 0) {
            long Start = System.currentTimeMillis();
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Start);

            try {
//                HashMap<String, String> params = Params.parse(FCGIInterface.request.params.getProperty("QUERY_STRING"));
                HashMap<String, String> params = Params.parse(readRequestBody());
                int x = Integer.parseInt(params.get("x"));
                float y = Float.parseFloat(params.get("y"));
                float r = Float.parseFloat(params.get("r"));

                if (Validator.validateX(x) && Validator.validateY(y) && Validator.validateR(r)) {
//                    String jsonResult = String.format("{\"isCorrect\": %b, \"startTime\": \"%s\", \"duration\": %d}", Checker.hit(x, y, r), formatter.format(calendar.getTime()), System.currentTimeMillis() - Start);
//                    sendJson(String.format("{\"result\": %s, \"duration\": %d}", jsonResult, System.currentTimeMillis() - Start ));

                    String jsonResult = String.format("{\"isCorrect\": %b, \"startTime\": \"%s\", \"duration\": %d}", Checker.hit(x, y, r), formatter.format(calendar.getTime()), System.currentTimeMillis() - Start);
                    sendJson(jsonResult);
                } else {
                    sendJson("{\"error\": \"invalid data\"}");
                }
            } catch (NumberFormatException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                String exceptionAsString = sw.toString();
                sendJson(String.format("{\"error1\": %s, \"error2\": %s}", e.getMessage(), exceptionAsString));

            } catch (NullPointerException e) {
                sendJson(String.format("{\"error\": \"missed necessary query param\"}"));
            } catch (Exception e) {
                sendJson(String.format("{\"error\": test - %s}", e.toString()));
            }
        }
    }
    private static String readRequestBody() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes =
                FCGIInterface.request.inStream.read(buffer.array(), 0,
                        contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();
        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }


    private static void sendJson(String jsonDump) {
        System.out.println(String.format(RESPONSE_TEMPLATE, jsonDump.getBytes(StandardCharsets.UTF_8).length, jsonDump));
    }


}