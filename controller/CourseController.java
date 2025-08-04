package com.aurionpro.controller;

import java.util.Scanner;

import com.aurionpro.service.CourseService;

public class CourseController {
    private static final CourseService courseService = CourseService.getCourseServiceInstance();

    public static void run(Scanner scanner) {
    	
        while (true) {
            System.out.println("\n========== Course Management Menu ==========");
            System.out.println("1. Add a Course");
            System.out.println("2. Delete a Course");
            System.out.println("3. Update Course Details");
            System.out.println("4. View Course Details (Students or Subjects)");
            System.out.println("5. Add Subject to Course");
            System.out.println("6. Remove Subject from Course");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    courseService.addCourse(scanner);
                    break;
                case "2":
                    courseService.deleteCourse(scanner);
                    break;
                case "3":
                    courseService.updateCourse(scanner);
                    break;
                case "4":
                    courseService.viewCourseDetails(scanner);
                    break;
                case "5":
                    courseService.addSubjectToCourse(scanner);
                    break;
                case "6":
                    courseService.removeSubjectFromCourse(scanner);
                    break;
                case "7":
                    System.out.println("Exiting Course Management. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
            }
        }
    }
}
