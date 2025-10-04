import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
        return "[AccNo: " + accountNumber + ", " + type + " : " + amount + ", BalanceAfter: " + balanceAfter +
               ", Time: " + timestamp + "]";
    }
}
class Account {
    String accountNumber;
    String name;
    String type;       // Savings / Current / Admin
    double balance;
    String contact;
    String pin;
    boolean isAdmin;
    public Account(String accountNumber, String name, String type, double balance, String contact, String pin, boolean isAdmin) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.type = type;
        this.balance = balance;
        this.contact = contact;
        this.pin = pin;
        this.isAdmin = isAdmin;
    }
    @Override
    public String toString() {
        return "{AccNo: " + accountNumber + ", Name: " + name + ", Type: " + type + ", Balance: " + balance + "}";
    }
}

class Bank {
    private static int accCounter = 1000;
    List<Account> accounts = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    // ------------------ Account Creation ------------------
    private String generateAccountNumber(boolean isAdmin) {
        return isAdmin ? "ADMIN" + (++accCounter) : "ACC" + (++accCounter);
    }

    public void openAccount(boolean isAdmin) {
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        String type = "User";
        double balance = 0;
        if (!isAdmin) {
            System.out.print("Enter Account Type (Savings/Current): ");
            type = sc.nextLine();
            System.out.print("Enter Initial Deposit: ");
            balance = sc.nextDouble();
            sc.nextLine();
        } else {
            type = "Admin";
        }

        System.out.print("Enter Contact Number: ");
        String contact = sc.nextLine();
        System.out.print("Set a 4-digit PIN: ");
        String pin = sc.nextLine();

        String accNumber = generateAccountNumber(isAdmin);
        Account acc = new Account(accNumber, name, type, balance, contact, pin, isAdmin);
        accounts.add(acc);
        System.out.println("✅ Account Created Successfully! " + accNumber);
    }

    // ------------------ Find Account ------------------
    private Account findAccount(String accNo) {
        for (Account acc : accounts) {
            if (acc.accountNumber.equals(accNo)) return acc;
        }
        return null;
    }

    private boolean verifyPin(Account acc) {
        System.out.print("Enter PIN: ");
        String enteredPin = sc.nextLine();
        if (!acc.pin.equals(enteredPin)) {
            System.out.println("❌ Invalid PIN!");
            return false;
        }
        return true;
    }

    // ------------------ Deposit ------------------
    public void deposit() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null || acc.isAdmin) { System.out.println("❌ Invalid Account"); return; }
        if (!verifyPin(acc)) return;

        System.out.print("Enter Deposit Amount: ");
        double amount = sc.nextDouble(); sc.nextLine();
        if (amount <= 0) { System.out.println("❌ Amount must be positive"); return; }

        acc.balance += amount;
        transactions.add(new Transaction(accNo, "Deposit", amount, acc.balance));
        System.out.println("✅ Deposit Successful. Balance: " + acc.balance);
    }

    // ------------------ Withdraw ------------------
    public void withdraw() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null || acc.isAdmin) { System.out.println("❌ Invalid Account"); return; }
        if (!verifyPin(acc)) return;

        System.out.print("Enter Withdrawal Amount: ");
        double amount = sc.nextDouble(); sc.nextLine();
        if (amount <= 0) { System.out.println("❌ Amount must be positive"); return; }

        double minBalance = acc.type.equalsIgnoreCase("Savings") ? 500 : 0;
        if (acc.balance - amount < minBalance) {
            System.out.println("❌ Insufficient Balance. Minimum balance: " + minBalance);
            return;
        }

        acc.balance -= amount;
        transactions.add(new Transaction(accNo, "Withdraw", amount, acc.balance));
        System.out.println("✅ Withdraw Successful. Balance: " + acc.balance);
    }

    // ------------------ Transfer ------------------
    public void transfer() {
        System.out.print("Enter Sender Account Number: ");
        String fromAccNo = sc.nextLine();
        Account fromAcc = findAccount(fromAccNo);
        if (fromAcc == null || fromAcc.isAdmin) { System.out.println("❌ Invalid Account"); return; }
        if (!verifyPin(fromAcc)) return;

        System.out.print("Enter Receiver Account Number: ");
        String toAccNo = sc.nextLine();
        Account toAcc = findAccount(toAccNo);
        if (toAcc == null || toAcc.isAdmin) { System.out.println("❌ Invalid Receiver"); return; }

        System.out.print("Enter Amount to Transfer: ");
        double amount = sc.nextDouble(); sc.nextLine();
        double minBalance = fromAcc.type.equalsIgnoreCase("Savings") ? 500 : 0;
        if (fromAcc.balance - amount < minBalance) { System.out.println("❌ Insufficient Balance"); return; }

        fromAcc.balance -= amount;
        toAcc.balance += amount;
        transactions.add(new Transaction(fromAccNo, "Transfer To " + toAccNo, amount, fromAcc.balance));
        transactions.add(new Transaction(toAccNo, "Transfer From " + fromAccNo, amount, toAcc.balance));
        System.out.println("✅ Transfer Successful");
    }

    // ------------------ Balance ------------------
    public void checkBalance() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null || acc.isAdmin) { System.out.println("❌ Invalid Account"); return; }
        if (!verifyPin(acc)) return;

        System.out.println("💰 Current Balance: " + acc.balance);
    }

    // ------------------ Transaction History ------------------
    public void transactionHistory() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null || acc.isAdmin) { System.out.println("❌ Invalid Account"); return; }
        if (!verifyPin(acc)) return;

        System.out.println("📋 Last Transactions:");
        for (Transaction t : transactions) {
            if (t.accountNumber.equals(accNo)) System.out.println(t);
        }
    }

    // ------------------ Account Statement ------------------
    public void accountStatement() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null || acc.isAdmin) { System.out.println("❌ Invalid Account"); return; }
        if (!verifyPin(acc)) return;

        System.out.println("📄 Account Statement:");
        System.out.println(acc);
        for (Transaction t : transactions) {
            if (t.accountNumber.equals(accNo)) System.out.println(t);
        }

        // Export to text file (bonus)
        System.out.print("Export statement to file? (yes/no): ");
        String resp = sc.nextLine();
        if (resp.equalsIgnoreCase("yes")) {
            try (FileWriter fw = new FileWriter(accNo + "_statement.txt")) {
                fw.write(acc + "\n");
                for (Transaction t : transactions) {
                    if (t.accountNumber.equals(accNo)) fw.write(t + "\n");
                }
                System.out.println("✅ Statement exported as " + accNo + "_statement.txt");
            } catch (IOException e) {
                System.out.println("❌ File Error: " + e.getMessage());
            }
        }
    }

    // ------------------ Close Account ------------------
    public void closeAccount() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc == null || acc.isAdmin) { System.out.println("❌ Invalid Account"); return; }
        if (!verifyPin(acc)) return;

        System.out.println("💰 Withdrawing remaining balance: " + acc.balance);
        acc.balance = 0;
        accounts.remove(acc);
        System.out.println("✅ Account Closed Successfully");
    }

    // ------------------ Admin View Accounts ------------------
    public void adminViewAccounts() {
        System.out.print("Enter Admin Account Number: ");
        String adminAccNo = sc.nextLine();
        Account admin = findAccount(adminAccNo);
        if (admin == null || !admin.isAdmin) { System.out.println("❌ Invalid Admin"); return; }
        if (!verifyPin(admin)) return;

        System.out.println("📋 All Bank Accounts:");
        for (Account acc : accounts) {
            if (!acc.isAdmin) System.out.println(acc);
        }
    }
}

public class BankManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. Create User Account");
            System.out.println("2. Create Admin Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Check Balance");
            System.out.println("7. Transaction History");
            System.out.println("8. Account Statement");
            System.out.println("9. Close Account");
            System.out.println("10. Admin: View All Accounts");
            System.out.println("11. Exit");
            System.out.print("Choose Option: ");

            int choice = sc.nextInt(); sc.nextLine();

            switch (choice) {
                case 1 -> bank.openAccount(false);
                case 2 -> bank.openAccount(true);
                case 3 -> bank.deposit();
                case 4 -> bank.withdraw();
                case 5 -> bank.transfer();
                case 6 -> bank.checkBalance();
                case 7 -> bank.transactionHistory();
                case 8 -> bank.accountStatement();
                case 9 -> bank.closeAccount();
                case 10 -> bank.adminViewAccounts();
                case 11 -> { System.out.println("Exiting..."); return; }
                default -> System.out.println("❌ Invalid Choice");
            }
        }
    }
}
