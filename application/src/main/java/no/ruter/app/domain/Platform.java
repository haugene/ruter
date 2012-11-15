package no.ruter.app.domain;

/**
 * Represents a platform
 * 
 * @author Kristian
 * 
 */
public class Platform {

	/*
	 * Member variables
	 */
	private String name;

	/**
	 * Parameterized constructor
	 * 
	 * @param name
	 *            platform name
	 */
	public Platform(String name) {
		this.name = name;
	}

	/*
	 * Getters and setters
	 */
	public String getName() {
		return name;
	}

}
