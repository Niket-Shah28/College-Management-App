package com.aurionpro.model;

public class StudentCourseFees {
	private int studentId;
	private String name;
	private int rollNumber;
	private int courseId;
	private String courseName;
	private int duration;
	private String stream;
	private int feesId;
	private double amount;
	private double amountPaid;
	private boolean isPaid;
	
	//CONSTRUCTORS
	public StudentCourseFees(int studentId, String name, int rollNumber, int courseId, String courseName, int duration,
			String stream, int feesId, double amount, double amountPaid, boolean isPaid) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.rollNumber = rollNumber;
		this.courseId = courseId;
		this.courseName = courseName;
		this.duration = duration;
		this.stream = stream;
		this.feesId = feesId;
		this.amount = amount;
		this.amountPaid = amountPaid;
		this.isPaid = isPaid;
	}
	
	public StudentCourseFees(int courseId, String courseName, double amount, double amountPaid) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.amount = amount;
		this.amountPaid = amountPaid;
	}

	//GETTERS & SETTERS
	public int getStudentId() {
		return studentId;
	}
	public String getName() {
		return name;
	}
	public int getRollNumber() {
		return rollNumber;
	}
	public int getCourseId() {
		return courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public int getDuration() {
		return duration;
	}
	public String getStream() {
		return stream;
	}
	public int getFeesId() {
		return feesId;
	}
	public double getAmount() {
		return amount;
	}
	public double getAmountPaid() {
		return amountPaid;
	}
	public boolean isPaid() {
		return isPaid;
	}
}
