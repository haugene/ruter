package no.ruter.app.service;

import java.util.List;

import no.ruter.app.domain.Platform;
import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.observers.NearMeObserver;
import android.content.Context;

/**
 * A common service class.
 * 
 * We'll start off with this one, and see if we need to separate
 * responsibilities later.
 * 
 * @author Kristian
 * 
 */
public interface RuterService {

	/**
	 * Takes a whole or partial location name as parameter and returns search
	 * results
	 * 
	 * @param locName
	 *            name of location
	 * @return list of {@link RealTimeLocation}
	 * @throws RepositoryException
	 */
	public List<RealTimeLocation> findRealTimeLocations(String locName)
			throws RepositoryException;

	/**
	 * Retrieves the real time data for the selected location
	 * 
	 * DEPRECATED: Use the GetRealTimeDataByPlatform()
	 * 
	 * @return list of (@link RealTimeData)
	 * @throws RepositoryException
	 */
	@Deprecated
	public List<RealTimeData> getRealTimeData(Integer id)
			throws RepositoryException;

	/**
	 * Retrieves realtime information for a given stop. The data is sorted by
	 * platforms.
	 * 
	 * @param id
	 *            place to get realtime data for
	 * @return {@link List} of {@link Platform} objects with departure data
	 * @throws RepositoryException
	 */
	public List<Platform> getRealTimeDataByPlatform(Integer id)
			throws RepositoryException;

	/**
	 * Does some magic and returns a string that has information about the
	 * location
	 * 
	 * @param context
	 *            context to use for looking up system services
	 * @return
	 */
	public String printLocationData(Context context);

	/**
	 * Registers a {@link NearMeObserver} that will be called when a new
	 * location is obtained. Once a new location is known, we query nearby stops
	 * and call the observer.
	 * 
	 * @param nearMeObserver
	 * @throws RepositoryException 
	 */
	public void registerNearMeObserver(NearMeObserver nearMeObserver, Context context) throws RepositoryException;
	
	/**
	 * Starts the process of getting our location. Our service will register a
	 * location listener to the repository
	 * 
	 * @param context
	 */
	public void startLookingForNearbyLocations(Context context);

}
