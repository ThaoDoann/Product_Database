package com.web;

/**
 * 
 */
import java.io.IOException;
import com.web.ConnectionFactory;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class ProductServlet
 *
 * Student name: Ngoc Phuong Thao, Doan
 * Student ID: 991466176
 * Program: Software development and network engineering
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static Connection con = null;
	PreparedStatement pst;
	static ResultSet rs = null;
	Statement st;
	
    public ProductServlet() {
    }

    
    /**
     * base on action, we can determine which button user clicked
     * then just execute different processes base on their actions
     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{	
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			String action = request.getParameter("action");
			
			if("addProduct".equals(action)) {
				addProduct(out, request);
			}else if ("viewProduct".equals(action)) {
				viewProducts(out, request);
			}else if ("edit".equals(action)) {
				editProduct(out, request);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * add a new product to database
	 * get user input and send it to execute query
	 * show an update database
	 * @param out print a web page
	 */
	public  void addProduct (PrintWriter out, HttpServletRequest request) throws Exception {
		try {
			con = ConnectionFactory.getConnection();
			String addQuery = "INSERT   INTO product (productID, productName, price, quantity, category)"
					+ " VALUES (?,?,?,?,?)";
			
			pst = con.prepareStatement(addQuery);
			
			String productID = request.getParameter("productID");
	
			String productName = request.getParameter("productName");
			double price = Double.parseDouble(request.getParameter("price"));
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			String category = request.getParameter("category");
			
			pst.setString(1, productID);
			pst.setString(2,  productName);
			pst.setDouble(3, price);
			pst.setInt(4,  quantity);
			pst.setString(5, category);
			
			pst.executeUpdate();
			
			out.println("<!doctype html> "
	        		+ "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"Style.css\">\r\n" + 
	        		"</head><html><body>");
			out.println("<h3>"+ productName+ " has been added to database "+ " </h3>");
			
			//show a new database after adding a new record
			String viewProductQuery = "SELECT * FROM Product";
			st = con.createStatement();
	        rs=st.executeQuery(viewProductQuery);
			
			out.println("<h2>Product information</h2>");
			displayInfo(rs, out);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionFactory.closeConnection(con, pst, null);
		}
	}
	
	
	/**
	 * the method to search products based on customer input from Product ID Box
	 * @param out to print a new web page if the the product ID is valid
	 */
	public  void viewProducts (PrintWriter out, HttpServletRequest request) throws Exception {
		try {
			String search = request.getParameter("productID");
			
			//only execute when the product ID is not null
			if(!search.isEmpty()) {
				con = ConnectionFactory.getConnection();
				String viewProductQuery = "SELECT * FROM Product WHERE productID like ?";
				pst = con.prepareStatement(viewProductQuery);
				
				pst.setString(1, "%"+search+"%");   //match anything contain user input
		        rs = pst.executeQuery();
				
		      //create a new web page for searching products
		        out.println("<!doctype html> <head>"
		        		+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"Style.css\">" 
		        		+"</head><html><body>");
				out.println("<h2>Search: \"" +search+ "\"</h2>");
				
				//display a 
				displayInfo(rs, out);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionFactory.closeConnection(con, pst, rs);
		}
	}
	
	
	/**this method is used to edit data of a specific record 
	* Data to edited are price and quantity
	* Category and product name are not necessary 
	*/
	public  void editProduct (PrintWriter out, HttpServletRequest request) throws Exception {
		try {
			con = ConnectionFactory.getConnection();
			
			String productID = request.getParameter("productID");
			String  price = request.getParameter("price");
			String quantity = request.getParameter("quantity");

			 if(!price.isEmpty() && quantity.isEmpty() ) {
				 	String editQuery = "UPDATE Product Set price = ? WHERE productID = ?";		
					pst = con.prepareStatement(editQuery);

					pst.setDouble(1, Double.parseDouble(price));
					pst.setString(2, productID);
					pst.executeUpdate();
			 }  else if (price.isEmpty() && !quantity.isEmpty()) {
				 String editQuery = "UPDATE Product Set quantity = ? WHERE productID = ?";		
					pst = con.prepareStatement(editQuery);

					pst.setInt(1, Integer.parseInt(quantity));
					pst.setString(2, productID);
					pst.executeUpdate(); 
			 } else if (!price.isEmpty() && !quantity.isEmpty()) {
				String editQuery = "UPDATE Product Set price = ?, quantity = ? WHERE productID = ?";		
				pst = con.prepareStatement(editQuery);

				pst.setDouble(1, Double.parseDouble(price));
				pst.setInt(2, Integer.parseInt(quantity));
				pst.setString(3, productID);
				pst.executeUpdate();
			}
			
			//view a new database with update information
			String viewProductQuery = "SELECT * FROM Product";
			st = con.createStatement();
	        rs=st.executeQuery(viewProductQuery);
	        
	        //create a new web page 
			
	        out.println("<!doctype html> "
	        		+ "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"Style.css\">\r\n" + 
	        		"</head><html><body>");
			out.println("<h3>The product has been edited to database  </h3>");
			
			out.println("<h2>Product information</h2>");
			displayInfo(rs, out);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionFactory.closeConnection(con, pst, rs);
		}
	}
	
	
	/**
	 * this method shows query results from database
	 * it is used in other methods after executing each executeQuery statement
	 * @param rs return a result set from queries from other methods
	 * @param out print web page
	 */
	public void displayInfo (ResultSet rs, PrintWriter out) throws Exception {
		try {
			out.println("<table>");
			out.println("<tr><th>ProductID</th><th>ProductName</th><th>Price</th><th>Quantity</th><th>Category</th></tr>");
	
			while(rs.next()) {
				String id = rs.getString("PRODUCTID");
				String name = rs.getString("PRODUCTNAME");
				double productPrice = rs.getDouble("PRICE");
				int productQty = rs.getInt("QUANTITY");
				String productCategory = rs.getString("CATEGORY");
				
				out.println("<tr><td>"+id+"</td><td>"+name+"</td><td>"+productPrice+"</td>"
						+ "<td>"+productQty+"</td>"+ "<td>"+productCategory+"</td></tr>");
			}
			
			//close the web page
			out.println("</table>");
			out.println("</body></html>");
		}catch( Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
