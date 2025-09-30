import java.time.LocalDateTime;
import java.util.*;

// TRANSACTION CLASS
class Transaction {
    String type;
    double amount;
    LocalDateTime timestamp;
    double balanceAfter;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[ " + type + " : " + amount + ", Balance: " + balanceAfter + ", Time: " + timestamp + " ]";
    }
}

// ACCOUNT CLASS
class Account {
    String accountNumber;
    String name;
    String type;
    double balance;
    String contact;
    String pin;
    List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String name, String type, double balance, String contact, String pin) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.contact = contact;
        this.pin = pin;
    }

    @Override
    public String toString() {
        return "{AccNo: " + accountNumber + ", Name: " + name + ", Type: " + type + ", Balance: " + balance + "}";
    }
}

// ADMIN CLASS
class Admin {
    String username;
    String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

// BANK CLASS
class Bank {
    private static int accCounter = 1000;
    List<Account> accounts = new ArrayList<>();
    Map<String, String> otps = new HashMap<>();
    Admin admin = new Admin("admin", "admin123"); // default admin

    private Scanner sc = new Scanner(System.in);

    private String generateAccountNumber() {
        return "ACC" + (++accCounter);
    }

    private Account findAccount(String accNo, String pin) {
        for (Account acc : accounts) {
            if (acc.accountNumber.equals(accNo) && acc.pin.equals(pin)) return acc;
        }
        return null;
    }

    private String generateOTP() {
        Random rand = new Random();
        int otp = 1000 + rand.nextInt(9000);
        return String.valueOf(otp);
    }

    private boolean verifyOTP(String accNo) {
        System.out.print("Generate OTP (yes/no)? ");
        String ans = sc.nextLine();
        if (ans.equalsIgnoreCase("yes")) {
            String otp = generateOTP();
            otps.put(accNo, otp);
            System.out.println("Your OTP: " + otp);
            System.out.print("Enter OTP: ");
            String userOtp = sc.nextLine();
            if (otp.equals(userOtp)) {
                otps.remove(accNo);
                return true;
            } else {
                System.out.println("❌ Invalid OTP");
            }
        }
        return false;
    }

    // Open account
    public void openAccount() {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Account Type (Savings/Current): ");
        String type = sc.nextLine();

        System.out.print("Enter Initial Deposit: ");
        double balance = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();

        System.out.print("Set a 4-digit PIN: ");
        String pin = sc.nextLine();

        String accNo = generateAccountNumber();
        accounts.add(new Account(accNo, name, type, balance, contact, pin));

        System.out.println("✅ Account Created! Account Number: " + accNo);
    }

    // Deposit
    public void deposit() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        Account acc = findAccount(accNo, pin);
        if (acc == null) {
            System.out.println("❌ Invalid Account or PIN");
            return;
        }

        if (!verifyOTP(accNo)) return;

        System.out.print("Enter Deposit Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) {
            System.out.println("❌ Amount must be positive.");
            return;
        }

        acc.balance += amount;
        acc.transactions.add(new Transaction("Deposit", amount, acc.balance));
        System.out.println("✅ Deposit Successful. Balance: " + acc.balance);
    }

    // Withdraw
    public void withdraw() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        Account acc = findAccount(accNo, pin);
        if (acc == null) {
            System.out.println("❌ Invalid Account or PIN");
            return;
        }

        if (!verifyOTP(accNo)) return;

        System.out.print("Enter Withdrawal Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0 || amount > acc.balance) {
            System.out.println("❌ Insufficient Balance or invalid amount.");
            return;
        }

        acc.balance -= amount;
        acc.transactions.add(new Transaction("Withdraw", amount, acc.balance));
        System.out.println("✅ Withdrawal Successful. Balance: " + acc.balance);
    }

    // Transfer
    public void transfer() {
        System.out.print("Enter Your Account Number: ");
        String accNoFrom = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        Account sender = findAccount(accNoFrom, pin);
        if (sender == null) {
            System.out.println("❌ Invalid Account or PIN");
            return;
        }

        if (!verifyOTP(accNoFrom)) return;

        System.out.print("Enter Recipient Account Number: ");
        String accNoTo = sc.nextLine();
        Account receiver = null;
        for (Account a : accounts) {
            if (a.accountNumber.equals(accNoTo)) {
                receiver = a;
                break;
            }
        }
        if (receiver == null) {
            System.out.println("❌ Recipient account not found.");
            return;
        }

        System.out.print("Enter Amount to Transfer: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0 || amount > sender.balance) {
            System.out.println("❌ Insufficient Balance or invalid amount.");
            return;
        }

        sender.balance -= amount;
        receiver.balance += amount;

        sender.transactions.add(new Transaction("Transfer Sent", amount, sender.balance));
        receiver.transactions.add(new Transaction("Transfer Received", amount, receiver.balance));

        System.out.println("✅ Transfer Successful. Your Balance: " + sender.balance);
    }

    // Mini Statement / Transaction History
    public void showTransactions() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        Account acc = findAccount(accNo, pin);
        if (acc == null) {
            System.out.println("❌ Invalid Account or PIN");
            return;
        }

        if (!verifyOTP(accNo)) return;

        System.out.println("---- Transaction History ----");
        for (Transaction t : acc.transactions) {
            System.out.println(t);
        }
    }

    // Balance check
    public void checkBalance() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        Account acc = findAccount(accNo, pin);
        if (acc == null) {
            System.out.println("❌ Invalid Account or PIN");
            return;
        }

        if (!verifyOTP(accNo)) return;

        System.out.println("✅ Current Balance: " + acc.balance);
    }

    // Admin view
    public void adminView() {
        System.out.print("Enter Admin Username: ");
        String user = sc.nextLine();
        System.out.print("Enter Admin Password: ");
        String pass = sc.nextLine();

        if (!admin.username.equals(user) || !admin.password.equals(pass)) {
            System.out.println("❌ Invalid Admin credentials.");
            return;
        }

        System.out.println("---- All Accounts ----");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
    }
}

// MAIN CLASS
public class BMSWithOTP {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Check Balance");
            System.out.println("6. Transaction History");
            System.out.println("7. Admin View");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> bank.openAccount();
                case 2 -> bank.deposit();
                case 3 -> bank.withdraw();
                case 4 -> bank.transfer();
                case 5 -> bank.checkBalance();
                case 6 -> bank.showTransactions();
                case 7 -> bank.adminView();
                case 8 -> { System.out.println("Exiting..."); return; }
                default -> System.out.println("❌ Invalid choice.");
            }
        }
    }
}
