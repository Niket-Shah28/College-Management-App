package com.aurionpro.service;

import java.util.List;
import java.util.Scanner;

import com.aurionpro.dao.CourseDao;
import com.aurionpro.model.Course;
import com.aurionpro.model.StudentCourseSubject;
import com.aurionpro.model.Subject;
import com.aurionpro.model.SubjectCourseData;
import com.aurionpro.model.SubjectType;

public class CourseService {
	private static CourseService courseService = null;
	private CourseDao courseDao = CourseDao.getCourseDaoInstance();
	private CourseService() {
	}
	
	public static CourseService getCourseServiceInstance() {
		if(courseService == null) {
			courseService = new CourseService();
		}
		return courseService;
	}
	
	//SERVICES
	public void addCourse(Scanner scanner) {
		Course course = getCourseDetails(scanner);
		double courseFees;
        while(true) {
	        System.out.println("Enter Course Fees: ");
	        courseFees = scanner.nextDouble();
	        if(courseFees >= 0) {
	        	break;
	        }
	        System.out.println("Course Fees Must be greater than Zero");
        }
		courseDao.addCourse(course, courseFees);
	}
	
	public void deleteCourse(Scanner scanner) {
		int courseIdToBeDeleted = chooseCourse(scanner);
		courseDao.deleteCourse(courseIdToBeDeleted);
	}
	
	public void updateCourse(Scanner scanner) {
		int courseIdToBeUpdated = chooseCourse(scanner);
		Course course = courseDao.getParticularCourseDetails(courseIdToBeUpdated);
		if(course == null) {
			System.out.println("COURSE NOT FOUND");
	        return;
		}
		updateCourseDetails(course, scanner);
		courseDao.updateCourse(course);
	}
	
	public void addSubjectToCourse(Scanner scanner) {
		int courseId = chooseCourse(scanner);
		List<Subject> subjects = courseDao.getSubjectsNotInCourse(courseId);
		displaySubjects(subjects);
		System.out.println("Enter the Choice OR ENTER 0 to Exit: ");
		int choice = scanner.nextInt();
		
		if(choice == 0) {
			return;
		}
		if(choice < 0 || choice > subjects.size()) {
			System.out.println("Invalid Choice");
			return;
		}
		int subjectId = subjects.get(choice - 1).getSubjectId();
		System.out.println("Enter Starting Year: ");
		int fromYear = scanner.nextInt();
		System.out.println("Enter Ending Year: ");
		int toYear = scanner.nextInt();
		
		System.out.println("Enter the Academic Year: ");
		int academicYear = scanner.nextInt();
		
		SubjectType[] choices = SubjectType.values();
        for (int i = 0; i < choices.length; i++) {
            System.out.println((i + 1) + ". " + choices[i]);
        }
        System.out.println("Enter the choice: ");
        int subjectTypeChoice = scanner.nextInt();
        SubjectType subjectType = choices[subjectTypeChoice - 1];
        
        courseDao.addSubjectToCourse(new SubjectCourseData(subjectId, courseId, fromYear, toYear, academicYear, subjectType));
	}
	
	public void removeSubjectFromCourse(Scanner scanner) {
		int courseId = chooseCourse(scanner);
		List<SubjectCourseData> subjects = courseDao.getSubjectsInCourse(courseId);
		displaySubjectsWithCourse(subjects);
		System.out.println("Enter the Choice OR ENTER 0 to Exit: ");
		int choice = scanner.nextInt();
		
		if(choice == 0) {
			return;
		}
		if(choice < 0 || choice > subjects.size()) {
			System.out.println("Invalid Choice");
			return;
		}
		int subjectCourseId = subjects.get(choice - 1).getSubjectCourseId();
		courseDao.removeSubjectFromCourse(subjectCourseId);
		
	}
	
	public void viewCourseDetails(Scanner scanner) {
		int courseId = chooseCourse(scanner);
		System.out.println();
		System.out.println();
        System.out.println("1. View Students");
        System.out.println("2. View Subjects");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
            	List<StudentCourseSubject> students = courseDao.getStudentsInCourse(courseId);
            	displayStudentsInCourse(students);
                break;
            case 2:
            	List<SubjectCourseData> subjects = courseDao.getSubjectsInCourse(courseId);
            	displaySubjectsWithCourse(subjects);
                break;
            default:
                System.out.println("Invalid choice.");
        }
	}
	
	//INPUTS
	public void updateCourseDetails(Course course, Scanner scanner) {
	    while (true) {
	        System.out.println("\n--- Update Course Menu ---");
	        System.out.println("1. Update Course Name (Current: " + course.getCourseName() + ")");
	        System.out.println("2. Update Duration (Current: " + course.getDuration() + " months)");
	        System.out.println("3. Update Stream (Current: " + course.getStream() + ")");
	        System.out.println("4. Done / Exit");
	        System.out.print("Enter your choice: ");
	        String input = scanner.nextLine().trim();

	        switch (input) {
	            case "1":
	                while (true) {
	                    System.out.print("Enter new course name: ");
	                    String newName = scanner.nextLine().trim();
	                    if (!newName.isEmpty()) {
	                        course.setCourseName(newName);
	                        System.out.println("Course name updated successfully.");
	                        break;
	                    } else {
	                        System.out.println("Course name cannot be empty. Try again.");
	                    }
	                }
	                break;

	            case "2":
	                while (true) {
	                    System.out.print("Enter new duration in months: ");
	                    String durationInput = scanner.nextLine().trim();
	                    try {
	                        int newDuration = Integer.parseInt(durationInput);
	                        if (newDuration > 0) {
	                            course.setDuration(newDuration);
	                            System.out.println("Duration updated successfully.");
	                            break;
	                        } else {
	                            System.out.println("Duration must be greater than 0.");
	                        }
	                    } catch (NumberFormatException e) {
	                        System.out.println("Invalid input. Please enter a valid number.");
	                    }
	                }
	                break;

	            case "3":
	                while (true) {
	                    System.out.print("Enter new stream: ");
	                    String newStream = scanner.nextLine().trim();
	                    if (!newStream.isEmpty()) {
	                        course.setStream(newStream);
	                        System.out.println("Stream updated successfully.");
	                        break;
	                    } else {
	                        System.out.println("Stream cannot be empty. Try again.");
	                    }
	                }
	                break;

	            case "4":
	                System.out.println("Exiting course update menu.");
	                return;

	            default:
	                System.out.println("Invalid choice. Please select between 1 and 4.");
	        }
	    }
	}
	
	public int chooseCourse(Scanner scanner) {
		List<Course> courses = courseDao.getAllCourseDetails();
		if (courses == null || courses.isEmpty()) {
            System.out.println("No courses available.");
            return -1;
        }

        System.out.println("Available Courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.println(i + ". " + courses.get(i).getCourseName());
        }

        int selectedIndex = -1;

        while (true) {
            System.out.print("Enter the index of the course you want to select: ");
            if (scanner.hasNextInt()) {
                selectedIndex = scanner.nextInt();
                if (selectedIndex >= 0 && selectedIndex < courses.size()) {
                    break;
                } else {
                    System.out.println("Invalid index. Please select a number between 0 and " + (courses.size() - 1));
                }
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next();
            }
        }

        Course selectedCourse = courses.get(selectedIndex);
        return selectedCourse.getCourseId();
    }
		
	
	public Course getCourseDetails(Scanner scanner) {
		String courseName = "";
        int duration = 0;
        String stream = "";
        
        while (true) {
            System.out.print("Enter course name: ");
            courseName = scanner.nextLine();
            if (courseName != null && !courseName.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Course name cannot be empty. Please try again.");
            }
        }

        while (true) {
            System.out.print("Enter course duration (in Years): ");
            if (scanner.hasNextInt()) {
                duration = scanner.nextInt();
                if (duration > 0) {
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Duration must be greater than 0.");
                }
            } else {
                System.out.println("Please enter a valid number.");
                scanner.next(); 
            }
        }

        while (true) {
            System.out.print("Enter course stream: ");
            stream = scanner.nextLine();
            if (stream != null && !stream.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Stream cannot be empty. Please try again.");
            }
        }

        return new Course(courseName, duration, stream);
	}
	
	//DISPLAY
	public void displayStudentsInCourse(List<StudentCourseSubject> students) {
        System.out.printf("%-12s %-12s %-20s %-12s %-20s%n", 
                          "Student ID", "Roll No", "Name", "Course ID", "Course Name");
        System.out.println("-------------------------------------------------------------------------------------");
        for (StudentCourseSubject s : students) {
            System.out.printf("%-12d %-12d %-20s %-12d %-20s%n",
                              s.getStudentId(),
                              s.getRollNumber(),
                              s.getName(),
                              s.getCourseId(),
                              s.getCourseName());
        }
    }
	
	public static void displaySubjectsWithCourse(List<SubjectCourseData> subjects) {
	    System.out.printf("%-5s %-12s %-25s %-15s %-15s %-12s %-20s %-15s%n",
	                      "No.", "Subject ID", "Subject Name", "Subject Type",
	                      "Academic Year", "Course ID", "Course Name", "Course Duration");
	    System.out.println("----------------------------------------------------------------------------------------------------------------------------");

	    int index = 1;
	    for (SubjectCourseData s : subjects) {
	        System.out.printf("%-5d %-12d %-25s %-15s %-15d %-12d %-20s %-15s%n",
	                          index++,
	                          s.getSubjectId(),
	                          s.getSubjectName(),
	                          s.getSubjectType(),
	                          s.getAcademicYear(),
	                          s.getCourseId(),
	                          s.getCourseName(),
	                          s.getFromYear() + " - " + s.getToYear());
	    }
	}

	
	public void displaySubjects(List<Subject> subjects) {
	    System.out.printf("%-5s %-12s %-30s%n", "No.", "Subject ID", "Subject Name");
	    System.out.println("--------------------------------------------------");

	    int index = 1;
	    for (Subject s : subjects) {
	        System.out.printf("%-5d %-12d %-30s%n",
	                          index++,
	                          s.getSubjectId(),
	                          s.getSubjectName());
	    }
	}
}
