package no.ruter.app.repository;

import java.io.IOException;

import us.monoid.web.Resty;
import us.monoid.web.TextResource;

public class RealTimeRepository {
	
	public static void main(String[] args) {
		doIt();
	}

	// It does it
	public static void doIt(){
		
		try {
			TextResource response = new Resty().text("http://api-test.trafikanten.no/Place/FindMatches/nationaltheateret");
			System.out.println(response.toString());
			
			
		} catch (IOException e) {
			System.out.println("I failed");
		}
	}
	
}
