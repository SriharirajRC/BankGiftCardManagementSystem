package com.Repository;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.Model.TransactionLog;
import com.Connection.ConnectionDB;

public class TransactionLogRepository {
	static String SELECTQUERY = "SELECT * FROM logoftransactions lg JOIN lookupevents le ON lg.eventid = le.id ";		
	static String O_BY_DESC = " ORDER BY lg.eventdate DESC ";
	static String O_BY_ASC = " ORDER BY lg.eventdate ASC ";
	static String LIMITOFFSET = " LIMIT ? OFFSET ? ";
	static String LOG = "INSERT INTO logoftransactions(userid,amount,eventid,details) VALUES(?,?,?,?)";


	public static List<TransactionLog> getAllTransaction(String username,int limit, int offset,String order){
		List<TransactionLog> list = new ArrayList<>();		
		
		String query = SELECTQUERY;
		
        if (username != null) {
        	query = query+" WHERE userid = '"+username+"' ";
		}
        
		if(order.equals("asc")) {
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
				TransactionLog transaction = new TransactionLog(
		                rs.getInt("id"),
		                rs.getString("userid"),
		                rs.getDouble("amount"),
		                rs.getString("eventname"),
		                rs.getTimestamp("eventdate"),
		                rs.getString("details"));
				
				list.add(transaction);
			}
			return list;

			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static int countoftransaction(String username) {
		String query = "SELECT COUNT(*) AS countoftrans FROM logoftransactions";
		
		if(username != null)
			query = query +" WHERE userid = '"+username+"' ";

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

	public static boolean logthetransaction(TransactionLog transaction) {

		try(Connection connection = ConnectionDB.getConnection();
				PreparedStatement stmt = connection.prepareStatement(LOG);){
				stmt.setString(1,transaction.getUserid());
				stmt.setDouble(2,transaction.getAmount());
				stmt.setInt(3,transaction.getId());
				stmt.setString(4, transaction.getDetails());
				return stmt.executeUpdate()>0;
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static List<TransactionLog> findByEventdateBetween(Timestamp start, Timestamp end) {
		List<TransactionLog> list = new ArrayList<>();		
		
		String query = SELECTQUERY +" WHERE lg.eventdate between ? AND ? ";
		try(Connection connection = ConnectionDB.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);){
			
			stmt.setTimestamp(1, start);
			stmt.setTimestamp(2, end);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				TransactionLog transaction = new TransactionLog(
		                rs.getInt("id"),
		                rs.getString("userid"),
		                rs.getDouble("amount"),
		                rs.getString("eventname"),
		                rs.getTimestamp("eventdate"),
		                rs.getString("details"));
				
				list.add(transaction);
			}
			return list;

			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static void insertCSVnames(String reportFilePath) {
		String query = "INSERT INTO csvfilenames(name,date) VALUES(?,CURRENT_TIMESTAMP)";		
		
		try(Connection connection = ConnectionDB.getConnection();
			PreparedStatement pdstmt = connection.prepareStatement(query);){
			
			pdstmt.setString(1, reportFilePath);
			
			pdstmt.executeUpdate();
			
			return;	
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	public static String getLastReportFileName() {
	    String sql = "SELECT name FROM csvfilenames ORDER BY date DESC LIMIT 1";

	    String lastReportFileName = null;

	    try (Connection connection = ConnectionDB.getConnection();
	    	Statement stmt = connection.createStatement()) {
	        ResultSet rs = stmt.executeQuery(sql);
	        if (rs.next()) {
	            lastReportFileName = rs.getString("name");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return lastReportFileName;
	}

}
