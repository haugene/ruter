package no.ruter.app.repository;

import static org.junit.Assert.*;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;

import org.junit.Test;

/**
 * Test class for {@link RealTimeRepository}
 * 
 * TODO: Should mock repository calls
 * 
 * @author Kristian
 * 
 */
public class RealTimeRepositoryTest {

	@Test
	public void init() {
		new RealTimeRepository();
	}

	@Test
	public void shouldReturnListOfLocations() {

		RealTimeRepository repo = new RealTimeRepository();

		List<RealTimeLocation> locations = repo.findLocations("skøyen");

		assertEquals("Did not return correct number of hits", 11,
				locations.size());

	}

}
