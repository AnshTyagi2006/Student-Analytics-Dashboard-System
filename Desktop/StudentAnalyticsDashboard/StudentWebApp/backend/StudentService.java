package StudentWebApp.backend;

import java.util.*;
import java.io.*;
import java.nio.file.*;

public class StudentService {
    private static List<Student> students = new ArrayList<>();
    private static int idCounter = 1;
    
    private static final String DATA_FILE = "../data/students.json";  // Relative from backend/
    
    static {
        loadData();
    }
    
    private static void loadData() {
        try {
            Path path = Paths.get(DATA_FILE);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                Files.writeString(path, "[]");
                return;
            }
            String content = Files.readString(path);
            content = content.trim();
            if ("[]".equals(content)) return;
            // Parse JSON array
            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1);
                String[] items = content.split("\\},\\s*");
                for (String item : items) {
                    item = item.trim();
                    if (item.isEmpty()) continue;
                    if (!item.startsWith("{")) continue;
                    if (!item.endsWith("}")) item += "}";
                    String idStr = extractJson(item, "id");
                    String nameStr = extractJson(item, "name");
                    String deptStr = extractJson(item, "department");
                    String marksStr = extractJson(item, "marks");
                    if (idStr != null && nameStr != null && deptStr != null && marksStr != null) {
                        int id = Integer.parseInt(idStr);
                        String name = unescapeJson(nameStr);
                        String dept = unescapeJson(deptStr);
                        double marks = Double.parseDouble(marksStr);
                        students.add(new Student(id, name, dept, marks));
                        idCounter = Math.max(idCounter, id + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
        }
    }
    
    private static void saveData() {
        try {
            Path path = Paths.get(DATA_FILE);
            StringBuilder json = new StringBuilder("[");
            for (Student s : students) {
                json.append("{\"id\":").append(s.id)
                    .append(",\"name\":\"").append(escapeJson(s.name)).append("\"")
                    .append(",\"department\":\"").append(escapeJson(s.department)).append("\"")
                    .append(",\"marks\":").append(s.marks).append("},");
            }
            if (!students.isEmpty()) json.setLength(json.length() - 1);
            json.append("]");
            Files.writeString(path, json.toString());
        } catch (IOException e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }
    
    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
    
    private static String unescapeJson(String s) {
        if (s == null) return "";
        s = s.replace("\\\\", "\\").replace("\\\"", "\"").replace("\\n", "\n");
        if (s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }
    
    private static String extractJson(String json, String key) {
        String k = "\"" + key + "\":";
        int idx = json.indexOf(k);
        if (idx == -1) return null;
        idx += k.length();
        char c = json.charAt(idx);
        if (c == '"' || c == '\'') {
            int end = json.indexOf(c == '"' ? "\"" : "'", idx + 1);
            if (end != -1) {
                return json.substring(idx + 1, end);
            }
        } else if (Character.isDigit(c) || c == '-') {
            int end = idx;
            while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-')) {
                end++;
            }
            return json.substring(idx, end);
        }
        return null;
    }
    
    public static void addStudent(String name, String dept, double marks) {
        students.add(new Student(idCounter++, name, dept, marks));
        saveData();
    }

    public static List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public static List<Student> searchByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.name.toLowerCase().contains(name.toLowerCase())) {
                result.add(s);
            }
        }
        return result;
    }

    public static List<Student> searchByDepartment(String dept) {
        List<Student> result = new ArrayList<>();
        for (Student s : students) {
            if (s.department.equalsIgnoreCase(dept)) {
                result.add(s);
            }
        }
        return result;
    }

    public static int getTotalStudents() {
        return students.size();
    }

    public static Map<String, Integer> getDepartmentStats() {
        Map<String, Integer> map = new HashMap<>();
        for (Student s : students) {
            String dept = s.department;
            map.put(dept, map.getOrDefault(dept, 0) + 1);
        }
        return map;
    }
}
