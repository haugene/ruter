package no.ruter.app.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

public class RealTimeDataTest {
	
	@Test
	public void shouldReturnNowIfExpectedArrivalTimeIsNear() {
		
		RealTimeData realTimeData = new RealTimeData(null, null, DateTime.now().plusSeconds(45), null, null, null);
		assertEquals("Formatting went wrong", "nå", realTimeData.getFormattedArrivalTime());
	}
	
	@Test
	public void shouldReturnMinutesIfExpectedArrivalTimeIsWithinSomeMinutes(){
		RealTimeData realTimeData = new RealTimeData(null, null, DateTime.now().plusMinutes(5), null, null, null);
		assertEquals("Formatting went wrong", "5 min", realTimeData.getFormattedArrivalTime());
	}
	
	@Test
	public void shouldReturnTimeAsHourAndMinutesIfExpectedArrivalTimeIsLarge(){
		RealTimeData realTimeData = new RealTimeData(null, null, DateTime.now().plusMinutes(11), null, null, null);
		assertTrue("Time was not 5 char long", realTimeData.getFormattedArrivalTime().length() == 5);
		assertTrue("Missing separator", realTimeData.getFormattedArrivalTime().contains(":"));
	}
}
