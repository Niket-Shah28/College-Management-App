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
import com.aurionpro.model.SubjectCourseData;
import com.aurionpro.model.SubjectType;
import com.aurionpro.model.Teacher;
import com.aurionpro.model.TeacherProfile;
import com.aurionpro.model.TeacherSubjectCourseData;

public class TeacherDao {
	private static TeacherDao teacherDao = null;
	
	private TeacherDao() {
	}
	
	public static TeacherDao getTeacherDaoInstance() {
		if(teacherDao == null) {
			teacherDao = new TeacherDao();
		}
		return teacherDao;
	}
	
	public void addTeacher(Profile profile, Teacher teacher) {
		Connection connection = Database.connect();
		
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL insert_teacher_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
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
			
			// TEACHER SPECEFIC DETAILS
			callableStatement.setString(16, teacher.getQualification());
			callableStatement.setInt(17, teacher.getExperience());
			
			// STATUS OUTPUT PARAMETERS
			callableStatement.registerOutParameter(18, Types.BOOLEAN); // INSERT STATUS (TRUE / FALSE)
			callableStatement.registerOutParameter(19, Types.VARCHAR); // MESSAGE IF ANY
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(18);
			String message = callableStatement.getString(19);
			
			if(isInserted) {
				System.out.println("Teacher Added Successfully !");
				return;
			}
			
			System.out.println("ERROR: "+message);
			return;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateTeacherProfile(Profile teacherProfile) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL update_profile_details(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// PROFILE DETAILS
			callableStatement.setInt(1, teacherProfile.getProfileId());
			callableStatement.setString(2, teacherProfile.getFirst_name());
			callableStatement.setString(3, teacherProfile.getEmail());
			callableStatement.setString(4, teacherProfile.getGender().toString());
			callableStatement.setString(5, teacherProfile.getContact_number());
			callableStatement.setString(6, teacherProfile.getAddress());
			callableStatement.setString(7, teacherProfile.getCity());
			callableStatement.setString(8, teacherProfile.getState());
			callableStatement.setString(9, teacherProfile.getCountry());
			callableStatement.setBoolean(10, teacherProfile.getDisabilityStatus());
			callableStatement.setString(11, teacherProfile.getEmergencyContactNumber());
			
			callableStatement.registerOutParameter(12, Types.BOOLEAN);
			callableStatement.registerOutParameter(13, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isUpdated = callableStatement.getBoolean(12);
			String message = callableStatement.getString(13);
			
			if(isUpdated) {
				System.out.println("Updated Teacher Profile Successfully");
				return;
			}
			System.out.println("Unable To Update Teacher Profile");
			System.out.println(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTeacher(int teacherId) {
		Connection connection = Database.connect();
		
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL delete_teacher(?, ?, ?)}");
			
			callableStatement.setInt(1,  teacherId);
			callableStatement.registerOutParameter(2, Types.BOOLEAN);
			callableStatement.registerOutParameter(3, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isDeleted = callableStatement.getBoolean(2);
			String message = callableStatement.getString(3);
			
			if(isDeleted) {
				System.out.println("Deleted Teacher Successfully");
				return;
			}
			System.out.println("Unable To Delete Teacher");
			System.out.println(message);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Teacher> getTeacherNameAndId() {
		Connection connection = Database.connect();
		List<Teacher> teachers = new ArrayList<>();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT teacher_id, CONCAT(first_name,' ', last_name) AS name FROM teachers_data_with_profile");
			
			while(resultSet.next()) {
				teachers.add(new Teacher(resultSet.getInt("teacher_id"), resultSet.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teachers;
	}
	
	public Profile getTeacherForUpdateProfile(int teacherId) {
		Connection connection = Database.connect();
		Profile teacherProfile = new Profile();
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT teacher_id, first_name,"
					+ "gender, email, contact_number, address, city, state, country, disability_status, emergency_contact_number"
					+ " FROM teachers_data_with_profile "
					+ "WHERE teacher_id = ?");
			preparedStatement.setInt(1, teacherId);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				teacherProfile.setProfileId(resultSet.getInt("student_id"));
				teacherProfile.setFirstName(resultSet.getString("first_name"));
			    String genderStr = resultSet.getString("gender");
			    teacherProfile.setGender(GenderChoice.valueOf(genderStr.toUpperCase()));
			    teacherProfile.setEmail(resultSet.getString("email"));
			    teacherProfile.setContactNumber(resultSet.getString("contact_number"));
			    teacherProfile.setAddress(resultSet.getString("address"));
			    teacherProfile.setCity(resultSet.getString("city"));
			    teacherProfile.setState(resultSet.getString("state"));
			    teacherProfile.setCountry(resultSet.getString("country"));
			    teacherProfile.setDisabilityStatus(resultSet.getBoolean("disability_status"));
			    teacherProfile.setEmergencyContactNumber(resultSet.getString("emergency_contact_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return teacherProfile;
	}
	
	public List<TeacherProfile> getAllTeachersProfile() {
		List<TeacherProfile> teachersProfile = new ArrayList<>();
		Connection connection = Database.connect();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM teachers_data_with_profile");
			while(resultSet.next()) {
				teachersProfile.add(
						new TeacherProfile(
								resultSet.getInt("teacher_id"),
								resultSet.getString("qualification"),
								resultSet.getInt("experience"),
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
			return teachersProfile;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TeacherProfile getParticularTeacherProfile(int teacherId) {
		TeacherProfile teacherProfile = null;
		Connection connection = Database.connect();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM teachers_data_with_profile WHERE teacher_id = "+teacherId);
			while(resultSet.next()) {
				teacherProfile = 
						new TeacherProfile(
								resultSet.getInt("teacher_id"),
								resultSet.getString("qualification"),
								resultSet.getInt("experience"),
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
			return teacherProfile;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Map<Integer, Map<String, Object>> computeTeacherCourseSubjectObject(List<TeacherSubjectCourseData> teacherSubjectsData) {
		
		Map<Integer, Map<String, Object>> teacherSubjectObject = new HashMap<>();
		
		for(TeacherSubjectCourseData teacherData: teacherSubjectsData) {
			int teacherId = teacherData.getTeacherID();
			int courseId = teacherData.getCourseId();
			teacherSubjectObject.computeIfAbsent(teacherId, k ->{
					Map<String, Object>teacherInfo = new HashMap<>();
					teacherInfo.put("name", teacherData.getName());
					teacherInfo.put("course", new HashMap<Integer, Map<String,Object>>());
					return teacherInfo;
				}
			);
			
			@SuppressWarnings("unchecked")
			Map<Integer, Map<String, Object>> courseMap = (Map<Integer, Map<String, Object>>)teacherSubjectObject.get(teacherId).get("course");
			
			courseMap.computeIfAbsent(courseId, k ->{
					Map<String, Object> courseInfo = new HashMap<>();
					courseInfo.put("courseName", teacherData.getCourseName());
					courseInfo.put("subject", new HashMap<Integer, Map<String, Object>>());
					return courseInfo;
				}
			);
			
			@SuppressWarnings("unchecked")
			Map<Integer, Map<String, Object>> subjectMap = (Map<Integer, Map<String, Object>>)courseMap.get(courseId).get("subject");
			
			Map<String, Object> subjectEntry = new HashMap<>();
			
			subjectEntry.put("subjectName", teacherData.getSubjectName());
			subjectEntry.put("subjectType", teacherData.getSubjectType().toString());
			subjectEntry.put("academicYear", teacherData.getAcademicYear());
			
			subjectMap.put(teacherData.getSubjectId(), subjectEntry);
		}
		return teacherSubjectObject;
	}
	
	public Map<Integer, Map<String, Object>> getSubjectsTaughtByEachTeacher() {
		List<TeacherSubjectCourseData> teacherSubjectCourses = new ArrayList<>();
		Connection connection = Database.connect();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM teachers_teaching_subjects");
			
			while(resultSet.next()) {
				teacherSubjectCourses.add(
						new TeacherSubjectCourseData(
								resultSet.getInt("teacher_id"),
								resultSet.getString("name"),
								resultSet.getInt("course_id"),
								resultSet.getString("course_name"),
								resultSet.getInt("subject_id"),
								resultSet.getString("subject_name"),
								SubjectType.valueOf(resultSet.getString("subject_type")),
								resultSet.getInt("academic_year")			
						)
				);
			}
			return computeTeacherCourseSubjectObject(teacherSubjectCourses);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Map<Integer, Map<String, Object>> getSubjectsTaughtByParticularTeacher(int teacherId) {
		List<TeacherSubjectCourseData> teacherSubjectCourses = new ArrayList<>();
		Connection connection = Database.connect();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM teachers_teaching_subjects WHERE teacher_id = "+teacherId);
			
			while(resultSet.next()) {
				teacherSubjectCourses.add(
						new TeacherSubjectCourseData(
								resultSet.getInt("teacher_id"),
								resultSet.getString("name"),
								resultSet.getInt("course_id"),
								resultSet.getString("course_name"),
								resultSet.getInt("subject_id"),
								resultSet.getString("subject_name"),
								SubjectType.valueOf(resultSet.getString("subject_type")),
								resultSet.getInt("academic_year")			
						)
				);
			}
			return computeTeacherCourseSubjectObject(teacherSubjectCourses);
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
	
	public void assignTeacherToSubject(int teacherId, int subjectCourseId) {
		Connection connection = Database.connect();
		try {
			CallableStatement callableStatement = connection.prepareCall("{CALL assign_teaching_subject(?, ?, ?, ?)}");
			callableStatement.setInt(1, teacherId);
			callableStatement.setInt(2, subjectCourseId);
			
			callableStatement.registerOutParameter(3, Types.BOOLEAN);
			callableStatement.registerOutParameter(4, Types.VARCHAR);
			
			callableStatement.execute();
			
			boolean isInserted = callableStatement.getBoolean(3);
			String message = callableStatement.getString(4);
			
			if(isInserted) {
				System.out.println("Teacher Assigned To Course Successfully !");
				return;
			}
			System.out.println("ERROR: "+message);
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
