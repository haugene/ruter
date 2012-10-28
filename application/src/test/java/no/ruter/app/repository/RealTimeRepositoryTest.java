package no.ruter.app.repository;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.List;

import no.ruter.app.domain.RealTimeLocation;

import org.junit.Test;

public class RealTimeRepositoryTest {

	@Test
	public void init(){
		new RealTimeRepository();
	}
	
	@Test
	public void shouldReturnListOfLocations(){
		
		RealTimeRepository repo = new RealTimeRepository();
		
		List<RealTimeLocation> locations = repo.findLocations("skøyen");
		
		assertEquals("Did not return correct number of hits", 11, locations.size());
		
	}
	
}
