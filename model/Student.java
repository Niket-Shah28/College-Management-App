package com.aurionpro.model;

public class Student {
	private int studentId;
	private int rollNumber;
	private String fatherName;
	private String motherName;
	
	public Student(int studentId, int rollNumber, String fatherName, String motherName) {
		super();
		this.studentId = studentId;
		this.rollNumber = rollNumber;
		this.fatherName = fatherName;
		this.motherName = motherName;
	}
	
	public Student(int rollNumber, String fatherName, String motherName) {
		super();
		this.rollNumber = rollNumber;
		this.fatherName = fatherName;
		this.motherName = motherName;
	}

	public int getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(int rollNumber) {
		this.rollNumber = rollNumber;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
}
