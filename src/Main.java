import com.library.model.*;
import com.library.service.BookService;
import com.library.service.BorrowService;
import com.library.service.UserService;
import com.library.util.DatabaseSeeder;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
         DatabaseSeeder seeder = new DatabaseSeeder();
         BookService bookService = new BookService();
         UserService userService = new UserService();
         BorrowService borrowService = new BorrowService();
         User currentUser = null;
         System.out.println("----Welcome to Library Management System----");
        int times = 0;
        int max_attempts = 3;
        while(currentUser == null ){
            if (times == max_attempts){
                System.out.println("Access denied. Exiting System");
                return;
            }
            if(times == 0){
                System.out.println("----Please login----");
            }
            else {
                int attemptsLeft = max_attempts - times;
                System.out.println("Login failed, please try again ("+ attemptsLeft+"/"+max_attempts+")" );
            }

            System.out.println("Please enter your username: ");
            String username = sc.nextLine();
            System.out.println("Please enter your phone number: ");
            String phoneNumber = sc.nextLine();
            currentUser = userService.loginUser(username,phoneNumber);
            times++;
        }
        int choice;

        do {
            System.out.println("\n---- MAIN MENU ----");
            System.out.println("1. Add com.library.model.Book to Database");
            System.out.println("2. Review all Books");
            System.out.println("3. Update com.library.model.Book");
            System.out.println("4. Delete com.library.model.Book");
            System.out.println("5. Add/Update com.library.model.User");
            System.out.println("6. Borrow a com.library.model.Book");
            System.out.println("7. Return a com.library.model.Book");
            System.out.println("8. Exit System");
            System.out.println("9. Creating new database");
            System.out.println("10. Reset database");
            System.out.print("Choose applications (1-10): ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    mainMenu(sc,bookService);
                    break;
                case 2:
                    reviewBook(bookService);
                    break;
                case 3:
                    updateBook(sc,bookService);
                    break;
                case 4:
                    System.out.println("Enter the com.library.model.Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    bookService.deleteBook(id);
                    break;
                case 5:
                    userManager(sc,userService);
                    break;
                case 6:
                    borrowBook(sc,bookService,borrowService,currentUser);
                    break;
                case 7:
                    int userid =currentUser.getId();
                    System.out.println("Enter com.library.model.Book ID to return: ");
                    int bookid = sc.nextInt();
                    sc.nextLine();
                    borrowService.returnBook(userid, bookid);
                    break;
                case 8:
                    System.out.println("Exiting the system. Goodbye " + currentUser.getName() + "!");
                    break;
                case 9:
                    seeder.seedMockData();
                    break;
                case 10:
                    reseedMockData(sc,currentUser,seeder);
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-10.");
            }
        } while (choice != 10);
    }
    private static void reseedMockData(Scanner sc,User currentUser, DatabaseSeeder seeder){
        if(currentUser.getBorrowLimit()!=999) {
            System.out.println("Action denied");
            return;
        }
        System.out.println("⚠️ Dangerous Operation ⚠️");
        System.out.println("This action will clear everythins");
        System.out.print("You sure to continue?(YES/NO): ");

        String confirm = sc.nextLine();

        if (!confirm.equals("YES")) {
            System.out.println("🛑 Operation canceled. Your data still available.");
            return;
            }
        seeder.clearDatabase();
            }

    private static void userManager(Scanner sc,UserService userService){
        System.out.println("---- USER MANAGEMENT ----");
        System.out.println("1. Add com.library.model.User To Database");
        System.out.println("2. Update com.library.model.User To Database");
        int choice2 = sc.nextInt();
        sc.nextLine();
        User userToUpdate = null;
        List<User> userList = userService.getAllUserList();
        switch (choice2) {
            case 1:
                System.out.println("Enter the Username: ");
                String username = sc.nextLine();
                System.out.println("Enter phone number: ");
                String phone = sc.nextLine();
                System.out.println("Enter role: ");
                String role = sc.nextLine();
                userToUpdate = new User(username,phone,role);
                userService.addUserToDatabase(userToUpdate);
                break;
            case 2:
                System.out.println("Chosen com.library.model.User ID you want to update: ");
                int userId = sc.nextInt();
                sc.nextLine();
                for(User u : userList){
                    if(u.getId() == userId){
                        userToUpdate = u;
                        break;
                    }
                }
                if (userToUpdate != null ) {
                    System.out.println("Enter new username: ");
                    String newUsername = sc.nextLine();
                    System.out.println("Enter new phone number: ");
                    String Newphone = sc.nextLine();
                    System.out.println("Enter new role: ");
                    String Newrole = sc.nextLine();

                    userToUpdate.setName(newUsername);
                    userToUpdate.setPhone(Newphone);
                    userToUpdate.setRole(Newrole);
                    userService.updateUserInDatabase(userToUpdate);
                }else {
                    System.out.println("Cant find UserID "+ userId);
                }
        }
    }
    private static void borrowBook(Scanner sc,BookService bookService,BorrowService borrowService,User currentUser){
        System.out.println("---- BORROW BOOK ----");
        List<Book> freshBookList = bookService.getAllBooks();
        for(Book b : freshBookList){
            b.displayInfo();
        }
        System.out.println("Choose com.library.model.Book ID you want to Borrow: ");
        int bookID = sc.nextInt();
        sc.nextLine();
        try {
            borrowService.borrowBook(currentUser, bookID);
            //  Thay vì bắt nhập uId,lấy luôn currentUser.getId() để truyền vào
        }catch (Exception e){
            System.out.println("Cant borrow com.library.model.Book now ");
        }
    }
    private static void updateBook(Scanner sc,BookService bookService){
        System.out.println("---- UPDATE BOOK ----");
        System.out.println("Enter com.library.model.Book ID to update: ");
        int idb =sc.nextInt();
        sc.nextLine();
        System.out.print("Enter NEW Title: ");
        String newTitle = sc.nextLine();
        System.out.print("Enter NEW Author: ");
        String newAuthor = sc.nextLine();

        System.out.println("Choose type book you want to update: ");
        System.out.println("1. com.library.model.EBook");
        System.out.println("2. com.library.model.TextBook");
        System.out.println("3. com.library.model.ReferenceBook");
        int choose = sc.nextInt();
        sc.nextLine();

        Book bookToUpdate = null;

        if (choose == 1) {
            System.out.println("Enter the file size: ");
            double fileSize = sc.nextDouble();
            bookToUpdate = new EBook(newTitle, newAuthor,idb, fileSize);
        }
        if (choose == 2) {
            System.out.println("Enter the book subject: ");
            String newSubject = sc.nextLine();
            System.out.println("Enter the book status: ");
            String status = sc.nextLine();
            bookToUpdate = new TextBook(newTitle, newAuthor, newSubject,idb, status);
        }
        if (choose == 3) {
            System.out.println("Enter the book status: ");
            String status = sc.nextLine();
            bookToUpdate =new ReferenceBook(newTitle, newAuthor,idb, status);
        }
        bookService.addBookToDatabase(bookToUpdate);
    }
    private static void reviewBook( BookService bookService){
        System.out.println("----com.library.model.Book List on the Library Management System----");
        List<Book> bookList = bookService.getAllBooks();
        if (bookList.isEmpty()) {
            System.out.println("There is no Books in the Library Management System");
        } else {
            for (Book b : bookList) {
                b.displayInfo();
            }
        }
        System.out.println("---------------------------------------");
    }
    private static void mainMenu(Scanner sc, BookService bookService){
        System.out.println("----Enter the com.library.model.Book Type----");
        System.out.println("1. com.library.model.EBook");
        System.out.println("2. com.library.model.TextBook");
        System.out.println("3. com.library.model.ReferenceBook");
        int bookType = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter the com.library.model.Book Title: ");
        String title = sc.nextLine();

        System.out.println("Enter the com.library.model.Book Author: ");
        String author = sc.nextLine();

        Book newBook = null;
        // Khai báo biến com.library.model.Book (Lớp cha) để chứa dữ liệu - ĐÂY LÀ ĐA HÌNH
        if (bookType == 1) {
            System.out.println("Enter the com.library.model.Book fileSize: ");
            double fileSize = sc.nextDouble();
            newBook = new EBook(title, author, fileSize);
        } else if (bookType == 2) {
            System.out.println("Enter the com.library.model.Book subject: ");
            String subject = sc.nextLine();
            System.out.println("Enter the com.library.model.Book status: ");
            String status = sc.nextLine();
            newBook = new TextBook(title, author, subject, status);
        } else if (bookType == 3) {
            System.out.println("Enter the com.library.model.Book status: ");
            String status = sc.nextLine();
            newBook = new ReferenceBook(title, author, status);
        } else {
            System.out.println("Invalid Input");
        }
        if (newBook != null) {
            bookService.addBookToDatabase(newBook);
        }
    }
}






