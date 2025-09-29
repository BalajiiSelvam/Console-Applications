
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
        return "{ ID : " + bookId + ", Name : " + bookName + ", Author : " + author + ", Quantity : " + quantity + " } ";
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
        return "{ Name : " + memberName + ", ID : " + memberId + " } ";
    }
}

//LIBRARY
class Library{
    Scanner scan = new Scanner(System.in);
    List<Book> books = new ArrayList<>();
    List<Member> members = new ArrayList<>();
    Map<String,List<String>> issued = new HashMap<>();

    //Adding Books
    public void addBooks(Book b){
        books.add(b);
        System.out.println("Book " + b.bookId + " added successfully");
    }

    //Updating quantity
    public void updateQuantity(String bookId){
        for(Book b : books){
            if(b.bookId.equals(bookId)){
                System.out.print("Enter the available copies : ");
                int newQuantity = scan.nextInt();
                b.quantity = newQuantity;
                System.out.println("Book Quantity updated sucessfully");
                return;
            }
        }
        System.out.println("Invalid BookId");
    }

    //Search books
    public void searchBooks(String bookName){
        for(Book b : books){
            if(b.bookName.equalsIgnoreCase(bookName)){
                System.out.println(b);
                return;
            }
        }
        System.out.println("Book Not Found");
    }

    //Remove books
    public void removeBooks(String bookID){
        for(Book b : books){
            if(b.bookId.equals(bookID)){
                System.out.println("Book " + bookID + " removed sucessfully");
                books.remove(b);
                return;
            }
        }
        System.out.println("Invalid BookId");
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
        System.out.println("\nTotal number of books : " + books.size());
    }

    //Adding Members
    public void addMembers(Member m){
        members.add(m);
        System.out.println("Member " + m.memberId + " added successfully");
    }

  //Display Members
    public void displayMembers(){
        if(members.isEmpty()){
            System.out.println("No members to display...");
            return;
        }

    //Search Members
    public void searchMembers(String memberId1){
        for(Member m : members){
            if(m.memberId.equals(memberId1)){
                System.out.println(m);
                return;
            }
        }
        System.out.println("Invalid Member ID");
    }

    //Remove Members
    public void removeMembers(String memberId2){
        for(Member m : members){
            if(m.memberId.equals(memberId2)){
                System.out.println("Member " + memberId2 + " removed successfully");
                members.remove(m);
                return;
            }
        }
        System.out.println("Invalid MemberId");
    }
    
    //Display Members
    public void displayMembers(){
        if(members.isEmpty()){
            System.out.println("No Members to display");
            return;
        }
        for(Member m : members){
            System.out.println(m);
        }
        System.out.println("Total number of members : " + members.size());
    }

    //Issue books
    private Book checkBookValidity(String bID){
        for(Book b : books){
            if(b.bookId.equals(bID)){
                return b;
            }
        }
        return null;
    }
    private Member checkMemberValidity(String mID){
        for(Member m : members){
            if(m.memberId.equals(mID)){
                return m;
            }
        }
        return null;
    }
    public void issueBooks(String bId, String mId){
        Book book = checkBookValidity(bId);
        if(book==null){
            System.out.println("Invalid BookId");
            return;
        }
        Member member = checkMemberValidity(mId);
        if(member==null){
            System.out.println("Invalid MemberId");
            return;
        }
        if(book.quantity > 0){
            book.quantity--;
            issued.putIfAbsent(bId, new ArrayList<>());
            issued.get(bId).add(mId);
            System.out.println("Book " + bId + " issued to Member " + mId);
        }
        else{
            System.out.println("Book not available at the moment");
        }
    
    }

    //Return books
    public void returnBooks(String bid, String mid){
        if(issued.containsKey(bid) && issued.get(bid).contains(mid)){
            for(Book b : books){
                if(b.bookId.equals(bid)){
                    b.quantity++;
                    issued.get(bid).remove(mid);
                    if(issued.get(bid).isEmpty()){
                        issued.remove(bid);
                    }
                    System.out.println("Book " + bid + " returned by Member " + mid);
                    return;
                }
            }
        }
        else{
            System.out.println("Invalid credentials");
        }
    }

    //Check Availability
    public void checkAvailability(String bookid){
        for(Book b : books){
            if(b.bookId.equals(bookid)){
                if(b.quantity > 0){
                    System.out.println("Book " + bookid + " available for issue");
                    return;
                }
                System.out.println("Book " + bookid + " not available at this moment");
                return;
            }
        }
        System.out.println("Invalid BookId");
    }

    //Track Books
    public void trackBooks(String biD){
        for(Book b : books){
            if(b.bookId.equals(biD)){
                int issuednumbers = issued.containsKey(biD) ? issued.get(biD).size() : 0;

                System.out.println("Book ID : " + biD);
                System.out.println("Book Name : " + b.bookName);
                System.out.println("Total Copies : " + b.quantity);
                System.out.println("Currently Available : " + (b.quantity-issuednumbers));
                if(issuednumbers>0){
                    System.out.println("Issued Members : "  + issued.get(biD));
                }
                else{
                    System.out.println("No Member currently holding this book");
                }
                return;
            }

        }
        System.out.println("Invalid BookId");
    }
}

//MANAGEMENT
class LibraryManagementSystem{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library lb = new Library();

        while(true){
            System.out.println("\n +++++ LIBRARY MANAGEMENT SYSTEM +++++");
            System.out.println("1. Add Books");
            System.out.println("2. Update Book Quantity");
            System.out.println("3. Search Books");
            System.out.println("4. Remove Books");
            System.out.println("5. Display Books");
            System.out.println("6. Add Members");
            System.out.println("7. Search Members");
            System.out.println("8. Remove Members");
            System.out.println("9. Display Members");
            System.out.println("10. Issue Books");
            System.out.println("11. Return Books");
            System.out.println("12. Check Book Availability");
            System.out.println("13. Track Books");
            System.out.println("14. Exit");


            System.out.print("\nEnter your choice(1-5) : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    System.out.print("\nEnter BookID : ");
                    String bookId = sc.nextLine();
                    System.out.print("Enter BookName : ");
                    String bookName = sc.nextLine();
                    System.out.print("Enter AuthorName : ");
                    String authorName = sc.nextLine();
                    System.out.print("Enter Quantity : ");
                    int quantity = sc.nextInt();
                    lb.addBooks(new Book(bookId, bookName, authorName, quantity));
                    break;
                case 2:
                    System.out.print("\nEnter the BookId to Update : ");
                    String bookId1 = sc.nextLine();
                    lb.updateQuantity(bookId1);
                    break;
                case 3:
                    System.out.print("\nEnter the BookName to search : ");
                    String bookName1 = sc.nextLine();
                    lb.searchBooks(bookName1);
                    break;
                case 4:
                    System.out.print("\nEnter the BookId to remove : ");
                    String bookId2 = sc.nextLine();
                    lb.removeBooks(bookId2);
                    break;
                case 5:
                    lb.displayBooks();
                    break;
                case 6:
                    System.out.print("\nEnter MemberId : ");
                    String memId = sc.nextLine();
                    System.out.print("Enter MemberName : ");
                    String memName = sc.nextLine();
                    lb.addMembers(new Member(memId, memName));
                    break;
                case 7:
                    System.out.print("\nEnter the MemberId to search : ");
                    String memId1 = sc.nextLine();
                    lb.searchMembers(memId1);
                    break;
                case 8:
                    System.out.print("\nEnter the MemberId to remove : ");
                    String memId2 = sc.nextLine();
                    lb.removeMembers(memId2);
                    break;
                case 9:
                    lb.displayMembers();
                    break;
                case 10:
                    System.out.print("\nEnter the BookId : ");
                    String bIDD = sc.nextLine();
                    System.out.print("Enter the MemberId : ");
                    String mIDD = sc.nextLine();
                    lb.issueBooks(bIDD, mIDD);
                    break;
                case 11:
                    System.out.print("\nEnter the BookId to return : ");
                    String bid = sc.nextLine();
                    System.out.print("Enter the MemberId : ");
                    String mid = sc.nextLine();
                    lb.returnBooks(bid, mid);
                    break;
                case 12:
                    System.out.print("\nEnter the BookId to check status : ");
                    String boookId = sc.nextLine();
                    lb.checkAvailability(boookId);
                    break;
                case 13:
                    System.out.print("\nEnter the BookId to Track : ");
                    String bIId = sc.nextLine();
                    lb.trackBooks(bIId);
                    break;
                case 14:
                    System.out.println("\nExiting.....");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Try again...");
            }
        }    
    }
}
