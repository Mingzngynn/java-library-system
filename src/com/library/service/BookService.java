package com.library.service;

import com.library.model.Book;
import com.library.model.EBook;
import com.library.model.ReferenceBook;
import com.library.model.TextBook;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    public void addBookToDatabase(Book book){
        String query = "INSERT INTO books(title,author,type,file_size,status,subject) VALUES(?,?,?,?,?,?)";
        try(Connection conn = DatabaseConnection.getConnection();
            //ket noi voi database
            PreparedStatement ppst = conn.prepareStatement(query))
        //dong nay de ty nua fill vao value "?"
         {
        ppst.setString(1,book.getTitle());
        ppst.setString(2, book.getAuthor());

            if(book instanceof EBook){
                //instanceof de xem book thuoc class nao
                ppst.setString(3,"com.library.model.EBook");
                EBook eb = (EBook) book;
                ppst.setDouble(4,eb.getFileSize());
                ppst.setNull(5, java.sql.Types.VARCHAR);
                ppst.setNull(6, java.sql.Types.VARCHAR);
                //ko co value hop le thi setNull
            }
            else if(book instanceof TextBook){
            ppst.setString(3,"textBook");
            ppst.setNull(4, java.sql.Types.DOUBLE);
            TextBook tb = (TextBook) book;
            ppst.setString(5,tb.getStatus());
            ppst.setString(6,tb.getSubject());
            }
            else {
                ppst.setString(3,"referenceBook");
                ppst.setNull(4, java.sql.Types.DOUBLE);
                ReferenceBook rb = (ReferenceBook) book;
                ppst.setString(5,rb.getStatus());
                ppst.setNull(6, java.sql.Types.VARCHAR);
            }
            ppst.executeUpdate();
            System.out.println("com.library.model.Book added successfully");
        }
        catch(Exception e){
            System.out.println("Error adding com.library.model.Book to database");
            e.printStackTrace();
        }
    }
    public List<Book> getAllBooks(){
        List<Book> booksList = new ArrayList<>();
        //Tạo một danh sách rỗng để chứa sách lấy về
        String query = "SELECT * FROM books";
        //Câu lệnh SQL để lấy TẤT CẢ dữ liệu từ bảng books
        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ppst = conn.prepareStatement(query);
                ResultSet rs = ppst.executeQuery())
        //Thực thi và nhận cái hộp dữ liệu (ResultSet)
        {
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String type = rs.getString("type");
                if(type.equals("com.library.model.EBook")){
                    Double fileSize = rs.getDouble("file_size");
                    EBook eb = new EBook(title,author,id,fileSize);
                    booksList.add(eb);
                }
                else if(type.equals("textBook")){
                    String status = rs.getString("status");
                    String subject = rs.getString("subject");
                    TextBook tb = new TextBook(title,author,subject,id,status);
                    booksList.add(tb);
                }
                else{
                    String status = rs.getString("status");
                    ReferenceBook rb = new ReferenceBook(title,author,id,status);
                    booksList.add(rb);
                }
            }
        }catch (Exception e) {
            System.out.println("Problem retrieving all books");
            e.printStackTrace();
        }
    return booksList;
    }
    public void deleteBook(int bid){
        String query = "DELETE FROM books WHERE id=?";
        //cu phap xoa book trong mySQL
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ppst = conn.prepareStatement(query)){
            ppst.setInt(1,bid);
            int rowDeleted = ppst.executeUpdate();
            if(rowDeleted>0){
                System.out.println("com.library.model.Book deleted successfully");
            }
            else {
                System.out.println("com.library.model.Book delete failed");
            }
        }catch (SQLException e) {
            System.out.println("Problem deleting book");
            e.printStackTrace();
        }
    }
}

