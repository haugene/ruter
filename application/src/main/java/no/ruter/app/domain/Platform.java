package no.ruter.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a platform, which has a list of departures
 * 
 * @author Kristian
 * 
 */
public class Platform {

	/*
	 * Member variables
	 */
	private String name;
	private List<RealTimeData> departures;

	/**
	 * Parameterized constructor
	 * 
	 * @param name
	 *            platform name
	 */
	public Platform(String name) {
		this.name = name;
		departures = new ArrayList<RealTimeData>();
	}

	/*
	 * Getters and setters
	 */
	public String getName() {
		return name;
	}
	
	public List<RealTimeData> getDepartures() {
		return departures;
	}

}
