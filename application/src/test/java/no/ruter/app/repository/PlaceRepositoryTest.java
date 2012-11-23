package no.ruter.app.repository;

import static org.junit.Assert.assertTrue;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;

import org.junit.Test;

public class PlaceRepositoryTest {

	private PlaceRepository repo = new PlaceRepositoryImpl();

	@Test
	public void shouldReturnListOfLocations() throws RepositoryException {

		List<RealTimeLocation> locations = repo.getLocationsNearMe(null);
		assertTrue("Did not return any locations", locations.size() > 0);
	}

}
