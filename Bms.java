import java.util.*;
import java.time.*;
import java.io.*;

//ACCOUNT CLASS
class Account{
    String accountNumber;
    String name;
    String type; // Savings/ Current/ Admin
    String contact;
    String pin;
    double balance;
    boolean isAdmin;

    //CONSTRUCTOR
    Account(String accountNumber, String name, String type, String contact, String pin, double balance, boolean isAdmin){
        this.accountNumber = accountNumber;
        this.name = name;
        this.type = type;
        this.contact = contact;
        this.pin = pin;
        this.balance = balance;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString(){
        return "{ Acc No : " + accountNumber + ", Name : " + name + ", Type : " + type + ", Balance : " + balance + " } ";
    }
}

//TRANSACTION CLASS
class Transaction{
    String accountNumber;
    String type; // Deposit / Withdraw / Transfer
    double amount;
    double balanceAfter;
    LocalDateTime timestamp;

    //CONSTRUCTOR
    Transaction(String accountNumber, String type, double amount, double balanceAfter){
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

  @Override
    public String toString() {
        return "[ AccNo: " + accountNumber + ", " + type + " : " + amount + ", BalanceAfter: " + balanceAfter +
               ", Time: " + timestamp + "]";
    }

}

//BANK CLASS
class Bank{
    private static int accGen = 9823130;
    List<Account> accounts = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    //ACCOUNT CREATION FUNCTIONALITY
    private static String generateAccNum(boolean isAdmin){
        return isAdmin ? "ADMIN" + ++accGen : "ACC" + ++accGen;
    }
    public void openAccount(boolean isAdmin){
        String name = IO.readln("Enter name : ");
        String type = "User";
        double balance = 0;
        if(!isAdmin){
            type = IO.readln("Enter type (Savings/Current) : ");
            balance = Integer.parseInt(IO.readln("Enter Initial deposit : "));
        }
        else{
            type = "Admin";
        }

        String contact = IO.readln("Enter phone : ");
        String pin = IO.readln("Enter pin : ");
        String accountNumber = generateAccNum(isAdmin);
        Account acc = new Account(accountNumber, name, type, contact, pin, balance, isAdmin);
        accounts.add(acc);
        IO.println("Account created successfully. Your Account No : " + accountNumber + ". Maintain 500 minimum balance always...");
    }

    //----------FIND ACCOUNT------------
    private Account findAccount(String accN){
        for(Account ac : accounts){
            if(ac.accountNumber.equals(accN)) return ac;
        }
        return null;
    }
    private boolean verifyPin(Account acc, String pin){
        if(acc.pin.equals(pin)) return true;
        return false;
    }

    //DEPOSIT
    public void deposit(){
        String accN = IO.readln("Enter your acc num : ");
        Account acc = findAccount(accN);
        if(acc==null || acc.type.startsWith("ADMIN")){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(acc,pin)){
            IO.println("Invalid PIN");
            return;
        }
        double amount = Integer.parseInt(IO.readln("Enter amount : "));
        if(amount<=0){
            IO.println("Amount must be greter than 0");
            return;
        }
        acc.balance+=amount;
        transactions.add(new Transaction(accN, "Deposit", amount, acc.balance));
        IO.println("Deposit Sucessfull. Balance : " + acc.balance);
    }

    //WITHDRAW
    public void withdraw(){
        String accN = IO.readln("Enter your acc num : ");
        Account acc = findAccount(accN);
        if(acc==null || acc.type.startsWith("ADMIN")){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(acc,pin)){
            IO.println("Invalid PIN");
            return;
        }
        double amount = Integer.parseInt(IO.readln("Enter amount : "));
        if(amount<=0){
            IO.println("Amount must be greter than 0");
            return;
        }
        double minBalance = acc.type.equalsIgnoreCase("Savings") ? 500 : 0;
        if(acc.balance - amount < minBalance){
            IO.println("Insufficient Balance. Unbale to withdraw amount");
            return;
        }
        acc.balance-=amount;
        transactions.add(new Transaction(accN, "Withdraw", amount, acc.balance));
        IO.println("Withdraw Sucessfull. Balance : " + acc.balance);
    }

    //TRANSFER
    public void transfer(){
        String accN = IO.readln("Enter Sender Acc Number : ");
        Account accS = findAccount(accN);
        if(accS==null || accS.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String acccN = IO.readln("Enter Receiver Acc Number : ");
        Account accR = findAccount(acccN);
        if(accR==null || accR.isAdmin){
            IO.println("Invalid Receiver Account");
            return;
        }
        String pin = IO.readln("Enter PIN  : ");
        if(!verifyPin(accS, pin)){
            IO.println("Invalid PIN");
            return;
        }
        double amount = Integer.parseInt(IO.readln("Enter the amount to transfer : "));
        int minBalance = accS.type.equalsIgnoreCase("Savings") ? 500 : 0;
        if(accS.balance - amount < minBalance){
            IO.println("Insufficient Balance. Unable to transer amount");
            return;
        }
        accS.balance -= amount;
        accR.balance += amount;
        transactions.add(new Transaction(accN, "Transfer", amount, accS.balance));
        IO.println("Transfer succesfull. Balance : " + accS.balance);
    }

    //CHECK BALANCE
    public void checkBalance(){
        String acN = IO.readln("Enter acc num : ");
        Account acc1 = findAccount(acN);
        if(acc1==null || acc1.accountNumber.startsWith("ADMIN")){
            IO.println("Invalid account");
            return;
        }
        String pin1 = IO.readln("Enter pin : ");
        if(!verifyPin(acc1, pin1)){
             IO.println("Invalid PIN");
            return;
        }
        IO.println("Current Balance : "  + acc1.balance);
    }

    //TRANSACTION HISTORY
    public void transactionHistory() {
        String accN = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accN);
        if(acc==null || acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }
        IO.println("---------------------------------- TRANSACTION HISTORY ----------------------------------\n");
        for(Transaction t : transactions){
            if(t.accountNumber.equals(accN)){
                System.out.printf("AccNo: %-12s Type: %-10s Amount: %-10s BalanceAfter: %-10s Time: %-20s%n",
                  t.accountNumber, t.type, t.amount, t.balanceAfter, t.timestamp);

            }
        }
    }
    
    //DOWNLOADING REPORT
    public void downloadStatement() {
        String accN = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accN);
        if(acc==null || acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }
        try(FileWriter fw = new FileWriter(accN + "_statement.txt")){
            fw.write(acc + "\n");
            for(Transaction t : transactions){
                if(t.accountNumber.equals(accN)) fw.write(t + "\n");
            }
            IO.println("Statement exported as " + accN + "_statement.txt");
        }catch (IOException e) {
                System.out.println("File Error: " + e.getMessage());
        }
    }

    //VIEW ALL ACCOUNT
    public void viewAllAccounts(){
        String accN = IO.readln("Enter Acc Num : ");
        Account ac = findAccount(accN);
        if(ac==null || !ac.isAdmin){
            IO.println("Requires Admin privilege to view all accounts");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(ac, pin)){
            IO.println("Invalid PIN");
            return;
        }

        IO.println("------------------------------------- ALL USER ACCOUNTS -------------------------------------");
        for(Account a : accounts){
            if(!a.isAdmin) IO.println("UserName : " + a.name + ", ACC Num : " + a.accountNumber + ", Type : " + a.type + ", Contact : " + a.contact);
        }
        IO.println("\n Total Users : " + accounts.size());
    }

    //VIEW ALL SAVINGS ACCOUNT
    public void viewAllSavingsAccount(){
        String accN = IO.readln("Enter Acc Num : ");
        Account ac = findAccount(accN);
        if(ac==null || !ac.isAdmin){
            IO.println("Requires Admin privilege to view all accounts");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(ac, pin)){
            IO.println("Invalid PIN");
            return;
        }
        
        int count = 0;
        IO.println("------------------------------------- ALL SAVINGS ACCOUNTS -------------------------------------");
        for(Account a : accounts){
            if(!a.isAdmin && a.type.equalsIgnoreCase("Savings")) {
                IO.println("UserName : " + a.name + ", ACC Num : " + a.accountNumber + ", Contact : " + a.contact);
                count++;
            }
        }
        IO.println("\n Total Savings account : " + count);
    }

    //VIEW ALL ADMINS ACCOUNT
    public void viewAllAdmins(){
        String accN = IO.readln("Enter Acc Num : ");
        Account ac = findAccount(accN);
        if(ac==null || !ac.isAdmin){
            IO.println("Requires Admin privilege to view all accounts");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(ac, pin)){
            IO.println("Invalid PIN");
            return;
        }
        
        int count = 0;
        IO.println("------------------------------------- ALL ADMINS LIST -------------------------------------");
        for(Account a : accounts){
            if(a.isAdmin) {
                IO.println("UserName : " + a.name + ", ACC Num : " + a.accountNumber + ", Contact : " + a.contact);
                count++;
            }
        }
        IO.println("\n Total Admin account : " + count);
    }

    //REMOVE USER ACCOUNT
    public void terminateUserAccount(){
        String accN = IO.readln("Enter Admin Acc Num : ");
        Account ac = findAccount(accN);
        if(ac==null || !ac.isAdmin){
            IO.println("Requires Admin privilege to view all accounts");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPin(ac, pin)){
            IO.println("Invalid PIN");
            return;
        }
        String acU = IO.readln("Enter the User Acc Num to terminate : ");
        Account accU = findAccount(acU);
        if(accU==null || accU.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        else{
            accounts.remove(accU);
            IO.println("ACcount  " + acU + " terminated succesfully");
        }
    }

}

//BMS class
public class Bms{
    void main(){
        Scanner sc = new Scanner(System.in);
        Bank b = new Bank();
        
        String type = IO.readln("Enter type (User/Admin) : ");
        if(type.equalsIgnoreCase("User")){
            while(true){
                IO.println("\n===== WELCOME TO BMS USER PORTAL ====\n");
                IO.println("1. Open User account");
                IO.println("2. Open Admin account");
                IO.println("3. Deposit");
                IO.println("4. Withdraw");
                IO.println("5. Transfer");
                IO.println("6. Check Balance");
                IO.println("7. View Transactions");
                IO.println("8. Download Account Statement");
                IO.println("9. Exit");

                int choice = Integer.parseInt(IO.readln("Enter your choice : "));
                switch(choice){
                    case 1 -> b.openAccount(false);
                    case 2 -> b.openAccount(true);
                    case 3 -> b.deposit();
                    case 4 -> b.withdraw();
                    case 5 -> b.transfer();
                    case 6 -> b.checkBalance();
                    case 7 -> b.transactionHistory();
                    case 8 -> b.downloadStatement();
                    case 9 -> { 
                                sc.close();
                                IO.println("Bye Bye ...\n"); 
                                return; 
                              }
                    default -> IO.println("Invalid choice");
                }
            }
        }
        else if(type.equalsIgnoreCase("Admin")){
            while(true){
                IO.println("\n===== WELCOME TO BMS ADMIN PORTAL");
                IO.println("1. View all user accounts");
                IO.println("2. View all savings accounts");
                IO.println("3. View Admins list");
                IO.println("4. Terminate user account");

                int choice = Integer.parseInt(IO.readln("Enter your choice : "));
                switch(choice){
                    case 1 -> b.viewAllAccounts();
                    case 2 -> b.viewAllSavingsAccount();
                    case 3 -> b.viewAllAdmins();
                    case 4 -> b.terminateUserAccount();
                    case 5 -> {
                                sc.close();
                                IO.println("Bye Bye...");
                                return;
                              }
                    default -> IO.println("Invalid Choice");
                }
            }
        }
        else{
            IO.println("Not a valid user type");
            sc.close();
            return;
        }
    }
}