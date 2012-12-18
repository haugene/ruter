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
	 * Notifies the observer that there is a new list of locations.
	 * 
	 * NOTE: If we have gotten a new location but an error occurs while getting
	 * nearby stops, we will pass null as parameter. Apart from this we will return
	 * an empty {@link List} if we just don't have any stops.
	 * 
	 * @param nearby
	 *            locations
	 */
	public void listUpdated(List<RealTimeLocation> nearby);
	
	/**
	 * Signals the observer that no more updates will come
	 */
	public void stoppedLooking();

}
