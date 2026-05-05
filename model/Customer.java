package model;

public class Customer extends User {
    private String customerName;

    // Constructor kosong
    public Customer() {
        super();
    }

    // Constructor dengan semua parameter
    public Customer(int id, String username, String password, String customerName) {
        super(id, username, password);
        this.customerName = customerName;
    }

    // Getter dan Setter
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
