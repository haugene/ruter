package no.ruter.app.repository;

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

	/**
	 * Default constructor.
	 * 
	 * Init {@link LocationUtil} and get reference to {@link LocationManager}
	 */
	public LocationRepositoryImpl() {
		super();
		
		// Init util
		locationUtil = new LocationUtil();
	}

	/**
	 * {@inheritDoc}
	 */
	public Location getCurrentLocation(Context context) {
		// Update timestamp for last repo request, this is it!
		lastRequest = DateTime.now();

		/*
		 * If we have no listener, create it and register it. Get cached
		 * location.
		 */
		if (locationListener == null) {
			createAndRegisterLocationListener(context);
		}

		// Return the best location we got
		return bestLocation;
	}

	/**
	 * This method is executed every time android gives us new data
	 * 
	 * @param location
	 *            from android
	 */
	private void makeUseOfLocation(Location location) {

		// Update our bet location if this one is better
		if (locationUtil.isBetterLocation(location, bestLocation)) {
			bestLocation = location;
		}

		/*
		 * If we have been probing for 5 minutes without timer reset, stop
		 */
		if (lastRequest.isBefore(DateTime.now().minusMinutes(5))) {

			// Deregister listener
			locationManager.removeUpdates(locationListener);

			// Remove references
			locationListener = null;
			locationManager = null;
		}
	}

	/**
	 * Creates a {@link LocationListener} and register it with
	 * {@link LocationManager} Also, read the cached last location in android
	 */
	private void createAndRegisterLocationListener(Context context) {
		
		// TODO: Check that a network_provider exists! Crashes in emulator

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		
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
