package company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class EmployeeManager 
{
	private Connection conn = null;
	
	public EmployeeManager()
	{
		// Load Oracle JDBC Driver
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver could not be loaded");
		}
		
		// Establish a connection to the database
		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu","mgallaha","awulixoa");
			System.out.println("Connection successful.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}
	
	public boolean VerifyManagerSSN(String ssn) throws SQLException
	{
		// Form the query and execute it
		String query = "select ssn from employee e, department d where ssn = ? and mgrssn = ?";
		PreparedStatement p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, ssn);
		p.setString(2, ssn);
		ResultSet result = p.executeQuery();
		
		result.next();
		return result.getString(1).equals(ssn);
		
	}
	
	public boolean AddNewEmployee(String fname, String minit, String lname, String ssn, Date bdate, String address, String sex, double salary, String superssn, int dno, String email) throws SQLException
	{
		if(fname.isEmpty() || lname.isEmpty())
		{
			System.out.println("The employee must have a name.");
			return false;
		}
		
		if(salary > 99999999.99 || salary < 0)
		{
			System.out.println("The employee's salary is not in the acceptable range.");
			return false;
		}
		
		// Form the query and execute it
		String query = "insert into employee values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, fname);
		p.setString(2, minit);
		p.setString(3, lname);
		p.setString(4, ssn);
		p.setDate(5, bdate);
		p.setString(6, address);
		p.setString(7, sex);
		p.setDouble(8, salary);
		p.setString(9, superssn);
		p.setInt(10, dno);
		p.setString(11, email);
		p.executeQuery();
		
		return true;
		
	}
	
	public boolean AddNewDependent(String essn, String dependent_name, String sex, Date bdate, String relationship) throws SQLException	
	{
		if(essn.isEmpty() || dependent_name.isEmpty())
		{
			System.out.println("The employee must have a name.");
			return false;
		}
		
		// Form the query and execute it
		String query = "insert into dependent values (?, ?, ?, ?, ?)";
		PreparedStatement p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, essn);
		p.setString(2, dependent_name);
		p.setString(3, sex);
		p.setDate(4, bdate);
		p.setString(5, relationship);
		p.executeQuery();
		
		return true;
		
	}
	
	public boolean AssignToProject(String essn, String projectName, int hours) throws SQLException	
	{
		// Make sure values are safe
		if(essn.isEmpty() || projectName.isEmpty())
		{
			System.out.println("The employee must have a name.");
			return false;
		}
		if(hours > 40 || hours < 0)
		{
			System.out.println("The hours are out of range.");
			return false;
		}
		
		String query = "select pnumber from project where pname = ?";
		PreparedStatement p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, projectName);
		ResultSet result = p.executeQuery();
		
		result.next();
		int pno = result.getInt(1);
		
		// Form the query and execute it
		String newQuery = "insert into works_on values (?, ?, ?)";
		PreparedStatement newP = conn.prepareStatement(newQuery);
		newP.clearParameters();
		newP.setString(1, essn);
		newP.setInt(2, pno);
		newP.setInt(3, hours);
		newP.executeQuery();
		
		return true;
		
	}

	public boolean projectExists(String project) throws SQLException
	{
		// Form the query and execute it
		String query = "select pname from project where pname = ?";
		PreparedStatement p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, project);
		ResultSet result = p.executeQuery();
		
		result.next();
		if(result.isAfterLast())
			return false;
		
		return true;
	}
	
	public void deleteOneEmp(String ssn) throws SQLException
	{
		// Form the query and execute it
		String query = "delete from employee where ssn = ?";
		PreparedStatement p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, ssn);
		ResultSet result = p.executeQuery();
	}
	
	public void printOneEmp(String ssn) throws SQLException
	{
		// Form the query and execute it
		String query = "select fname, minit, lname, ssn, bdate, address, sex, salary, superssn, dno, email from employee where ssn = ?";
		PreparedStatement p = conn.prepareStatement(query);
		p.clearParameters();
		p.setString(1, ssn);
		ResultSet result = p.executeQuery();
		
		// Print the results of the executed query
		while (result.next()) {
			System.out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3) + "\t" + result.getString(4) + "\t" + result.getString(5) + "\t" + result.getString(6) + "\t" + result.getString(7) + "\t" + result.getDouble(8) + "\t" + result.getString(9) + "\t" + result.getInt(10) + "\t" + result.getString(11));
		}
	}
	
	public void printAllEmp() throws SQLException
	{
		// Form the query and execute it
		String query = "select fname, minit, lname, ssn, bdate, address, sex, salary, superssn, dno, email from employee";
		PreparedStatement p = conn.prepareStatement(query);
		ResultSet result = p.executeQuery();
		
		// Print the results of the executed query
		while (result.next()) {
			System.out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3) + "\t" + result.getString(4) + "\t" + result.getString(5) + "\t" + result.getString(6) + "\t" + result.getString(7) + "\t" + result.getDouble(8) + "\t" + result.getString(9) + "\t" + result.getInt(10) + "\t" + result.getString(11));
		}
	}
	
	public void printAllWorks() throws SQLException
	{
		// Form the query and execute it
		String query = "select essn, pno, hours from works_on";
		PreparedStatement p = conn.prepareStatement(query);
		ResultSet result = p.executeQuery();
		
		// Print the results of the executed query
		while (result.next()) {
			System.out.println(result.getString(1) + "\t" + result.getInt(2) + "\t" + result.getInt(3));
		}
	}
	
	public void printAllDepend() throws SQLException
	{
		// Form the query and execute it
		String query = "select essn, dependent_name, sex, bdate, relationship from dependent";
		PreparedStatement p = conn.prepareStatement(query);
		ResultSet result = p.executeQuery();
		
		// Print the results of the executed query
		while (result.next()) {
			System.out.println(result.getString(1) + "\t" + result.getString(2) + "\t" + result.getString(3) + "\t" + result.getDate(4).toString() + "\t" + result.getString(5));
		}
	}
}
