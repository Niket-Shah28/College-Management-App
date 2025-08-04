package com.aurionpro.controller;

import java.util.Scanner;

import com.aurionpro.service.TeacherService;

public class TeacherController {
	private static final TeacherService teacherService = TeacherService.getTeacherServiceInstance();

    public static void run(Scanner scanner) {

        while (true) {
            System.out.println("\n========== Teacher Management Menu ==========");
            System.out.println("1. Add a Teacher");
            System.out.println("2. Delete a Teacher");
            System.out.println("3. Update Teacher Profile");
            System.out.println("4. Show All Teachers");
            System.out.println("5. View a Particular Teacher Profile");
            System.out.println("6. View All Teachers with Courses & Subjects");
            System.out.println("7. View Particular Teacher's Courses & Subjects");
            System.out.println("8. Assign Teacher to a Subject");
            System.out.println("9. Exit");
            System.out.print("Enter your choice (1-9): ");
            
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    teacherService.addTeacher(scanner);
                    break;
                case "2":
                    teacherService.deleteTeacher(scanner);
                    break;
                case "3":
                    teacherService.updateTeacherProfile(scanner);
                    break;
                case "4":
                    teacherService.showAllTeachersProfile();
                    break;
                case "5":
                    teacherService.getParticularTeacherProfile(scanner);
                    break;
                case "6":
                    teacherService.getTeachersDataWithCourses();
                    break;
                case "7":
                    teacherService.getParticularTeacherCourseData(scanner);
                    break;
                case "8":
                    teacherService.assignTeacherToSubject(scanner);
                    break;
                case "9":
                    System.out.println("Exiting Teacher Management. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 9.");
            }
        }
    }
}
