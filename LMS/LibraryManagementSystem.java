
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/* * A SIMPLE LIBRARY MANAGEMENT SYSTEM WITH BELOW FEATURS
   * 1. Add books
   * 2. Display books
   * 3. Remove books
   * 4. Search books
   * 5. Add Members
   * 6. Remove Members
   * 7. Display Members
   * 8. Issue Books
   * 9. Return books
   * 10. Check book availability
   * 11. Track Issues
   * 12. Exit
*/
//BOOKS
class Book{
    String bookId;
    String bookName;
    String author;
    int quantity;

    Book(String bookId, String bookName, String author, int quantity){
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.quantity = quantity;
    }

    public String toString(){
        return "{ " + bookId + ", " + bookName + ", " + author + ", " + quantity + ", " + " }";
    }
}

//MEMBERS
class Member{
    String memberId;
    String memberName;

    Member(String memberId, String memberName){
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public String toString(){
        return "{ " + memberId + ", " + memberName + "} ";
    }
}

//LIBRARY MANAGEMENT
class Library{
    List<Book> books = new ArrayList<>();
    List<Member> members = new ArrayList<>();
    Map<String, List<String>> issued = new HashMap<>();

    int totalBooks = 0;
    int totalMembers = 0;

    //Adding books
    public void addBooks(Book b){
        books.add(b);
        totalBooks++;
        System.out.println("Book " + b.bookId + " added succesfully");
    }

    //Display books
    public void displayBooks(){
        if(books.isEmpty()){
            System.out.println("No Books to display");
            return;
        }
        for(Book b : books){
            System.out.println(b);
        }
        System.out.println("Total Books : " + totalBooks);
        
    }

    //Remove Books
    public void removeBooks(String bOOkID){
        for(Book b : books){
            if(b.bookId.equals(bOOkID)){
                books.remove(b);
                System.out.println("Book removed Sucessfully");
                return;
            }
        }
        System.out.println("No Book exists with the given BookId");
    }

    //Search books
    public void searchBooks(String title){
        for(Book b : books){
            if(b.bookName.equalsIgnoreCase(title)){
                System.out.println("Book " + title + " found");
                return;
            }
        }
        System.out.println("Book " + title + " not found");
    }

    //Adding Members
    public void addMembers(Member m){
        members.add(m);
        System.out.println("Member " + m.memberId + " added succesfully");
    }

    //Display Members
    public void displayMembers(){
        if(members.isEmpty()){
            System.out.println("No members to display...");
            return;
        }
        for(Member m : members){
            totalMembers++;
            System.out.println(m);
        }
        System.out.println("Total members : " + totalMembers);
    }

    //Remove Members
    public void removeMembers(String memId){
        for(Member m : members){
            if(m.memberId.equals(memId)){
                members.remove(m);
                System.out.println("Member removed successfully");
                return;
            }
        }
        System.out.println("MemberId not exist");

    }
    //Issue books
    public void issueBooks(String bId, String mId){
        for(Book b : books){
            if(b.bookId.equalsIgnoreCase(bId)){
                if(b.quantity > 0){
                    b.quantity--;
                    issued.putIfAbsent(bId, new ArrayList<>());
                    issued.get(bId).add(mId);
                    System.out.println("Book " + bId + " issued to " + mId + " Succesfully" );
                    return;
                }
                else{
                    System.out.println("Book not available");
                    return;
                }
            }
        }
        System.out.println("Invalid BookId");    
    }

    //Return Books
    public void returnBooks(String bookId, String memberId){
        if(issued.containsKey(bookId) && issued.get(bookId).contains(memberId)){
            for(Book b : books){
                if(b.bookId.equals(bookId)){
                    b.quantity++;
                    issued.get(bookId).remove(memberId);
                    if(issued.get(bookId).isEmpty()){
                        issued.remove(bookId);
                    }
                System.out.println("Book " + bookId + " returned by " + memberId);
                return;
                }
            }
           
        }
        else{
                System.out.println("Invalid Operation");
        }
    }

    //Track Books
    public void trackBooks(String bookId){
    for(Book b : books){
        if(b.bookId.equals(bookId)){
            int available = b.quantity;
            int totalIssued = issued.containsKey(bookId) ? issued.get(bookId).size() : 0;

            System.out.println("\nTracking Book: " + bookId + " (" + b.bookName + ")");
            System.out.println("Available copies: " + available);
            System.out.println("Issued copies: " + totalIssued);

            if(totalIssued > 0){
                System.out.println("Issued to members: " + issued.get(bookId));
            } else {
                System.out.println("No members currently holding this book.");
            }
            return;
        }
    }
    System.out.println("Invalid Book Id");
}

    //Book availability
    public void checkAvailability(String bookIdd){
        for(Book b : books){
            if(b.bookId.equals(bookIdd)){
                if(b.quantity!=0){
                    System.out.println("Book available. Ready fo Issue");
                    return;
                }
                else{
                    System.out.println("Sorry Book not availble. Come after some time");
                    return;
                }
            }
        }
        System.out.println("Invalid Book Id");
    }
}

public class LibraryManagementSystem{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library lb = new Library();

        while(true){
            System.out.println("\n=====LIBRARY MANAGEMENT SYSTEM=====");
            System.out.println("1. Add Book");
            System.out.println("2. Display Books");
            System.out.println("3. Remove Books");
            System.out.println("4. Search Books");
            System.out.println("5. Add Member");
            System.out.println("6. Display Members");
            System.out.println("7. Remove Members");
            System.out.println("8. Issue Book");
            System.out.println("9. Return Book");
            System.out.println("10. Check Book Availability");
            System.out.println("11. Track books");
            System.out.println("12. Exit");

            System.out.print("\nEnter your choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    System.out.print("Enter Book Id : ");
                    String id = sc.nextLine();
                    System.out.print("Enter Book Title : ");
                    String name = sc.nextLine();
                    System.out.print("Enter Author name : ");
                    String aname = sc.nextLine();
                    System.out.print("Enter Quantity : ");
                    int qty = sc.nextInt();
                    lb.addBooks(new Book(id, name, aname, qty));
                    break;
                case 2:
                    lb.displayBooks();
                    break;
                case 3:
                    System.out.print("Enter the BookId to remove : ");
                    String bIId = sc.nextLine();
                    lb.removeBooks(bIId);
                    break;
                case 4:
                    System.out.print("Enter the Book title to search : ");
                    String btitle = sc.nextLine();
                    lb.searchBooks(btitle);
                    break;
                case 5:
                    System.out.print("Enter Member Id : ");
                    String mId = sc.nextLine();
                    System.out.print("Enter member name : ");
                    String mname = sc.nextLine();
                    lb.addMembers(new Member(mId, mname));
                    break;
                case 6:
                    lb.displayMembers();
                    break;
                case 7:
                    System.out.println("Enter the MemberId to remove : ");
                    String membId = sc.nextLine();
                    lb.removeMembers(membId);
                    break;
                case 8:
                    System.out.print("Enter the Book Id to get : ");
                    String bId = sc.nextLine();
                    System.out.print("Please provide your member Id : ");
                    String mIdd = sc.nextLine();
                    lb.issueBooks(bId, mIdd);
                    break;
                case 9:
                    System.out.print("Enter the Book Id to return : ");
                    String bIdd = sc.nextLine();
                    System.out.print("Enter your MemberId : ");
                    String mIddd = sc.nextLine();
                    lb.returnBooks(bIdd, mIddd);
                    break;
                case 10:
                    System.out.print("Enter the Book Id to check availability : ");
                    String bookID = sc.nextLine();
                    lb.checkAvailability(bookID);
                    break;
                case 11:
                    System.out.print("Enter the Book Id to track : ");
                    String tbId = sc.nextLine();
                    lb.trackBooks(tbId);
                    break;
                case 12:
                    System.out.println("Exiting... Bye Bye");
                    sc.close();
                    break;
                default:
                    System.out.println("Invalid Choice try again");
            }
        }
    }
    
}