import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {
    private String url ="jdbc:mysql://localhost:3306/new_schema";
    private String username = "root";
    private String password =  "root";
    public void addBookToDatabase(Book book){
        String query = "INSERT INTO books(title,author,type,file_size,status,subject) VALUES(?,?,?,?,?,?)";
        try(Connection conn = DriverManager.getConnection(url, username,password);
            //ket noi voi database
        PreparedStatement ppst = conn.prepareStatement(query))
        //dong nay de ty nua fill vao value "?"
         {
        ppst.setString(1,book.getTitle());
        ppst.setString(2, book.getAuthor());

            if(book instanceof EBook ){
                //instanceof de xem book thuoc class nao
                ppst.setString(3,"EBook");
                EBook eb = (EBook) book;
                ppst.setDouble(4,eb.getFileSize());
                ppst.setNull(5, java.sql.Types.VARCHAR);
                ppst.setNull(6, java.sql.Types.VARCHAR);
                //ko co value hop le thi setNull
            }
            else if(book instanceof textBook){
            ppst.setString(3,"textBook");
            ppst.setNull(4, java.sql.Types.DOUBLE);
            textBook tb = (textBook) book;
            ppst.setString(5,tb.getStatus());
            ppst.setString(6,tb.getSubject());
            }
            else {
                ppst.setString(3,"referenceBook");
                ppst.setNull(4, java.sql.Types.DOUBLE);
                referenceBook rb = (referenceBook) book;
                ppst.setString(5,rb.getStatus());
                ppst.setNull(6, java.sql.Types.VARCHAR);
            }
            ppst.executeUpdate();
            System.out.println("Book added successfully");
        }
        catch(Exception e){
            System.out.println("Error adding Book to database");
            e.printStackTrace();
        }
    }
    public List<Book> getAllBooks(){
        List<Book> booksList = new ArrayList<>();
        //Tạo một danh sách rỗng để chứa sách lấy về
        String query = "SELECT * FROM books";
        //Câu lệnh SQL để lấy TẤT CẢ dữ liệu từ bảng books
        try(
            Connection conn = DriverManager.getConnection(url,username,password);
            PreparedStatement ppst = conn.prepareStatement(query);
            ResultSet rs = ppst.executeQuery())
        //Thực thi và nhận cái hộp dữ liệu (ResultSet)
        {
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String type = rs.getString("type");
                if(type.equals("EBook")){
                    Double fileSize = rs.getDouble("file_size");
                    EBook eb = new EBook(title,author,id,fileSize);
                    booksList.add(eb);
                }
                else if(type.equals("textBook")){
                    String status = rs.getString("status");
                    String subject = rs.getString("subject");
                    textBook tb = new textBook(title,author,subject,id,status);
                    booksList.add(tb);
                }
                else{
                    String status = rs.getString("status");
                    referenceBook rb = new referenceBook(title,author,id,status);
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
        try(Connection conn = DriverManager.getConnection(url,username,password);
        PreparedStatement ppst = conn.prepareStatement(query)){
            ppst.setInt(1,bid);
            int rowDeleted = ppst.executeUpdate();
            if(rowDeleted>0){
                System.out.println("Book deleted successfully");
            }
            else {
                System.out.println("Book delete failed");
            }
        }catch (SQLException e) {
            System.out.println("Problem deleting book");
            e.printStackTrace();
        }
    }
    public void updateBookInDatabase(Book book){
        String query ="UPDATE books SET title=?,author=?,type=?,file_size=?,subject=? WHERE id=?";
        try(Connection conn = DriverManager.getConnection(url,username,password);
        PreparedStatement ppst = conn.prepareStatement(query);){
            ppst.setString(1, book.getTitle());
            ppst.setString(2, book.getAuthor());
            ppst.setInt(3,book.getId());
            if(book instanceof EBook){
                EBook eb = (EBook) book;
                ppst.setString(3,"Ebook");
                ppst.setDouble(4,eb.getFileSize());
                ppst.setNull(5, java.sql.Types.VARCHAR);
                ppst.setNull(6, java.sql.Types.VARCHAR);
            }
            else if(book instanceof textBook){
                textBook tb = (textBook) book;
                ppst.setString(3,"textBook");
                ppst.setString(5,tb.getStatus());
                ppst.setNull(4, java.sql.Types.DOUBLE);
                ppst.setString(6, tb.getSubject());
            } else if (book instanceof referenceBook){
                referenceBook rb = (referenceBook) book;
                ppst.setString(3,"referenceBook");
                ppst.setString(5,rb.getStatus());
                ppst.setNull(6, java.sql.Types.VARCHAR);
                ppst.setNull(4, java.sql.Types.DOUBLE);

            }
            int rowUpdated = ppst.executeUpdate();
            if(rowUpdated>0){
                System.out.println("Book "+book.getId()+" updated successfully");
            }
            }catch (Exception e) {
            System.out.println("Problem updating book");
            e.printStackTrace();
        }
    }



}

