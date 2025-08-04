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
import com.aurionpro.model.StudentCourseSubject;
import com.aurionpro.model.Subject;
import com.aurionpro.model.SubjectCourseData;
import com.aurionpro.model.SubjectType;

public class SubjectDao {
	private static SubjectDao subjectDao = null;
	
	private SubjectDao() {
	}
	
	public static SubjectDao getSubjectDaoInstance() {
		if(subjectDao == null) {
			subjectDao = new SubjectDao();
		}
		return subjectDao;
	}
	
	public void addSubject(Subject subject) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL add_subject(?, ?, ?)}");
			callableStatement.setString(1, subject.getSubjectName());
			
			callableStatement.registerOutParameter(2, Types.BOOLEAN);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(2);
			String message = callableStatement.getString(3);
			
			if(isInserted) {
				System.out.println("Subject Added Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSubject(int subjectId) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL delete_subject(?, ?, ?)}");
			callableStatement.setInt(1, subjectId);
			
			callableStatement.registerOutParameter(2, Types.BOOLEAN);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isDeleted = callableStatement.getBoolean(2);
			String message = callableStatement.getString(3);
			
			if(isDeleted) {
				System.out.println("Subject Deleted Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateSubject(Subject subject) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL update_subject(?, ?, ?, ?)}");
			callableStatement.setInt(1, subject.getSubjectId());
			callableStatement.setString(2, subject.getSubjectName());
			
			callableStatement.registerOutParameter(3, Types.BOOLEAN);
			callableStatement.registerOutParameter(4, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isUpdated = callableStatement.getBoolean(3);
			String message = callableStatement.getString(4);
			
			if(isUpdated) {
				System.out.println("Subject Details Updated Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<SubjectCourseData> getCourseDetailsForSubject(int subjectId) {
		Connection connection = Database.connect();
		List<SubjectCourseData> subjectsDataWithCourses = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT course_id, course_name, academic_year, from_year, to_year, subject_type FROM courses_subjects_data WHERE subject_id = ?");
			preparedStatement.setInt(1, subjectId);
			
			preparedStatement.execute();
			
			ResultSet resultSet = preparedStatement.getResultSet();
			
			while(resultSet.next()) {
				subjectsDataWithCourses.add(
					new SubjectCourseData(
						resultSet.getInt("course_id"),
						resultSet.getString("course_name"),
						resultSet.getInt("academic_year"),
						resultSet.getInt("from_year"),
						resultSet.getInt("to_year"),
						SubjectType.valueOf(resultSet.getString("subject_type"))
					)	
				);	
			}
			return subjectsDataWithCourses;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<StudentCourseSubject> getStudentsOptingParticularSubject(int subjectId) {
		Connection connection = Database.connect();
		List<StudentCourseSubject> students = new ArrayList<>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM students_opted_course_and_subjects WHERE subject_id = ?");
			preparedStatement.setInt(1, subjectId);
			
			preparedStatement.execute();
			
			ResultSet resultSet = preparedStatement.getResultSet();
			while(resultSet.next()) {
				students.add(
					new StudentCourseSubject(
						resultSet.getInt("student_id"),
						resultSet.getInt("roll_no"),
						resultSet.getString("name"),
						resultSet.getInt("course_id"),
						resultSet.getString("course_name"),
						resultSet.getInt("subject_id"),
						resultSet.getString("subject_name"),
						SubjectType.valueOf(resultSet.getString("subject_type")),
						resultSet.getInt("academic_year")	
					)
				);
			}
			return students;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Subject> getAllSubjects(){
		Connection connection = Database.connect();
		List<Subject> subjects = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT * FROM subject WHERE is_deleted = FALSE");
			while(resultSet.next()) {
				subjects.add(
					new Subject(
							resultSet.getInt("subject_id"),
							resultSet.getString("subject_name")
					)
				);
			}
			return subjects;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
