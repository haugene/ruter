package no.ruter.app.builders;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.enums.VehicleType;

import org.joda.time.DateTime;

public class RealTimeDataBuilder {

	/*
	 * Member variables
	 */
	private String line = "17";
	private String destination = "Grefsen";
	private DateTime expectedArrivalTime = DateTime.now().plusMinutes(2);
	private DateTime timestamp = DateTime.now().minusMinutes(2);
	private String platformName = "1";
	private VehicleType vehicleType = VehicleType.TRAM;

	/*
	 * Builder methods
	 */
	public RealTimeDataBuilder withLine(String line) {
		this.line = line;
		return this;
	}

	public RealTimeDataBuilder withDestination(String destination) {
		this.destination = destination;
		return this;
	}

	public RealTimeDataBuilder withExpectedArrivalTime(DateTime arrivalTime) {
		this.expectedArrivalTime = arrivalTime;
		return this;
	}

	public RealTimeDataBuilder withTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public RealTimeDataBuilder withPlatformName(String platformName) {
		this.platformName = platformName;
		return this;
	}

	public RealTimeDataBuilder withVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
		return this;
	}

	// Build the RealTimeData object based on our values
	public RealTimeData build() {
		return new RealTimeData(line, destination, expectedArrivalTime,
				timestamp, platformName, vehicleType);
	}

}
