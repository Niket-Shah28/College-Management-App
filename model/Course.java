package com.aurionpro.model;

public class Course {
	private int courseId;
	private String courseName;
	private int duration;
	private String stream;
	
	// Constructor
	public Course(int courseId, String courseName, int duration, String stream) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.duration = duration;
		this.stream = stream;
	}
	
	public Course(int courseId, String courseName) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
	}

	public Course(String courseName, int duration, String stream) {
		super();
		this.courseName = courseName;
		this.duration = duration;
		this.stream = stream;
	}

	// Getters & Setters
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}	
}
