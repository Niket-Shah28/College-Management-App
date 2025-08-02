package com.aurionpro.model;

public class SubjectCourseData {
	private int subjectCourseId;
	private int courseId;
	private String courseName;
	private int subjectId;
	private String subjectName;
	private int academicYear;
	private SubjectType subjectType;
	private int fromYear;
	private int toYear;
	
	public SubjectCourseData(int subjectCourseId, int courseId, String courseName, int subjectId, String subjectName, int academicYear,
			SubjectType subjectType, int fromYear, int toYear) {
		super();
		this.subjectCourseId = subjectCourseId;
		this.courseId = courseId;
		this.courseName = courseName;
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.academicYear = academicYear;
		this.subjectType = subjectType;
		this.fromYear = fromYear;
		this.toYear = toYear;
	}
	
	public int getSubjectCourseId() {
		return subjectCourseId;
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

	public int getAcademicYear() {
		return academicYear;
	}

	public String getSubjectType() {
		return subjectType.toString();
	}

	public int getFromYear() {
		return fromYear;
	}

	public int getToYear() {
		return toYear;
	}
}
