public class SalesMan {
    private long id;
    private String name;
    private String department;

    public SalesMan(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public SalesMan(long id, String name, String department) {
        this(name, department);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "SalesMan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
