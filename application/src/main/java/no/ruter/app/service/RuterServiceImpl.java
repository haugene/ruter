package no.ruter.app.service;

import java.util.ArrayList;
import java.util.List;

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

	public String printLocationData(Context context) {

		Location location = locationRepository.getCurrentLocation(context);

		StringBuilder sb = new StringBuilder();
		sb.append("Accuracy: " + location.getAccuracy() + " | ");
		sb.append("Lat: " + location.getLatitude() + " | ");
		sb.append("Long: " + location.getLongitude());
		
		return sb.toString();
	}

}
