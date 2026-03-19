package StudentWebApp.backend;

public class Student {
    public int id;
    public String name;
    public String department;
    public double marks;

    public Student(int id, String name, String department, double marks) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
}
