package no.ruter.app.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a platform, which has a list of departures
 * 
 * @author Kristian
 * 
 */
public class Platform implements Comparable<Platform>{

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
	
	/*
	 * Override equals. Two platforms are equal if their name is.
	 * In our cases, it's what we want. When do you compare platforms ;)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Platform other = (Platform) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * When we compare platforms, sort them alphabetically on name
	 */
	public int compareTo(Platform o) {
		return this.name.compareTo(o.name);
	}
}