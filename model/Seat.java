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

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", seatNumber=" + seatNumber +
                ", isBooked=" + isBooked +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        if (id != seat.id) return false;
        if (scheduleId != seat.scheduleId) return false;
        if (seatNumber != seat.seatNumber) return false;
        return isBooked == seat.isBooked;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + scheduleId;
        result = 31 * result + seatNumber;
        result = 31 * result + (isBooked ? 1 : 0);
        return result;
    }
}
