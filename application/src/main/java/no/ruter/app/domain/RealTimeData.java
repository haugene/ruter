package no.ruter.app.domain;

import org.joda.time.DateTime;

/**
 * Represents real time data, contains fields we want to utilize
 * 
 * @author Kristian
 * 
 */
public class RealTimeData {

	/**
	 * All variables constructor
	 * 
	 * @param line
	 * @param destination
	 * @param aimedArrivalTime
	 * @param expectedArrivalTime
	 * @param timestamp
	 */
	public RealTimeData(String line, String destination,
			DateTime expectedArrivalTime, DateTime timestamp) {
		super();
		this.line = line;
		this.destination = destination;
		this.expectedArrivalTime = expectedArrivalTime;
		this.timestamp = timestamp;
	}

	/*
	 * Member variables
	 */
	private String line;
	private String destination;
	private DateTime expectedArrivalTime;
	private DateTime timestamp;

	/*
	 * Getters and setters
	 */
	public String getLine() {
		return line;
	}

	public String getDestination() {
		return destination;
	}

	public DateTime getExpectedArrivalTime() {
		return expectedArrivalTime;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

    public String toString() {
        return "Linje " + line + "\nDestinasjon: " + destination;
    }
}