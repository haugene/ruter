package no.ruter.app.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.location.Location;

public class PlaceRepositoryTest {

	private PlaceRepository repo = new PlaceRepositoryImpl();
	
	@Mock
	private Location location;
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldReturnListOfLocations() throws RepositoryException {
		
		when(location.getLatitude()).thenReturn(59.9323824);
		when(location.getLongitude()).thenReturn(10.7636175);

		List<RealTimeLocation> locations = repo.getLocationsNearMe(location);
		assertTrue("Did not return any locations", locations.size() > 0);
	}
}
