package com.aurionpro.controller;

import java.util.Scanner;
import com.aurionpro.service.SubjectService;

public class SubjectController {

    private static final SubjectService subjectService = SubjectService.getSubjectServiceInstance();

    public static void run(Scanner scanner) {

        while (true) {
            System.out.println("\n========== Subject Management Menu ==========");
            System.out.println("1. Add a Subject");
            System.out.println("2. Delete a Subject");
            System.out.println("3. Update Subject Details");
            System.out.println("4. View All Subjects");
            System.out.println("5. View Subject Details (Courses or Students)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    subjectService.addSubject(scanner);
                    break;
                case "2":
                    subjectService.deleteSubject(scanner);
                    break;
                case "3":
                    subjectService.updateSubject(scanner);
                    break;
                case "4":
                	subjectService.viewAllSubjects();
                	break;
                case "5":
                    subjectService.viewSubjectDetails(scanner);
                    break;
                case "6":
                    System.out.println("Exiting Subject Management. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
            }
        }
    }
}
