package com.aurionpro.service;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.dao.SubjectDao;
import com.aurionpro.model.StudentCourseSubject;
import com.aurionpro.model.Subject;
import com.aurionpro.model.SubjectCourseData;

public class SubjectService {
	private static SubjectService subjectService = null;
	private static SubjectDao subjectDao = SubjectDao.getSubjectDaoInstance();
	
	private SubjectService() {
	}
	
	public static SubjectService getSubjectServiceInstance() {
		if(subjectService == null) {
			subjectService = new SubjectService();
		}
		return subjectService;
	}
	
	//SERVICES
	public void addSubject(Scanner scanner) {
		System.out.println("Enter Subject Name: ");
		String subjectName = scanner.nextLine();
		
		if(subjectName.isBlank()) {
			System.out.println("Invalid Subject Name");
			return;
		}
		subjectDao.addSubject(new Subject(subjectName));
	}
	
	public void deleteSubject(Scanner scanner) {
		int subjectIdToBeDeleted = chooseSubject(scanner);
		
		if(subjectIdToBeDeleted == -1) {
			return;
		}
		
		subjectDao.deleteSubject(subjectIdToBeDeleted);
	}
	
	public void updateSubject(Scanner scanner) {
		int subjectIdToBeUpdated = chooseSubject(scanner);
		if(subjectIdToBeUpdated == -1) {
			return;
		}
		scanner.nextLine();
		System.out.println("Enter New Subject Name: ");
		String subjectName = scanner.nextLine();
		if(subjectName.isBlank()) {
			System.out.println("Invalid Subject Name");
			return;
		}
		subjectDao.updateSubject(new Subject(subjectIdToBeUpdated, subjectName));
	}
	
	public void viewAllSubjects() {
		List<Subject> subjects = subjectDao.getAllSubjects();
		displayAllSubject(subjects);
	}
	
	public void viewSubjectDetails(Scanner scanner) {
		int subjectId = chooseSubject(scanner);
		System.out.println();
		System.out.println();
        System.out.println("1. View Courses");
        System.out.println("2. View Students");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
            	List<SubjectCourseData> courses = subjectDao.getCourseDetailsForSubject(subjectId);
            	displaySubjectCourseData(courses);
                break;
            case 2:
            	List<StudentCourseSubject> students = subjectDao.getStudentsOptingParticularSubject(subjectId);
            	displayStudentCourseSubjects(students);
                break;
            default:
                System.out.println("Invalid choice.");
        }
	}
	
	//INPUTS
	public int chooseSubject(Scanner scanner) {
		
		List<Subject> subjects = subjectDao.getAllSubjects();
		
		if (subjects == null || subjects.isEmpty()) {
	        System.out.println("No subjects available.");
	        return -1;
	    }
		displayAllSubject(subjects);
		
	    while (true) {
	        System.out.print("Enter your choice (1 - " + subjects.size() + ") or 0 to cancel: ");

	        int choice;
	        try {
	            choice = scanner.nextInt();
	        } catch (Exception e) {
	            System.out.println("Invalid input. Please enter a number.");
	            scanner.nextLine(); 
	            continue;
	        }

	        if (choice == 0) {
	            System.out.println("Selection cancelled.");
	            return -1;
	        }

	        if (choice >= 1 && choice <= subjects.size()) {
	            return subjects.get(choice - 1).getSubjectId();
	        } else {
	            System.out.println("Invalid choice. Try again.");
	        }
	    }
	}
	
	//DISPLAY
	public void displaySubjectCourseData(List<SubjectCourseData> dataList) {
	    if (dataList == null || dataList.isEmpty()) {
	        System.out.println("No data to display.");
	        return;
	    }

	    System.out.printf("%-5s %-10s %-25s %-15s %-15s %-15s %-15s%n",
	                      "No.", "Course ID", "Course Name", "Academic Year", "From Year", "To Year", "Subject Type");
	    System.out.println("-----------------------------------------------------------------------------------------------");

	    int index = 1;
	    for (SubjectCourseData data : dataList) {
	        System.out.printf("%-5d %-10d %-25s %-15d %-15d %-15d %-15s%n",
	                          index++,
	                          data.getCourseId(),
	                          data.getCourseName(),
	                          data.getAcademicYear(),
	                          data.getFromYear(),
	                          data.getToYear(),
	                          data.getSubjectType().toString());
	    }
	}

	public void displayStudentCourseSubjects(List<StudentCourseSubject> dataList) {
	    if (dataList == null || dataList.isEmpty()) {
	        System.out.println("No student-course-subject data to display.");
	        return;
	    }

	    System.out.printf("%-5s %-10s %-10s %-20s %-10s %-20s %-10s %-25s %-15s %-15s%n",
	                      "No.", "Stud ID", "Roll No", "Student Name", "Crs ID", "Course Name",
	                      "Sub ID", "Subject Name", "Sub Type", "Academic Year");
	    System.out.println("------------------------------------------------------------------------------------------------------------------------------");

	    int index = 1;
	    for (StudentCourseSubject s : dataList) {
	        System.out.printf("%-5d %-10d %-10d %-20s %-10d %-20s %-10d %-25s %-15s %-15d%n",
	                          index++,
	                          s.getStudentId(),
	                          s.getRollNumber(),
	                          s.getName(),
	                          s.getCourseId(),
	                          s.getCourseName(),
	                          s.getSubjectId(),
	                          s.getSubjectName(),
	                          s.getSubjectType().name(),
	                          s.getAcademicYear());
	    }
	}
	
	public void displayAllSubject(List<Subject> subjects) {
	    System.out.println("\nSubjects:");
	    for (int i = 0; i < subjects.size(); i++) {
	        System.out.printf("%d. %s (ID: %d)%n", i + 1, subjects.get(i).getSubjectName(), subjects.get(i).getSubjectId());
	    }
	}
}
