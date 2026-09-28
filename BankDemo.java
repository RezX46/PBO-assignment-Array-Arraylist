import java.util.Scanner;

public class BankDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank();
        boolean running = true;

        System.out.println("=================================================");
        System.out.println("        SELAMAT DATANG DI BANK          ");
        System.out.println("=================================================");

        while (running) {
            System.out.println("\n--- MENU UTAMA ---");
            System.out.println("1. Tambah Nasabah Baru");
            System.out.println("2. Buka Rekening Tambahan untuk Nasabah");
            System.out.println("3. Setor Tunai (Deposit)");
            System.out.println("4. Tarik Tunai (Withdraw)");
            System.out.println("5. Cek Saldo Nasabah");
            System.out.println("6. Tampilkan Daftar Semua Nasabah");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu (0-6): ");

            int pilihan;
            try {
                pilihan = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid");
                continue;
            }

            switch (pilihan) {
                case 1:
                    // 1. Tambah Nasabah Baru
                    System.out.println("\n-- Tambah Nasabah Baru --");
                    System.out.print("Masukkan Nama Depan   : ");
                    String firstName = scanner.nextLine().trim();
                    System.out.print("Masukkan Nama Belakang: ");
                    String lastName = scanner.nextLine().trim();

                    if (firstName.isEmpty() || lastName.isEmpty()) {
                        System.out.println("Nama tidak boleh kosong!");
                        break;
                    }

                    bank.addCustomer(firstName, lastName);
                    int customerIdx = bank.getNumOfCustomers() - 1;
                    Customer newCustomer = bank.getCustomer(customerIdx);

                    // Buatkan rekening awal langsung untuk nasabah baru
                    System.out.print("Masukkan Saldo Awal Rekening: Rp ");
                    try {
                        double initBal = Double.parseDouble(scanner.nextLine().trim());
                        if (initBal < 0) {
                            System.out.println("Saldo awal tidak boleh negatif! Rekening diset dengan saldo 0.");
                            initBal = 0;
                        }
                        newCustomer.setAccount(new Account(initBal));
                        System.out.println("Nasabah " + firstName + " " + lastName
                                + " berhasil ditambahkan (Saldo: Rp " + formatSaldo(initBal) + ")");
                    } catch (NumberFormatException e) {
                        System.out.println("Format saldo salah! Rekening awal diset dengan Rp 0.");
                        newCustomer.setAccount(new Account(0));
                    }
                    break;

                case 2:
                    // 2. Buka Rekening Tambahan
                    if (bank.getNumOfCustomers() == 0) {
                        System.out.println("Belum ada nasabah terdaftar.");
                        break;
                    }

                    tampilkanDaftarNasabahRingkas(bank);
                    System.out.print("Pilih nomor nasabah: ");
                    int custIndex = pilihIndexNasabah(scanner, bank.getNumOfCustomers());
                    if (custIndex == -1)
                        break;

                    Customer targetCust = bank.getCustomer(custIndex);

                    System.out.print("Masukkan saldo awal rekening baru: Rp ");
                    try {
                        double newBal = Double.parseDouble(scanner.nextLine().trim());
                        if (newBal < 0) {
                            System.out.println("Saldo awal tidak boleh negatif!");
                            break;
                        }
                        targetCust.setAccount(new Account(newBal));
                        System.out.println("Rekening ke-" + targetCust.getNumOfAccounts() + " untuk "
                                + targetCust.getFirstName() + " " + targetCust.getLastName() + " berhasil dibuka.");
                    } catch (NumberFormatException e) {
                        System.out.println("Input saldo tidak valid!");
                    }
                    break;

                case 3:
                    // 3. Setor Tunai (Deposit)
                    if (bank.getNumOfCustomers() == 0) {
                        System.out.println("Belum ada nasabah terdaftar.");
                        break;
                    }

                    tampilkanDaftarNasabahRingkas(bank);
                    System.out.print("Pilih nomor nasabah untuk setor tunai: ");
                    int depCustIdx = pilihIndexNasabah(scanner, bank.getNumOfCustomers());
                    if (depCustIdx == -1)
                        break;

                    Customer depCust = bank.getCustomer(depCustIdx);
                    Account depAcc = pilihRekeningNasabah(scanner, depCust);
                    if (depAcc == null)
                        break;

                    System.out.print("Masukkan nominal setor (deposit): Rp ");
                    try {
                        double depAmt = Double.parseDouble(scanner.nextLine().trim());
                        if (depAcc.deposit(depAmt)) {
                            System.out.println(
                                    "Setor tunai berhasil, Saldo saat ini: Rp " + formatSaldo(depAcc.getBalance()));
                        } else {
                            System.out.println("Setor tunai gagal, Nominal harus lebih besar dari 0.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input nominal tidak valid");
                    }
                    break;

                case 4:
                    // 4. Tarik Tunai (Withdraw)
                    if (bank.getNumOfCustomers() == 0) {
                        System.out.println("Belum ada nasabah terdaftar.");
                        break;
                    }

                    tampilkanDaftarNasabahRingkas(bank);
                    System.out.print("Pilih nomor nasabah untuk tarik tunai: ");
                    int wdCustIdx = pilihIndexNasabah(scanner, bank.getNumOfCustomers());
                    if (wdCustIdx == -1)
                        break;

                    Customer wdCust = bank.getCustomer(wdCustIdx);
                    Account wdAcc = pilihRekeningNasabah(scanner, wdCust);
                    if (wdAcc == null)
                        break;

                    System.out.println("Saldo saat ini: Rp " + formatSaldo(wdAcc.getBalance()));
                    System.out.print("Masukkan nominal tarik tunai: Rp ");
                    try {
                        double wdAmt = Double.parseDouble(scanner.nextLine().trim());
                        if (wdAcc.withdraw(wdAmt)) {
                            System.out
                                    .println("Tarik tunai berhasil! Sisa saldo: Rp " + formatSaldo(wdAcc.getBalance()));
                        } else {
                            System.out.println("Tarik tunai gagal! Saldo tidak mencukupi atau nominal tidak valid.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input nominal tidak valid!");
                    }
                    break;

                case 5:
                    // 5. Cek Saldo Nasabah
                    if (bank.getNumOfCustomers() == 0) {
                        System.out.println("Belum ada nasabah terdaftar.");
                        break;
                    }

                    tampilkanDaftarNasabahRingkas(bank);
                    System.out.print("Pilih nomor nasabah untuk cek saldo: ");
                    int cekIdx = pilihIndexNasabah(scanner, bank.getNumOfCustomers());
                    if (cekIdx == -1)
                        break;

                    Customer cekCust = bank.getCustomer(cekIdx);
                    System.out.println(
                            "\n--- Informasi Saldo: " + cekCust.getFirstName() + " " + cekCust.getLastName() + " ---");
                    if (cekCust.getNumOfAccounts() == 0) {
                        System.out.println("Nasabah belum memiliki rekening.");
                    } else {
                        for (int a = 0; a < cekCust.getNumOfAccounts(); a++) {
                            System.out.println(
                                    "Rekening #" + (a + 1) + ": Rp " + formatSaldo(cekCust.getAccount(a).getBalance()));
                        }
                    }
                    break;

                case 6:
                    // 6. Tampilkan Semua Nasabah
                    if (bank.getNumOfCustomers() == 0) {
                        System.out.println("Belum ada nasabah terdaftar di bank.");
                        break;
                    }

                    System.out.println("\n=======================================================");
                    System.out.println("                 DAFTAR NASABAH BANK                   ");
                    System.out.println("=======================================================");
                    System.out.println("Total Nasabah: " + bank.getNumOfCustomers());
                    System.out.println("-------------------------------------------------------");

                    for (int i = 0; i < bank.getNumOfCustomers(); i++) {
                        Customer c = bank.getCustomer(i);
                        System.out.println((i + 1) + ". " + c.getFirstName() + " " + c.getLastName());
                        if (c.getNumOfAccounts() == 0) {
                            System.out.println("    (Belum ada rekening)");
                        } else {
                            for (int j = 0; j < c.getNumOfAccounts(); j++) {
                                System.out.println(
                                        "    -> Rekening " + (j + 1) + " | Saldo: Rp "
                                                + formatSaldo(c.getAccount(j).getBalance()));
                            }
                        }
                    }
                    System.out.println("=======================================================");
                    break;

                case 0:
                    System.out.println("Terima kasih");
                    running = false;
                    break;

                default:
                    System.out.println("Pilihan menu tidak valid");
            }
        }

        scanner.close();
    }

    // Helper untuk menampilkan saldo angka biasa tanpa notasi E
    private static String formatSaldo(double amount) {
        return String.format("%.0f", amount);
    }

    // Helper untuk menampilkan ringkasan nasabah yang terdaftar
    private static void tampilkanDaftarNasabahRingkas(Bank bank) {
        System.out.println("\nDaftar Nasabah:");
        for (int i = 0; i < bank.getNumOfCustomers(); i++) {
            Customer c = bank.getCustomer(i);
            System.out.println("  [" + (i + 1) + "] " + c.getFirstName() + " " + c.getLastName());
        }
    }

    // Helper untuk memilih index nasabah yang valid
    private static int pilihIndexNasabah(Scanner scanner, int totalNasabah) {
        try {
            int inputNo = Integer.parseInt(scanner.nextLine().trim());
            if (inputNo < 1 || inputNo > totalNasabah) {
                System.out.println("Nomor nasabah di luar rentang!");
                return -1;
            }
            return inputNo - 1; // Konversi ke index 0-based
        } catch (NumberFormatException e) {
            System.out.println("Input nomor harus berupa angka!");
            return -1;
        }
    }

    // Helper untuk memilih rekening nasabah
    private static Account pilihRekeningNasabah(Scanner scanner, Customer customer) {
        int totalAccounts = customer.getNumOfAccounts();
        if (totalAccounts == 0) {
            System.out.println("Nasabah ini belum memiliki rekening!");
            return null;
        }
        if (totalAccounts == 1) {
            return customer.getAccount(0);
        }

        System.out.println(
                "Pilih rekening nasabah (" + customer.getFirstName() + " memiliki " + totalAccounts + " rekening):");
        for (int i = 0; i < totalAccounts; i++) {
            System.out.println("  [" + (i + 1) + "] Saldo: Rp " + formatSaldo(customer.getAccount(i).getBalance()));
        }
        System.out.print("Pilih nomor rekening (1-" + totalAccounts + "): ");
        try {
            int accChoice = Integer.parseInt(scanner.nextLine().trim());
            if (accChoice < 1 || accChoice > totalAccounts) {
                System.out.println("Nomor rekening tidak valid!");
                return null;
            }
            return customer.getAccount(accChoice - 1);
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return null;
        }
    }
}
