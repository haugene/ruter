package no.ruter.app.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.EncoderException;

import android.location.Location;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.repository.LocationRepository;
import no.ruter.app.repository.RealTimeRepository;
import no.ruter.app.repository.RepositoryFactory;

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
	public List<RealTimeLocation> findRealTimeLocations(String locName) {
		try {
			return realTimeRepository.findLocations(locName);
		} catch (EncoderException e) {
			// TODO Signal error
			e.printStackTrace();
		}
		return new ArrayList<RealTimeLocation>();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<RealTimeLocation> findRealTimeLocationsNearMe() {
		
		Location location = locationRepository.getCurrentLocation();
		System.out.println("Location:");
		System.out.println("Accuracy: " + location.getAccuracy());
		System.out.println("Lat: " + location.getLatitude());
		System.out.println("Long: " + location.getLongitude());
		
		return null;
	}

    /**
     * {@inheritDoc}
     */
    public List<RealTimeData> getRealTimeData(Integer id) {
        return realTimeRepository.getRealTimeData(id);
    }

}
