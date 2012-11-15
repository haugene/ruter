package no.ruter.app.domain;

import no.ruter.app.enums.VehicleType;

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
	 * @param vehicleType 
	 * @param platformName 
	 */
	public RealTimeData(String line, String destination,
			DateTime expectedArrivalTime, DateTime timestamp, String platformName, VehicleType vehicleType) {
		super();
		this.line = line;
		this.destination = destination;
		this.expectedArrivalTime = expectedArrivalTime;
		this.timestamp = timestamp;
		this.setPlatformName(platformName);
		this.setVehicleType(vehicleType);
	}

	/*
	 * Member variables
	 */
	private String line;
	private String destination;
	private DateTime expectedArrivalTime;
	private DateTime timestamp;
	private String platformName;
	private VehicleType vehicleType;

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

    public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String toString() {
        return "Linje " + line + "\nDestinasjon: " + destination;
    }
}