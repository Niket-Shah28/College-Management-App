package com.aurionpro.validator;

import java.util.regex.Pattern;

public class ProfileDetailsValidator {
	
	private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z]{1,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^.{3,100}$");
    private static final Pattern LOCATION_PATTERN = Pattern.compile("^[A-Za-z ]{1,50}$");
    private static final Pattern BLOOD_GROUP_PATTERN = Pattern.compile("^(A|B|AB|O)[+-]$");

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhoneNumber(String number) {
        return number != null && PHONE_PATTERN.matcher(number.trim()).matches();
    }

    public static boolean isValidAddress(String address) {
        return address != null && ADDRESS_PATTERN.matcher(address.trim()).matches();
    }

    public static boolean isValidLocation(String location) {
        return location != null && LOCATION_PATTERN.matcher(location.trim()).matches();
    }

    public static boolean isValidBloodGroup(String bloodGroup) {
        return bloodGroup != null && BLOOD_GROUP_PATTERN.matcher(bloodGroup.trim()).matches();
    }
}
