package no.ruter.app.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import no.ruter.app.builders.RealTimeDataBuilder;
import no.ruter.app.domain.Platform;
import no.ruter.app.domain.RealTimeData;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.repository.RealTimeRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RuterServiceTest {

	@Mock
	private RealTimeRepository realTimeRepository;

	@InjectMocks
	RuterService ruterService = new RuterServiceImpl();

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldSortByPlatform() throws RepositoryException {

		/*
		 * Given
		 */
		List<RealTimeData> data = new ArrayList<RealTimeData>();

		// Add a couple of real time data
		data.add(new RealTimeDataBuilder() //
				.withPlatformName("1") //
				.build());
		data.add(new RealTimeDataBuilder() //
				.withPlatformName("2") //
				.build());

		when(realTimeRepository.getRealTimeData(anyInt())).thenReturn(data);

		/*
		 * When
		 */
		List<Platform> platforms = ruterService
				.getRealTimeDataByPlatform(new Integer(1337));

		/*
		 * Then
		 */
		assertTrue("Should have two platforms", platforms.size() == 2);

		for (Platform platform : platforms) {

			assertTrue("Each platform should have one real time data object",
					platform.getDepartures().size() == 1);
		}
	}
	
	@Test
	public void shouldPutAllEntriesInSamePlatformWhenEqual() throws RepositoryException {

		/*
		 * Given
		 */
		List<RealTimeData> data = new ArrayList<RealTimeData>();

		// Add a couple of real time data
		data.add(new RealTimeDataBuilder() //
				.withPlatformName("1") //
				.build());
		data.add(new RealTimeDataBuilder() //
				.withPlatformName("1") //
				.build());

		when(realTimeRepository.getRealTimeData(anyInt())).thenReturn(data);

		/*
		 * When
		 */
		List<Platform> platforms = ruterService
				.getRealTimeDataByPlatform(new Integer(1337));

		/*
		 * Then
		 */
		assertTrue("Should have one platform", platforms.size() == 1);
		assertTrue("Platform should have two entries", platforms.get(0).getDepartures().size() == 2);
	}
}
