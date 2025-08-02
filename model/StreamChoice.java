package com.aurionpro.model;

public enum StreamChoice {
    COMPUTER_SCIENCE,
    ELECTRICAL_ENGINEERING,
    MECHANICAL_ENGINEERING,
    CIVIL_ENGINEERING,
    BUSINESS_ADMINISTRATION,
    BIOLOGY,
    CHEMISTRY,
    PHYSICS,
    MATHEMATICS,
    ECONOMICS;

    public static StreamChoice stream(int input) {
        switch (input) {
            case 1: return COMPUTER_SCIENCE;
            case 2: return ELECTRICAL_ENGINEERING;
            case 3: return MECHANICAL_ENGINEERING;
            case 4: return CIVIL_ENGINEERING;
            case 5: return BUSINESS_ADMINISTRATION;
            case 6: return BIOLOGY;
            case 7: return CHEMISTRY;
            case 8: return PHYSICS;
            case 9: return MATHEMATICS;
            case 10: return ECONOMICS;
            default: throw new IllegalArgumentException("Invalid stream choice: " + input);
        }
    }
    
    public static StreamChoice stream(String stream) {
    	StreamChoice streamChoice = null;
    	try {
    		streamChoice  = StreamChoice.valueOf(stream);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid stream value in database: " + stream);
            // handle invalid value, e.g., set streamChoice to null or a default value
        }
		return streamChoice;
    	
    }
}

