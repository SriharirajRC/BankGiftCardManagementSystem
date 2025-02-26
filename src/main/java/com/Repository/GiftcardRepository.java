package com.Repository;

import java.security.SecureRandom;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.Model.Giftcard;
import com.Connection.ConnectionDB;

public class GiftcardRepository {
	
	static String SELECTALL = "SELECT * FROM giftcard ";
	static String O_BY_DESC = " ORDER BY generatedtime DESC ";
	static String O_BY_ASC = " ORDER BY generatedtime ASC ";
	static String LIMITOFFSET = " LIMIT ? OFFSET ? ";
	
	public static List<Giftcard> getAllGiftcard(int limit, int offset, String orderby){
		List<Giftcard> list = new ArrayList<>();
		
		String query = SELECTALL;
		if(orderby.equals("asc")) {
			query = query+O_BY_ASC;
		}
		else {
			query = query+O_BY_DESC;
		}
		query = query + LIMITOFFSET;

		
		try(Connection connection = ConnectionDB.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);){
			
			stmt.setInt(1, limit);
			stmt.setInt(2, offset);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Giftcard giftcard = new Giftcard(
						rs.getString("code"),
		                rs.getString("pin"),
		                rs.getDouble("amount"),
		                rs.getString("ownerofgiftcard"),
		                rs.getTimestamp("generatedtime"));
				
				list.add(giftcard);
			}
			return list;
	
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int countofgiftcard() {
		String query = "SELECT COUNT(*) AS countoftrans FROM giftcard";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("countoftrans");
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong "+e.getMessage());
        }
        return 0;
	}

	public static String generateGiftcard(Giftcard giftcard) {
		String sql = "INSERT INTO giftcard (code, pin, amount, ownerofgiftcard) VALUES ( ?, ?, ?, ?)";
        String sql2 = "UPDATE users SET availablebalance = availablebalance - ? ,giftcardbalance = giftcardbalance + ? WHERE userid = ?";
        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);) {
            if (giftcard.getAmount() <=0 || UserRepository.getUserByUserid(giftcard.getOwnerofgiftcard()).getAvailablebalance() < giftcard.getAmount()) {
            	return null;
            }

            String code = generateGiftCardCode();
            pstmt.setString(1, code);
            pstmt.setString(2, giftcard.getPin());
            pstmt.setDouble(3, giftcard.getAmount());
            pstmt.setString(4, giftcard.getOwnerofgiftcard());
            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {

                pstmt2.setDouble(1, giftcard.getAmount());
                pstmt2.setDouble(2, giftcard.getAmount());
                pstmt2.setString(3, giftcard.getOwnerofgiftcard());
                if( pstmt2.executeUpdate()>0) {
                	return code;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
	
	public static String generateGiftCardCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder code = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }
	
	//specific
	public static List<Giftcard> getAllGiftcard(String userid,int limit, int offset, String orderby){
		List<Giftcard> list = new ArrayList<>();
		
		String query = SELECTALL+"WHERE ownerofgiftcard = ? ";
		if(orderby.equals("asc")) {
			query = query+O_BY_ASC;
		}
		else {
			query = query+O_BY_DESC;
		}
		query = query + LIMITOFFSET;

		
		try(Connection connection = ConnectionDB.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);){
			
			stmt.setString(1, userid);
			stmt.setInt(2, limit);
			stmt.setInt(3, offset);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Giftcard giftcard = new Giftcard(
						rs.getString("code"),
		                rs.getString("pin"),
		                rs.getDouble("amount"),
		                rs.getString("ownerofgiftcard"),
		                rs.getTimestamp("generatedtime"));
				
				list.add(giftcard);
			}
			return list;
	
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int countofgiftcard(String userid) {
		String query = "SELECT COUNT(*) AS countoftrans FROM giftcard WHERE ownerofgiftcard = ?";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

        	pstmt.setString(1, userid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("countoftrans");
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong "+e.getMessage());
        }
        return 0;
	}

	public static boolean rechargeGiftcard(Giftcard giftcard) {
		String updateuser = "UPDATE users SET availablebalance = availablebalance - ? ,giftcardbalance = giftcardbalance + ? WHERE userid = ?";
        String updategift = "UPDATE giftcard SET amount = amount + ? WHERE code = ?";
        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmtupdateuser = conn.prepareStatement(updateuser);
                PreparedStatement pstmtupdategift = conn.prepareStatement(updategift);) {
           
            pstmtupdateuser.setDouble(1, giftcard.getAmount());
            pstmtupdateuser.setDouble(2, giftcard.getAmount());
            pstmtupdateuser.setString(3, giftcard.getOwnerofgiftcard());
            
            pstmtupdategift.setDouble(1, giftcard.getAmount());
            pstmtupdategift.setString(2, giftcard.getCode());
            
            int ps2 = pstmtupdateuser.executeUpdate();
            int ps3 = pstmtupdategift.executeUpdate();


            if (ps2 == 0 || ps3 == 0) {
                System.out.println("Error while recharging gift card");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error while recharging gift card: " + e.getMessage());
        }
        return false;
	}

	public static boolean redeemGiftcard(Giftcard giftcard) {
		String updateuser = "UPDATE users SET giftcardbalance = giftcardbalance - ?,bonus = bonus + ? WHERE userid = ?";
        String updategift = "UPDATE giftcard SET amount = amount - ? WHERE code = ?";
        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmtupdateuser = conn.prepareStatement(updateuser);
                PreparedStatement pstmtupdategift = conn.prepareStatement(updategift);) {
           
        	int bonus = (int)giftcard.getAmount()/100;
            pstmtupdateuser.setDouble(1, giftcard.getAmount());
            pstmtupdateuser.setInt(2, bonus);
            pstmtupdateuser.setString(3, giftcard.getOwnerofgiftcard());
            
            pstmtupdategift.setDouble(1, giftcard.getAmount());
            pstmtupdategift.setString(2, giftcard.getCode());
            
            int ps2 = pstmtupdateuser.executeUpdate();
            int ps3 = pstmtupdategift.executeUpdate();
            
            System.out.println("MESAGE _"+giftcard.toString()+bonus);

            if (ps2 == 0 || ps3 == 0) {
                System.out.println("Error while redeeming gift card");
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error while redeeming gift card: " + e.getMessage());
        }
        return false;
	}

	public static Giftcard getGiftcardByCode(String code) {
		String query = SELECTALL + " WHERE code = ?";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

        	pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Giftcard(
                		rs.getString("code"),
                		rs.getString("pin"),
                		rs.getDouble("amount"),
                		rs.getString("ownerofgiftcard"));
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong "+e.getMessage());
        }
        return null;
	}
}
