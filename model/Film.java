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

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", rating='" + rating + '\'' +
                ", director='" + director + '\'' +
                '}';
    }
}
