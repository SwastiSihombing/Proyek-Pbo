package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * Entity untuk representasi Jadwal Film di teater
 * Memiliki informasi tentang film, waktu, tempat, dan harga tiket
 */
public class Schedule extends BaseEntity {
    private Film film;
    private int filmId;  // untuk menyimpan film ID saat perlu
    private LocalDate date;
    private LocalTime time;
    private LocalDate startDate;   // Tanggal mulai penayangan
    private LocalDate endDate;     // Tanggal akhir penayangan
    private String studio;
    private double price;
    private int totalSeats = 25;   // 5 rows x 5 columns
    private int availableSeats = 25;

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
        this.totalSeats = 25; // 5 rows x 5 columns
        this.availableSeats = 25;
    }

    public Schedule(Film film, LocalDate date, LocalTime time, String studio, double price) {
        super();
        this.film = film;
        this.date = date;
        this.time = time;
        this.studio = studio;
        this.price = price;
        this.totalSeats = 25;
        this.availableSeats = 25;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    // Indonesian aliases and additional methods for compatibility
    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public LocalDateTime getWaktu() {
        if (date != null && time != null) {
            return LocalDateTime.of(date, time);
        }
        return null;
    }

    public void setWaktu(LocalDateTime waktu) {
        if (waktu != null) {
            this.date = waktu.toLocalDate();
            this.time = waktu.toLocalTime();
        }
    }

    public double getHarga() {
        return getPrice();
    }

    public void setHarga(double harga) {
        setPrice(harga);
    }

    public String getFilmTitle() {
        return film != null ? film.getTitle() : null;
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
