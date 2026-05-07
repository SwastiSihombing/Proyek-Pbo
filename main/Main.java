package main;

import java.util.Scanner;
import java.util.List;

import database.Database;
import mapper.BookingMapper;
import mapper.FilmMapper;
import mapper.ScheduleMapper;
import mapper.SeatMapper;
import model.Film;

public class Main {

    public static void main(String[] args) {

        // Inisialisasi database
        Database.connect();

        Scanner input = new Scanner(System.in);

        FilmMapper filmMapper = new FilmMapper();
        ScheduleMapper scheduleMapper = new ScheduleMapper();
        BookingMapper bookingMapper = new BookingMapper();
        SeatMapper seatMapper = new SeatMapper();

        int pilih;

        do {
            System.out.println("\n=== SISTEM MANAJEMEN BIOSKOP ===");
            System.out.println("1. Admin - Tambah Film");
            System.out.println("2. Admin - Tambah Jadwal");
            System.out.println("3. Customer - Lihat Film & Jadwal");
            System.out.println("4. Customer - Pesan Kursi");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");

            pilih = input.nextInt();
            input.nextLine();

            switch (pilih) {

                case 1:
                    // Tambah Film
                    System.out.print("Judul Film: ");
                    String judul = input.nextLine();

                    System.out.print("Genre: ");
                    String genre = input.nextLine();

                    System.out.print("Durasi (menit): ");
                    int durasi = input.nextInt();
                    input.nextLine();

                    Film film = new Film();
                    film.setTitle(judul);
                    film.setGenre(genre);
                    film.setDuration(durasi);
                    filmMapper.insert(film);

                    System.out.println("Film berhasil ditambahkan!");
                    break;

                case 2:
                    // Tambah Jadwal
                    System.out.println("=== DAFTAR FILM ===");
                    List<Film> films = filmMapper.findAll();
                    for (Film f : films) {
                        System.out.println("ID: " + f.getId() + " | Judul: " + f.getTitle() + 
                                         " | Genre: " + f.getGenre() + " | Durasi: " + f.getDuration() + " menit");
                    }

                    System.out.print("Masukkan ID Film: ");
                    int filmId = input.nextInt();
                    input.nextLine();

                    System.out.print("Jam Tayang: ");
                    String jam = input.nextLine();

                    System.out.print("Studio: ");
                    String studio = input.nextLine();

                    int scheduleId = scheduleMapper.insert(filmId, jam, studio);
                    seatMapper.generateSeats(scheduleId);

                    System.out.println("Jadwal berhasil ditambahkan!");
                    break;

                case 3:
                    // Lihat Film & Jadwal
                    System.out.println("=== FILM & JADWAL ===");
                    scheduleMapper.showScheduleWithFilm();
                    break;

                case 4:
                    // Booking Kursi
                    System.out.println("=== JADWAL TERSEDIA ===");
                    scheduleMapper.showScheduleWithFilm();

                    System.out.print("Masukkan ID Jadwal: ");
                    int scheduleIdBook = input.nextInt();
                    input.nextLine();

                    System.out.println("=== KURSI TERSEDIA ===");
                    seatMapper.showSeats(scheduleIdBook);

                    System.out.print("Nama Customer: ");
                    String customer = input.nextLine();

                    System.out.print("Nomor Kursi: ");
                    String seat = input.nextLine();

                    bookingMapper.insert(customer, scheduleIdBook, seat);

                    System.out.println("Booking berhasil!");
                    break;

                case 5:
                    System.out.println("Terima kasih!");
                    break;

                default:
                    System.out.println("Menu tidak tersedia!");
            }

        } while (pilih != 5);

        input.close();
    }
}
