package no.ruter.app.repository;

import org.joda.time.DateTime;

import no.ruter.app.utils.LocationUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationEngine extends Service implements Runnable {
	
	/** Holds the current best location */
	private Location bestLocation;
	
	/** Holds the utility class for location calculations */
	private LocationUtil locationUtil;
	
	/** Holds the {@link LocationManager} object */
	private LocationManager locationManager;
	
	/** Holds the {@link LocationListener} object */
	private LocationListener locationListener;
	
	/** Holds a timestamp from when this engine was created */
	private DateTime started;

	public LocationEngine() {
		
		// Init util
		locationUtil = new LocationUtil();
	}

	/**
	 * Main execution code. This will run in separate {@link Thread}
	 */
	public void run() {
		
		// Set timestamp
		started = DateTime.now();
	
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
	
		locationListener = createLocationListener();
	
		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
		// Get the last known, maybe cached, location
		bestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * Method for getting the current best location
	 * 
	 * @return {@link Location} best location
	 */
	public Location getBestLocation() {
		return bestLocation;
	}

	private void makeUseOfLocation(Location location){
		
		// Update our bet location if this one is better
		if(locationUtil.isBetterLocation(location, bestLocation)){
			bestLocation = location;
		}
		
		/*
		 * If this update arrived over 2 minutes after we started listening,
		 * stop this LocationEngine
		 */
		if (started.isBefore(DateTime.now().minusMinutes(2))){
			
			// Deregister listener
			locationManager.removeUpdates(locationListener);
			
			// Remove reference to this engine
			RepositoryFactory.locationEngineStopped();
		}
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

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}