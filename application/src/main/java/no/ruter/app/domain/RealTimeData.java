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
	 * @param expectedDepartureTime
	 * @param timestamp
	 * @param vehicleType
	 * @param platformName
	 */
	public RealTimeData(String line, String destination,
			DateTime expectedDepartureTime, DateTime timestamp,
			String platformName, VehicleType vehicleType) {
		super();
		this.line = line.trim();
		this.destination = destination.trim();
		this.expectedDepartureTime = expectedDepartureTime;
		this.timestamp = timestamp;
		this.platformName = platformName.trim();
		this.vehicleType = vehicleType;
	}

	/*
	 * Member variables
	 */
	private String line;
	private String destination;

	private DateTime expectedDepartureTime;
	private DateTime timestamp;
	private String platformName;
	private VehicleType vehicleType;

	/*
	 * Type methods
	 */

	/**
	 * Calculates the time between now and expected departure time. Returns
	 * formatted time on the form "nå, 5 min or 15:45"
	 * 
	 * @return formatted departure info
	 */
	public String getFormattedDepartureTime() {
		
		if(expectedDepartureTime.isBefore(DateTime.now().minusMinutes(1))){
			return "-1";
		}

		if (expectedDepartureTime.isBefore(DateTime.now().plusSeconds(45))) {
			return "nå";
		}

		if (expectedDepartureTime.isBefore(DateTime.now().plusMinutes(9)
				.plusSeconds(59))) {
			Integer minutes = Minutes.minutesBetween(DateTime.now(),
					expectedDepartureTime).getMinutes();
			
			// Never display 0 min, wait for "nå" at 45 sec before departure
			int minutesNotZero = Math.max(minutes, 1);
			
			return minutesNotZero + " min";
		}

		return zeroPad(String.valueOf(expectedDepartureTime.getHourOfDay()), 2) + ":"
				+ zeroPad(String.valueOf(expectedDepartureTime.getMinuteOfHour()), 2);
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

	public DateTime getExpectedDepartureTime() {
		return expectedDepartureTime;
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

	private String zeroPad(String input, int targetLength) {

		// Check input
		if (targetLength <= input.length()) {
			return input;
		}

		String result = input;
		int currentLength = input.length();
		int zerosNeeded = targetLength - currentLength;
		for (int i = 0; i < zerosNeeded; i++) {
			result = "0".concat(result);
		}

		return result;

	}
}