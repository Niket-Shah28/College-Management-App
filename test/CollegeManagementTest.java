package com.aurionpro.test;

import java.sql.SQLException;
import java.util.Scanner;

import com.aurionpro.controller.CourseController;
import com.aurionpro.controller.StudentController;
import com.aurionpro.controller.SubjectController;
import com.aurionpro.controller.TeacherController;
import com.aurionpro.database.Database;

public class CollegeManagementTest {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n========== School Management Dashboard ==========");
            System.out.println("1. Student Dashboard");
            System.out.println("2. Teacher Dashboard");
            System.out.println("3. Course Dashboard");
            System.out.println("4. Subject Dashboard");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    StudentController.run(scanner);
                    break;
                case "2":
                    TeacherController.run(scanner);
                    break;
                case "3":
                    CourseController.run(scanner);
                    break;
                case "4":
                    SubjectController.run(scanner);
                    break;
                case "5":
                	try {
            			Database.connect().close();
            		} catch (SQLException e) {
            			e.printStackTrace();
            		}
                	scanner.close();
                    System.out.println("Exiting School Management System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
            }
        }  
	}
}
