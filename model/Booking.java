package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity untuk representasi pemesanan tiket
 * Hanya menghandle seat selection, TIDAK termasuk payment
 * Payment dihandle oleh Payment entity secara terpisah
 */
public class Booking extends BaseEntity {
    private int customerId;
    private String customerName;
    private int scheduleId;
    private List<String> selectedSeats;  // List untuk menyimpan seats (A1, A2, dst)
    private double totalPrice;
    private OrderStatus status;

    public Booking() {
        super();
        this.selectedSeats = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public Booking(int id, int customerId, String customerName, int scheduleId) {
        super(id);
        this.customerId = customerId;
        this.customerName = customerName;
        this.scheduleId = scheduleId;
        this.selectedSeats = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public Booking(int customerId, String customerName, int scheduleId) {
        super();
        this.customerId = customerId;
        this.customerName = customerName;
        this.scheduleId = scheduleId;
        this.selectedSeats = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public List<String> getSelectedSeats() {
        return selectedSeats;
    }

    public void setSelectedSeats(List<String> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }

    public void addSeat(String seatNumber) {
        if (!selectedSeats.contains(seatNumber)) {
            selectedSeats.add(seatNumber);
        }
    }

    public void removeSeat(String seatNumber) {
        selectedSeats.remove(seatNumber);
    }

    public int getNumberOfSeats() {
        return selectedSeats.size();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", scheduleId=" + scheduleId +
                ", selectedSeats=" + selectedSeats +
                ", totalPrice=" + totalPrice +
                ", status=" + status.getDescription() +
                '}';
    }
}
