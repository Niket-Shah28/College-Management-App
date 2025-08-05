package com.aurionpro.service;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.dao.FeesDao;
import com.aurionpro.model.CourseFee;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentCourseFees;

public class FeesService {
	private static FeesService feesService = null;
	private FeesDao feesDao = FeesDao.getFeesDaoInstance();
	private FeesService() {
	}
	
	public static FeesService getFeesServiceInstance() {
		if(feesService == null) {
			feesService = new FeesService();
		}
		return feesService;
	}
	
	public void viewAllFeeDetails() {
		List<StudentCourseFees> studentCourseFees = feesDao.getStudentsFeesDetails();
		if(studentCourseFees.isEmpty()) {
			System.out.println("No students");
			return;
		}
		displayStudentCourseFeesTable(studentCourseFees);
	}
	
	public void getCourseFeeDetails() {
		List<CourseFee> courseFees = feesDao.getCourseFeesDetails();
		displayCourseFees(courseFees);
	}
	
	public void updateCourseFeeDetails(Scanner scanner) {
		List<CourseFee> courseFees = feesDao.getCourseFeesDetails();
		displayCourseFees(courseFees);
		System.out.println("Enter the choice whose fee amount needs to be changed OR enter 0 to EXIT: ");
		int choice = scanner.nextInt();
		if(choice == 0) {
			return;
		}
		if(choice < 0 || choice > courseFees.size()) {
			System.out.println("Invalid Choice !");
			return;
		}
		double amount;
		while(true) {
			System.out.println("Enter the new Fee Amount: ");
			amount = scanner.nextDouble();
			if(amount <= 0) {
				System.out.println("Amount Must be Greater Than Zero");
				continue;
			}
			break;
		}
		
		CourseFee courseFeeObject = courseFees.get(choice - 1);
		courseFeeObject.setFeeAmount(amount);
		feesDao.updateCourseFeesDetails(courseFeeObject);
	}
	
	public void viewPendingFeesDetails() {
		List<StudentCourseFees> studentCourseFees = feesDao.getCourseFeesDetailsOfPendingFees();
		if(studentCourseFees.isEmpty()) {
			System.out.println("No students");
			return;
		}
		displayStudentCourseFeesTable(studentCourseFees);
	}
	
	public void payFees(Scanner scanner) {
		List<Student> students = feesDao.getStudentsWithPendingFees();
		if(students.isEmpty()) {
			System.out.println("No Students with pending fees");
			return;
		}
		displayStudents(students);
		System.out.println("Enter Your Choice OR enter 0 to EXIT: ");
		int studChoice = scanner.nextInt();
		if(studChoice == 0) {
			return;
		}
		if(studChoice < 0 || studChoice > students.size()) {
			System.out.println("Invalid Choice");
			return;
		}
		int studentId = students.get(studChoice-1).getStudentId();
		List<StudentCourseFees> courses = feesDao.getCoursesWithPendingFeesForStudent(studentId);
		displayCourseFeesWithPendingAmount(courses);
		System.out.println("Enter choice whose fees you want to Pay OR enter 0 to EXIT: ");
		int choice = scanner.nextInt();
		if(choice == 0) {
			return;
		}
		if(choice < 0 || choice > courses.size()) {
			System.out.println("Invalid Choice");
			return;
		}
		StudentCourseFees selectedCourse = courses.get(choice - 1);
		int courseId = selectedCourse.getCourseId();
		double remainingAmount =  selectedCourse.getAmount() - selectedCourse.getAmountPaid();
		System.out.println("Remaining amount is "+remainingAmount);
		double amount;
		while(true) {
			System.out.println("Enter Amount: ");
			amount = scanner.nextDouble();
			if(amount <= remainingAmount && amount > 0) {
				break;
			}
			System.out.println("Amount Must be between 0 and "+remainingAmount);
		}
		feesDao.payFees(studentId, courseId, amount);
	}
	
	//DISPLAY
	public static void displayCourseFeesWithPendingAmount(List<StudentCourseFees> data) {
	    System.out.printf("%-5s %-10s %-20s %-12s %-12s%n",
	            "No.", "CourseID", "Course Name", "Amount", "Amount Paid");
	    System.out.println("-------------------------------------------------------------");

	    int index = 1;
	    for (StudentCourseFees scf : data) {
	        System.out.printf("%-5d %-10d %-20s %-12.2f %-12.2f%n",
	                index,
	                scf.getCourseId(),
	                scf.getCourseName(),
	                scf.getAmount(),
	                scf.getAmountPaid()
	        );
	        index++;
	    }
	}
	public static void displayCourseFees(List<CourseFee> data) {
		
		if(data == null || data.isEmpty()) {
			System.out.println("No Data To Show");
			return;
		}
	    // Table Header
	    System.out.printf("%-5s %-10s %-20s %-12s%n", "No.", "CourseID", "Course Name", "Fee Amount");
	    System.out.println("-------------------------------------------------------------");

	    // Table Rows with index
	    int index = 1;
	    for (CourseFee cf : data) {
	        System.out.printf(
	            "%-5d %-10d %-20s %-12.2f%n",
	            index++,
	            cf.getCourseId(),
	            cf.getCourseName(),
	            cf.getFeeAmount()
	        );
	    }
	}
	
	public void displayStudents(List<Student> students) {
		
		System.out.printf("%-5s %-15s %-30s%n", "No.", "Roll Number", "Name");
		System.out.println("--------------------------------------------------------");

		for (int i = 0; i < students.size(); i++) {
		    Student student = students.get(i);
		    System.out.printf("%-5d %-15s %-30s%n", (i + 1), student.getRollNumber(), student.getName());
		}
	}

	public static void displayStudentCourseFeesTable(List<StudentCourseFees> data) {

	    System.out.printf(
	        "%-10s %-15s %-12s %-10s %-15s %-10s %-10s %-10s %-10s %-12s %-8s%n",
	        "StudID", "Name", "RollNo", "CourseID", "Course Name", "Duration", "Stream", "FeesID", "Amount", "Amt Paid", "Paid"
	    );
	    System.out.println("---------------------------------------------------------------------------------------------------------------");

	    for (StudentCourseFees scf : data) {
	        System.out.printf(
	            "%-10d %-15s %-12d %-10d %-15s %-10d %-10s %-10d %-10.2f %-12.2f %-8s%n",
	            scf.getStudentId(),
	            scf.getName(),
	            scf.getRollNumber(),
	            scf.getCourseId(),
	            scf.getCourseName(),
	            scf.getDuration(),
	            scf.getStream(),
	            scf.getFeesId(),
	            scf.getAmount(),
	            scf.getAmountPaid(),
	            scf.isPaid() ? "Yes" : "No"
	        );
	    }
	}

	
}
