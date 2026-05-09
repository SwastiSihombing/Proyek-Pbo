package main;

import java.util.Scanner;
import java.util.List;

import database.Database;
import mapper.BookingMapper;
import mapper.FilmMapper;
import mapper.ScheduleMapper;
import mapper.SeatMapper;
import mapper.PaymentMapper;
import model.Film;
import model.Payment;

public class Main {

    public static void main(String[] args) {

        // Inisialisasi database
        Database.init();
        Database.connect();

        Scanner input = new Scanner(System.in);

        FilmMapper filmMapper = new FilmMapper();
        ScheduleMapper scheduleMapper = new ScheduleMapper();
        BookingMapper bookingMapper = new BookingMapper();
        SeatMapper seatMapper = new SeatMapper();
        PaymentMapper paymentMapper = new PaymentMapper();

        int pilih;

        do {
            System.out.println("\n=== SISTEM MANAJEMEN BIOSKOP ===");
            System.out.println("1. Admin - Tambah Film");
            System.out.println("2. Admin - Tambah Jadwal");
            System.out.println("3. Customer - Lihat Film & Jadwal");
            System.out.println("4. Customer - Pesan Kursi & Bayar");
            System.out.println("5. Customer - Lihat Riwayat Pembayaran");
            System.out.println("6. Keluar");
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

                    int bookingId = bookingMapper.insert(customer, scheduleIdBook, seat);
                    System.out.println("Booking berhasil! ID Booking: " + bookingId);

                    // Proses Pembayaran
                    System.out.println("\n=== PROSES PEMBAYARAN ===");
                    System.out.print("Harga Tiket (Rp): ");
                    double amount = input.nextDouble();
                    input.nextLine();

                    System.out.println("Metode Pembayaran:");
                    System.out.println("1. CASH");
                    System.out.println("2. CARD");
                    System.out.println("3. TRANSFER");
                    System.out.print("Pilih metode (1-3): ");
                    int methodChoice = input.nextInt();
                    input.nextLine();

                    String paymentMethod = "";
                    switch (methodChoice) {
                        case 1:
                            paymentMethod = "CASH";
                            break;
                        case 2:
                            paymentMethod = "CARD";
                            break;
                        case 3:
                            paymentMethod = "TRANSFER";
                            break;
                        default:
                            System.out.println("Metode tidak valid!");
                            paymentMethod = "CASH";
                    }

                    Payment payment = new Payment(bookingId, amount, paymentMethod);
                    int paymentId = paymentMapper.insert(payment);

                    if (paymentId > 0) {
                        System.out.print("\nProses pembayaran... ");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        paymentMapper.updateStatus(paymentId, "COMPLETED");
                        System.out.println("BERHASIL!");
                        System.out.println("ID Pembayaran: " + paymentId);
                        System.out.println("Status: COMPLETED");
                    } else {
                        System.out.println("Pembayaran gagal!");
                    }
                    break;

                case 5:
                    // Lihat Riwayat Pembayaran
                    paymentMapper.showPaymentHistory();
                    break;

                case 6:
                    System.out.println("Terima kasih!");
                    break;

                default:
                    System.out.println("Menu tidak tersedia!");
            }

        } while (pilih != 6);

        input.close();
    }
}
