package no.ruter.app.observers;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;

/**
 * Defines an interface for classes that want to observe, and get notification
 * about, nearby stops.
 * 
 * @author Kristian
 * 
 */
public interface NearMeObserver {
	
	/**
	 * Notifies the observer that there is a new list of locations
	 * 
	 * @param nearby locations
	 */
	public void listUpdated(List<RealTimeLocation> nearby);
	
	/**
	 * Signals the observer that no more updates will come
	 */
	public void stoppedLooking();

}
