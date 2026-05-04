package com.library.util;

import com.library.model.EBook;
import com.library.model.ReferenceBook;
import com.library.model.TextBook;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.UserService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseSeeder {

    public void seedMockData(){
        String checkQuery = "SELECT COUNT(*) FROM users";
        try(Connection conn = DatabaseConnection.getConnection();
            Statement stmt= conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkQuery)  ) {
            if (rs.next() && rs.getInt(1) > 0) {
                // Đã có dữ liệu thì cảnh báo và thoát hàm luôn (Fail-fast)
                System.out.println("⚠️ Dữ liệu mẫu đã tồn tại trong Database. Bỏ qua lệnh Seed.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Generating data for the system....");
        UserService us = new UserService();
        BookService bs = new BookService();
        try{
            User admin = new User(1,10,"Minh","0988845124","Admin");
            us.addUserToDatabase(admin);

            User member1 = new User(2, 3, "Đạt", "0911222333", "Member");
            us.addUserToDatabase(member1);

            User member2 = new User(3,3,"Linh","09862790833","Member");
            us.addUserToDatabase(member2);

            // EBooks (Cần fileSize)
            bs.addBookToDatabase(new EBook("Clean Code", "Robert Martin", 1, 15.5));
            bs.addBookToDatabase(new EBook("Head First Java", "Kathy Sierra", 2, 42.0));

            // textBooks (Cần subject và status)
            bs.addBookToDatabase(new TextBook("Toan Roi Rac", "NXB", "Toan Hoc", 3, "Mới"));
            bs.addBookToDatabase(new TextBook("Giao trinh C++", "PTIT", "Lap Trinh", 4, "Cũ"));

            // referenceBooks (Chỉ cần status)
            bs.addBookToDatabase(new ReferenceBook("TOEIC Preparation Vol 1", "ETS", 5, "Mới"));
            bs.addBookToDatabase(new ReferenceBook("Tu dien Anh-Viet", "Oxford", 6, "Cũ"));

            System.out.println("Successfully generated data to database");
        }catch (Exception e){
            System.out.println("Error in generating data to database");
            e.printStackTrace();
        }
    }
    public void clearDatabase(){
        System.out.println("🧹 Clearing database...");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            // Tắt MySQL
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");

            // Xóa trắng các bảng và reset ID
            stmt.execute("TRUNCATE TABLE borrow_records;");
            stmt.execute("TRUNCATE TABLE books;");
            stmt.execute("TRUNCATE TABLE users;");

            // Mở MySQL
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");


            String createSuperAdmin = "INSERT INTO users (name, phone, role, borrow_limit) " +
                    "VALUES ('Super Admin', '000', 'SuperAdmin', 999)";
            stmt.execute(createSuperAdmin);

            System.out.println("✨ Data cleared! Successfully created SuperAdmin.");
            System.out.println("🔑 username: Super Admin | phonenumber: 000");


        } catch (Exception e) {
            System.out.println("❌ Error in clearing database");
            e.printStackTrace();
        }
    }
}
