import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

// ------------------- Transaction Class -------------------
class Transaction {
    String accountNumber;
    String type;   // Deposit / Withdraw / Transfer
    double amount;
    double balanceAfter;
    LocalDateTime timestamp;

    public Transaction(String accountNumber, String type, double amount, double balanceAfter) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "[AccNo: " + accountNumber + ", " + type + " : " + amount +
               ", Balance: " + balanceAfter + ", Time: " + timestamp + "]";
    }
}

// ------------------- Account Class -------------------
class Account {
    String accountNumber;
    String name;
    String type;
    double balance;
    String contact;
    String pin;

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
        return "{ AccNo: " + accountNumber + ", Name: " + name + ", Type: " + type +
               ", Balance: " + balance + ", Contact: " + contact + " }";
    }
}

// ------------------- Bank Class -------------------
class Bank {
    private static int accCounter = 1000;
    private static final String ADMIN_PIN = "9999"; // fixed admin PIN
    List<Account> accounts = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();

    private String generateAccountNumber() {
        return "ACC" + (++accCounter);
    }

    private Account findAccount(String accNo) {
        for (Account acc : accounts) {
            if (acc.accountNumber.equals(accNo)) {
                return acc;
            }
        }
        return null;
    }

    private boolean verifyPin(Account acc, Scanner sc) {
        System.out.print("Enter PIN for Account " + acc.accountNumber + ": ");
        String enteredPin = sc.nextLine();
        if (enteredPin.equals(acc.pin)) return true;
        System.out.println("❌ Invalid PIN!");
        return false;
    }

    // ----------------- Features -----------------
    public void openAccount(Scanner sc) {
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

        String accNumber = generateAccountNumber();
        Account acc = new Account(accNumber, name, type, balance, contact, pin);
        accounts.add(acc);

        transactions.add(new Transaction(accNumber, "Account Opened", balance, balance));

        System.out.println("✅ Account Created Successfully!");
        System.out.println(acc);
    }

    public void depositMoney(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null) { System.out.println("❌ Invalid Account Number."); return; }
        if (!verifyPin(acc, sc)) return;

        System.out.print("Enter Amount to Deposit: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0) { System.out.println("❌ Deposit must be positive."); return; }

        acc.balance += amount;
        transactions.add(new Transaction(accNo, "Deposit", amount, acc.balance));
        System.out.println("✅ Deposit Successful. Balance: " + acc.balance);
    }

    public void withdrawMoney(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null) { System.out.println("❌ Invalid Account Number."); return; }
        if (!verifyPin(acc, sc)) return;

        System.out.print("Enter Amount to Withdraw: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0 || amount > acc.balance) {
            System.out.println("❌ Invalid or Insufficient Balance.");
            return;
        }

        if (acc.type.equalsIgnoreCase("Savings") && (acc.balance - amount) < 1000) {
            System.out.println("❌ Savings account must maintain minimum balance 1000.");
            return;
        }

        acc.balance -= amount;
        transactions.add(new Transaction(accNo, "Withdraw", amount, acc.balance));
        System.out.println("✅ Withdrawal Successful. Balance: " + acc.balance);
    }

    public void transferMoney(Scanner sc) {
        System.out.print("Enter Sender Account Number: ");
        String senderNo = sc.nextLine();
        Account sender = findAccount(senderNo);
        if (sender == null) { System.out.println("❌ Invalid Sender Account."); return; }
        if (!verifyPin(sender, sc)) return;

        System.out.print("Enter Receiver Account Number: ");
        String receiverNo = sc.nextLine();
        Account receiver = findAccount(receiverNo);
        if (receiver == null) { System.out.println("❌ Invalid Receiver Account."); return; }

        System.out.print("Enter Amount to Transfer: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount <= 0 || amount > sender.balance) {
            System.out.println("❌ Invalid or Insufficient Balance.");
            return;
        }

        sender.balance -= amount;
        receiver.balance += amount;

        transactions.add(new Transaction(senderNo, "Transfer Sent", amount, sender.balance));
        transactions.add(new Transaction(receiverNo, "Transfer Received", amount, receiver.balance));

        System.out.println("✅ Transfer Successful!");
    }

    public void showTransactions(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null) { System.out.println("❌ Invalid Account."); return; }
        if (!verifyPin(acc, sc)) return;

        System.out.print("Show last how many transactions? ");
        int n = sc.nextInt();
        sc.nextLine();

        int count = 0;
        for (int i = transactions.size() - 1; i >= 0 && count < n; i--) {
            if (transactions.get(i).accountNumber.equals(accNo)) {
                System.out.println(transactions.get(i));
                count++;
            }
        }
    }

    public void accountStatement(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null) { System.out.println("❌ Invalid Account."); return; }
        if (!verifyPin(acc, sc)) return;

        System.out.println("----- Account Statement -----");
        System.out.println(acc);
        for (Transaction t : transactions) {
            if (t.accountNumber.equals(accNo)) System.out.println(t);
        }

        // Bonus: Export to text file
        try (PrintWriter pw = new PrintWriter(new FileWriter(accNo + "_statement.txt"))) {
            pw.println("----- Account Statement -----");
            pw.println(acc);
            for (Transaction t : transactions) {
                if (t.accountNumber.equals(accNo)) pw.println(t);
            }
            System.out.println("📂 Statement exported to " + accNo + "_statement.txt");
        } catch (IOException e) {
            System.out.println("❌ Error writing file.");
        }
    }

    public void closeAccount(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null) { System.out.println("❌ Invalid Account."); return; }
        if (!verifyPin(acc, sc)) return;

        System.out.println("⚠ Closing account " + accNo + ". Final Balance: " + acc.balance);
        accounts.remove(acc);
        transactions.add(new Transaction(accNo, "Account Closed", 0, 0));
        System.out.println("✅ Account Closed Successfully.");
    }

    // ---------- Admin Access ----------
    public void adminViewAccounts(Scanner sc) {
        System.out.print("Enter Admin PIN: ");
        String pin = sc.nextLine();
        if (!pin.equals(ADMIN_PIN)) {
            System.out.println("❌ Invalid Admin PIN.");
            return;
        }
        System.out.println("----- All Accounts -----");
        for (Account acc : accounts) {
            System.out.println(acc);
        }
    }
   
    // ------------ Balance Check -----------
    public void checkBalance(Scanner sc) {
    System.out.print("Enter Account Number: ");
    String accNo = sc.nextLine();
    Account acc = findAccount(accNo);
    if (acc == null) {
        System.out.println("❌ Invalid Account Number.");
        return;
    }
    if (!verifyPin(acc, sc)) return;

    System.out.println("💰 Current Balance for " + acc.accountNumber + ": " + acc.balance);
}

}

// ------------------- Main Class -------------------
public class BankManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("1. Open Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transfer Money");
            System.out.println("5. Balance Check");   // ✅ new
            System.out.println("6. Show Transactions");
            System.out.println("7. Account Statement");
            System.out.println("8. Close Account");
            System.out.println("9. Admin - View All Accounts");
            System.out.println("10. Exit");

            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

        switch (choice) {
            case 1 -> bank.openAccount(sc);
            case 2 -> bank.depositMoney(sc);
            case 3 -> bank.withdrawMoney(sc);
            case 4 -> bank.transferMoney(sc);
            case 5 -> bank.checkBalance(sc);   // ✅ new
            case 6 -> bank.showTransactions(sc);
            case 7 -> bank.accountStatement(sc);
            case 8 -> bank.closeAccount(sc);
            case 9 -> bank.adminViewAccounts(sc);
            case 10 -> { System.out.println("👋 Exiting..."); return; }
            default -> System.out.println("❌ Invalid choice!");
        }

            
        }
    }
}
