package model;

public class Seat {
    private int id;
    private int scheduleId;
    private int seatNumber;
    private boolean isBooked;

    // Constructor kosong
    public Seat() {
    }

    // Constructor dengan semua parameter
    public Seat(int id, int scheduleId, int seatNumber, boolean isBooked) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.seatNumber = seatNumber;
        this.isBooked = isBooked;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
