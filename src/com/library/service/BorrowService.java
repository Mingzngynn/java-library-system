package com.library.service;

import com.library.model.User;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;

public class BorrowService {
    public void borrowBook(User user, int bookID) throws SQLException {
        Connection conn = null;
        String query2 = "select count(*) from borrow_records where user_id =? and status ='Borrowing'";
        String query1 = "SELECT COUNT(*) FROM borrow_records WHERE book_id=? AND status='Borrowing'";
        String query = "INSERT INTO borrow_records (user_id,book_id,borrow_date) VALUES(?,?,?) ";
        try{conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement ppst1 = conn.prepareStatement(query1);){
        ppst1.setInt(1, bookID);
        ResultSet rs1 = ppst1.executeQuery();
        if (rs1.next() && rs1.getInt(1 )>0) {
            System.out.println("com.library.model.Book already borrowed");
            conn.rollback();
            return;
        }
        }
        try(PreparedStatement ppst2 = conn.prepareStatement(query2);){
            ppst2.setInt(1, user.getId());
            ResultSet rs2 = ppst2.executeQuery();
            if(rs2.next() && rs2.getInt(1)>= user.getBorrowLimit() ){
                System.out.println("com.library.model.User has reached limit of " + user.getBorrowLimit());
                conn.rollback();
                return;
            }
        }
        try(PreparedStatement ppst = conn.prepareStatement(query);){
            ppst.setInt(1, user.getId());
            ppst.setInt(2, bookID);
            ppst.setString(3, LocalDate.now().toString());
            ppst.executeUpdate();
        }
        conn.commit();
            System.out.println("Successfully borrowed!");

        }catch (Exception e) {
            if(conn != null){
                try {
                    conn.rollback();
                    System.out.println("Error while trying to borrow!");
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally {
            if(conn != null){
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }
    public void returnBook(int userId, int bookId) {
        String query = "UPDATE borrow_records SET status = 'available' WHERE user_id = ? AND book_id = ? AND status = 'Borrowing'";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ppst = conn.prepareStatement(query);){

            ppst.setInt(1,userId);
            ppst.setInt(2,bookId);

            int rowsAffected = ppst.executeUpdate();
            if(rowsAffected>0){
                System.out.println("com.library.model.Book id: "+ bookId + " has been successfully returned!");
            }else {
                System.out.println("Failed to return book ");
            }
        }catch (Exception e){
            System.out.println("Error in returning book id from database");
            e.printStackTrace();
        }
    }
}
