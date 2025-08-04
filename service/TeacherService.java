package com.aurionpro.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.aurionpro.dao.TeacherDao;
import com.aurionpro.model.Course;
import com.aurionpro.model.GenderChoice;
import com.aurionpro.model.Profile;
import com.aurionpro.model.SubjectCourseData;
import com.aurionpro.model.Teacher;
import com.aurionpro.model.TeacherProfile;
import com.aurionpro.validator.ProfileDetailsValidator;

public class TeacherService {
	private TeacherDao teacherDao = TeacherDao.getTeacherDaoInstance();
	private static TeacherService teacherService = null;
		
	private TeacherService() {
	}
	
	public static TeacherService getTeacherServiceInstance() {
		if(teacherService == null) {
			teacherService = new TeacherService();
		}
		return teacherService;
	}
	
	//SERVICE
	
	public void addTeacher(Scanner scanner) {
		Profile profile = getProfileDetails(scanner);
		if(profile == null) {
			System.out.println("Invalid Details");
			return;
		}
		Teacher teacher = getTeacherDetails(scanner);
		teacherDao.addTeacher(profile, teacher);
	}
	
	public void deleteTeacher(Scanner scanner) {
		int teacherIdToBeDeleted = chooseTeacher(scanner);
		if(teacherIdToBeDeleted == 0) {
			return;
		}
		if(teacherIdToBeDeleted == -1) {
			System.out.println("Invalid Choice");
			return;
		}
		teacherDao.deleteTeacher(teacherIdToBeDeleted);
	}
	
	public void updateTeacherProfile(Scanner scanner) {
		int studentToBeUpdated = chooseTeacher(scanner);
		if(studentToBeUpdated == 0) {
			return;
		}
		Profile teacherProfile = teacherDao.getTeacherForUpdateProfile(studentToBeUpdated);
		updateProfile(scanner, teacherProfile);
		teacherDao.updateTeacherProfile(teacherProfile);
	}
	
	public void showAllTeachersProfile() {
		List<TeacherProfile> teachersProfile = teacherDao.getAllTeachersProfile();
		if(teachersProfile.isEmpty()){
			System.out.println("No Teachers in Database !");
			System.out.println();
			return;
		}
		displayTeacherProfilesTable(teachersProfile);
	}
	
	public void getParticularTeacherProfile(Scanner scanner) {
		int teacherId = chooseTeacher(scanner);
		if(teacherId == 0) {
			return;
		}
		
		TeacherProfile teacherProfile = teacherDao.getParticularTeacherProfile(teacherId);
		displayTeacherProfilesTable(List.of(teacherProfile));
	}
	
	public void getTeachersDataWithCourses() {
		Map<Integer, Map<String, Object>> teachersDataWithCourses = teacherDao.getSubjectsTaughtByEachTeacher();
		displayTeacherCourseData(teachersDataWithCourses);
	}
	
	public void getParticularTeacherCourseData(Scanner scanner) {
		int teacherId = chooseTeacher(scanner);
		Map<Integer, Map<String, Object>> teacherDataWithCourses = teacherDao.getSubjectsTaughtByParticularTeacher(teacherId);
		displayTeacherCourseData(teacherDataWithCourses);
	}
	
	public void assignTeacherToSubject(Scanner scanner) {
		int teacherId = chooseTeacher(scanner);
		if(teacherId == 0) {
			return;
		}
		Map<Integer, List<SubjectCourseData>> subjectsInEachCourse = teacherDao.getCoursesWithSubjects();
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
		
		int subjectCourseId = selectSubject(scanner, subjectsInSelectedCourse);
		teacherDao.assignTeacherToSubject(teacherId, subjectCourseId);
	}
	
	
	
	//INPUTS
	
	public int selectSubject(Scanner scanner, List<SubjectCourseData> selectedSubjects) {
	    // Display all subjects in a formatted table
	    String format = "| %-3s | %-25s | %-10s | %-14s | %-10s | %-10s |\n";
	    String separator = String.join("", Collections.nCopies(90, "-"));

	    System.out.println("\nAvailable Subjects:");
	    System.out.println(separator);
	    System.out.printf(format, "No", "Subject Name", "Type", "Academic Year", "From Year", "To Year");
	    System.out.println(separator);

	    for (int i = 0; i < selectedSubjects.size(); i++) {
	        SubjectCourseData s = selectedSubjects.get(i);
	        System.out.printf(format,
	                i+1,
	                s.getSubjectName(),
	                s.getSubjectType(),
	                s.getAcademicYear(),
	                s.getFromYear(),
	                s.getToYear());
	    }

	    System.out.println(separator);
	    System.out.print("Enter the subject you want to choose: ");

	    int choice = -1;
	    while (true) {
	        try {
	            choice = scanner.nextInt();
	            if (choice >= 0 && choice < selectedSubjects.size()) {
	                break;
	            } else {
	                System.out.print("Invalid choice. Please enter a valid number from the list: ");
	            }
	        } catch (InputMismatchException e) {
	            System.out.print("Invalid input. Enter a number: ");
	            scanner.next(); // clear invalid input
	        }
	    }

	    return selectedSubjects.get(choice-1).getSubjectCourseId();
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
	
	private int chooseTeacher(Scanner scanner) {
		List<Teacher> teachers = teacherDao.getTeacherNameAndId();
		if(teachers.isEmpty()) {
			System.out.println("No Teachers Present In Database");
			System.out.println();
			return 0;
		}

		System.out.printf("%-5s %-15s %-30s%n", "No.", "Teacher ID", "Name");
		System.out.println("--------------------------------------------------------");

		for (int i = 0; i < teachers.size(); i++) {
		    Teacher teacher = teachers.get(i);
		    System.out.printf("%-5d %-15d %-30s%n", 
		        (i + 1),
		        teacher.getTeacherId(), 
		        teacher.getName());
		}
		System.out.println("Enter Your Choice: ");
		int choice = scanner.nextInt();
		if(choice > 0 && choice <= teachers.size()) {
			return teachers.get(choice - 1).getTeacherId();
		}
		return -1;
	}
	
	private Teacher getTeacherDetails(Scanner scanner) {
		scanner.nextLine();
		System.out.println("Enter Qualification of Teacher: ");
		String qualification = scanner.nextLine();
		System.out.println("Enter Experience: ");
		int experience = scanner.nextInt();
		return new Teacher(qualification, experience);
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
        boolean isStudent = false;
        boolean isTeacher = true;
        return new Profile(first_name, middle_name, last_name, email, gender, 
        		           contactNumber, address, city, state, country, bloodGroup, 
        		           disability, emergencyContactNumber, isStudent, isTeacher
        );
	}
	
	//DISPLAY
	
	public void displayTeacherCourseData(Map<Integer, Map<String, Object>> teachersDataMap) {
	    for (Map.Entry<Integer, Map<String, Object>> teacherEntry : teachersDataMap.entrySet()) {
	        Map<String, Object> teacherInfo = teacherEntry.getValue();

	        System.out.println("TEACHER NAME: " + teacherInfo.get("name"));

	        @SuppressWarnings("unchecked")
			Map<Integer, Map<String, Object>> courseMap =
	            (Map<Integer, Map<String, Object>>) teacherInfo.get("course");

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
	
	public void displayTeacherProfilesTable(List<TeacherProfile> teachers) {
	    // Header format
	    String format = "| %-5s | %-15s | %-10s | %-12s | %-12s | %-12s | %-25s | %-6s | %-15s | %-12s | %-12s | %-12s | %-10s | %-8s | %-10s | %-18s |\n";
	    String separator = String.join("", Collections.nCopies(210, "-"));

	    // Print header
	    System.out.println(separator);
	    System.out.printf(format,
	        "ID", "Qualification", "Exp(Yrs)", "First Name", "Middle Name", "Last Name", "Email",
	        "Gender", "Contact No", "Address", "City", "State", "Country", "Blood", "Disabled", "Emergency Contact");
	    System.out.println(separator);

	    // Print teacher data
	    for (TeacherProfile t : teachers) {
	        System.out.printf(format,
	            t.getTeacherId(),
	            safe(t.getQualification(), 15),
	            t.getExperience(),
	            safe(t.getFirstName(), 12),
	            safe(t.getMiddleName(), 12),
	            safe(t.getLastName(), 12),
	            safe(t.getEmail(), 25),
	            t.getGender() != null ? t.getGender().toString() : "",
	            safe(t.getContactNumber(), 15),
	            safe(t.getAddress(), 12),
	            safe(t.getCity(), 12),
	            safe(t.getState(), 12),
	            safe(t.getCountry(), 10),
	            safe(t.getBloodGroup(), 8),
	            t.isDisabilityStatus() ? "Yes" : "No",
	            safe(t.getEmergencyContactNumber(), 18)
	        );
	    }

	    System.out.println(separator);
	}

	
	private String safe(String value, int maxLength) {
	    if (value == null) return "";
	    return value.length() > maxLength ? value.substring(0, maxLength - 1) + "…" : value;
	}
}
