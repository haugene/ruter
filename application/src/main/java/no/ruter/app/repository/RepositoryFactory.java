package no.ruter.app.repository;

/**
 * Factory class that holds a singleton for each repository
 * @author Kristian
 *
 */
public class RepositoryFactory {

	private static RealTimeRepository realTimeRepository;
	
	public static RealTimeRepository getRealTimeRepository(){
		
		if (realTimeRepository == null) {
			realTimeRepository = new RealTimeRepositoryImpl();
		}
		
		return realTimeRepository;
	}
}
