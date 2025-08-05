package com.aurionpro.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.aurionpro.dao.StudentDao;
import com.aurionpro.model.Course;
import com.aurionpro.model.GenderChoice;
import com.aurionpro.model.Profile;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentProfile;
import com.aurionpro.model.SubjectCourseData;
import com.aurionpro.model.SubjectType;
import com.aurionpro.validator.ProfileDetailsValidator;

public class StudentService {
	private StudentDao studentDao = StudentDao.getStudentDaoInstance();
	private static StudentService studentService = null;
		
	private StudentService() {
	}
	
	public static StudentService getstudentServiceInstance() {
		if(studentService == null) {
			studentService = new StudentService();
		}
		return studentService;
	}
	
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
	
	public void deleteStudent(Scanner scanner) {
		int studentIdToBeDeleted = chooseStudent(scanner);
		if(studentIdToBeDeleted == 0) {
			return;
		}
		if(studentIdToBeDeleted == -1) {
			System.out.println("Invalid Choice");
			return;
		}
		studentDao.deleteStudent(studentIdToBeDeleted);
	}
	
	public void updateStudentProfile(Scanner scanner) {
		int studentToBeUpdated = chooseStudent(scanner);
		if(studentToBeUpdated == 0) {
			return;
		}
		Profile studentProfile = studentDao.getStudentForUpdateProfile(studentToBeUpdated);
		updateProfile(scanner, studentProfile);
		studentDao.updateStudentProfile(studentProfile);
	}
	
	public void getStudentsDataWithCourses() {
		Map<Integer, Map<String, Object>> studentsDataWithCourses = studentDao.getStudentsCoursesAndSubjects();
		displayStudentCourseData(studentsDataWithCourses);
	}
	
	public void getParticularStudentCourseData(Scanner scanner) {
		int studentId = chooseStudent(scanner);
		Map<Integer, Map<String, Object>> studentDataWithCourses = studentDao.getParticularStudentsCoursesAndSubjects(studentId);
		displayStudentCourseData(studentDataWithCourses);
	}
	
	public void showAllStudentsProfile() {
		List<StudentProfile> studentsProfile = studentDao.getAllStudentsProfile();
		if(studentsProfile.isEmpty()){
			System.out.println("No Students in Database !");
			System.out.println();
			return;
		}
		displayStudentProfilesTable(studentsProfile);
	}
	
	public void getParticularStudentProfile(Scanner scanner) {
		int studentId = chooseStudent(scanner);
		if(studentId == 0) {
			return;
		}
		StudentProfile studentProfile = studentDao.getParticularStudentProfile(studentId);
		displayStudentProfilesTable(List.of(studentProfile));
	}
	
	public void assignCourseToStudent(Scanner scanner) {
		int studentId = chooseStudent(scanner);
		if(studentId == 0) {
			return;
		}
		Map<Integer, List<SubjectCourseData>> subjectsInEachCourse = studentDao.getCoursesWithSubjects();
		List<Course> courseList = new ArrayList<>();
		for (Map.Entry<Integer, List<SubjectCourseData>> entry : subjectsInEachCourse.entrySet()) {
		    int courseId = entry.getKey();
		    String courseName = entry.getValue().get(0).getCourseName(); 
		    courseList.add(new Course(courseId, courseName));
		}
		int courseId = selectCourse(scanner, courseList);
		List<SubjectCourseData> subjectsInSelectedCourse = subjectsInEachCourse.get(courseId);
		if(subjectsInSelectedCourse == null) {
			System.out.println("Invalid Course Id");
			return;
		}
		
		List<Integer> subjectCourseIds = selectSubjects(scanner, subjectsInSelectedCourse);
		studentDao.assignCourseToStudent(studentId, subjectCourseIds, courseId);
	}
	
	public void removeStudentFromCourse(Scanner scanner) {
		int studentId = chooseStudent(scanner);
		Map<Integer, Map<String, Object>> studentDataWithCourses = studentDao.getParticularStudentsCoursesAndSubjects(studentId);
		List<Course> studentCourses = extractCoursesFromStudentObject(studentDataWithCourses);
		int courseId = selectCourse(scanner, studentCourses);
		studentDao.removeStudentFromCourse(studentId,  courseId);
	}
	
	
	// INPUTS
	
	public List<Course> extractCoursesFromStudentObject(Map<Integer, Map<String, Object>> studentCourseObject) {
	    Map<Integer, Course> uniqueCourses = new LinkedHashMap<>();

	    for (Map<String, Object> studentData : studentCourseObject.values()) {
	        @SuppressWarnings("unchecked")
			Map<Integer, Map<String, Object>> courses = (Map<Integer, Map<String, Object>>) studentData.get("course");

	        if (courses != null) {
	            for (Map.Entry<Integer, Map<String, Object>> courseEntry : courses.entrySet()) {
	                int courseId = courseEntry.getKey();

	                // Avoid duplicates
	                if (!uniqueCourses.containsKey(courseId)) {
	                    Map<String, Object> courseData = courseEntry.getValue();
	                    String courseName = (String) courseData.get("courseName");

	                    // You can add duration and stream if needed and available
	                    Course course = new Course(courseId, courseName);
	                    uniqueCourses.put(courseId, course);
	                }
	            }
	        }
	    }

	    return new ArrayList<>(uniqueCourses.values());
	}
	
	public int selectCourse(Scanner scanner, List<Course> courseList) {
	    System.out.println("Available Courses:");
	    for (Course course : courseList) {
	        System.out.println(course.getCourseId() + ": " + course.getCourseName());
	    }

	    System.out.print("Enter course ID to select: ");
	    int selectedCourseId = scanner.nextInt();

	    boolean exists = courseList.stream()
	            .anyMatch(c -> c.getCourseId() == selectedCourseId);

	    if (!exists) {
	        System.out.println("Invalid course ID.");
	        return -1;
	    }

	    return selectedCourseId;
	}


	
	public List<Integer> selectSubjects(Scanner scanner, List<SubjectCourseData> selectedSubjects) {
	    List<SubjectCourseData> mandatorySubjects = selectedSubjects.stream()
	            .filter(s -> s.getSubjectType().equals(SubjectType.MANDATORY.toString()))
	            .collect(Collectors.toList());

	    System.out.println("\nMandatory Subjects:");
	    for (SubjectCourseData s : mandatorySubjects) {
	        System.out.println("- " + s.getSubjectName() + " (ID: " + s.getSubjectId() + ")");
	    }

	    List<SubjectCourseData> optionalSubjects = selectedSubjects.stream()
	            .filter(s -> s.getSubjectType().equals(SubjectType.OPTIONAL.toString()))
	            .collect(Collectors.toList());

	    System.out.println("\nOptional Subjects (choose only one):");
	    for (int i = 0; i < optionalSubjects.size(); i++) {
	        SubjectCourseData s = optionalSubjects.get(i);
	        System.out.println(i + ": " + s.getSubjectName() + " (ID: " + s.getSubjectId() + ")");
	    }

	    int optionalChoice = -1;
	    if (!optionalSubjects.isEmpty()) {
	        System.out.print("\nEnter the number of the optional subject you want to choose (only one): ");
	        while (true) {
	            try {
	                optionalChoice = scanner.nextInt();
	                if (optionalChoice >= 0 && optionalChoice < optionalSubjects.size()) {
	                    break;
	                } else {
	                    System.out.print("Invalid choice. Please enter a number from the list: ");
	                }
	            } catch (InputMismatchException e) {
	                System.out.print("Invalid input. Enter a valid number: ");
	                scanner.next(); // consume bad input
	            }
	        }
	    }

	    List<Integer> subjectCourseIds = new ArrayList<>();
	    for (SubjectCourseData s : mandatorySubjects) {
	        subjectCourseIds.add(s.getSubjectCourseId());
	    }
	    if (optionalChoice != -1) {
	        subjectCourseIds.add(optionalSubjects.get(optionalChoice).getSubjectCourseId());
	    }

	    return subjectCourseIds;
	}

	public int chooseStudent(Scanner scanner) {
		List<Student> students = studentDao.getStudentsNameAndId();
		if(students.isEmpty()) {
			System.out.println("No Students Present In Database");
			System.out.println();
			return 0;
		}

		System.out.printf("%-5s %-15s %-30s%n", "No.", "Roll Number", "Name");
		System.out.println("--------------------------------------------------------");

		for (int i = 0; i < students.size(); i++) {
		    Student student = students.get(i);
		    System.out.printf("%-5d %-15s %-30s%n", (i + 1), student.getRollNumber(), student.getName());
		}
		System.out.println("Enter Your Choice: ");
		int choice = scanner.nextInt();
		if(choice > 0 && choice <= students.size()) {
			return students.get(choice - 1).getStudentId();
		}
		return -1;
	}
	
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
	
	private void showProfileUpdatableFields() {
		System.out.println();
		System.out.println("1. First Name\n2. Email\n3. Gender\n"
				+ "4. Contact Number\n5. Address\n6. City\n7. State\n8. Country\n"
				+ "9. Emergency Contact Number\n10. Disability Status\n11. Exit");
		System.out.println("Enter Your Choice: ");
	}
	
	private void updateProfile(Scanner scanner, Profile studentProfile) {
		outerWhile:
		while(true) {
			showProfileUpdatableFields();
			int choice = scanner.nextInt();
			scanner.nextLine();
			switch(choice) {
				case 1:
					System.out.println("Enter New First Name: ");
					String first_name = scanner.next();
					if(!ProfileDetailsValidator.isValidName(first_name)) {
						System.out.println("Invalid Name");
						break;
					}
					studentProfile.setFirst_name(first_name);
					break;
				case 2:
					System.out.println("Enter Email: ");
					String email = scanner.next();
					if(!ProfileDetailsValidator.isValidEmail(email)) {
						System.out.println("Invalid Email");
						break;
					}
					studentProfile.setEmail(email);
					break;
				case 3:
					GenderChoice[] choices = GenderChoice.values();
			        for (int i = 0; i < choices.length; i++) {
			            System.out.println((i + 1) + ". " + choices[i]);
			        }
			        System.out.println("Enter the choice: ");
			        int genderChoice = scanner.nextInt();
			        studentProfile.setGender(choices[genderChoice - 1]);
			        break;
				case 4:
					System.out.println("Enter New Contact Number: ");
					String contactNumber = scanner.next();
					if (!ProfileDetailsValidator.isValidPhoneNumber(contactNumber)) {
						System.out.println("Invalid Number");
						break;
					}
					studentProfile.setContact_number(contactNumber);
					break;
				case 5:
					System.out.println("Enter New Address: ");
					String address = scanner.nextLine();
					if (!ProfileDetailsValidator.isValidAddress(address)) {
						System.out.println("Invalid Address");
						break;
					}
					studentProfile.setAddress(address);
					break;
				case 6:
					System.out.println("Enter City: ");
					String city = scanner.next();
					if (!ProfileDetailsValidator.isValidLocation(city)) {
						System.out.println("Invalid City");
						break;
					}
					studentProfile.setCity(city);
					break;
				case 7:
					System.out.println("Enter State: ");
					String state = scanner.next();
					if (!ProfileDetailsValidator.isValidLocation(state)) {
						System.out.println("Invalid State");
						break;
					}
					studentProfile.setState(state);
					break;
				case 8:
					System.out.println("Enter Country: ");
					String country = scanner.next();
					if (!ProfileDetailsValidator.isValidLocation(country)) {
						System.out.println("Invalid Country");
						break;
					}
					studentProfile.setCountry(country);
					break;
				case 9:
					System.out.println("Enter Emergency Contact Number: ");
					String emergencyContactNumber = scanner.next();
					if (!ProfileDetailsValidator.isValidPhoneNumber(emergencyContactNumber)) {
						System.out.println("Invalid Number");
						break;
					}
					studentProfile.setEmergencyContactNumber(emergencyContactNumber);
					break;
				case 10:
					System.out.println("Enter Disability Status(T/F): ");
					String disabilityStatus = scanner.next();
					studentProfile.setDisabilityStatus(disabilityStatus.equals("T") ? true:false);
					break;
				case 11:
					break outerWhile;
				default:
					System.out.println("Invalid Choice");
			}
		}
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
	
	//DISPLAY
	public void displayStudentCourseData(Map<Integer, Map<String, Object>> studentDataMap) {
		if(studentDataMap == null || studentDataMap.isEmpty()) {
			System.out.println("No Courses Assigned to Student");
		}
	    for (Map.Entry<Integer, Map<String, Object>> studentEntry : studentDataMap.entrySet()) {
	        Map<String, Object> studentInfo = studentEntry.getValue();

	        System.out.println("STUDENT NAME: " + studentInfo.get("name"));
	        System.out.println("ROLL NUMBER: " + studentInfo.get("rollNumber"));

	        @SuppressWarnings("unchecked")
			Map<Integer, Map<String, Object>> courseMap =
	            (Map<Integer, Map<String, Object>>) studentInfo.get("course");

	        for (Map.Entry<Integer, Map<String, Object>> courseEntry : courseMap.entrySet()) {
	            Map<String, Object> courseInfo = courseEntry.getValue();

	            System.out.println("    COURSE: " + courseInfo.get("courseName"));

	            @SuppressWarnings("unchecked")
				Map<Integer, Map<String, Object>> subjectMap =
	                (Map<Integer, Map<String, Object>>) courseInfo.get("subject");

	            for (Map<String, Object> subjectInfo : subjectMap.values()) {
	                System.out.println("        Subject Name: " + subjectInfo.get("subjectName"));
	                System.out.println("        Subject Type: " + subjectInfo.get("subjectType"));
	                System.out.println("        Academic Year: " + subjectInfo.get("academicYear"));
	                System.out.println();
	            }
	        }

	        System.out.println("--------------------------------------------------");
	    }
	}
	
	public void displayStudentProfilesTable(List<StudentProfile> students) {
	    // Header
	    String format = "| %-5s | %-12s | %-12s | %-12s | %-25s | %-6s | %-10s | %-15s | %-12s | %-12s | %-12s | %-10s | %-8s | %-18s |\n";
	    String separator = String.join("", Collections.nCopies(195, "-"));

	    System.out.println(separator);
	    System.out.printf(format,
	        "ID", "First Name", "Middle Name", "Last Name", "Email",
	        "Gender", "Roll No", "Contact No", "Address", "City",
	        "State", "Country", "Blood", "Emergency Contact");
	    System.out.println(separator);

	    for (StudentProfile s : students) {
	        System.out.printf(format,
	            s.getStudentId(),
	            safe(s.getFirstName(), 12),
	            safe(s.getMiddleName(), 12),
	            safe(s.getLastName(), 12),
	            safe(s.getEmail(), 25),
	            s.getGender() != null ? s.getGender().toString() : "",
	            s.getRollNumber(),
	            safe(s.getContactNumber(), 15),
	            safe(s.getAddress(), 12),
	            safe(s.getCity(), 12),
	            safe(s.getState(), 12),
	            safe(s.getCountry(), 10),
	            safe(s.getBloodGroup(), 8),
	            safe(s.getEmergencyContactNumber(), 18)
	        );
	    }

	    System.out.println(separator);
	}

	private String safe(String value, int maxLength) {
	    if (value == null) return "";
	    return value.length() > maxLength ? value.substring(0, maxLength - 1) + "…" : value;
	}
}
