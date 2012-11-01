package no.ruter.app.repository;

import static org.junit.Assert.*;

import java.util.List;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;

import org.junit.Test;

/**
 * Test class for {@link RealTimeRepositoryImpl}
 * 
 * TODO: Should mock repository calls
 * 
 * @author Kristian
 * 
 */
public class RealTimeRepositoryTest {

	@Test
	public void shouldReturnListOfLocations() {

		RealTimeRepositoryImpl repo = new RealTimeRepositoryImpl();

		List<RealTimeLocation> locations = repo.findLocations("skøyen");

		assertEquals("Did not return correct number of hits", 11,
				locations.size());
		
		
		boolean containsElement = "Skøyen [tog]".equalsIgnoreCase(locations.get(0).getName());
		assertTrue("Did not contain expected element", containsElement);

	}
	
	@Test
	public void shouldGetRealTimeDataGivenValidLocationId(){
		
		RealTimeRepositoryImpl repo = new RealTimeRepositoryImpl();
		
		// Query real time data for Skøyen [TOG]
		// TODO: Mock this. Will fail if you're coding late ;)
		List<RealTimeData> realTimeData = repo.getRealTimeData(3012500);
		assertTrue("No real time data response", realTimeData.size() > 0);
		
	}

}
