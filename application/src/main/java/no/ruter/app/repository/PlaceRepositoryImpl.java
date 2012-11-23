package no.ruter.app.repository;

import static no.ruter.app.utils.JSONUtil.PLACE_NEAR_ME;
import static no.ruter.app.utils.JSONUtil.WEB_SERVICE_HOST_URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import us.monoid.json.JSONObject;

import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.utils.JSONUtil;
import android.location.Location;

/**
 * Implementaion of the {@link PlaceRepository} interface
 * 
 * @author Kristian
 * 
 */
public class PlaceRepositoryImpl implements PlaceRepository {

	/** Holds the {@link JSONUtil} for calling services and parsing json */
	private JSONUtil jsonUtil;

	public PlaceRepositoryImpl() {
		jsonUtil = new JSONUtil();
	}

	/**
	 * {@inheritDoc}
	 * @throws RepositoryException 
	 */
	public List<RealTimeLocation> getLocationsNearMe(Location location) throws RepositoryException {

		String service = WEB_SERVICE_HOST_URL + PLACE_NEAR_ME;
		List<RealTimeLocation> locations;

		try {

			JSONObject response = jsonUtil.runService(service, "?coordinates=(X=593918,Y=6644077)&proposals=7");
			locations = jsonUtil.parseRealTimeLocationsFromResponse(response);

		} catch (Exception e) {
			throw new RepositoryException(
					"Failed to find nearby places from location: " + location);
		}

		// Return the list
		return locations;
	}

}
