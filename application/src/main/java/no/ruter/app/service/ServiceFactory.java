package no.ruter.app.service;

public class ServiceFactory {
	
	private static RuterService ruterService;
	
	public static RuterService getRuterService(){
		
		if(ruterService == null){
			ruterService = new RuterServiceImpl();
		}
		
		return ruterService;
	}

}
