package com.aurionpro.model;

public class TeacherSubjectCourseData {
	private int teacherID;
	private String name;
	private int courseId;
	private String courseName;
	private int subjectId;
	private String subjectName;
	private SubjectType subjectType;
	private int academicYear;
	
	public TeacherSubjectCourseData(int teacherID, String name, int courseId, String courseName, int subjectId,
			String subjectName, SubjectType subjectType, int academicYear) {
		super();
		this.teacherID = teacherID;
		this.name = name;
		this.courseId = courseId;
		this.courseName = courseName;
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.subjectType = subjectType;
		this.academicYear = academicYear;
	}

	public int getTeacherID() {
		return teacherID;
	}

	public String getName() {
		return name;
	}

	public int getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public SubjectType getSubjectType() {
		return subjectType;
	}

	public int getAcademicYear() {
		return academicYear;
	}
}
