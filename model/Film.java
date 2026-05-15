package model;

/**
 * Entity untuk representasi Film/Movie di sistem
 */
public class Film extends BaseEntity {
    private String title;
    private String genre;
    private int duration;      // dalam menit
    private String rating;     // G, PG, PG-13, R, dst
    private String director;
    private String showtime;   // Waktu tayang (HH:mm)
    private String endShowtime;// Waktu akhir tayang (HH:mm)
    private double price;      // Harga tiket

    public Film() {
        super();
    }

    public Film(int id, String title, String genre, int duration) {
        super(id);
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    public Film(String title, String genre, int duration, String rating, String director) {
        super();
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.director = director;
    }

    public Film(String title, String genre, int duration, String showtime, String endShowtime, double price) {
        super();
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.showtime = showtime;
        this.endShowtime = endShowtime;
        this.price = price;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public String getEndShowtime() {
        return endShowtime;
    }

    public void setEndShowtime(String endShowtime) {
        this.endShowtime = endShowtime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Indonesian aliases for compatibility
    public String getJudul() {
        return getTitle();
    }

    public void setJudul(String judul) {
        setTitle(judul);
    }

    public int getDurasi() {
        return getDuration();
    }

    public void setDurasi(int durasi) {
        setDuration(durasi);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", rating='" + rating + '\'' +
                ", director='" + director + '\'' +
                ", showtime='" + showtime + '\'' +
                ", endShowtime='" + endShowtime + '\'' +
                ", price=" + price +
                '}';
    }
}
