package no.ruter.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import no.ruter.app.domain.Platform;
import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.observers.LocationObserver;
import no.ruter.app.observers.NearMeObserver;
import no.ruter.app.repository.LocationRepository;
import no.ruter.app.repository.PlaceRepository;
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
	
	/** Holds a {@link PlaceRepository} */
	private PlaceRepository placeRepository;
	
	/** Holds the {@link LocationObserver} used to get location updates */
	private LocationObserver locationObserver;
	
	/** Holds a {@link List} of {@link NearMeObserver} that want to be updated */
	private List<NearMeObserver> nearMeObservers;

	/**
	 * Default constructor
	 */
	public RuterServiceImpl() {
		realTimeRepository = RepositoryFactory.getRealTimeRepository();
		locationRepository = RepositoryFactory.getLocationRepository();
		placeRepository = RepositoryFactory.getPlaceRepository();
		nearMeObservers = new ArrayList<NearMeObserver>();
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

		Location location;
		try {
			location = locationRepository.getCurrentLocation(context);
			
			StringBuilder sb = new StringBuilder();

			if(location == null){
				return "Got null location";
			}
			
			List<RealTimeLocation> locations = placeRepository.getLocationsNearMe(location);
			for(RealTimeLocation loc : locations){
				sb.append(loc.getName()).append(" | ");
			}
			
			return sb.toString();
		} catch (RepositoryException e) {
			return "Caught exception";
		}
		
	}

	/**
	 * {@inheritDoc}
	 * @throws RepositoryException 
	 */
	public void registerNearMeObserver(NearMeObserver nearMeObserver, Context context) throws RepositoryException {
		
		// Add the observer if it's not already there
		/*
		 * NOTE: This needs to be done before registering the listener. This is
		 * because the register process will call the locationObserver. If we
		 * haven't added the nearMeObserver by then, that call will be lost.
		 */
		if(!nearMeObservers.contains(nearMeObserver)){
			nearMeObservers.add(nearMeObserver);
		}
		
		// We need an observer for location changes
		createAndRegisterLocationObserver(context);
	}

	/**
	 * {@inheritDoc}
	 */
	public void startLookingForNearbyLocations(Context context) {
		try {
			createAndRegisterLocationObserver(context);
		} catch (RepositoryException e) {
			// TODO: kristian: We probably don't have location provider.
			/*
			 * should we throw it? This method simply starts looking for
			 * locations. The caller will call another method later to use
			 * the information. It will get the same error.
			 */
		}
	}

	/**
	 * Creates a {@link LocationObserver} if one does not already exist.
	 * The observer is then registered with the repository.
	 * @param context
	 * @throws RepositoryException
	 */
	private void createAndRegisterLocationObserver(Context context) throws RepositoryException {
		
		// First check if we have an observer listening to location changes
		if(locationObserver == null){
			locationObserver = createLocationObserver();
		}
		
		try {
			
			// Register it. If it's already registered, a timer will be reset.
			locationRepository.registerLocationObserver(locationObserver, context);
			
		} catch (RepositoryException e) {
			
			// We couldn't register our observer, set it to null
			locationObserver = null;
			
			// Then throw the exception
			throw e;
		}
	}

	/**
	 * Creates a location observer. This object implements what we do when a new
	 * location is known.
	 * 
	 * @return
	 */
	private LocationObserver createLocationObserver() {
		
		return new LocationObserver() {
			
			public void stoppedLooking() {
				
				// Will not get any more updates, clear observer
				locationObserver = null;
				
				// Notify observers
				for(NearMeObserver observer : nearMeObservers){
					observer.stoppedLooking();
				}
				
				// Remove observers
				nearMeObservers.clear();
			}
			
			public void foundBetterLocation(Location location) {
				
				try {
					
					// Found a better location, find stops
					List<RealTimeLocation> nearMe = placeRepository.getLocationsNearMe(location);
					
					// Notify observers
					for(NearMeObserver observer : nearMeObservers){
						observer.listUpdated(nearMe);
					}
					
				} catch (RepositoryException e) {
					
					/*
					 *  Something went wrong when trying to get nearby stops.
					 *  We indicate this to the view by updating with "null".
					 */
					
					for(NearMeObserver observer : nearMeObservers){
						observer.listUpdated(null);
					}
				}
			}
		};
	}

	private List<Platform> sortRealTimeDataOnPlatform(List<RealTimeData> data) {

		List<Platform> platforms = new ArrayList<Platform>();

		// Iterate all the data and put them in platforms
		for (RealTimeData realTimeData : data) {

			Platform platform = new Platform(realTimeData.getPlatformName());
			
			if (platforms.contains(platform)) {

				// The platform exists, add this RealTimeData
				platforms.get(platforms.indexOf(platform)).getDepartures().add(realTimeData);
				
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
