package com.aurionpro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.aurionpro.database.Database;
import com.aurionpro.model.Course;
import com.aurionpro.model.StudentCourseSubject;
import com.aurionpro.model.Subject;
import com.aurionpro.model.SubjectCourseData;
import com.aurionpro.model.SubjectType;

public class CourseDao {
	private static CourseDao courseDao = null;
	
	private CourseDao() {
	}
	
	public static CourseDao getCourseDaoInstance() {
		if(courseDao == null) {
			courseDao = new CourseDao();
		}
		return courseDao;
	}
	
	public void addCourse(Course course) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL add_course(?, ?, ?, ?, ?)}");
			callableStatement.setString(1, course.getCourseName());
			callableStatement.setInt(2, course.getDuration());
			callableStatement.setString(3, course.getStream());
			
			callableStatement.registerOutParameter(4, Types.BOOLEAN);
			callableStatement.registerOutParameter(5, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(4);
			String message = callableStatement.getString(5);
			
			if(isInserted) {
				System.out.println("COURSE ADDED SUCCESSFULLY !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteCourse(int courseId) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL delete_course(?, ?, ?)}");
			callableStatement.setInt(1, courseId);
			
			callableStatement.registerOutParameter(2, Types.BOOLEAN);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isDeleted = callableStatement.getBoolean(2);
			String message = callableStatement.getString(3);
			if(isDeleted) {
				System.out.println("Course Deleted Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCourse(Course course) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL update_course(?, ?, ?, ?, ?, ?)}");
			callableStatement.setInt(1, course.getCourseId());
			callableStatement.setString(2, course.getCourseName());
			callableStatement.setInt(3, course.getDuration());
			callableStatement.setString(4, course.getStream());
			
			callableStatement.registerOutParameter(5, Types.BOOLEAN);
			callableStatement.registerOutParameter(6, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isUpdated = callableStatement.getBoolean(5);
			String message = callableStatement.getString(6);
			if(isUpdated) {
				System.out.println("Course Updated Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<StudentCourseSubject> getStudentsInCourse(int courseId) {
		
		Connection connection = Database.connect();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT DISTINCT(student_id), roll_no, name, course_id, course_name FROM students_opted_course_and_subjects WHERE course_id = "+courseId);
			List<StudentCourseSubject> studentSubjectsData = new ArrayList<>();
			
			while(resultSet.next()) {
				studentSubjectsData.add(
					new StudentCourseSubject(
							resultSet.getInt("student_id"),
							resultSet.getInt("roll_no"),
							resultSet.getString("name"),
							resultSet.getInt("course_id"),
							resultSet.getString("course_name")
					));
			}
			return studentSubjectsData;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SubjectCourseData> getSubjectsInCourse(int courseId) {
		
		Connection connection = Database.connect();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM courses_subjects_data WHERE course_id = "+courseId);
			List<SubjectCourseData> subjectCourseData = new ArrayList<>();
			
			while(resultSet.next()) {
				subjectCourseData.add(
					new SubjectCourseData(
							resultSet.getInt("subject_course_id"),
							resultSet.getInt("course_id"),
							resultSet.getString("course_name"),
							resultSet.getInt("subject_id"),
							resultSet.getString("subject_name"),
							resultSet.getInt("academic_year"),
							SubjectType.valueOf(resultSet.getString("subject_type")),
							resultSet.getInt("from_year"),
							resultSet.getInt("to_year")
					));
			}
			return subjectCourseData;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Course> getAllCourseDetails() {
		Connection connection = Database.connect();
		List<Course> courses = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM course WHERE is_deleted = FALSE");
			while(resultSet.next()) {
				courses.add(
					new Course(
							resultSet.getInt("course_id"),
							resultSet.getString("course_name"),
							resultSet.getInt("duration"),
							resultSet.getString("stream")
					)
				);
			}
			return courses;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Course getParticularCourseDetails(int courseId) {
		Connection connection = Database.connect();
		Course course = null;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM course WHERE course_id = "+courseId+" AND is_deleted = FALSE");
			while(resultSet.next()) {
				course =
					new Course(
							resultSet.getInt("course_id"),
							resultSet.getString("course_name"),
							resultSet.getInt("duration"),
							resultSet.getString("stream")
					);
			}
			return course;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Subject> getSubjectsNotInCourse(int courseId) {
		Connection connection = Database.connect();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT DISTINCT(subject_id), subject_name "
														+ "FROM subject "
														+ "WHERE subject_id NOT IN ( "
														+ "SELECT subject_id "
														+ "FROM  courses_subjects_data "
														+ "WHERE course_id = "+courseId+" )");
			List<Subject> subjectData = new ArrayList<>();
			
			while(resultSet.next()) {
				subjectData.add(
					new Subject(
							resultSet.getInt("subject_id"),
							resultSet.getString("subject_name")
					));
			}
			return subjectData;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addSubjectToCourse(SubjectCourseData subjectCourse) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL add_subject_to_course(?, ?, ?, ?, ?, ?, ?, ?)}");
			callableStatement.setInt(1, subjectCourse.getSubjectId());
			callableStatement.setInt(2, subjectCourse.getCourseId());
			callableStatement.setInt(3, subjectCourse.getFromYear());
			callableStatement.setInt(4, subjectCourse.getToYear());
			callableStatement.setInt(5, subjectCourse.getAcademicYear());
			callableStatement.setString(6, subjectCourse.getSubjectType().toString());
			
			callableStatement.registerOutParameter(7, Types.BOOLEAN);
			callableStatement.registerOutParameter(8, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(7);
			String message = callableStatement.getString(8);
			if(isInserted) {
				System.out.println("Subject Added To Course Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeSubjectFromCourse(int subject_course_id) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL remove_subject_from_course(?, ?, ?)}");
			callableStatement.setInt(1, subject_course_id);
			
			callableStatement.registerOutParameter(2, Types.BOOLEAN);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isRemoved = callableStatement.getBoolean(2);
			String message = callableStatement.getString(3);
			if(isRemoved) {
				System.out.println("Subject Removed From Course Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
