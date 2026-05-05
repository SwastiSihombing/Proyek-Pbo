package model;

public class Schedule {
    private int id;
    private Film film;
    private String time;
    private String studio;

    // Constructor kosong
    public Schedule() {
    }

    // Constructor dengan semua parameter
    public Schedule(int id, Film film, String time, String studio) {
        this.id = id;
        this.film = film;
        this.time = time;
        this.studio = studio;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }
}
