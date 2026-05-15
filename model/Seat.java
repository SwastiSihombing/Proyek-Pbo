package model;

/**
 * Entity untuk representasi kursi di teater
 * Dengan row (A,B,C) dan column (1-5) representation
 */
public class Seat extends BaseEntity {
    private int scheduleId;
    private String seatRow;      // A, B, C
    private int seatColumn;      // 1-5
    private boolean isBooked;
    private String bookedByCustomer; // Nama customer yang booking (nullable)

    public Seat() {
        super();
    }

    public Seat(int id, int scheduleId, String seatRow, int seatColumn, boolean isBooked) {
        super(id);
        this.scheduleId = scheduleId;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.isBooked = isBooked;
    }

    public Seat(int scheduleId, String seatRow, int seatColumn) {
        super();
        this.scheduleId = scheduleId;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.isBooked = false;
    }

    // Getters and Setters
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(int seatColumn) {
        this.seatColumn = seatColumn;
    }

    public String getSeatNumber() {
        return seatRow + seatColumn;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getBookedByCustomer() {
        return bookedByCustomer;
    }

    public void setBookedByCustomer(String bookedByCustomer) {
        this.bookedByCustomer = bookedByCustomer;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", seatRow='" + seatRow + '\'' +
                ", seatColumn=" + seatColumn +
                ", isBooked=" + isBooked +
                ", bookedByCustomer='" + bookedByCustomer + '\'' +
                '}';
    }
}
