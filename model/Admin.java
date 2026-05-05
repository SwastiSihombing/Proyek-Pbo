package model;

public class Admin extends User {
    
    // Constructor kosong
    public Admin() {
        super();
    }

    // Constructor dengan semua parameter
    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
