package com.web;

import java.sql.*;

/**
* Student name: Ngoc Phuong Thao, Doan
* Student ID: 991466176
* Program: Software development and network engineering
*/

public class ConnectionFactory {
	private static final String user = "root";
	private static final String password = "root";
	private static final String url = "jdbc:mysql://localhost:3306/productDatabase";
	
	
	public ConnectionFactory() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	   /**
     * to connect to database everytime we use a query
     * @exception if we can't connect to MySQL
     */
    public static Connection getConnection() throws Exception{
    	Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("ERROR: Connection Failed "+e.getMessage());
		}
		return connection;

   }
 
    /**
     * terminate connection after executing each query
     * @throws Exception if an error occurs
     */
    public static void closeConnection(Connection con, Statement st, ResultSet rs) throws Exception{
            if(con!=null)
                    con.close();
            if(st!=null)
                    st.close();
            if(rs!=null)
                    rs.close();
    }
}
