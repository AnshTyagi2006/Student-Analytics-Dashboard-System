package StudentWebApp.backend;

import com.sun.net.httpserver.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class WebServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/stats", new StatsHandler());
        server.createContext("/api/students", new StudentsHandler());
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/", new RootHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Web Server started at http://localhost:8080");
        System.out.println("Frontend at ../frontend/index.html");
    }
    
    static class StatsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                Map<String, Integer> stats = StudentService.getDepartmentStats();
                int total = StudentService.getTotalStudents();
                String json = "{\"total\":" + total + ",\"depts\":" + mapToJson(stats) + "}";
                sendJson(exchange, json);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    
    static class StudentsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("GET".equals(method)) {
                List<Student> students = StudentService.getAllStudents();
                String json = listToJson(students);
                sendJson(exchange, json);
            } else if ("POST".equals(method)) {
                String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                    .lines().collect(Collectors.joining("\\n"));
                // Simple form parse name=xx&amp;dept=yy&amp;marks=zz
                String name = parseParam(body, "name");
                String dept = parseParam(body, "dept");
                double marks = parseDoubleParam(body, "marks");
                StudentService.addStudent(name, dept, marks);
                exchange.getResponseHeaders().set("Location", "/api/students");
                exchange.sendResponseHeaders(201, -1);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    
    static class SearchHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String type = parseParam(query, "type");
                String q = parseParam(query, "q");
                List<Student> results;
                if ("name".equals(type)) {
                    results = StudentService.searchByName(q);
                } else {
                    results = StudentService.searchByDepartment(q);
                }
                String json = listToJson(results);
                sendJson(exchange, json);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    
    static class RootHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<h1>Backend API ready</h1><p>Frontend: <a href='../frontend/index.html'>../frontend/index.html</a></p>";
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            sendString(exchange, html);
        }
    }
    
    private static void sendJson(HttpExchange exchange, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        byte[] bytes = json.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
    
    private static void sendString(HttpExchange exchange, String str) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        byte[] bytes = str.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
    
    private static String mapToJson(Map<String, Integer> map) {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\":").append(entry.getValue()).append(",");
        }
        if (!map.isEmpty()) sb.setLength(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
    
    private static String listToJson(List<Student> list) {
        StringBuilder sb = new StringBuilder("[");
        for (Student s : list) {
            sb.append("{\"id\":").append(s.id)
              .append(",\"name\":\"").append(s.name.replace("\"", "\\\"")).append("\"")
              .append(",\"department\":\"").append(s.department.replace("\"", "\\\"")).append("\"")
              .append(",\"marks\":").append(s.marks).append("},");
        }
        if (!list.isEmpty()) sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
    
    private static String parseParam(String query, String key) {
        if (query == null) return "";
        for (String param : query.split("&")) {
            String[] kv = param.split("=", 2);
            if (kv.length == 2 && key.equals(kv[0])) {
                return urlDecode(kv[1]);
            }
        }
        return "";
    }
    
    private static double parseDoubleParam(String query, String key) {
        String val = parseParam(query, key);
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private static String urlDecode(String s) {
        if (s == null) return "";
        return s.replace("+", " ").replace("%20", " ").replace("%21", "!").replace("%27", "'").replace("%28", "(").replace("%29", ")").replace("%2C", ",");
    }
}
