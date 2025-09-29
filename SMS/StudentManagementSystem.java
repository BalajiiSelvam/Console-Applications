
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/* * A SIMPLE STUDENT MANAGEMENT SYSTEM WITH BELOW LISTED FEATURES
   * 1. Adding students
   * 2. Updating studdent details
   * 3. Removing students
   * 4. Displaying student details
   * 5. Seraching a student by Roll NUmber
   * 6. Adding marks
   * 7. Calculating percentage and remarks
   * 8. Generate Overall student report 
   * 9. Download the students data as text files
   * 10. Exiting the system
   * 
   * 
   * Author : BalajiSelvam
   * Java Developer
   */

class Student{
    String rollNo;
    String name;
    String department;
    String yearOfStudy;
    Map<String, Integer> marks = new HashMap<>();
    Student(String rollNo, String name, String department, String yearOfStudy){
        this.rollNo = rollNo;
        this.name = name;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
    }

    public String toString(){
        return "[ " + rollNo + ", " + name + ", " + department + ", " + yearOfStudy 
            + ", Marks=" + marks + " ]";
    }

}

//STUDENT MANAGEMENT
class StudentRecords{
    Scanner sc = new Scanner(System.in);
    List<Student> students = new ArrayList<>();
    int totalStudents = 0;


    //Add Students
    public void addStudents(Student s){
        totalStudents++;
        students.add(s);
        System.out.println("Student " + s.rollNo + " was added Sucessfully");
    }

    //Display Students
    public void displayStudents(){
        if(students.isEmpty()){
            System.out.println("No Students to dispplay");
            return;
        }
        for(Student s : students){
            System.out.println(s);
        }
        System.out.println("Total students : "+totalStudents);
    }

    //Update Students
    public void updateStudents(String rollNo){
        int flag = 0;
        for(Student s : students){
            if(s.rollNo.equalsIgnoreCase(rollNo)){
                flag = 1;
                System.out.print("Edit name (Yes/No) : ");
                String resp = sc.nextLine();
                if(resp.equalsIgnoreCase("yes")){
                    System.out.print("Enter the updated name : ");
                    String newName = sc.nextLine();
                    s.name = newName;
                }
                System.out.print("Edit department (Yes/No) : ");
                String resp1 = sc.nextLine();
                if(resp1.equalsIgnoreCase("yes")){
                    System.out.print("Enter the updated department : ");
                    String newDept = sc.nextLine();
                    s.department = newDept;
                }
                System.out.println("Edit year (Yes/No) : ");
                String resp2 = sc.nextLine();
                if(resp2.equalsIgnoreCase("yes")){
                    System.out.print("Enter the updated year : ");
                    String newYear = sc.nextLine();
                    s.yearOfStudy = newYear;
                }
            }
        }
        if(flag==0){
            System.out.println("Invalid Roll Number");
        }
    }

    //Remove Students
    public void removeStudents(String rollNO){
        for(Student s : students){
            if(s.rollNo.equalsIgnoreCase(rollNO)){
                totalStudents--;
                System.out.println("Student " + s.rollNo + " is removed sucessfully");
                students.remove(s);
                return;
            }
        }
        System.out.println("Invalid Roll Number");
    }

    //Search Student by rollNo
    public void searchStudent(String rollNOO){
        for(Student s : students){
            if(s.rollNo.equalsIgnoreCase(rollNOO)){
                System.out.println(s);
                return;
            }
        }
    System.out.println("Invalid Roll Number");
    }

    //Add marks
    public void addMarks(String rollNo) {
        for(Student s : students){
            if(s.rollNo.equalsIgnoreCase(rollNo)){
                System.out.print("How many subjects ? ");
                int noOfSubjects = sc.nextInt();
                sc.nextLine();
                for(int i=1; i<=noOfSubjects; i++){
                    System.out.print("Enter subject name: ");
                    String subject = sc.nextLine();
                    System.out.print("Enter marks: ");
                    int mark = sc.nextInt();
                    sc.nextLine(); 
                    s.marks.put(subject, mark);
                }
                System.out.println("Marks added for " + s.name);
                return;
            }
        }
        System.out.println("Invalid Roll Number");
    }

    //Calculate percentage
    public void calculatePecentage(String rolllNo){
        for(Student s : students){
            if(s.rollNo.equalsIgnoreCase(rolllNo)){
                if(s.marks.isEmpty()){
                    System.out.println("No marks available for " + s.rollNo);
                    return;
                }
                int total = 0;
                for(int m : s.marks.values()){
                    total += m;
                }
                double per = (double)(total / s.marks.size());
                System.out.println("Percentage : " + per);
                if(per>=90){
                    System.out.println("Remarks : Execellent");
                }
                else if(per>=80 && per<=90){
                    System.out.println("Remarks : Good");
                }
                else if (per>=60 && per<=80){
                    System.out.println("Remarks : Fair");
                }
                else{
                    System.out.println("Remarks : Need to Improve");
                }
                return;
            }
        }
        System.out.print("Invalid roll number");
    }

    //Generate Report
    public void generateReport(String rnum){
        for(Student s : students){
            if(s.rollNo.equalsIgnoreCase(rnum)){
                System.out.println("Student Name : " + s.name);
                System.out.println("Student Roll Number : " + s.rollNo);
                System.out.println("Student Department : " + s.department);
                System.out.println("Student Study Year : " + s.yearOfStudy);
                System.out.println("Number of Subjects Studied : " + s.marks.size());
                int i = 1;
                for(Map.Entry<String, Integer> entry : s.marks.entrySet()){
                    String subname = entry.getKey();
                    int submarks = entry.getValue();
                    System.out.println("Subject-" + i++ + " : " + subname + " ... " + "Marks : " + submarks);
                }
                calculatePecentage(rnum);
                return;
            }
        }
        System.out.println("Invalid Roll Number");
    }

    //Download Report
    public void saveStudentsList(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            if (students.isEmpty()) {
                bw.write("No students available.");
            } else {
                for (Student s : students) {
                    bw.write(s.toString() + "\n");
                }
            }
            System.out.println("All students saved to file: " + fileName);
        } catch (Exception e) {
            System.out.println("Error while saving students: " + e.getMessage());
        }
    }



}

//STUDENT MANAGEMENT
public class StudentManagementSystem{
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        StudentRecords sr = new StudentRecords();

        while(true){
            System.out.println("\n ===== STUDENT MANAGEMENT SYSTEM ===== ");
            System.out.println("1. Add Students");
            System.out.println("2. Update Student Details");
            System.out.println("3. Remove Students");
            System.out.println("4. Display Students");
            System.out.println("5. Search Students");
            System.out.println("6. Add marks");
            System.out.println("7. Calculate Percentage");
            System.out.println("8. Generate Report");
            System.out.println("9. Download Students List");
            System.out.println("10. Exit");

            System.out.print("\nEnter your choice : ");
            int choice = scan.nextInt();
            scan.nextLine();

            switch(choice){
                case 1:
                    System.out.print("Enter Student RollNumber : ");
                    String rollNo = scan.nextLine();
                    System.out.print("Enter Student Name : ");
                    String name = scan.nextLine();
                    System.out.print("Enter the department : ");
                    String dept = scan.nextLine();
                    System.out.print("Enter the Year of Stduy : ");
                    String yearOfStudy = scan.nextLine();
                    sr.addStudents(new Student(rollNo, name, dept, yearOfStudy));
                    break;
                case 2:
                    System.out.print("Enter the Student Roll Number to edit : ");
                    String rNo = scan.nextLine();
                    sr.updateStudents(rNo);
                    break;
                case 3:
                    System.out.print("Enter the Student Roll Number to remove : ");
                    String rNoo = scan.nextLine();
                    sr.removeStudents(rNoo);
                    break;
                case 4:
                    sr.displayStudents();
                    break;
                case 5:
                    System.out.print("Enter the Roll Number to Search : ");
                    String rn = scan.nextLine();
                    sr.searchStudent(rn);
                    break;
                case 6:
                    System.out.print("Enter Roll Number: ");
                    String rnMarks = scan.nextLine();
                    sr.addMarks(rnMarks);
                    break;
                case 7:
                    System.out.print("Enter the Student Roll Number : ");
                    String rnr = scan.nextLine();
                    sr.calculatePecentage(rnr);
                    break;
                case 8:
                    System.out.print("Enter the Student Roll NUmber : ");
                    String rnumber = scan.nextLine();
                    sr.generateReport(rnumber);
                    break;
                case 9:
                    System.out.println("Enter the file name in .txt format : ");
                    String fname = scan.nextLine();
                    sr.saveStudentsList(fname);
                    break;
                case 10:
                    System.out.println("Exiting....\n");
                    scan.close();
                    return;
                default:
                    System.out.print("Invalid choice...");
            }
        }
    }
}