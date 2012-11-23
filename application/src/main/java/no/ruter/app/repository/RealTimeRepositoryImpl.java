package no.ruter.app.repository;

import static no.ruter.app.utils.JSONUtil.FIND_MATCHES;
import static no.ruter.app.utils.JSONUtil.GET_DATA;
import static no.ruter.app.utils.JSONUtil.WEB_SERVICE_HOST_URL;

import java.util.ArrayList;
import java.util.List;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;
import no.ruter.app.utils.JSONUtil;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import us.monoid.json.JSONObject;

/**
 * Implements the {@link RealTimeRepository}
 * 
 * @author Kristian
 * 
 */
public class RealTimeRepositoryImpl implements RealTimeRepository {

	/** Holds the {@link JSONUtil} for calling services and parsing json */
	private JSONUtil jsonUtil;

	public RealTimeRepositoryImpl() {
		jsonUtil = new JSONUtil();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws EncoderException
	 * @throws RepositoryException
	 */
	public List<RealTimeLocation> findLocations(String query)
			throws RepositoryException {

		/*
		 * Encode the query string. If it fails, return an empty list for
		 * invalid query
		 */
		String encodedQuery;
		try {
			URLCodec urlCodec = new URLCodec("UTF-8");
			encodedQuery = urlCodec.encode(query);
		} catch (EncoderException ee) {
			return new ArrayList<RealTimeLocation>();
		}

		String service = WEB_SERVICE_HOST_URL + FIND_MATCHES;
		List<RealTimeLocation> locations;

		try {

			JSONObject response = jsonUtil.runService(service, encodedQuery);
			locations = jsonUtil.parseRealTimeLocationsFromResponse(response);

		} catch (Exception e) {
			throw new RepositoryException(
					"Failed to find locations for query string: " + query);
		}

		// Return the list
		return locations;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws RepositoryException
	 */
	public List<RealTimeData> getRealTimeData(Integer id)
			throws RepositoryException {

		String service = WEB_SERVICE_HOST_URL + GET_DATA;

		try {

			JSONObject response = jsonUtil.runService(service, id.toString());
			return jsonUtil.parseRealTimeDataFromResponse(response);

		} catch (Exception e) {
			throw new RepositoryException("Getting real time data for id: "
					+ id + " failed");
		}

	}
}
