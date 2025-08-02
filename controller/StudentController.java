package com.aurionpro.controller;

import java.util.Scanner;

import com.aurionpro.service.StudentService;

public class StudentController {
    private static final StudentService studentService = StudentService.getstudentServiceInstance();

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
        	scanner.nextLine();
            System.out.println("\n========== Student Management Menu ==========");
            System.out.println("1. Add a Student");
            System.out.println("2. Delete a Student");
            System.out.println("3. Update Student Details");
            System.out.println("4. View Courses Opted with Subjects (All Students)");
            System.out.println("5. Show All Students");
            System.out.println("6. Assign Course to a Student");
            System.out.println("7. View a Student");
            System.out.println("8. View Courses Opted with Subjects (Particular Student)");
            System.out.println("9. Remove Student From a Course");
            System.out.println("10. Exit");
            System.out.print("Enter your choice (1-9): ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    studentService.addStudent(scanner);
                    break;
                case "2":
                    studentService.deleteStudent(scanner);
                    break;
                case "3":
                    studentService.updateStudentProfile(scanner);
                    break;
                case "4":
                    studentService.getStudentsDataWithCourses();
                    break;
                case "5":
                    studentService.showAllStudentsProfile();
                    break;
                case "6":
                    studentService.assignCourseToStudent(scanner);
                    break;
                case "7":
                    studentService.getParticularStudentProfile(scanner);
                    break;
                case "8":
                    studentService.getParticularStudentCourseData(scanner);
                    break;
                case "9":
                	studentService.removeStudentFromCourse(scanner);
                	break;
                case "10":
                    System.out.println("Exiting Student Management. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 9.");
            }
        }
    }
}

