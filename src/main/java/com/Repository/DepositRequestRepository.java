package com.Repository;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Model.DepositRequest;
import com.Connection.ConnectionDB;

public class DepositRequestRepository {
	
	static String O_BY_DESC = " ORDER BY requesteddate DESC ";
	static String O_BY_ASC = " ORDER BY requesteddate ASC ";
	static String LIMITOFFSET = " LIMIT ? OFFSET ? ";

	public static List<DepositRequest> getAllRequest(int limit, int offset,String filter, String orderby) throws ClassNotFoundException, SQLException {
		
		List<DepositRequest> list = new ArrayList<>();
		String query = "SELECT * FROM depositrequest WHERE status = ?";
		if(orderby.equals("asc")) {
			query = query+O_BY_ASC;
		}
		else {
			query = query+O_BY_DESC;
		}
		
		query = query + LIMITOFFSET;
		
		try(Connection connection = ConnectionDB.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);){
			
			stmt.setString(1, filter);
			stmt.setInt(2, limit);
			stmt.setInt(3, offset);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				DepositRequest deprequest = new DepositRequest(
		                rs.getInt("id"),
		                rs.getString("userid"),
		                rs.getDouble("requestedamount"),
		                rs.getTimestamp("requesteddate"),
		                rs.getString("status"),
		                rs.getTimestamp("responsedate"));
				
				list.add(deprequest);
			}
			return list;
		}
	}

	public static boolean acceptRequestStatus(int id, DepositRequest request) {
		String updsql = "UPDATE depositrequest SET status = 'approved',responsedate = current_timestamp WHERE id = ?";
        String updbal = "UPDATE users SET availablebalance = availablebalance + ? WHERE userid = ?";
        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt1 = conn.prepareStatement(updsql);
                PreparedStatement pstmt2 = conn.prepareStatement(updbal)) {
        	        	
        	pstmt1.setInt(1,request.getId());
        	
        	pstmt2.setDouble(1, request.getRequestedamount());
        	pstmt2.setString(2,request.getUserid());
        	
            ;
            
			return pstmt1.executeUpdate()>0 && pstmt2.executeUpdate()>0;
		}
		catch(Exception e) {
			System.out.println("Something Went Wrong in startDepositTransaction"+e.getMessage());
		}
		return false;

	}
	
	public static DepositRequest getRequestById(int id) {
		String selectQuery = "SELECT * FROM depositrequest WHERE id = ?";
		try(
				Connection con = ConnectionDB.getConnection();
				PreparedStatement selectPdsmt = con.prepareStatement(selectQuery);
				){
			selectPdsmt.setInt(1,id);
			ResultSet rs = selectPdsmt.executeQuery();
			if(rs.next()) {
				return new DepositRequest(
					rs.getInt("id"),
					rs.getString("userid"),
					rs.getDouble("requestedamount"),
					rs.getTimestamp("requesteddate"),
					rs.getString("status"),
					rs.getTimestamp("responsedate")
					);
			}
			return null;
		}
		catch(Exception e) {
			System.out.println("Something Went Wrong in getRequestByID "+e.getMessage());
		}
		return null;
	}

	public static boolean rejectRequest(int id) {
		String updsql = "UPDATE depositrequest SET status = 'rejected',responsedate = current_timestamp WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt1 = conn.prepareStatement(updsql);)
        		{        	
        	pstmt1.setInt(1,id);
        	            
			return pstmt1.executeUpdate()>0;
		}
		catch(Exception e) {
			System.out.println("Something Went Wrong in rejectrequest"+e.getMessage());
		}
		return false;
	}

	public static boolean createRequest(DepositRequest depositrequest) {
		String sql = "INSERT INTO depositrequest (userid, requestedamount, requesteddate, status) " +
                "VALUES (?, ?, CURRENT_TIMESTAMP, 'pending')";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, depositrequest.getUserid());
            pstmt.setDouble(2, depositrequest.getRequestedamount());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        }
        catch(Exception e) {
        	System.out.println("EE"+e.getMessage());
        }
        return false;
	}

	public static int countofrequest(String filter) {
		String query = "SELECT COUNT(*) AS countofusers FROM depositrequest WHERE status = ?";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

        	pstmt.setString(1, filter);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("countofusers");
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong "+e.getMessage());
        }
        return 0;
	}
// specific
	public static List<DepositRequest> getAllRequest(String userid, int limit, int offset,String filter, String orderby) throws ClassNotFoundException, SQLException {
		
		List<DepositRequest> list = new ArrayList<>();
		String query = "SELECT * FROM depositrequest WHERE status = ? AND userid = ?";
		if(orderby.equals("asc")) {
			query = query+O_BY_ASC;
		}
		else {
			query = query+O_BY_DESC;
		}
		
		query = query + LIMITOFFSET;
		
		try(Connection connection = ConnectionDB.getConnection();
			PreparedStatement stmt = connection.prepareStatement(query);){
			
			stmt.setString(1, filter);
			stmt.setString(2, userid);
			stmt.setInt(3, limit);
			stmt.setInt(4, offset);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				DepositRequest deprequest = new DepositRequest(
		                rs.getInt("id"),
		                rs.getString("userid"),
		                rs.getDouble("requestedamount"),
		                rs.getTimestamp("requesteddate"),
		                rs.getString("status"),
		                rs.getTimestamp("responsedate"));
				
				list.add(deprequest);
			}
			return list;
		}
	}
	
	public static int countofrequest(String userid,String filter) {
		String query = "SELECT COUNT(*) AS countofusers FROM depositrequest WHERE status = ? AND userid = ? ";

        try (Connection conn = ConnectionDB.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

        	pstmt.setString(1, filter);
        	pstmt.setString(2, userid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("countofusers");
            }
        } catch (Exception e) {
            System.out.println("Something Went Wrong "+e.getMessage());
        }
        return 0;
	}
}
