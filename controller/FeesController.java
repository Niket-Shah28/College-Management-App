package com.aurionpro.controller;

import java.util.Scanner;

import com.aurionpro.service.FeesService;

public class FeesController {
	private static final FeesService feesService = FeesService.getFeesServiceInstance();

	public static void run(Scanner scanner) {
		while (true) {
			System.out.println("\n========== Fees Management Menu ==========");
			System.out.println("1. View All Student Fees Details");
			System.out.println("2. View Course Fee Details");
			System.out.println("3. Update Course Fee Details");
			System.out.println("4. View Fees Details of Remaining Payment");
			System.out.println("5. Pay Fees");
			System.out.println("6. Exit");
			System.out.print("Enter your choice (1-6): ");

			String input = scanner.nextLine().trim();

			switch (input) {
			case "1":
				feesService.viewAllFeeDetails();
				break;
			case "2":
				feesService.getCourseFeeDetails();
				break;
			case "3":
				feesService.updateCourseFeeDetails(scanner);
				break;
			case "4":
				feesService.viewPendingFeesDetails();
				break;
			case "5":
				feesService.payFees(scanner);
				break;
			case "6":
				System.out.println("Exiting Fees Management. Goodbye!");
				return;
			default:
				System.out.println("Invalid input. Please enter a number between 1 and 6.");
			}
		}
	}

}
