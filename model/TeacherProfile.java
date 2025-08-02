package com.aurionpro.model;

public class TeacherProfile {
	private int teacherId;
	private String qualification;
	private int experience;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private GenderChoice gender;
	private String contactNumber;
	private String address;
	private String city;
	private String state;
	private String country;
	private String bloodGroup;
	private boolean disabilityStatus;
	private String emergencyContactNumber;
	
	public TeacherProfile(int teacherId, String qualification, int experience, String firstName, String middleName,
			String lastName, String email, GenderChoice gender, String contactNumber, String address, String city,
			String state, String country, String bloodGroup, boolean disabilityStatus, String emergencyContactNumber) {
		super();
		this.teacherId = teacherId;
		this.qualification = qualification;
		this.experience = experience;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.contactNumber = contactNumber;
		this.address = address;
		this.city = city;
		this.state = state;
		this.country = country;
		this.bloodGroup = bloodGroup;
		this.disabilityStatus = disabilityStatus;
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public String getQualification() {
		return qualification;
	}

	public int getExperience() {
		return experience;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public GenderChoice getGender() {
		return gender;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getCountry() {
		return country;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public boolean isDisabilityStatus() {
		return disabilityStatus;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}
}
