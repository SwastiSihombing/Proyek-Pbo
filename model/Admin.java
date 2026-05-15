package model;

/**
 * Class Admin yang mewarisi dari User (Inheritance)
 * Admin memiliki akses untuk menambah film dan jadwal
 */
public class Admin extends User {
    private String adminLevel;
    private String department;

    public Admin() {
        super();
    }

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    public Admin(int id, String username, String password, String adminLevel, String department) {
        super(id, username, password);
        this.adminLevel = adminLevel;
        this.department = department;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("Admin{id=%d, username='%s', level='%s', department='%s'}", 
            id, username, adminLevel, department);
    }
}
