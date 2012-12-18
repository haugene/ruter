package no.ruter.app.repository;

import java.util.ArrayList;
import java.util.List;

import no.ruter.app.exception.RepositoryException;
import no.ruter.app.observers.LocationObserver;
import no.ruter.app.utils.LocationUtil;

import org.joda.time.DateTime;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Implementing class of {@link RepositoryFactory}
 * 
 * @author Kristian
 */
public class LocationRepositoryImpl implements
		LocationRepository {

	/** Holds the current best location */
	private Location bestLocation;

	/** Holds the utility class for location calculations */
	private LocationUtil locationUtil;

	/** Holds the {@link LocationManager} object */
	private LocationManager locationManager;

	/** Holds the {@link LocationListener} object */
	private LocationListener locationListener;

	/** Holds a timestamp from last repository call */
	private DateTime lastRequest;
	
	/** Objects that should be notified on location change */
	private List<LocationObserver> observers;
	
	private final Integer MINUTES_TO_LISTEN_FOR_LOCATION = 1;
	
	/**
	 * Default constructor.
	 * 
	 * Init {@link LocationUtil} and get reference to {@link LocationManager}
	 */
	public LocationRepositoryImpl() {
		super();
		
		// Init util
		locationUtil = new LocationUtil();
		observers = new ArrayList<LocationObserver>();
	}

	/**
	 * {@inheritDoc}
	 */
	public Location getCurrentLocation(Context context) throws RepositoryException {
		
		// We've been called, check that listener is running
		updateTimestampAndVerifyListener(context);

		// Return the best location we got
		return bestLocation;
	}

	/**
	 * {@inheritDoc}
	 */
	public void registerLocationObserver(LocationObserver observer, Context context) throws RepositoryException {
		
		// We've been called, check that listener is running
		updateTimestampAndVerifyListener(context);
		
		// Add the observer to the list of observers
		if(!observers.contains(observer)){
			observers.add(observer);
		}
		
		// Give him his first update
		observer.foundBetterLocation(bestLocation);
	}

	/**
	 * Common helper method for all queries to the location. Updates the
	 * timestamp for lastRequest and makes sure a listener is registered
	 * 
	 * @param context for access to android services(listener)
	 * @throws RepositoryException
	 */
	private void updateTimestampAndVerifyListener(Context context)
			throws RepositoryException {
		
		// Update timestamp for last repo request, this is it!
		lastRequest = DateTime.now();
	
		/*
		 * If we have no listener, create it and register it. Get cached
		 * location.
		 */
		if (locationListener == null) {
			createAndRegisterLocationListener(context);
		}
	}

	/**
	 * This method is executed every time android gives us new data
	 * 
	 * @param location
	 *            from android
	 */
	private void makeUseOfLocation(Location location) {

		// Check if the new location is better
		if (locationUtil.isBetterLocation(location, bestLocation)) {

			// Update our best location
			bestLocation = location;

			// Notify observers
			for (LocationObserver observer : observers) {
				observer.foundBetterLocation(location);
			}
		}

		/*
		 * If we have been probing for a while without timer reset, stop
		 */
		if (lastRequest.isBefore(DateTime.now().minusMinutes(MINUTES_TO_LISTEN_FOR_LOCATION))) {

			// Deregister listener
			locationManager.removeUpdates(locationListener);

			// Remove references
			locationListener = null;
			locationManager = null;
			
			// Notify observers, no more updates
			for (LocationObserver observer : observers) {
				observer.stoppedLooking();
			}
			
			// Clear list of observers, they'll have to register again
			observers.clear();
		}
	}

	/**
	 * Creates a {@link LocationListener} and register it with
	 * {@link LocationManager} Also, read the cached last location in android
	 * @throws RepositoryException 
	 */
	private void createAndRegisterLocationListener(Context context) throws RepositoryException {
		
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
		// Check that the network provider is enabled
		boolean isNetworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if(!isNetworkProviderEnabled){
			throw new RepositoryException("Location manager is not enabled");
		}
		
		// Create listener
		locationListener = createLocationListener();

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		// Get the last known, maybe cached, location
		bestLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * Creates a {@link LocationListener} that calls our makeUseOfLocation()
	 * when a new location is avaliable
	 * 
	 * @return {@link LocationListener} to register in the manager
	 */
	private LocationListener createLocationListener() {
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				makeUseOfLocation(location);
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}
		};
		return locationListener;
	}

}
