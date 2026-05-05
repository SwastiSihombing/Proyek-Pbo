package model;

public class Booking {
    private int id;
    private String customerName;
    private Schedule schedule;
    private int seatNumber;

    // Constructor kosong
    public Booking() {
    }

    // Constructor dengan semua parameter
    public Booking(int id, String customerName, Schedule schedule, int seatNumber) {
        this.id = id;
        this.customerName = customerName;
        this.schedule = schedule;
        this.seatNumber = seatNumber;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }
}
