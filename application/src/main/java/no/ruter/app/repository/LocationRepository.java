package no.ruter.app.repository;

import no.ruter.app.exception.RepositoryException;
import android.content.Context;
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
	 * @param context
	 *            context to use for looking up system services
	 * 
	 * @return {@link Location} best location
	 * @throws RepositoryException 
	 */
	public Location getCurrentLocation(Context context) throws RepositoryException;

}
