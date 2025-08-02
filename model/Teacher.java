package com.aurionpro.model;

public class Teacher {
	private int teacherId;
	private String qualification;
	private int experience;
	
	// ONLY USED WHEN FULL NAME OF STUDENT IS RETREIVED BY JOINING PROFILE TABLE
	private String name ;
	
	//CONSTRUCTORS
	public Teacher(int teacherId, String qualification, int experience) {
		super();
		this.teacherId = teacherId;
		this.qualification = qualification;
		this.experience = experience;
	}

	public Teacher(String qualification, int experience) {
		super();
		this.qualification = qualification;
		this.experience = experience;
	}
	
	// ONLY USED WHEN FULL NAME OF STUDENT IS RETREIVED BY JOINING PROFILE TABLE
	public Teacher(int studentId, String name) {
		this.teacherId = studentId;
		this.name = name;
	}

	//GETTERS & SETTERS
	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public String getName() {
		return name;
	}	
}
