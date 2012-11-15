package no.ruter.app.repository;

import android.location.Location;


/**
 * Repository methods for getting location data from Android
 * 
 * @author Kristian
 *
 */
public interface LocationRepository {
	
	/**
	 * Method for getting the current best location
	 * 
	 * @return {@link Location} best location
	 */
	public Location getCurrentLocation();

}
