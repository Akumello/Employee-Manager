package company;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
	EmployeeManager empMgr = null;
	
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        empMgr = new EmployeeManager();
    }

    @FXML AnchorPane manager_login;
    @FXML AnchorPane emp_entry;
    @FXML AnchorPane proj_entry;
    @FXML AnchorPane depend_entry;
    @FXML AnchorPane report_pane;
    
    @FXML TextField field_mgrssn;
    @FXML Text text_loginerror;
    @FXML DatePicker date_entry;


    @FXML Button button_mgrlogin;
    @FXML Button next_button;
    @FXML Button submit_button;
    
    @FXML TextField text_fname;
    @FXML TextField text_minit;
    @FXML TextField text_lname;
    @FXML TextField text_ssn;
    @FXML TextField text_address;
    @FXML TextField text_salary;
    @FXML TextField text_supssn;
    @FXML TextField text_dno;
    @FXML TextField text_email;
    @FXML DatePicker date_bdate;
    @FXML RadioButton radio_male;
    @FXML RadioButton radio_female;
    
    @FXML TextArea txtarea_assignments;
    @FXML TextArea txtarea_report;
    @FXML TextField text_proj;
    @FXML TextField text_hours;
    @FXML Text text_error;
    
    @FXML TextField dep_fname;
    @FXML DatePicker dep_bdate;
    @FXML RadioButton dep_male;
    @FXML RadioButton dep_female;
    @FXML TextField dep_relation;
    @FXML RadioButton radio_yes;
    @FXML RadioButton radio_no;
    
    
    ArrayList<String> assignments = new ArrayList<String>();
    ArrayList<Integer> assignmentHours = new ArrayList<Integer>();
    ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
    boolean hasDependent = false;
    boolean infoMissing = false;
    
    @FXML
    private void AddDepend()
    {
    	String depSex = "";
    	if(dep_male.isSelected())
    		depSex = "M";
    	else if(dep_female.isSelected())
    		depSex = "F";
    	else
    		infoMissing = true;
    	
    	Dependency depend = new Dependency(dep_fname.getText(), Date.valueOf(dep_bdate.getValue()), depSex, dep_relation.getText());
    	dependencies.add(depend);
    }
    
    @FXML
    private void DependEntry()
    {
    	proj_entry.setVisible(false);
    	depend_entry.setVisible(true);
    }
    
    private int getTotalHours()
    {
    	int total = 0;
    	for(int hours : assignmentHours)
    	{
    		total += hours;
    	}
    	return total;
    }
    
    @FXML
    private void AddAssignment()
    {
    	// Verify the next entry does not exceed the hours limit
    	int hours = Integer.valueOf(text_hours.getText());
    	if(hours + getTotalHours() > 40)
    	{
    		text_error.setText("Total hours may not exceed 40");
    		text_error.setVisible(true);
    		return;
    	}
    	
    	// Verify project exists
    	String project = text_proj.getText();
    	try {
			if(!empMgr.projectExists(project))
			{
	    		text_error.setText("Project \"" + project + "\" does not exist");
	    		text_error.setVisible(true);
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	// If entry is valid, then add to the lists
    	assignments.add(project);
    	assignmentHours.add(hours);
    	
    	// Show current assigned projects to the user
    	String display = "Project Name\tHours\n";
    	for(int i = 0; i < assignments.size(); i++)
    	{
    		display += assignments.get(i) + "\t\t" + assignmentHours.get(i) + "\n";
    	}
		txtarea_assignments.setText(display);
		
		text_error.setVisible(false);
    }    
    
    @FXML
    private void ProjectAssignmentEntry()
    {
    	hasDependent = false;
    	if(radio_yes.isSelected())
    		hasDependent = true;
    	else if(radio_no.isSelected())
    		hasDependent = false;
    	
    	// Switch to project pane
    	emp_entry.setVisible(false);
    	proj_entry.setVisible(true);
    	
    	// Reveal the appropriate button depending on if the emp has dependents
    	// One button goes to dependent pane and the other to the report pane
    	if(hasDependent)
    	{
    		submit_button.setVisible(false);
    		next_button.setVisible(true);
    	}
    	else
    	{
    		submit_button.setVisible(true);
    		next_button.setVisible(false);
    	}
    }

    @FXML
    private void SubmitNewEmployee(ActionEvent event)
    {
    	// ######################### Employee #########################
    	// Get info
    	boolean infoMissing = false;
    	String fname = text_fname.getText();
    	String minit = text_minit.getText();
    	String lname = text_lname.getText();
    	String ssn = text_ssn.getText();
    	String address = text_address.getText();
    	double salary = Double.valueOf(text_salary.getText());
    	String supssn = text_supssn.getText();
    	int dno = Integer.valueOf(text_dno.getText());
    	String email = text_email.getText();
    	Date date = Date.valueOf(date_bdate.getValue());
    	Date today = new Date(System.currentTimeMillis());
    	String sex = "";
    	if(radio_male.isSelected())
    		sex = "M";
    	else if(radio_female.isSelected())
    		sex = "F";

    	// Add new employee to the database
    	try 
    	{
			empMgr.AddNewEmployee(fname, minit, lname, ssn, date, address, sex, salary, supssn, dno, email);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	// Start the report, and add to it as we go
    	String report = "New Employee Report:\n" + "First Name:\t" + fname + "\nM. Initial:\t" +
    			minit + "\nLast Name:\t\t" +
    			lname + "\nSSN:\t\t\t" +
    			ssn + "\nAddress:\t\t" +
    			address + "\nSalary:\t\t\t" +
    			salary + "\nSuper SSN:\t\t" +
    			supssn + "\nDept Number:\t" +
    			dno + "\nEmail:\t\t\t" +
    			email + "\nBirthdate:\t\t" +
    			date.toString() + "\n\n";

    	
    	// ############################ Project Assignments #############################
    	for(int i = 0; i < assignments.size(); i++)
    	{
	    	try {
				empMgr.AssignToProject(ssn, assignments.get(i), assignmentHours.get(i));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}    	
    	
    	// Show current assigned projects to the user
    	String display = "Project(s) Assigned:\nProject\tHours\n";
    	for(int i = 0; i < assignments.size(); i++)
    	{
    		display += assignments.get(i) + "\t" + assignmentHours.get(i) + "\n";
    	}
		report += display;
		
    	
		
		// ###################### Dependents ###########################
    	report += "\nDependents:\n";
    	if(hasDependent)
    	{
    		// Add each dependent to the database
	    	for(Dependency dep : dependencies)
	    	{
		    	try {
					empMgr.AddNewDependent(ssn, dep.getName(), dep.getSex(), dep.getBirthdate(), dep.getRelationship());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	
	    		report += dep.toString() + "\n";
	    	}
	    	
	    	depend_entry.setVisible(false);
    	}
    	else 
    	{
    		report += "None\n";
    		proj_entry.setVisible(false);
    	}
    	
    	
    	// Reveal the pane with the report on it
    	report_pane.setVisible(true);
    	txtarea_report.setText(report + "\nDatabase has been updated.");
    	

    	/*/
    	Date today = new Date(System.currentTimeMillis());
    	try {
			empMgr.AddNewEmployee("Joe", "D", "Bangus", "111881111", today, "123 Cherry Blossom Ln", "M", 30000.50, "333445555", 5, "hello@gmail.com");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	try {
			empMgr.printAllEmp();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//*/
    }

    @FXML
    private void ManagerLogin(ActionEvent event)
    {	
    	/*/
    	try {
    		System.out.println("All Employees\n");
    		empMgr.printAllEmp();
    		System.out.println("All Dependents\n");
			empMgr.printAllDepend();
			System.out.println("All project assignments\n");
			empMgr.printAllWorks();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//*/
    	
    	
    	String ssn = field_mgrssn.getText();
    	
    	boolean auth_pass = false;
    	try {
			auth_pass = empMgr.VerifyManagerSSN(ssn);
		} catch (SQLException e) {
			System.out.println("Problem with SQL");
		}
    	
    	if(auth_pass)
    	{
    		emp_entry.setVisible(true);
    		manager_login.setVisible(false);
    	}
    	else
    	{
    		text_loginerror.setVisible(true);
    	}
    }
}
