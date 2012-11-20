package no.ruter.app.service;

import java.util.List;
import java.util.Map;

import android.content.Context;

import no.ruter.app.domain.Platform;
import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;

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
	 * Uses the gps/network/wlan location of the device to find a list of nearby
	 * realtime locations
	 * 
	 * @param context
	 *            context to use for looking up system services
	 * 
	 * @return list of {@link RealTimeLocation} that are close
	 */
	public List<RealTimeLocation> findRealTimeLocationsNearMe(Context context);

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
	 * Returns a map that represents departures from a {@link RealTimeLocation}.
	 * The departures are ordered as lists tied to a platform by a {@link Map}
	 * 
	 * @param id for the wanted {@link RealTimeLocation}
	 * @return map of all departures, each platform has a list of departures
	 * @throws RepositoryException
	 */
	public Map<Platform, List<RealTimeData>> getRealTimeDataByPlatform(Integer id) throws RepositoryException;

	/**
	 * Does some magic and returns a string that has information about the
	 * location
	 * 
	 * @param context
	 *            context to use for looking up system services
	 * @return
	 */
	public String printLocationData(Context context);

}
