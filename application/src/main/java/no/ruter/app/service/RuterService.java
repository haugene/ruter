package no.ruter.app.service;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;

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
	 */
	public List<RealTimeLocation> findRealTimeLocations(String locName);

	/**
	 * Uses the gps/network/wlan location of the device to find a list of nearby
	 * realtime locations
	 * 
	 * @return list of {@link RealTimeLocation} that are close
	 */
	public List<RealTimeLocation> findRealTimeLocationsNearMe();

}
