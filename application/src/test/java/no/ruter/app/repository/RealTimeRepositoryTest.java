package no.ruter.app.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RealTimeRepositoryImpl}
 * 
 * @author Kristian
 * 
 */
public class RealTimeRepositoryTest {

	private RealTimeRepository repo;

	@Before
	public void initTest() {
		repo = new RealTimeRepositoryImpl();

	}

	@Test
	public void shouldReturnListOfLocations() throws RepositoryException {
		
		List<RealTimeLocation> locations = repo.findLocations("skøyen");

		assertEquals("Did not return correct number of hits", 11,
				locations.size());

		boolean containsElement = "Skøyen [tog]".equalsIgnoreCase(locations
				.get(0).getName());
		assertTrue("Did not contain expected element", containsElement);

	}
	
	@Test
	public void locationsShouldContainNameAndDistrictAndID() throws RepositoryException {
		
		List<RealTimeLocation> locations = repo.findLocations("national");
		
		assertTrue("Did not get any locations", locations.size() > 0);
		
		// Check all of them
		for(RealTimeLocation location : locations){
			assertTrue("Location did not have a name", StringUtils.isNotBlank(location.getName()));
			assertTrue("Location did not have ID", location.getId() > 0);
			assertTrue("Location did not have District", StringUtils.isNotBlank(location.getDistrict()));
		}
	}

	@Test
	public void shouldGetRealTimeDataGivenValidLocationId() throws RepositoryException {

		// Query real time data for welhavensgate
		List<RealTimeData> realTimeData = repo.getRealTimeData(3010211);
		assertTrue("No real time data response", realTimeData.size() > 0);

	}

	@Test
	public void realTimeDataShouldContainExpectedDepartureTimeAndTimeStamp() throws RepositoryException {

		// Query for welhavensgate
		List<RealTimeData> realTimeData = repo.getRealTimeData(3010211);
		assertTrue("Expected departure time was null", realTimeData.get(0)
				.getExpectedDepartureTime() != null);
		assertTrue("We did not get any real time data", realTimeData.size() > 0);

		/*
		 * This is live data, but we could assert that realTime is after now()
		 * and within 2 hrs. Also, the timestamp should be before now and should
		 * not be older than 1 hr.
		 */
		for (RealTimeData realTime : realTimeData) {
			assertTrue(
					"RealTimeData did not have expected departure between now and 1 hr",
					realTime.getExpectedDepartureTime().isAfterNow()
							&& realTime.getExpectedDepartureTime().isBefore(
									DateTime.now().plusHours(2)));
			assertTrue(
					"RealTimeData did have a timestamp newer than 1 hr and older than now(ish)",
					realTime.getTimestamp().minusMinutes(5).isBeforeNow()
							&& realTime.getTimestamp().isAfter(
									DateTime.now().minusHours(1)));
		}
	}
	
	@Test
	public void realTimeDataShouldContainVehicleTypeAndPlatformName() throws RepositoryException{
		List<RealTimeData> realTimeData = repo.getRealTimeData(3010211);
		assertTrue("Did not get any results", realTimeData.size() > 0);
		
		assertTrue("VehicleType was not set", realTimeData.get(0).getVehicleType() != null);
		assertTrue("PlatformName was not set", realTimeData.get(0).getPlatformName() != null);
	}
}
