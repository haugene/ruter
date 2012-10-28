package no.ruter.app.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ruter.app.domain.RealTimeLocation;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

public class RealTimeRepository {

	private final static String WEB_SERVICE_HOST_URL = "http://api-test.trafikanten.no/";
	private final static String REAL_TIME_FIND_MATCHES = "RealTime/FindMatches/";

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
	 */
	public List<RealTimeLocation> findLocations(String query) {

		String service = WEB_SERVICE_HOST_URL + REAL_TIME_FIND_MATCHES;

		List<RealTimeLocation> realTimeLocations = new ArrayList<RealTimeLocation>();

		try {

			System.out.println("Query url: " + service + query);

			/*
			 * Make a Resty service and call the service with location parameter
			 * We request a text resource instead of a JSONObject because the
			 * ruter service returns a JSON array, we want a valid object for
			 * the parser.
			 * 
			 * Solution: Wrap the array in JSON, we call the array resultArray
			 */
			TextResource json = new Resty().text(service + query);

			// We have the textual json, add {} wrapper and parse
			JSONObject response = new JSONObject("{\"resultArray\":"
					+ json.toString().trim() + "}");

			JSONArray resultArray = response.getJSONArray("resultArray");

			for (int i = 0; i < resultArray.length(); i++) {

				JSONObject location = resultArray.getJSONObject(i);

				RealTimeLocation realTimeLocation = getRealTimeLocation(location);

				realTimeLocations.add(realTimeLocation);

			}

		} catch (Exception e) {
			/*
			 * TODO: kristian : 28.10.2012 : We should maybe try to signal that
			 * there was an error. So that it differs from the
			 * "no hits matching that name" case. Custom RuntimeException?
			 */
			e.printStackTrace();
		}

		// Return the list
		return realTimeLocations;

	}

	/**
	 * Reads specific keys from the json object. The value of the objects read
	 * is used to make a {@link RealTimeLocation}
	 * 
	 * @param location
	 *            a {@link JSONObject} representing a location
	 * @return a {@link RealTimeLocation}
	 * @throws JSONException
	 *             if json parse errors occurs
	 */
	private RealTimeLocation getRealTimeLocation(JSONObject location)
			throws JSONException {

		String name = location.getString("Name");
		Integer id = location.getInt("ID");
		Integer xcoord = location.getInt("X");
		Integer ycoord = location.getInt("Y");

		return new RealTimeLocation(name, id, xcoord, ycoord);
	}

}
