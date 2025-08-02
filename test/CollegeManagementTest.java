package com.aurionpro.test;

import java.sql.SQLException;

import com.aurionpro.controller.StudentController;
import com.aurionpro.database.Database;

public class CollegeManagementTest {

	public static void main(String[] args) {
		StudentController.run();
		try {
			Database.connect().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
