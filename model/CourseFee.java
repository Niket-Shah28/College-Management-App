package com.aurionpro.model;

public class CourseFee {
	private int courseId;
	private String courseName;
	private double feeAmount;
	
	//CONSTRUCTORS
	public CourseFee(int courseId, String courseName, double feeAmount) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.feeAmount = feeAmount;
	}

	//GETTERS & SETTERS
	public int getCourseId() {
		return courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}
}
