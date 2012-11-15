package no.ruter.app.domain;

import no.ruter.app.enums.VehicleType;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

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
			DateTime expectedArrivalTime, DateTime timestamp,
			String platformName, VehicleType vehicleType) {
		super();
		this.line = line;
		this.destination = destination;
		this.expectedArrivalTime = expectedArrivalTime;
		this.timestamp = timestamp;
		this.platformName = platformName;
		this.vehicleType = vehicleType;
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
	 * Type methods
	 */

	/**
	 * Calculates the time between now and expected arrival time. Returns
	 * formatted time on the form "nå, 5 min or 15:45"
	 * 
	 * @return formatted arrival info
	 */
	public String getFormattedArrivalTime() {

		if (expectedArrivalTime.isBefore(DateTime.now().plusSeconds(45))) {
			return "nå";
		}

		if (expectedArrivalTime.isBefore(DateTime.now().plusMinutes(9).plusSeconds(59))) {
			Integer minutes = Minutes.minutesBetween(DateTime.now(),
					expectedArrivalTime).getMinutes();
			return minutes.toString() + " min";
		}

		return String.valueOf(expectedArrivalTime.getHourOfDay()) + ":"
				+ String.valueOf(expectedArrivalTime.getMinuteOfHour());
	}

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

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public String toString() {
		return "Linje " + line + "\nDestinasjon: " + destination;
	}
}