package com.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.Model.User;
import com.Model.UserModel;
import com.Connection.ConnectionDB;

public class UserRepository {

	static String SELECTALLFROM = "SELECT * FROM users ";
	static String ORDERBYDESC = " ORDER BY createdat DESC ";
	
	public static User getUserByUserid(String username) {
        String query = SELECTALLFROM +" WHERE userid = ?";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                rs.getString("userid"),
                rs.getLong("accountnumber"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getDouble("availablebalance"),
                rs.getDouble("giftcardbalance"),
                rs.getTimestamp("createdat"),
                rs.getTimestamp("lastupdated"),
                rs.getDouble("bonus"),
                rs.getBoolean("isnewuser"),
                rs.getString("role"));
            }
            else {
            	return null;
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong "+e.getMessage());
        }
        return null;
    }

	public static boolean createUser(User usermodel) {
		String query = "INSERT INTO users(userid,name,email,password) VALUES(?,?,?,?)";		
		
		try(Connection connection = ConnectionDB.getConnection();
			PreparedStatement pdstmt = connection.prepareStatement(query);){
			
			pdstmt.setString(1, usermodel.getUserid());
			pdstmt.setString(2, usermodel.getName());
			pdstmt.setString(3, usermodel.getEmail());
			pdstmt.setString(4, usermodel.getName().substring(0,3)+usermodel.getEmail().substring(0,3));
			
			return pdstmt.executeUpdate()==1;	
			
		}catch (Exception e) {
			e.getMessage();
		}
		
		return false;
	}

	 public static List<User> getAllUsers(int limit,int offset) {
	        List<User> users = new ArrayList<>();
	        String query = SELECTALLFROM +" WHERE role = 'user' "+ ORDERBYDESC +" LIMIT ? OFFSET ? ";

	        try (Connection conn = ConnectionDB.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(query);
	             ) {
	        	
	        	System.err.print(limit+"--"+offset);
	        	pstmt.setInt(1, limit);
	        	pstmt.setInt(2, offset);
	        	
	        	ResultSet rs = pstmt.executeQuery();
	            while (rs.next()) {
	                User user = new User(
	                        rs.getString("userid"),
	                        rs.getLong("accountnumber"),
	                        rs.getString("name"),
	                        rs.getString("email"),
	                        rs.getString("password"),
	                        rs.getDouble("availablebalance"),
	                        rs.getDouble("giftcardbalance"),
	                        rs.getTimestamp("createdat"),
	                        rs.getTimestamp("lastupdated"),
	                        rs.getDouble("bonus"),
	                        rs.getBoolean("isnewuser"),
	                        rs.getString("role")
	                );
	                users.add(user);
	            }
	        } catch (Exception e) {
	            System.out.println("Error fetching users: " + e.getMessage());
	        }
	        return users;
	    }

	public static boolean deleteUser(String userid) {
		String query = "DELETE FROM users WHERE userid = ?";
        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userid);
            return pstmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}

	public static boolean updateUser(User user) {
		String query = "UPDATE users SET name = ?, email = ?, password = ? , lastupdated = CURRENT_TIMESTAMP WHERE userid = ? ";
        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getUserid());

            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;		
	}

	public static int getCount() {
			String query = "SELECT COUNT(*) AS countofusers FROM users WHERE role = 'user'";

	        try (Connection conn = ConnectionDB.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(query)) {

	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                return rs.getInt("countofusers");
	            }
	        } catch (Exception e) {
	            System.out.println("Something Went Wrong "+e.getMessage());
	        }
	        return 0;

	}

	public static boolean markAsOldUser(String userid){
		 String query = "UPDATE users SET isnewuser = false WHERE userid = ? ";
	        try (Connection conn = ConnectionDB.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(query)) {

	            pstmt.setString(1, userid);

	            int rowsUpdated = pstmt.executeUpdate();
	            return rowsUpdated > 0;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return false;
	}

	public static boolean convertBonus(String userid, Double bonus) {
		
        String updateUsers = "UPDATE users SET bonus = bonus - ?,availablebalance = availablebalance + ? WHERE userid = ?";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement updateUsersPSTMT = conn.prepareStatement(updateUsers);) {
        	System.out.println(bonus+"fff");
            updateUsersPSTMT.setDouble(1, bonus);
            updateUsersPSTMT.setDouble(2, bonus);
            updateUsersPSTMT.setString(3, userid);
            
            if (updateUsersPSTMT.executeUpdate() > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error while fetching transactions: " + e.getMessage());
        }
		return false;
	}
	
}
