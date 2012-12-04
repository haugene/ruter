package no.ruter.app.repository;

import static no.ruter.app.utils.JSONUtil.PLACE_NEAR_ME;
import static no.ruter.app.utils.JSONUtil.WEB_SERVICE_HOST_URL;

import java.util.ArrayList;
import java.util.List;

import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.geoconverter.Conversion;
import no.ruter.app.geoconverter.GeographicPoint;
import no.ruter.app.utils.JSONUtil;
import us.monoid.json.JSONObject;
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

	/** Holds a UTM32-converter. See https://github.com/kjella/UTM32_converter */
	private Conversion converter;

	public PlaceRepositoryImpl() {
		jsonUtil = new JSONUtil();
		converter = new Conversion();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws RepositoryException
	 */
	public List<RealTimeLocation> getLocationsNearMe(Location location)
			throws RepositoryException {

		if (location == null) {
			return new ArrayList<RealTimeLocation>();
		}

		String service = WEB_SERVICE_HOST_URL + PLACE_NEAR_ME;
		List<RealTimeLocation> locations;

		GeographicPoint point = converter.LatLonToUTMXY(location.getLatitude(),
				location.getLongitude(), 32);
		String x = Long.toString(Math.round(point.getLatitude()));
		String y = Long.toString(Math.round(point.getLongitude()));

		try {

			JSONObject response = jsonUtil.runService(service,
					"?coordinates=(X=" + x + ",Y=" + y + ")&proposals=7");
			locations = jsonUtil.parseRealTimeLocationsFromResponse(response);

		} catch (Exception e) {
			throw new RepositoryException(
					"Failed to find nearby places from location: " + location);
		}

		// Return the list
		return locations;
	}

}