package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity untuk representasi Jadwal Film di teater
 * Memiliki informasi tentang film, waktu, tempat, dan harga tiket
 */
public class Schedule extends BaseEntity {
    private Film film;
    private LocalDate date;
    private LocalTime time;
    private String studio;
    private double price;
    private int totalSeats;
    private int availableSeats;

    public Schedule() {
        super();
    }

    public Schedule(int id, Film film, LocalDate date, LocalTime time, String studio, double price) {
        super(id);
        this.film = film;
        this.date = date;
        this.time = time;
        this.studio = studio;
        this.price = price;
        this.totalSeats = 15; // 3 rows x 5 columns
        this.availableSeats = 15;
    }

    public Schedule(Film film, LocalDate date, LocalTime time, String studio, double price) {
        super();
        this.film = film;
        this.date = date;
        this.time = time;
        this.studio = studio;
        this.price = price;
        this.totalSeats = 15;
        this.availableSeats = 15;
    }

    // Getters and Setters
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getBookedSeats() {
        return totalSeats - availableSeats;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", film=" + film +
                ", date=" + date +
                ", time=" + time +
                ", studio='" + studio + '\'' +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                '}';
    }
}
