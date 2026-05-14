package model;

public class Schedule {
    private int id;
    private Film film;
    private String date;
    private String time;
    private String studio;
    private double price;

    public Schedule() {
    }

    public Schedule(int id, Film film, String date, String time, String studio, double price) {
        this.id = id;
        this.film = film;
        this.date = date;
        this.time = time;
        this.studio = studio;
        this.price = price;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", film=" + film +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", studio='" + studio + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (id != schedule.id) return false;
        if (Double.compare(schedule.price, price) != 0) return false;
        if (film != null ? !film.equals(schedule.film) : schedule.film != null) return false;
        if (date != null ? !date.equals(schedule.date) : schedule.date != null) return false;
        if (time != null ? !time.equals(schedule.time) : schedule.time != null) return false;
        return studio != null ? studio.equals(schedule.studio) : schedule.studio == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (film != null ? film.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (studio != null ? studio.hashCode() : 0);
        long temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
