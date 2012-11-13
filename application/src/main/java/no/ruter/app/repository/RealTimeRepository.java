package no.ruter.app.repository;

import java.util.List;

import org.apache.commons.codec.EncoderException;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;

/**
 * Repository class for all api calls towards the RealTime services of Ruter API
 * 
 * @author Kristian
 * 
 */
public interface RealTimeRepository {

	/**
	 * Calls the findMathces service of the ruter api.
	 * 
	 * Searches for locations with real time data given an input to search for.
	 * Because of experienced server crashes, the query string must be at least
	 * 3 characters.
	 * 
	 * @param query
	 *            name or partial name of a location
	 * @return {@link List} of {@link RealTimeLocation} objects. Never null
	 * @throws EncoderException 
	 */
	public List<RealTimeLocation> findLocations(String query) throws EncoderException;

	/**
	 * Calls the GetRealTimeData service of the ruter api.
	 * 
	 * @param id
	 *            id of the {@link RealTimeLocation} to get {@link RealTimeData}
	 *            for
	 * @return {@link List} of {@link RealTimeData}
	 */
	public List<RealTimeData> getRealTimeData(Integer id);

}
