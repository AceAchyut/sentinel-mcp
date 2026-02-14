import java.io.*;
import java.util.*;
import java.nio.file.*;

public class SentinelAgent {

    // 1. Define the Tool Capability
    private static final String TOOL_DEFINITION = 
        "{" +
        "  \"jsonrpc\": \"2.0\"," +
        "  \"result\": {" +
        "    \"tools\": [{" +
        "      \"name\": \"secure_log_audit\"," +
        "      \"description\": \"Scans a log file for errors using a high-performance C engine. Requires strict path validation.\"," +
        "      \"inputSchema\": {" +
        "        \"type\": \"object\"," +
        "        \"properties\": {" +
        "          \"path\": { \"type\": \"string\", \"description\": \"Absolute path to the log file\" }" +
        "        }," +
        "        \"required\": [\"path\"]" +
        "      }" +
        "    }]" +
        "  }," +
        "  \"id\": 1" +
        "}";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // The Main MCP Loop: Read JSON-RPC from Stdio
        while (scanner.hasNextLine()) {
            String inputLine = scanner.nextLine();
            handleRequest(inputLine);
        }
    }

    private static void handleRequest(String jsonRequest) {
        // Simplified JSON parsing for the Hackathon (Use a library like Gson/Jackson in real prod)

        // 1. Handshake (Initialize)
        if (jsonRequest.contains("\"method\": \"initialize\"")) {
            System.out.println("{\"jsonrpc\": \"2.0\", \"result\": { \"protocolVersion\": \"2024-11-05\", \"capabilities\": { \"tools\": {} }, \"serverInfo\": { \"name\": \"Sentinel\", \"version\": \"1.0\" } }, \"id\": 1}");
        }
        // 2. List Tools
        else if (jsonRequest.contains("\"method\": \"tools/list\"")) {
            System.out.println(TOOL_DEFINITION);
        }
        // 3. Execute Tool (The Core Logic)
        else if (jsonRequest.contains("\"method\": \"tools/call\"")) {
            // Quick hack extract path (In prod, use real JSON parser!)
            String path = extractPathFromJson(jsonRequest);

            if (path == null) {
                sendError("Invalid parameters");
                return;
            }

            // SECURITY: Path Traversal Check
            if (path.contains("..") || path.contains("~")) {
                sendError("SECURITY ALERT: Path traversal attempt blocked by Sentinel Java Guard.");
                return;
            }

            // PERFORMANCE: Call C Engine
            runCEngine(path);
        }
    }

    private static void runCEngine(String path) {
        try {
            // Adjust this path to where your compiled C file is!
          ProcessBuilder pb = new ProcessBuilder("../engine/log_scanner.exe", path);
            Process p = pb.start();

            String output = new String(p.getInputStream().readAllBytes());
            // Wrap C output in MCP response
            System.out.printf("{\"jsonrpc\": \"2.0\", \"result\": { \"content\": [{ \"type\": \"text\", \"text\": %s }] }, \"id\": 2}\n", escapeJson(output));

        } catch (Exception e) {
            sendError("Engine Failure: " + e.getMessage());
        }
    }

    private static void sendError(String msg) {
         System.out.printf("{\"jsonrpc\": \"2.0\", \"error\": {\"code\": -32000, \"message\": \"%s\"}, \"id\": null}\n", msg);
    }

    // Helper to extract path blindly (Hackathon speed mode)
    private static String extractPathFromJson(String json) {
        int start = json.indexOf("\"path\":");
        if (start == -1) return null;
        int valueStart = json.indexOf("\"", start + 8) + 1;
        int valueEnd = json.indexOf("\"", valueStart);
        return json.substring(valueStart, valueEnd);
    }

    private static String escapeJson(String raw) {
        return "\"" + raw.replace("\"", "'").replace("\n", " ") + "\"";
    }
}