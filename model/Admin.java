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
}
