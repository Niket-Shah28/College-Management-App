package com.aurionpro.model;

import java.sql.Date;

public class CourseStudent {

    private int studentId;
    private int courseId;
    private String firstName;
    private String lastName;
    private String rollNo;
    private String courseName;
    private Date enrollmentDate;
    private boolean isActive;
    private String academicYear;
    
	public CourseStudent(int studentId, int courseId, String firstName, String lastName, String rollNo,
			String courseName, Date enrollmentDate, boolean isActive, String academicYear) {
		super();
		this.studentId = studentId;
		this.courseId = courseId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.rollNo = rollNo;
		this.courseName = courseName;
		this.enrollmentDate = enrollmentDate;
		this.isActive = isActive;
		this.academicYear = academicYear;
	}

	public CourseStudent() {
		super();
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Date getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	@Override
	public String toString() {
		return "CourseStudent [studentId=" + studentId + ", courseId=" + courseId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", rollNo=" + rollNo + ", courseName=" + courseName + ", enrollmentDate="
				+ enrollmentDate + ", isActive=" + isActive + ", academicYear=" + academicYear + "]";
	}
	
	
}
