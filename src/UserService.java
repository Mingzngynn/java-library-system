import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private String url ="jdbc:mysql://localhost:3306/new_schema";
    private String username = "root";
    private String password =  "root";
    public void addUserToDatabase(User user){
        String query = "INSERT INTO users(name,phone,role,borrow_limit) VALUES(?,?,?,3)";
        try(Connection conn = DriverManager.getConnection(url,username,password);
            PreparedStatement ppst = conn.prepareStatement(query);){
            ppst.setString(1,user.getName());
            ppst.setString(2,user.getphone());
            ppst.setString(3,user.getRole());
            int rows = ppst.executeUpdate();
            if(rows>0){
                System.out.println(user.getName()+" has been added to the database");

            }
        }catch(Exception e){
            System.out.println("Error in adding user to database");
            e.printStackTrace();
        }
    }
    public List<User> getALlUserList(){
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try(Connection conn = DriverManager.getConnection(url,username,password);
        PreparedStatement ppst = conn.prepareStatement(query);
            ResultSet rs = ppst.executeQuery()){
            while(rs.next()){
                int borrowLimit = rs.getInt("borrow_limit");
                int id =  rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String role = rs.getString("role");
                User u = new User(borrowLimit,id,name, phone, role);
                users.add(u);
            }
        }catch(Exception e){
            System.out.println("Error in getting all users from database");
            e.printStackTrace();
        }
        return users;
    }
    public void deleteUserFromDatabase(int uid){
        String query = "DELETE FROM users WHERE id=?";
        try(Connection conn = DriverManager.getConnection(url, username,password);
        PreparedStatement ppst = conn.prepareStatement(query);){
            ppst.setInt(1,uid);
            int rowDeleted = ppst.executeUpdate();
            if(rowDeleted!=0){
                System.out.println("User with id "+uid+" deleted successfully");
            }
        } catch (Exception e) {
            System.out.println("Error in deleting user from database");
            e.printStackTrace();
        }
    }
    public void updateUserInDatabase(User user){
        String query = "UPDATE users SET name=?,phone=?, role =? WHERE id=?";
        try(Connection conn = DriverManager.getConnection(url,username,password);
        PreparedStatement ppst = conn.prepareStatement(query);){
            ppst.setString(1,user.getName());
            ppst.setString(2,user.getphone());
            ppst.setString(3,user.getRole());
            ppst.setInt(4,user.getId());
            int rowUpdated = ppst.executeUpdate();
            if(rowUpdated!=0){
                System.out.println("User with id "+user.getId()+" updated successfully");
            }
        }catch(Exception e){
            System.out.println("Error in updating user in database");
            e.printStackTrace();
        }
    }
    public int getBorrowLimit(int userId) {
        String query = "SELECT borrow_limit From users WHERE id=?";
        int borrowLimit = 0 ;
        try(Connection conn = DriverManager.getConnection(url,username,password);
        PreparedStatement ppst = conn.prepareStatement(query);
        ){
            ppst.setInt(1,userId);
            try (ResultSet rs = ppst.executeQuery()) {
                if (rs.next()) {
                    borrowLimit = rs.getInt("borrow_limit");
                }
        }
        }catch (Exception e){
            System.out.println("Error in getting borrow limit from database");
            e.printStackTrace();

    }
        return  borrowLimit;}
    public User loginUser(String username, String phoneNumber){
        String query = "SELECT * FROM users WHERE name =? AND phone =?";
        User loggedInUser = null;
        try(Connection conn = DriverManager.getConnection(url,this.username,password);
        PreparedStatement ppst = conn.prepareStatement(query);)
        {
            ppst.setString(1,username);
            ppst.setString(2,phoneNumber);
            try (ResultSet rs = ppst.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    int limit = rs.getInt("borrow_limit");
                    String role = rs.getString("role");
                    loggedInUser = new User(id,limit,username,phoneNumber,role);
                }
            }
        }catch (Exception e){
            System.out.println("Error in getting phone number from database");
            e.printStackTrace();
        }
        return loggedInUser;
    }

}
