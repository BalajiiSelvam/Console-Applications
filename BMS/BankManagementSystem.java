import java.util.*;

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

class Bank {
    private static int accCounter = 1000;  
    List<Account> accounts = new ArrayList<>();

    private String generateAccountNumber() {
        return "ACC" + (++accCounter);
    }

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
        System.out.println("Account Created Successfully!");
        System.out.println(acc);
    }
}
public class BankManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();
        bank.openAccount(sc);
    }
}
