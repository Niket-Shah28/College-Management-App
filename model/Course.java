package com.aurionpro.model;

public class Course {
	private int course_id;
	private String course_name;
	private int duration;
	private String stream;
	
	// Constructor
	public Course(int course_id, String course_name, int duration, String stream) {
		super();
		this.course_id = course_id;
		this.course_name = course_name;
		this.duration = duration;
		this.stream = stream;
	}
	
	// Getters & Setters
	public int getCourse_id() {
		return course_id;
	}
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
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
