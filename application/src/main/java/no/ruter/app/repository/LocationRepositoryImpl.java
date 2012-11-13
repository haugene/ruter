package no.ruter.app.repository;

import android.location.Location;



public class LocationRepositoryImpl implements LocationRepository {
	
	public Location getCurrentLocation() {
		
		LocationEngine locationEngine = RepositoryFactory.getLocationEngine();
		
		// Possibly wait for a location
		while(locationEngine.getBestLocation() == null){
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return locationEngine.getBestLocation();
	}

}
