import java.sql.*;

public class BorrowService {
    private String url ="jdbc:mysql://localhost:3306/new_schema";
    private String username = "root";
    private String password =  "root";
    public int countBooksHolding(int userId) {
        String query = "select count(*) from borrow_records where user_id =? and status ='Borrowing'";
        int booksHolding = 0;
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ppst = conn.prepareStatement(query);) {
            ppst.setInt(1, userId);
            try (ResultSet rs = ppst.executeQuery();) {
                if (rs.next()) {
                    booksHolding = rs.getInt(1);
                }
            }
        }catch (Exception e) {
            System.out.println("Error in counting books holding");
            e.printStackTrace();

        }
        return booksHolding;
    }
    public boolean isBookAvailable(int bookID){
        String query = "SELECT COUNT(*) FROM borrow_records WHERE book_id=? AND status='Borrowing'";
        try(Connection conn = DriverManager.getConnection(url,username,password);
            PreparedStatement ppst = conn.prepareStatement(query);){
            ppst.setInt(1,bookID);
            try(ResultSet rs = ppst.executeQuery()) {
                if(rs.next()){
                    int count = rs.getInt(1);
                    return count == 0;
                }
            }
        }catch (Exception e) {
            System.out.println("Book not available");
            e.printStackTrace();
        }
        return false;
    }


    public void borrowBook(int userID, int bookID) throws SQLException {
        UserService us = new UserService();
        BookService bs = new BookService();
        int currentHolding = countBooksHolding(userID);
        int limit = us.getBorrowLimit(userID);
        if (currentHolding >= limit) {
            System.out.println("You have been borrowed" + limit + " books. Please return before borrowing!");
            return;
        }
        if (!isBookAvailable(bookID)) {
            System.out.println("Book is not available");
            return;
        }
        String query = "INSERT INTO borrow_records (user_id,book_id,borrow_date) VALUES(?,?,?) ";
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            conn.setAutoCommit(false);
            try (PreparedStatement ppst = conn.prepareStatement(query);
            ) {
                ppst.setInt(1, userID);
                ppst.setInt(2, bookID);
                ppst.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                ppst.executeUpdate();

                conn.commit();
                System.out.println(bookID + " Borrowed successfully");
            } catch (Exception e) {
                conn.rollback(); // Lỗi thì hủy hết, không để dữ liệu dở dang
                throw e;
            }
        } catch (Exception e) {
            System.out.println("Error in borrowing");
            e.printStackTrace();
        }
    }
    public void returnBook(int userId, int bookId) {
        String query = "UPDATE borrow_records SET status = 'available' WHERE user_id = ? AND book_id = ? AND status = 'Borrowing'";
        try(Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ppst = conn.prepareStatement(query);){

            ppst.setInt(1,userId);
            ppst.setInt(2,bookId);

            int rowsAffected = ppst.executeUpdate();
            if(rowsAffected>0){
                System.out.println("Book id: "+ bookId + " has been successfully returned!");
            }else {
                System.out.println("Failed to return book ");
            }
        }catch (Exception e){
            System.out.println("Error in returning book id from database");
            e.printStackTrace();
        }
    }
}
