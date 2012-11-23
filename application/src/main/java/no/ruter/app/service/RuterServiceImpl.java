package no.ruter.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.ruter.app.domain.Platform;
import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.repository.LocationRepository;
import no.ruter.app.repository.RealTimeRepository;
import no.ruter.app.repository.RepositoryFactory;
import android.content.Context;
import android.location.Location;

/**
 * Implements the {@link RealTimeRepository} interface
 * 
 * @author Kristian
 * 
 */
public class RuterServiceImpl implements RuterService {

	/** Holds a {@link RealTimeRepository} */
	private RealTimeRepository realTimeRepository;

	/** Holds a {@link LocationRepository} */
	private LocationRepository locationRepository;

	/**
	 * Default constructor
	 */
	public RuterServiceImpl() {
		realTimeRepository = RepositoryFactory.getRealTimeRepository();
		locationRepository = RepositoryFactory.getLocationRepository();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<RealTimeLocation> findRealTimeLocations(String locName)
			throws RepositoryException {
		return realTimeRepository.findLocations(locName);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<RealTimeLocation> findRealTimeLocationsNearMe(Context context) {
		return new ArrayList<RealTimeLocation>();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<RealTimeData> getRealTimeData(Integer id)
			throws RepositoryException {
		return realTimeRepository.getRealTimeData(id);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Platform> getRealTimeDataByPlatform(Integer id)
			throws RepositoryException {

		List<RealTimeData> realTimeDepartures = realTimeRepository
				.getRealTimeData(id);

		return sortRealTimeDataOnPlatform(realTimeDepartures);
	}

	/**
	 * {@inheritDoc}
	 */
	public String printLocationData(Context context) {

		Location location = locationRepository.getCurrentLocation(context);

		StringBuilder sb = new StringBuilder();
		sb.append("Accuracy: " + location.getAccuracy() + " | ");
		sb.append("Lat: " + location.getLatitude() + " | ");
		sb.append("Long: " + location.getLongitude());

		return sb.toString();
	}

	private List<Platform> sortRealTimeDataOnPlatform(List<RealTimeData> data) {

		List<Platform> platforms = new ArrayList<Platform>();

		// Iterate all the data and put them in platforms
		for (RealTimeData realTimeData : data) {

			Platform platform = new Platform(realTimeData.getPlatformName());
			if (platforms.contains(platform)) {

				// The platform exists, add this RealTimeData
				platforms.get(platforms.indexOf(platform)).getDepartures()
						.add(realTimeData);
			} else {

				// First entry for this platform, add data and platform
				platform.getDepartures().add(realTimeData);
				platforms.add(platform);
			}

		}
		
		// Sort the list of platforms and return it
		Collections.sort(platforms);
		return platforms;
	}
}
