import java.util.*;
import java.time.*;
import java.io.*;

// CLASS ACCOUNT
class Account{
    String accountNumber;
    String name;
    String type; // Savings, Current, AdminAccount
    String phone;
    double balance;
    String pin; // 6-digit pin number for security
    boolean isAdmin;

    Account(String accountNumber, String name, String type, String phone, double balance, String pin, boolean isAdmin ){
        this.accountNumber = accountNumber;
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.balance = balance;
        this.pin = pin;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString(){
        return "Acc No : " + accountNumber + ", Name : " + name + ", Type : " + type + ", Balance : " + balance + ", Phone : " + phone;
    }

}

// CLASS TRANSACTION
class Transaction{
    String accountNumber;
    String type; // Deposit, Withdraw, Transfer
    double amount;
    double balanceAfter; // Denotes the balance after the transaction
    LocalDateTime timestamp;

    Transaction(String accountNumber, String type, double amount, double balanceAfter){
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString(){
        return "Acc No : " + accountNumber + ", Type : " + type + ", BalanceAfter : " + balanceAfter + ", Timestamp : " + timestamp;
    }
}

// CLASS OTPDETAILS
class OtpDetails{
    String otp;
    LocalDateTime expiryTime;

    OtpDetails(String otp, LocalDateTime expiryTime){
        this.otp = otp;
        this.expiryTime = expiryTime;
    }
}

// CLASS BANK
class Bank{
    private static int acn = 1000;
    // Bank b = new Bank();
    Scanner sc = new Scanner(System.in);

    List<Account> accounts = new ArrayList<>();
    List<Transaction> transactions = new ArrayList<>();

    Map<String, OtpDetails> otpMapping = new HashMap<>();

    // ----------------- OPEN ACCOUNT -----------------
    private String generateAccountNumber(boolean isAdmin){
        return isAdmin ? "ADMINACC" + ++acn : "USERACC" + ++acn;
    }
    public void openAccount(boolean isAdmin){
        String name = IO.readln("Enter name : ");
        String type = "User";
        double balance = 0;
        if(!isAdmin){
            type = IO.readln("Enter the account type (Savings/Current) : ");
            balance = Integer.parseInt(IO.readln("Enter initial deposit amount : "));
        }
        else{
            type = "Admin";
        }
        String pin = IO.readln("Set PIN number : ");
        String phone = IO.readln("Enter phone number : ");
        String accountNumber = generateAccountNumber(isAdmin);
        Account acc = new Account(accountNumber, name, type, phone, balance, pin, isAdmin);
        accounts.add(acc);
        IO.println("Account created success. Account Number : " + accountNumber);
    }

    // ----------------- FIND ACCOUNT -----------------
    private Account findAccount(String accountNumber){
        for(Account a : accounts){
            if(a.accountNumber.equals(accountNumber)){
                return a;
            }
        }
        return null;
    }

    // ----------------- PIN VERIFICATION -----------------
    private boolean verifyPIN(Account acc, String pin){
        if(acc.pin.equals(pin)){
            return true;
        }
        return false;
    }

    // ----------------- OTP VERIFICATION -----------------
    private String generateOTP(){
        Random r = new Random();
        return String.valueOf(1000 + r.nextInt(8000));
    }
    // public boolean verifyOTP(String accountNumber){
    //     String otp = generateOTP();
    //     otpMapping.put(accountNumber, otp);

    //     IO.println("OTP Generated : " + otp);
    //     String user_entered = IO.readln("Enter the OTP : ");
    //     if(user_entered.equals(otp)){
    //         otpMapping.remove(accountNumber);
    //         return true;
    //     }
    //     return false;
    // }
    public boolean verifyOTP(String accountNumber){
        String otp = generateOTP();
        LocalDateTime expire = LocalDateTime.now().plusSeconds(30);
        otpMapping.put(accountNumber, new OtpDetails(otp, expire));

        IO.println("OTP Generated ( valid for 30 sec ): " + otp);
        String user_entered = IO.readln("Enter OTP : ");

        OtpDetails details = otpMapping.get(accountNumber);
        if(details != null){
            if(LocalDateTime.now().isAfter(details.expiryTime)){
                IO.println("OTP Expired");
                otpMapping.remove(accountNumber);
                return false;
            }
            if(details.otp.equals(user_entered)){
                otpMapping.remove(accountNumber);
                return true;
            }
        }
        IO.println("Invalid OTP");
        return false;
    }

    // ----------------- DEPOSIT -----------------
    public void deposit(){
        String accountNumber = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accountNumber);
        if(acc==null || acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPIN(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }
        double amount = Integer.parseInt(IO.readln("Enter amount : "));
        if(amount<=0){
            IO.println("Amount must be greater than zero");
            return;
        }
        acc.balance += amount;
        transactions.add(new Transaction(accountNumber, "DEPOSIT", amount, acc.balance));
        IO.println("Deposit Success. Current Balance : " + acc.balance);
    }

    // ----------------- WITHDRAW -----------------
    public void withdraw(){
        String accountNumber = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accountNumber);
        if(acc==null || acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        double amount = Integer.parseInt(IO.readln("Enter amount : "));
        if(amount<=0){
            IO.println("Amount must be greater than zero");
            return;
        }
        double minBal = acc.type.equalsIgnoreCase("Savings") ? 500 : 0;
        if(acc.balance - amount < minBal){
            IO.println("Insufficient balance. Unable to withdraw");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPIN(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }
        if(!verifyOTP(accountNumber)){
            return;
        }
        acc.balance -= amount;
        transactions.add(new Transaction(accountNumber, "WITHDRAW", amount, acc.balance));
        IO.println("Withdraw success. Current Balance : " + acc.balance);
    }

    // ----------------- TRANSFER -----------------
    public void transfer(){
        String accNumS = IO.readln("Enter Sender Acc Num : ");
        Account accS = findAccount(accNumS);
        if(accS==null || accS.isAdmin){
            IO.println("Invalid Sender Account");
            return;
        }
        String accNumR = IO.readln("Enter Recipent Acc Num : ");
        Account accR = findAccount(accNumR);
        if(accR==null || accR.isAdmin){
            IO.println("Invalid Recipent Account");
            return;
        }
        double amount = Integer.parseInt(IO.readln("Enter amount : "));
        if(amount<=0){
            IO.println("Amount must be greater than zero");
            return;
        }
        double minBal = accS.type.equalsIgnoreCase("Savings") ? 500 : 0;
        if(accS.balance - amount < minBal){
            IO.println("Insufficient balance. Unable to withdraw");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPIN(accS, pin)){
            IO.println("Invalid PIN");
            return;
        }
        if(!verifyOTP(accNumS)){
            return;
        }
        accS.balance -= amount;
        accR.balance += amount;
        transactions.add(new Transaction(accNumS, "TRANSFER", amount, accS.balance));
        IO.println("Transfer success. Current Balance : " + accS.balance);
    }

    // ----------------- BALANCE CHECK -----------------
    public void checkBalance(){
        String accountNumber = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accountNumber);
        if(acc==null || acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPIN(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }
        IO.println("Current Balance : " + acc.balance);
    }

    // ----------------- TRANSACTION HISTORY -----------------
    public void viewTransactions(){
        String accountNumber = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accountNumber);
        if(acc==null || acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPIN(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }
        for(Transaction t : transactions){
            if(t.accountNumber.equals(accountNumber)){
                IO.println(t);
            }
        }
    }

    // ----------------- DOWNLOAD STATEMENT -----------------
    public void downloadStatement(){
        String accountNumber = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accountNumber);
        if(acc==null || acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPIN(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }   
        try(FileWriter f = new FileWriter(accountNumber + "_statement.txt")){
            f.write(acc + "\n");
            for(Transaction t : transactions){
                if(t.accountNumber.equals(accountNumber)){
                    f.write(t + "\n");
                }
            }
            IO.println("Account Statement downloaded : " + accountNumber + "_statement.txt");
        }catch(Exception e){
            IO.println(e);
        }
    }

    // ----------------- VIEW ACCOUNTS -----------------
    public void viewAccounts(){
        String accountNumber = IO.readln("Enter Acc Num : ");
        Account acc = findAccount(accountNumber);
        if(acc==null || !acc.isAdmin){
            IO.println("Invalid Account");
            return;
        }
        String pin = IO.readln("Enter PIN : ");
        if(!verifyPIN(acc, pin)){
            IO.println("Invalid PIN");
            return;
        }          
        for(Account t : accounts){
            if(!t.isAdmin)
               System.out.printf("AccNo: %-14s Type: %-10s Name:  %-14s Phone: %-10s%n", t.accountNumber, t.type, t.name, t.phone);
        } 
    }

}

// CLASS BANKSYSTEM
public class BMSWithTimeOTP{
    public static void main(String[] args) {
        Bank b = new Bank();
        Scanner scan = new Scanner(System.in);
        while(true){
            IO.println("\n===== WELCOME TO BALA BANK -----");
            IO.println("1. Open User Account");
            IO.println("2. Open Admin Account");
            IO.println("3. Deposit");
            IO.println("4. Withdraw");
            IO.println("5. Transfer");
            IO.println("6. Check Balance");
            IO.println("7. View Transaction History");
            IO.println("8. Download Statement");
            IO.println("9. View Accounts ( ADMIN )");
            IO.println("10. Exit\n");

            int choice = Integer.parseInt(IO.readln("Enter your choice : "));
            switch(choice){
                case 1 -> b.openAccount(false);
                case 2 -> b.openAccount(true);
                case 3 -> b.deposit();
                case 4 -> b.withdraw();
                case 5 -> b.transfer();
                case 6 -> b.checkBalance();
                case 7 -> b.viewTransactions();
                case 8 -> b.downloadStatement();
                case 9 -> b.viewAccounts();
                case 10 -> {
                            scan.close();
                            IO.println("Bye Bye...\n");
                            return;
                          }
                default -> IO.println("Invalid Choice");
            }
        }
    }
}