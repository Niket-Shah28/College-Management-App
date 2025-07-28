package com.aurionpro.model;

public class Profile {
	private int profileId;
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
	private boolean isStudent = false;
	private boolean isTeacher = false;
	
	// Constructors 
	public Profile(String firstName, String middleName, String lastName, String email, GenderChoice gender,
			String contactNumber, String address, String city, String state, String country, String bloodGroup,
			boolean disabilityStatus, String emergencyContactNumber, boolean isStudent, boolean isTeacher) {
		super();
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
		this.isStudent = isStudent;
		this.isTeacher = isTeacher;
	}
	
	
	public Profile(int profileId, String firstName, String middleName, String lastName, String email,
			GenderChoice gender, String contactNumber, String address, String city, String state, String country,
			String bloodGroup, boolean disabilityStatus, String emergencyContactNumber, boolean isStudent,
			boolean isTeacher) {
		super();
		this.profileId = profileId;
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
		this.isStudent = isStudent;
		this.isTeacher = isTeacher;
	}


	// Getters & Setters
	public String getFirst_name() {
		return firstName;
	}
	public void setFirst_name(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddle_name() {
		return middleName;
	}
	public void setMiddle_name(String middleName) {
		this.middleName = middleName;
	}
	public String getLast_name() {
		return lastName;
	}
	public void setLast_name(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public GenderChoice getGender() {
		return gender;
	}
	public void setGender(GenderChoice gender) {
		this.gender = gender;
	}
	public String getContact_number() {
		return contactNumber;
	}
	public void setContact_number(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public boolean getDisabilityStatus() {
		return disabilityStatus;
	}
	public void setDisabilityStatus(boolean disabilityStatus) {
		this.disabilityStatus = disabilityStatus;
	}
	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}
	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}
	public boolean isStudent() {
		return isStudent;
	}
	public void setStudent(boolean isStudent) {
		this.isStudent = isStudent;
	}
	public boolean isTeacher() {
		return isTeacher;
	}
	public void setTeacher(boolean isTeacher) {
		this.isTeacher = isTeacher;
	}
}
