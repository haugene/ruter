package no.ruter.app.repository;

import no.ruter.app.exception.RepositoryException;
import no.ruter.app.observers.LocationObserver;
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
	
	/**
	 * This method lets you register a {@link LocationObserver} that will be
	 * notified each time a better location is found. <br />
	 * NOTE: After a certain time, we will stop listening for updates and a new
	 * observer will need to be registered to initiate new location searching
	 * 
	 * @param observer
	 * @param context
	 * @throws RepositoryException
	 */
	public void registerLocationObserver(LocationObserver observer, Context context) throws RepositoryException;

}
