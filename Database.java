import java.sql.Connection;  
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;  

/*
 * Database object to reference for calls to the local databse from inside other objects. 
 */

public class Database {

	private Connection conn;
	/**
	 * Constructor establishes a connection to the database for further use. 
	 */
	public Database() {
	
	try{
		String url = "jdbc:sqlite:Userdata.db";
		conn = DriverManager.getConnection(url);
		System.out.println("Connection to Database Established!");

		
	} 
	catch (SQLException e) {
		e.printStackTrace();
	}

	}

	/**
	 * Check if a user exists in the database by matching username and password, returns true if exists.
	 * 
	 * @param user The username to check against the database
	 * @param pass The password to check against the database
	 * @return true if a user with matching user and pass exists, false otherwise
	 * 
	 */
	public boolean checkUser(String user, String pass) {		
		boolean status = false;
		
		try{			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserInfo where Username ='" + user + 
			          "' and Password ='" + pass + "'");
			
			if(rs.next())
			{
				status = true;
			}
		
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return status;
	}

	/**
	 * Do I keep or Delete total savings? 
	 */
	public String[] getUserData(String username) {
				
		String[] userDataArray = new String[3];		
		try{

			Statement stmt = conn.createStatement();
			System.out.println("Selecting User info for: " + username);
			
			ResultSet rs = stmt.executeQuery("SELECT Firstname, Lastname, Savings FROM UserData WHERE Username = '" + username + "'");

			while(rs != null && rs.next()) {
				for(int i = 0; i < 3; i++) {
					userDataArray[i] = rs.getString(i+1);
					
				}
				System.out.println("Loaded the String Array just fine!");
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("File Accessed: " + userDataArray[0] + " " + userDataArray[1] + ", Current Savings: " + userDataArray[2] + ".");
		return userDataArray;
	}
	
	/**
	 * Retrieves all purchases for corresponding username and creates a List of Purchases
	 * @param username Choose which purchases to select based on username 
	 * @return returns an ArrayList of type Purchase
	 */
	public List<Purchase> getPurchaseHistory(String username) {
		
		ArrayList<Purchase> purchasesList = new ArrayList<>();	
		try{

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT Description, Cost, Category FROM Purchases WHERE Username = '" + username + "'");
			double formattedCost = 0;
			while(rs.next()) {
				Purchase current = new Purchase();
				current.setDescription(rs.getString(1));
				current.setCost(rs.getDouble(2));
				current.setCategory(rs.getString(3));
				purchasesList.add(current);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		return purchasesList;
	}
	
	/**
	 * Inserts a new Purchase to the database using provided information
	 * @param username Used to associate purchase with a user, used in prepared statement
	 * @param description The description for purchase being added, used in prepared statement
	 * @param cost The cost for purchase being added, used in prepared statement
	 * @param category The category for purchase being added, used in prepared statement 
	 */
	public void InsertNewPurchase(String username, String description, double cost, String category) {				
		try{

			String sql = ("INSERT INTO Purchases (Username, Description, Cost, Category) VALUES (?,?,?,?)");
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, description);
			pstmt.setDouble(3, cost);
			pstmt.setString(4, category);
			pstmt.executeUpdate();
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Adds new user info to the database
	 * @param submittedInfo a String array containing the 4 values used in the prepared statement 				
	 */
	public void AddUserInfo(String[] submittedInfo) {	
		try{

			String sql = ("INSERT INTO UserData (Username, Firstname, Lastname, Savings) VALUES (?,?,?,?)");
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, submittedInfo[3]);
			pstmt.setString(2, submittedInfo[0]);
			pstmt.setString(3, submittedInfo[1]);
			pstmt.setDouble(4, Double.parseDouble(submittedInfo[2]));
			pstmt.executeUpdate();
			System.out.println("UserData updated!");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		createUserLogin(submittedInfo[3], submittedInfo[4]);
	}

	/**
	 * Adds new username and password combo to the database, now allowing proper login
	 * @param username The username added to the database
	 * @param password The password matched with the username added to the database
	 */
	public void createUserLogin(String username, String password) {				
		try{

			String sql = ("INSERT INTO UserInfo (Username, Password) VALUES (?,?)");
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);

			pstmt.executeUpdate();
			System.out.println("UserInfo updated!");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Checks to see if a username is available or already taken during account creation
	 * @param username The username checked against the database to check availability
	 * @return returns true if the provided username is available, false if it is taken 
	 */
	public boolean checkExistingUserName(String username) {				
			
		boolean isNotTaken = true;
		try{

			Statement stmt = conn.createStatement();
			System.out.println("Checking if username is taken for: " + username);
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM UserInfo where Username ='" + username + "'");

			if(rs.next()) {

				System.out.println("Username is taken.");
				isNotTaken = false;
			}
			else {
				System.out.println("Username is not taken.");
			}

		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return isNotTaken;
	}
	
	/**
	 * Updates the current users Savings
	 * @param user Specifies which user's Savings is being updated
	 * @param currentSavings The value to update Savings with
	 */
	public void updateUserSavings(String user, double currentSavings) {
		try{

			String sql = ("UPDATE UserData SET Savings = '" + currentSavings + "' WHERE Username = '" + user + "'" );
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			
			System.out.println("UserInfo updated!");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Called whenever the program is closed out to ensure database connection is terminated
	 */
	public void closeDatabase() {
		
		if(conn != null) {
		try {
			conn.close();
			System.out.println("Connection to Database succesfully terminated!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		
	}
	
	public String formatToDollar(String unformatted) {
		
		
		
		
		
		return "";
	}
	
		
}
