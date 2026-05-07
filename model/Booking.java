package model;

public class Booking {
    private int id;
    private String customerName;
    private Schedule schedule;
    private int seatNumber;

    public Booking() {
    }

    public Booking(int id, String customerName, Schedule schedule, int seatNumber) {
        this.id = id;
        this.customerName = customerName;
        this.schedule = schedule;
        this.seatNumber = seatNumber;
    }

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

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", schedule=" + schedule +
                ", seatNumber=" + seatNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (id != booking.id) return false;
        if (seatNumber != booking.seatNumber) return false;
        if (customerName != null ? !customerName.equals(booking.customerName) : booking.customerName != null)
            return false;
        return schedule != null ? schedule.equals(booking.schedule) : booking.schedule == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (customerName != null ? customerName.hashCode() : 0);
        result = 31 * result + (schedule != null ? schedule.hashCode() : 0);
        result = 31 * result + seatNumber;
        return result;
    }
}
