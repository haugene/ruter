package no.ruter.app.service;

/**
 * Factory class that holds singletons of all services
 * 
 * @author Kristian
 * 
 */
public class ServiceFactory {

	private static RuterService ruterService;

	public static RuterService getRuterService() {

		if (ruterService == null) {
			ruterService = new RuterServiceImpl();
		}

		return ruterService;
	}

}
