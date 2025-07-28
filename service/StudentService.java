package com.aurionpro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.aurionpro.dao.StudentDao;
import com.aurionpro.model.GenderChoice;
import com.aurionpro.model.Profile;
import com.aurionpro.model.Student;
import com.aurionpro.validator.ProfileDetailsValidator;

public class StudentService {
	private StudentDao studentDao = StudentDao.getstudentDaoInstance();
	
	// SERVICES
	public void addStudent(Scanner scanner) {
		Profile profile = getProfileDetails(scanner);
		if(profile == null) {
			System.out.println("Invalid Details");
			return;
		}
		Student student = getStudentDetails(scanner);
		studentDao.addStudent(profile, student);
	}
	
	
	// INPUTS
	private Student getStudentDetails(Scanner scanner) {
		System.out.println("Enter Roll Number: ");
		int rollNumber = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter Father's First Name: ");
		String fatherName = scanner.next();
		System.out.println("Enter Mother's First Name: ");
		String motherName = scanner.next();
		return new Student(rollNumber, fatherName, motherName);
	}
	
	private Profile getProfileDetails(Scanner scanner) {
		System.out.println("Enter First Name: ");
		String first_name = scanner.next();
		System.out.println("Enter Middle Name: ");
		String middle_name = scanner.next();
		System.out.println("Enter Last Name: ");
		String last_name = scanner.next();
		System.out.println("Enter Email: ");
		String email = scanner.next();
		System.out.println();
		GenderChoice[] choices = GenderChoice.values();
        for (int i = 0; i < choices.length; i++) {
            System.out.println((i + 1) + ". " + choices[i]);
        }
        System.out.println("Enter the choice: ");
        int genderChoice = scanner.nextInt();
        GenderChoice gender = choices[genderChoice - 1];
		System.out.println("Enter Contact Number: ");
		String contactNumber = scanner.next();
		scanner.nextLine();
		System.out.println("Enter Address: ");
		String address = scanner.nextLine();
		System.out.println("Enter City: ");
		String city = scanner.next();
		System.out.println("Enter State: ");
		String state = scanner.next();
		System.out.println("Enter Country: ");
		String country = scanner.next();
		System.out.println("Enter Emergency Contact Number: ");
		String emergencyContactNumber = scanner.next();
		System.out.println("Enter Disability Status(T/F): ");
		String disabilityStatus = scanner.next();
		boolean disability;
		disability = (disabilityStatus.equals("T")) ? true:false;
		System.out.println("Enter Blood Group: ");
		String bloodGroup = scanner.next();
		
		List<String> invalidFields = new ArrayList<>();

        if (!ProfileDetailsValidator.isValidName(first_name)) invalidFields.add("First Name");
        if (!ProfileDetailsValidator.isValidName(middle_name)) invalidFields.add("Middle Name");
        if (!ProfileDetailsValidator.isValidName(last_name)) invalidFields.add("Last Name");
        if (!ProfileDetailsValidator.isValidEmail(email)) invalidFields.add("Email");
        if (!ProfileDetailsValidator.isValidPhoneNumber(contactNumber)) invalidFields.add("Contact Number");
        if (!ProfileDetailsValidator.isValidAddress(address)) invalidFields.add("Address");
        if (!ProfileDetailsValidator.isValidLocation(city)) invalidFields.add("City");
        if (!ProfileDetailsValidator.isValidLocation(state)) invalidFields.add("State");
        if (!ProfileDetailsValidator.isValidLocation(country)) invalidFields.add("Country");
        if (!ProfileDetailsValidator.isValidPhoneNumber(emergencyContactNumber)) invalidFields.add("Emergency Contact Number");
        if (!ProfileDetailsValidator.isValidBloodGroup(bloodGroup)) invalidFields.add("Blood Group");

        // Output only field names
        if (!invalidFields.isEmpty()) {
        	System.out.println("Invalid fields:");
	        for (String field : invalidFields) {
	            System.out.println(field);
	        }
	        return null;
        } 
        boolean isStudent = true;
        boolean isTeacher = false;
        return new Profile(first_name, middle_name, last_name, email, gender, 
        		           contactNumber, address, city, state, country, bloodGroup, 
        		           disability, emergencyContactNumber, isStudent, isTeacher
        		);
	}
}
