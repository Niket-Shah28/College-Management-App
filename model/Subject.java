package com.aurionpro.model;

public class Subject {
	private int subject_id;
	private String subject_name;
	
	// Constructors
	public Subject(int subject_id, String subject_name) {
		super();
		this.subject_id = subject_id;
		this.subject_name = subject_name;
	}
	
	public Subject(String subject_name) {
		super();
		this.subject_name = subject_name;
	}


	// Getters & Setters
	public String getSubject_name() {
		return subject_name;
	}
	public void setSubject_name(String subject_name) {
		this.subject_name = subject_name;
	}
}
