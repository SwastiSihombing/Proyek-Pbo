package main;

import database.Database;
import mapper.BookingMapper;
import mapper.FilmMapper;
import mapper.ScheduleMapper;
import mapper.SeatMapper;
import model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Database.init();

        Scanner scanner = new Scanner(System.in);
        FilmMapper filmMapper         = new FilmMapper();
        ScheduleMapper scheduleMapper = new ScheduleMapper();
        SeatMapper seatMapper         = new SeatMapper();
        BookingMapper bookingMapper   = new BookingMapper();

        int pilihan = 0;

        while (pilihan != 5) {
            System.out.println("\n=== SISTEM MANAJEMEN BIOSKOP ===");
            System.out.println("1. Admin - Tambah Film");
            System.out.println("2. Admin - Tambah Jadwal");
            System.out.println("3. Customer - Lihat Film & Jadwal");
            System.out.println("4. Customer - Pesan Kursi");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");

            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {

                case 1:
                    System.out.println("\n--- Tambah Film Baru ---");
                    System.out.print("Judul Film    : ");
                    String judul = scanner.nextLine();
                    System.out.print("Genre         : ");
                    String genre = scanner.nextLine();
                    System.out.print("Durasi (menit): ");
                    int durasi = scanner.nextInt();
                    scanner.nextLine();

                    Film filmBaru = new Film();
                    filmBaru.setTitle(judul);
                    filmBaru.setGenre(genre);
                    filmBaru.setDuration(durasi);
                    filmMapper.insert(filmBaru);
                    System.out.println("Film berhasil ditambahkan!");
                    break;

                case 2:
                    System.out.println("\n--- Tambah Jadwal Tayang ---");
                    List<Film> semuaFilm = filmMapper.findAll();
                    if (semuaFilm.isEmpty()) {
                        System.out.println("Belum ada film. Tambah film dulu (Menu 1).");
                        break;
                    }
                    for (Film f : semuaFilm) {
                        System.out.println("ID: " + f.getId() + " | " + f.getTitle()
                                + " (" + f.getGenre() + ", " + f.getDuration() + " menit)");
                    }
                    System.out.print("Masukkan ID Film  : ");
                    int filmId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Jam Tayang        : ");
                    String jamTayang = scanner.nextLine();
                    System.out.print("Nama Studio       : ");
                    String studio = scanner.nextLine();

                    int scheduleId = scheduleMapper.insert(filmId, jamTayang, studio);
                    if (scheduleId != -1) {
                        seatMapper.generateSeats(scheduleId);
                        System.out.println("Jadwal berhasil ditambahkan & 15 kursi di-generate!");
                    } else {
                        System.out.println("Gagal menambah jadwal.");
                    }
                    break;

                case 3:
                    System.out.println("\n--- Daftar Film ---");
                    List<Film> daftarFilm = filmMapper.findAll();
                    if (daftarFilm.isEmpty()) {
                        System.out.println("Belum ada film.");
                    } else {
                        for (Film f : daftarFilm) {
                            System.out.println("[" + f.getId() + "] " + f.getTitle()
                                    + " | Genre: " + f.getGenre()
                                    + " | Durasi: " + f.getDuration() + " menit");
                        }
                    }
                    System.out.println("\n--- Jadwal Tayang ---");
                    scheduleMapper.showScheduleWithFilm();
                    break;

                case 4:
                    System.out.println("\n--- Pesan Kursi ---");
                    scheduleMapper.showScheduleWithFilm();

                    System.out.print("\nMasukkan ID Jadwal : ");
                    int idJadwal = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Kursi (O=Kosong, X=Terisi):");
                    seatMapper.showSeats(idJadwal);

                    System.out.print("Nomor Kursi (contoh A1): ");
                    String nomorKursi = scanner.nextLine().toUpperCase();

                    System.out.print("Nama Anda           : ");
                    String namaPemesan = scanner.nextLine();

                    try (Connection conn = Database.connect()) {
                        String cek = "SELECT * FROM seat WHERE schedule_id=? AND seat_number=? AND is_booked=0";
                        PreparedStatement stmtCek = conn.prepareStatement(cek);
                        stmtCek.setInt(1, idJadwal);
                        stmtCek.setString(2, nomorKursi);
                        ResultSet rs = stmtCek.executeQuery();

                        if (rs.next()) {
                            String update = "UPDATE seat SET is_booked=1 WHERE schedule_id=? AND seat_number=?";
                            PreparedStatement stmtUpdate = conn.prepareStatement(update);
                            stmtUpdate.setInt(1, idJadwal);
                            stmtUpdate.setString(2, nomorKursi);
                            stmtUpdate.executeUpdate();

                            bookingMapper.insert(namaPemesan, idJadwal, nomorKursi);
                            System.out.println("Pemesanan berhasil! Kursi " + nomorKursi + " atas nama " + namaPemesan);
                        } else {
                            System.out.println("Kursi tidak tersedia atau sudah dipesan.");
                        }
                    } catch (Exception e) {
                        System.out.println("Gagal memesan kursi.");
                    }
                    break;

                case 5:
                    System.out.println("Terima kasih! Sampai jumpa.");
                    break;

                default:
                    System.out.println("Pilihan tidak valid. Coba lagi.");
            }
        }

        scanner.close();
    }
}