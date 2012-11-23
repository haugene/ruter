package no.ruter.app.repository;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;
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
	 */
	public List<RealTimeLocation> getLocationsNearMe(Location location);

}
