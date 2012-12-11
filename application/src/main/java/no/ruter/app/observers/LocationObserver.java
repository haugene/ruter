package no.ruter.app.observers;

import android.location.Location;

/**
 * Defines an interface for classes that want to observe, and get notification
 * about, updates in current location.
 * 
 * @author Kristian
 * 
 */
public interface LocationObserver {

	/**
	 * A better location was found
	 * @param location
	 */
	public void foundBetterLocation(Location location);
	
	/**
	 * Signals the observer that no more updates will come
	 */
	public void stoppedLooking();
	
}
