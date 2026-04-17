import java.util.List;
import java.util.Scanner;

public class main {
     static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
         DatabaseSeeder seeder = new DatabaseSeeder();
         BookService bookService = new BookService();
         UserService userService = new UserService();
         BorrowService borrowService = new BorrowService();
         List<Book> listBooks = bookService.getAllBooks();
         Book bookToUpdate = null;
         List<User> userList = userService.getALlUserList();
         User userToUpdate = null;
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
            System.out.println("1. Add Book to Database");
            System.out.println("2. Review all Books");
            System.out.println("3. Update Book");
            System.out.println("4. Delete Book");
            System.out.println("5. Add/Update User");
            System.out.println("6. Borrow a Book");
            System.out.println("7. Return a Book");
            System.out.println("8. Exit System");
            System.out.println("9. Creating new database");
            System.out.println("10. Reset database");
            System.out.print("Chọn chức năng (1-10): ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("----Enter the Book Type----");
                    System.out.println("1. EBook");
                    System.out.println("2. TextBook");
                    System.out.println("3. ReferenceBook");
                    int bookType = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter the Book Title: ");
                    String title = sc.nextLine();

                    System.out.println("Enter the Book Author: ");
                    String author = sc.nextLine();

                    Book newBook = null;
                    // Khai báo biến Book (Lớp cha) để chứa dữ liệu - ĐÂY LÀ ĐA HÌNH
                    if (bookType == 1) {
                        System.out.println("Enter the Book fileSize: ");
                        double fileSize = sc.nextDouble();
                        newBook = new EBook(title, author, fileSize);
                    } else if (bookType == 2) {
                        System.out.println("Enter the Book subject: ");
                        String subject = sc.nextLine();
                        System.out.println("Enter the Book status: ");
                        String status = sc.nextLine();
                        newBook = new textBook(title, author, subject, status);
                    } else if (bookType == 3) {
                        System.out.println("Enter the Book status: ");
                        String status = sc.nextLine();
                        newBook = new referenceBook(title, author, status);
                    } else {
                        System.out.println("Invalid Input");
                    }
                    if (newBook != null) {
                        bookService.addBookToDatabase(newBook);
                    }

                    break;
                case 2:
                    System.out.println("----Book List on the Library Management System----");
                    List<Book> bookList = bookService.getAllBooks();
                    if (bookList.isEmpty()) {
                        System.out.println("There is no Books in the Library Management System");
                        break;
                    } else {
                        for (Book b : bookList) {
                            b.inThongTin();
                        }
                    }
                    System.out.println("---------------------------------------");
                    break;
                case 3:
                    System.out.println("---- UPDATE BOOK ----");
                    System.out.print("Enter NEW Title: ");
                    String newTitle = sc.nextLine();
                    System.out.print("Enter NEW Author: ");
                    String newAuthor = sc.nextLine();

                    System.out.println("Choose type book you want to update: ");
                    System.out.println("1. EBook");
                    System.out.println("2. TextBook");
                    System.out.println("3. ReferenceBook");
                    int choose = sc.nextInt();
                    sc.nextLine();

                    Book newBookToUpdate = null;

                    if (choose == 1) {
                        System.out.println("Enter the file size: ");
                        double fileSize = sc.nextDouble();
                        bookToUpdate = new EBook(newTitle, newAuthor, fileSize);
                    }
                    if (choose == 2) {
                        System.out.println("Enter the book subject: ");
                        String newSubject = sc.nextLine();
                        System.out.println("Enter the book status: ");
                        String status = sc.nextLine();
                        bookToUpdate = new textBook(newTitle, newAuthor, newSubject, status);
                    }
                    if (choose == 3) {
                        System.out.println("Enter the book status: ");
                        String status = sc.nextLine();
                        bookToUpdate =new referenceBook(newTitle, newAuthor, status);
                    }
                    bookService.addBookToDatabase(bookToUpdate);
                    break;
                case 4:
                    System.out.println("Enter the Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    bookService.deleteBook(id);
                    break;
                case 5:
                    System.out.println("---- USER MANAGEMENT ----");
                    System.out.println("1. Add User To Database");
                    System.out.println("2. Update User To Database");
                    int choice2 = sc.nextInt();
                    User newUser = null;
                    switch (choice2) {
                        case 1:
                            System.out.println("Enter the Username: ");
                            String username = sc.nextLine();
                            System.out.println("Enter phone number: ");
                            String phone = sc.nextLine();
                            System.out.println("Enter role: ");
                            String role = sc.nextLine();
                            newUser = new User(username,phone,role);
                            userService.addUserToDatabase(newUser);
                            break;
                        case 2:
                            System.out.println("Chosen User ID you want to update: ");
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
                                userToUpdate.setphone(Newphone);
                                userToUpdate.setRole(Newrole);
                                userService.updateUserInDatabase(userToUpdate);
                            }else {
                                System.out.println("Cant find UserID "+ userId);
                            }
                    }
                    break;
                case 6:
                    System.out.println("---- BORROW BOOK ----");
                    for(Book b : listBooks){
                        b.inThongTin();
                    }
                    System.out.println("Choose Book ID you want to Borrow: ");
                    int bookID = sc.nextInt();
                    sc.nextLine();
                    try {
                        borrowService.borrowBook(currentUser.getId(), bookID);
                        //  Thay vì bắt nhập uId,lấy luôn currentUser.getId() để truyền vào
                    }catch (Exception e){
                        System.out.println("Cant borrow Book now ");
                    }
                    break;
                case 7:
                    int userid =currentUser.getId();
                    System.out.println("Enter Book ID to return: ");
                    int bookid = sc.nextInt();
                    borrowService.returnBook(userid, bookid);
                    break;
                case 8:
                    System.out.println("Exiting the system. Goodbye " + currentUser.getName() + "!");
                    break;
                case 9:
                    seeder.seedMockData();
                    break;
                case 10:
                    if(currentUser.getRole().equals("Super Admin")) {
                        System.out.println("⚠️ Dangerous Operation ⚠️");
                        System.out.println("This action will clear everythins");
                        System.out.print("You sure to continue?(YES/NO): ");

                        String confirm = sc.nextLine();

                        if (confirm.equals("YES")) {
                            seeder.clearDatabase();
                        } else {
                            System.out.println("🛑 Operation canceled. Your data still available.");
                        }
                        break;
                    }
                    else {
                        System.out.println("Action denied");
                        break;
                    }
                default:
                    System.out.println("Invalid choice. Please select 1-10.");
                    break;
            }
        } while (choice != 8);
            }}



