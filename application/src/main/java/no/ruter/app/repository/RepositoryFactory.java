package no.ruter.app.repository;

/**
 * Factory class that holds a singleton for each repository
 * 
 * @author Kristian
 * 
 */
public class RepositoryFactory {

	private static RealTimeRepository realTimeRepository;
	private static LocationEngine locationEngine;
	private static LocationRepository locationRepository;

	public static RealTimeRepository getRealTimeRepository() {

		if (realTimeRepository == null) {
			realTimeRepository = new RealTimeRepositoryImpl();
		}

		return realTimeRepository;
	}

	public static LocationRepository getLocationRepository() {

		if (locationRepository == null) {
			locationRepository = new LocationRepositoryImpl();
		}
		return locationRepository;
	}

	public static LocationEngine getLocationEngine() {

		if (locationEngine == null) {
			locationEngine = new LocationEngine();
			new Thread(locationEngine).start();
		}
		return locationEngine;
	}

	/**
	 * The {@link LocationEngine} object itself calls this method when it stops
	 */
	public static void locationEngineStopped() {
		locationEngine = null;
	}

}
