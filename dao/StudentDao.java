package com.aurionpro.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aurionpro.database.Database;
import com.aurionpro.model.GenderChoice;
import com.aurionpro.model.Profile;
import com.aurionpro.model.Student;
import com.aurionpro.model.StudentCourseSubject;
import com.aurionpro.model.StudentProfile;
import com.aurionpro.model.SubjectCourseData;
import com.aurionpro.model.SubjectType;

public class StudentDao {
	private static StudentDao studentDao = null;
	
	private StudentDao() {
	}
	
	public static StudentDao getStudentDaoInstance() {
		if(studentDao == null) {
			studentDao = new StudentDao();
		}
		return studentDao;
	}
	
	public void addStudent(Profile profile, Student student) {
		Connection connection = Database.connect();
		
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL insert_student_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// PROFILE DETAILS
			callableStatement.setString(1, profile.getFirst_name());
			callableStatement.setString(2, profile.getMiddle_name());
			callableStatement.setString(3, profile.getLast_name());
			callableStatement.setString(4, profile.getEmail());
			callableStatement.setString(5, profile.getGender().toString());
			callableStatement.setString(6, profile.getContact_number());
			callableStatement.setString(7, profile.getAddress());
			callableStatement.setString(8, profile.getCity());
			callableStatement.setString(9, profile.getState());
			callableStatement.setString(10, profile.getCountry());
			callableStatement.setString(11, profile.getBloodGroup());
			callableStatement.setBoolean(12, profile.getDisabilityStatus());
			callableStatement.setString(13, profile.getEmergencyContactNumber());
			callableStatement.setBoolean(14, profile.isStudent());
			callableStatement.setBoolean(15, profile.isTeacher());
			
			// STUDENT SPECEFIC DETAILS
			callableStatement.setInt(16, student.getRollNumber());
			callableStatement.setString(17, student.getFatherName());
			callableStatement.setString(18, student.getMotherName());
			
			// STATUS OUTPUT PARAMETERS
			callableStatement.registerOutParameter(19, Types.BOOLEAN); // INSERT STATUS (TRUE / FALSE)
			callableStatement.registerOutParameter(20, Types.VARCHAR); // MESSAGE IF ANY
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(19);
			String message = callableStatement.getString(20);
			
			if(isInserted) {
				System.out.println("Student Added Successfully !");
				return;
			}
			
			System.out.println("ERROR: "+message);
			return;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteStudent(int studentId) {
		Connection connection = Database.connect();
		
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL delete_student(?, ?, ?)}");
			
			callableStatement.setInt(1,  studentId);
			callableStatement.registerOutParameter(2, Types.BOOLEAN);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isDeleted = callableStatement.getBoolean(2);
			String message = callableStatement.getString(3);
			
			if(isDeleted) {
				System.out.println("Deleted Student Successfully");
				return;
			}
			System.out.println("Unable To Delete Student");
			System.out.println(message);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateStudentProfile(Profile studentProfile) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL update_profile_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// PROFILE DETAILS
			callableStatement.setInt(1, studentProfile.getProfileId());
			callableStatement.setString(2, studentProfile.getFirst_name());
			callableStatement.setString(3, studentProfile.getEmail());
			callableStatement.setString(4, studentProfile.getGender().toString());
			callableStatement.setString(5, studentProfile.getContact_number());
			callableStatement.setString(6, studentProfile.getAddress());
			callableStatement.setString(7, studentProfile.getCity());
			callableStatement.setString(8, studentProfile.getState());
			callableStatement.setString(9, studentProfile.getCountry());
			callableStatement.setBoolean(10, studentProfile.getDisabilityStatus());
			callableStatement.setString(11, studentProfile.getEmergencyContactNumber());
			
			callableStatement.registerOutParameter(12, Types.BOOLEAN);
			callableStatement.registerOutParameter(13, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isUpdated = callableStatement.getBoolean(12);
			String message = callableStatement.getString(13);
			
			if(isUpdated) {
				System.out.println("Updated Student Profile Successfully");
				return;
			}
			System.out.println("Unable To Update Student Profile");
			System.out.println(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Student> getStudentsNameAndId() {
		Connection connection = Database.connect();
		List<Student> students = new ArrayList<>();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT student_id, roll_no, CONCAT(first_name,' ', last_name) AS name FROM students_data_with_profile");
			
			while(resultSet.next()) {
				students.add(new Student(resultSet.getInt("student_id"), resultSet.getInt("roll_no"), resultSet.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}
	
	public Profile getStudentForUpdateProfile(int studentId) {
		Connection connection = Database.connect();
		Profile studentProfile = new Profile();
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT student_id, first_name,"
					+ "gender, email, contact_number, address, city, state, country, disability_status, emergency_contact_number"
					+ " FROM students_data_with_profile "
					+ "WHERE student_id = ?");
			preparedStatement.setInt(1, studentId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				studentProfile.setProfileId(resultSet.getInt("student_id"));
			    studentProfile.setFirstName(resultSet.getString("first_name"));
			    String genderStr = resultSet.getString("gender");
			    studentProfile.setGender(GenderChoice.valueOf(genderStr.toUpperCase()));
			    studentProfile.setEmail(resultSet.getString("email"));
			    studentProfile.setContactNumber(resultSet.getString("contact_number"));
			    studentProfile.setAddress(resultSet.getString("address"));
			    studentProfile.setCity(resultSet.getString("city"));
			    studentProfile.setState(resultSet.getString("state"));
			    studentProfile.setCountry(resultSet.getString("country"));
			    studentProfile.setDisabilityStatus(resultSet.getBoolean("disability_status"));
			    studentProfile.setEmergencyContactNumber(resultSet.getString("emergency_contact_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return studentProfile;
	}
	
	private Map<Integer, Map<String, Object>> computeStudentCourseObject(List<StudentCourseSubject> studentSubjectsData) {
		
		Map<Integer, Map<String, Object>> studentCourseObject = new HashMap<>();
		
		for(StudentCourseSubject studData: studentSubjectsData) {
			int studentId = studData.getStudentId();
			int courseId = studData.getCourseId();
			studentCourseObject.computeIfAbsent(studentId, k ->{
					Map<String, Object>studInfo = new HashMap<>();
					studInfo.put("name", studData.getName());
					studInfo.put("rollNumber", studData.getRollNumber());
					studInfo.put("course", new HashMap<Integer, Map<String,Object>>());
					return studInfo;
				}
			);
			
			@SuppressWarnings("unchecked")
			Map<Integer, Map<String, Object>> courseMap = (Map<Integer, Map<String, Object>>)studentCourseObject.get(studentId).get("course");
			
			courseMap.computeIfAbsent(courseId, k ->{
					Map<String, Object> courseInfo = new HashMap<>();
					courseInfo.put("courseName", studData.getCourseName());
					courseInfo.put("subject", new HashMap<Integer, Map<String, Object>>());
					return courseInfo;
				}
			);
			
			@SuppressWarnings("unchecked")
			Map<Integer, Map<String, Object>> subjectMap = (Map<Integer, Map<String, Object>>)courseMap.get(courseId).get("subject");
			
			Map<String, Object> subjectEntry = new HashMap<>();
			
			subjectEntry.put("subjectName", studData.getSubjectName());
			subjectEntry.put("subjectType", studData.getSubjectType().toString());
			subjectEntry.put("academicYear", studData.getAcademicYear());
			
			subjectMap.put(studData.getSubjectId(), subjectEntry);
		}
		return studentCourseObject;
	}
	
	public Map<Integer, Map<String, Object>> getStudentsCoursesAndSubjects() {
		Connection connection = Database.connect();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM students_opted_course_and_subjects");
			List<StudentCourseSubject> studentSubjectsData = new ArrayList<>();
			
			while(resultSet.next()) {
				studentSubjectsData.add(
					new StudentCourseSubject(
							resultSet.getInt("student_id"),
							resultSet.getInt("roll_no"),
							resultSet.getString("name"),
							resultSet.getInt("course_id"),
							resultSet.getString("course_name"),
							resultSet.getInt("subject_id"),
							resultSet.getString("subject_name"),
							SubjectType.valueOf(resultSet.getString("subject_type")),
							resultSet.getInt("academic_year")
					));
			}
			return computeStudentCourseObject(studentSubjectsData);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<Integer, Map<String, Object>> getParticularStudentsCoursesAndSubjects(int studentId){
		Connection connection = Database.connect();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM students_opted_course_and_subjects WHERE student_id = "+studentId);
			List<StudentCourseSubject> studentSubjectsData = new ArrayList<>();
			
			while(resultSet.next()) {
				studentSubjectsData.add(
					new StudentCourseSubject(
							resultSet.getInt("student_id"),
							resultSet.getInt("roll_no"),
							resultSet.getString("name"),
							resultSet.getInt("course_id"),
							resultSet.getString("course_name"),
							resultSet.getInt("subject_id"),
							resultSet.getString("subject_name"),
							SubjectType.valueOf(resultSet.getString("subject_type")),
							resultSet.getInt("academic_year")
					));
			}
			return computeStudentCourseObject(studentSubjectsData);
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<StudentProfile> getAllStudentsProfile() {
		List<StudentProfile> studentsProfile = new ArrayList<>();
		Connection connection = Database.connect();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM students_data_with_profile");
			while(resultSet.next()) {
				studentsProfile.add(
						new StudentProfile(
								resultSet.getInt("student_id"),
								resultSet.getInt("roll_no"),
								resultSet.getString("first_name"),
								resultSet.getString("middle_name"),
								resultSet.getString("last_name"),
								resultSet.getString("email"),
								GenderChoice.valueOf(resultSet.getString("gender")),
								resultSet.getString("contact_number"),
								resultSet.getString("address"),
								resultSet.getString("city"),
								resultSet.getString("state"),
								resultSet.getString("country"),
								resultSet.getString("blood_group"),
								resultSet.getBoolean("disability_status"),
								resultSet.getString("emergency_contact_number")
						)
				);
			}
			return studentsProfile;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public StudentProfile getParticularStudentProfile(int studentId) {
		StudentProfile studentProfile = null;
		Connection connection = Database.connect();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM students_data_with_profile WHERE student_id = "+studentId);
			while(resultSet.next()) {
				studentProfile =
						new StudentProfile(
								resultSet.getInt("student_id"),
								resultSet.getInt("roll_no"),
								resultSet.getString("first_name"),
								resultSet.getString("middle_name"),
								resultSet.getString("last_name"),
								resultSet.getString("email"),
								GenderChoice.valueOf(resultSet.getString("gender")),
								resultSet.getString("contact_number"),
								resultSet.getString("address"),
								resultSet.getString("city"),
								resultSet.getString("state"),
								resultSet.getString("country"),
								resultSet.getString("blood_group"),
								resultSet.getBoolean("disability_status"),
								resultSet.getString("emergency_contact_number")
						
								);
			}
			return studentProfile;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<Integer, List<SubjectCourseData>> getCoursesWithSubjects() {
		Connection connection = Database.connect();
		List<SubjectCourseData> subjectsWithCourses = new ArrayList<>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM courses_subjects_data");
			
			while(resultSet.next()) {
				subjectsWithCourses.add(
						new SubjectCourseData(resultSet.getInt("subject_course_id"), 
											  resultSet.getInt("course_id"), 
								              resultSet.getString("course_name"), 
								              resultSet.getInt("subject_id"), 
								              resultSet.getString("subject_name"), 
								              resultSet.getInt("academic_year"), 
								              SubjectType.valueOf(resultSet.getString("subject_type")), 
								              resultSet.getInt("from_year"), 
								              resultSet.getInt("to_year"))
				);
			}
			
			Map<Integer, List<SubjectCourseData>> subjectsInEachCourse = subjectsWithCourses.stream().collect(Collectors.groupingBy(SubjectCourseData::getCourseId));
			return subjectsInEachCourse;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void assignCourseToStudent(int studentId, List<Integer> subjectCourseIds, int courseId) {
		String subjectCourseIdConcatenated = subjectCourseIds.stream()
		        .map(String::valueOf)
		        .collect(Collectors.joining(","));
		
		Connection connection = Database.connect();
		try {
			System.out.println(subjectCourseIdConcatenated);
			CallableStatement callableStatement = connection.prepareCall("{CALL assign_course(?, ?, ?, ?, ?)}");
			callableStatement.setInt(1, studentId);
			callableStatement.setString(2, subjectCourseIdConcatenated);
			callableStatement.setInt(3, courseId);
			
			callableStatement.registerOutParameter(4, Types.BOOLEAN);
			callableStatement.registerOutParameter(5, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(4);
			String message = callableStatement.getString(5);
			
			if(isInserted) {
				System.out.println("Student Added To COurse Successfully !");
				return;
			}
			
			System.out.println("ERROR: "+message);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeStudentFromCourse(int studentId, int courseId) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL remove_student_from_course(?, ?, ?, ?)}");
			callableStatement.setInt(1, studentId);
			callableStatement.setInt(2, courseId);
			
			callableStatement.registerOutParameter(3, Types.BOOLEAN);
			callableStatement.registerOutParameter(4, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isRemoved = callableStatement.getBoolean(3);
			String message = callableStatement.getString(4);
			
			if(isRemoved) {
				System.out.println("Student Removed from the course Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
