package no.ruter.app.repository;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import android.location.Location;

/**
 * Repository class for the ruter Places api services
 * 
 * @author Kristian
 * 
 */
public interface PlaceRepository {

	/**
	 * Calls the ruter API to get locations near the user provided a location
	 * 
	 * @param location
	 *            current location of user
	 * @return close stops
	 * @throws RepositoryException 
	 */
	public List<RealTimeLocation> getLocationsNearMe(Location location) throws RepositoryException;

}
