package com.aurionpro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.aurionpro.database.Database;
import com.aurionpro.model.Course;

public class CourseDao {
private static CourseDao courseDao = null;
	
	private CourseDao() {
	}
	
	public static CourseDao getstudentDaoInstance() {
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
			
			boolean isInserted = callableStatement.getBoolean(3);
			String message = callableStatement.getString(4);
			
			if(isInserted) {
				System.out.println("Teacher Assigned To Course Successfully !");
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
			CallableStatement callableStatement = connection.prepareCall("{CALL delete_course(");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
