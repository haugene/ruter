package no.ruter.app.repository;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;
import android.location.Location;

/**
 * Implementaion of the {@link PlaceRepository} interface
 * 
 * @author Kristian
 * 
 */
public class PlaceRepositoryImpl implements PlaceRepository {

	/**
	 * {@inheritDoc}
	 */
	public List<RealTimeLocation> getLocationsNearMe(Location location) {
		return null;
	}

}
