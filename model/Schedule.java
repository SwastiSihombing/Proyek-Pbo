package model;

public class Schedule {
    private int id;
    private Film film;
    private String time;
    private String studio;

    public Schedule() {
    }

    public Schedule(int id, Film film, String time, String studio) {
        this.id = id;
        this.film = film;
        this.time = time;
        this.studio = studio;
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

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", film=" + film +
                ", time='" + time + '\'' +
                ", studio='" + studio + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (id != schedule.id) return false;
        if (film != null ? !film.equals(schedule.film) : schedule.film != null) return false;
        if (time != null ? !time.equals(schedule.time) : schedule.time != null) return false;
        return studio != null ? studio.equals(schedule.studio) : schedule.studio == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (film != null ? film.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (studio != null ? studio.hashCode() : 0);
        return result;
    }
}
