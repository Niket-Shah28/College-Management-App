package com.aurionpro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.database.Database;
import com.aurionpro.model.CourseFee;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentCourseFees;

public class FeesDao {
	private static FeesDao feesDao = null;
	
	private FeesDao() {
	}
	
	public static FeesDao getFeesDaoInstance() {
		if(feesDao == null) {
			feesDao = new FeesDao();
		}
		return feesDao;
	}
	
	public List<StudentCourseFees> getStudentsFeesDetails() {
		Connection connection = Database.connect();
		List<StudentCourseFees> studentFees = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM students_course_fees");
			
			while(resultSet.next()) {
				studentFees.add(
					new StudentCourseFees(
						resultSet.getInt("student_id"),
						resultSet.getString("name"),
						resultSet.getInt("roll_no"),
						resultSet.getInt("course_id"),
						resultSet.getString("course_name"),
						resultSet.getInt("duration"),
						resultSet.getString("stream"),
						resultSet.getInt("fees_id"),
						resultSet.getDouble("amount"),
						resultSet.getDouble("amount_paid"),
						resultSet.getBoolean("is_paid")
					)
				);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return studentFees;
	}
	
	public List<CourseFee> getCourseFeesDetails() {
		Connection connection = Database.connect();
		List<CourseFee> courseFeesData = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM course_fees_details");
			
			while(resultSet.next()) {
				courseFeesData.add(
					new CourseFee(
						resultSet.getInt("course_id"),
						resultSet.getString("course_name"),
						resultSet.getDouble("fee_amount")
					)
				);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return courseFeesData;
	}
	
	public void updateCourseFeesDetails(CourseFee courseFee) {
		Connection connection = Database.connect();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE course_fee SET fee_amount = ? WHERE course_id = ?");
			preparedStatement.setDouble(1, courseFee.getFeeAmount());
			preparedStatement.setInt(2, courseFee.getCourseId());
			
			preparedStatement.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}	
	
	public List<StudentCourseFees> getCourseFeesDetailsOfPendingFees() {
		Connection connection = Database.connect();
		List<StudentCourseFees> studentFees = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM students_course_fees WHERE is_paid = FALSE");
			
			while(resultSet.next()) {
				studentFees.add(
					new StudentCourseFees(
						resultSet.getInt("student_id"),
						resultSet.getString("name"),
						resultSet.getInt("roll_no"),
						resultSet.getInt("course_id"),
						resultSet.getString("course_name"),
						resultSet.getInt("duration"),
						resultSet.getString("stream"),
						resultSet.getInt("fees_id"),
						resultSet.getDouble("amount"),
						resultSet.getDouble("amount_paid"),
						resultSet.getBoolean("is_paid")
					)
				);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return studentFees;
	}
	
	public void payFees(int studentId, int courseId, double amount) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL pay_fees(?, ?, ?, ?, ?)}");
			callableStatement.setInt(1, studentId);
			callableStatement.setInt(2, courseId);
			callableStatement.setDouble(3, amount);
			
			callableStatement.registerOutParameter(4, Types.BOOLEAN);
			callableStatement.registerOutParameter(5, Types.VARCHAR);
			
			callableStatement.execute();
			
			@SuppressWarnings("unused")
			boolean isPaid = callableStatement.getBoolean(4);
			String message = callableStatement.getString(5);
			System.out.println(message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<StudentCourseFees> getCoursesWithPendingFeesForStudent(int studentId) {
		Connection connection = Database.connect();
		List<StudentCourseFees> courses = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT course_id, course_name, amount, amount_paid FROM students_course_fees WHERE is_paid = FALSE AND student_id = "+studentId);
			
			while(resultSet.next()) {
				courses.add(
					new StudentCourseFees(
						resultSet.getInt("course_id"),
						resultSet.getString("course_name"),
						resultSet.getDouble("amount"),
						resultSet.getDouble("amount_paid")
					)
				);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}
	
	public List<Student> getStudentsWithPendingFees() {
		Connection connection = Database.connect();
		List<Student> students = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT DISTINCT(student_id), name, roll_no FROM students_course_fees WHERE is_paid = FALSE");
			
			while(resultSet.next()) {
				students.add(
					new Student(
						resultSet.getInt("student_id"),
						resultSet.getInt("roll_no"),
						resultSet.getString("name")
					)
				);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
}
