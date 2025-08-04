package com.aurionpro.model;

public class Subject {
	private int subjectId;
	private String subjectName;
	
	// Constructors
	public Subject(int subjectId, String subjectName) {
		super();
		this.subjectId = subjectId;
		this.subjectName = subjectName;
	}
	
	public Subject(String subjectName) {
		super();
		this.subjectName = subjectName;
	}


	// Getters & Setters
	
	public String getSubjectName() {
		return subjectName;
	}
	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
}
