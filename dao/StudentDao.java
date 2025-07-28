package com.aurionpro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.aurionpro.database.Database;
import com.aurionpro.model.Profile;
import com.aurionpro.model.Student;

public class StudentDao {
	private static StudentDao studentDao = null;
	
	private StudentDao() {
	}
	
	public static StudentDao getstudentDaoInstance() {
		if(studentDao == null) {
			studentDao = new StudentDao();
		}
		return studentDao;
	}
	
	public void addStudent(Profile profile, Student student) {
		Connection connection = Database.connect();
		
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL insert_student_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// PROFILE DETAILS
			callableStatement.setString(1, profile.getFirst_name());
			callableStatement.setString(2, profile.getMiddle_name());
			callableStatement.setString(3, profile.getLast_name());
			callableStatement.setString(4, profile.getEmail());
			callableStatement.setString(5, profile.getGender().toString());
			callableStatement.setString(6, profile.getContact_number());
			callableStatement.setString(7, profile.getAddress());
			callableStatement.setString(8, profile.getCity());
			callableStatement.setString(9, profile.getState());
			callableStatement.setString(10, profile.getCountry());
			callableStatement.setString(11, profile.getBloodGroup());
			callableStatement.setBoolean(12, profile.getDisabilityStatus());
			callableStatement.setString(13, profile.getEmergencyContactNumber());
			callableStatement.setBoolean(14, profile.isStudent());
			callableStatement.setBoolean(15, profile.isTeacher());
			
			// STUDENT SPECEFIC DETAILS
			callableStatement.setInt(16, student.getRollNumber());
			callableStatement.setString(17, student.getFatherName());
			callableStatement.setString(18, student.getMotherName());
			
			// STATUS OUTPUT PARAMETERS
			callableStatement.registerOutParameter(19, Types.BOOLEAN); // INSERT STATUS (TRUE / FALSE)
			callableStatement.registerOutParameter(20, Types.VARCHAR); // MESSAGE IF ANY
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(19);
			String message = callableStatement.getString(20);
			
			if(isInserted) {
				System.out.println("Student Added Successfully !");
				return;
			}
			
			System.out.println("ERROR: "+message);
			return;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStudent() {
		
	}
	
	public void updateStudent() {
		
	}
	
	public void readAllStudents() {
		
	}
	
	public void viewStudent(int studentId) {
		
	}
	
	public void assignCourseToStudent() {
		
	}
	
	public void removeStudentFromCourse() {
		
	}
}
