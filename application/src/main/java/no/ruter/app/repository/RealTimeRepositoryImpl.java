package no.ruter.app.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.exception.RepositoryException;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

/**
 * Implements the {@link RealTimeRepository}
 * @author Kristian
 *
 */
public class RealTimeRepositoryImpl implements RealTimeRepository{

	private final static String WEB_SERVICE_HOST_URL = "http://api-test.trafikanten.no/";
	private final static String FIND_MATCHES = "RealTime/FindMatches/";
	private final static String GET_DATA = "RealTime/GetRealTimeData/";

	/**
	 * {@inheritDoc}
	 * @throws EncoderException 
	 * @throws RepositoryException 
	 */
	public List<RealTimeLocation> findLocations(String query) throws RepositoryException {

		/*
		 * Encode the query string.
		 * If it fails, return an empty list for invalid query
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

			JSONObject response = runService(service, encodedQuery);
			locations = parseRealTimeLocationsFromResponse(response);

		} catch (Exception e) {
			throw new RepositoryException("Failed to find locations for query string: " + query);
		}

		// Return the list
		return locations;

	}

	/**
	 * {@inheritDoc}
	 * @throws RepositoryException 
	 */
	public List<RealTimeData> getRealTimeData(Integer id) throws RepositoryException {

		String service = WEB_SERVICE_HOST_URL + GET_DATA;

		try {

			JSONObject response = runService(service, id.toString());
			return parseRealTimeDataFromResponse(response);

		} catch (Exception e) {
			throw new RepositoryException("Getting real time data for id: "
					+ id + " failed");
		}

	}

	/**
	 * Reads a json response for the GetRealTimeData ruter api service.
	 * 
	 * The response will contain an array of real time data entries that needs
	 * to be parsed into {@link RealTimeData} objects.
	 * 
	 * @param response
	 *            the {@link JSONObject} returned by ruter api service
	 * @return {@link List} of {@link RealTimeData}
	 * @throws JSONException
	 */
	private List<RealTimeData> parseRealTimeDataFromResponse(JSONObject response)
			throws JSONException {

		List<RealTimeData> dataEntries = new ArrayList<RealTimeData>();

		JSONArray resultArray = response.getJSONArray("result");

		for (int i = 0; i < resultArray.length(); i++) {

			JSONObject data = resultArray.getJSONObject(i);
			RealTimeData realTimeData = getRealTimeData(data);
			dataEntries.add(realTimeData);
		}

		return dataEntries;

	}

	/**
	 * Parses a {@link RealTimeData} object from JSON representation
	 * 
	 * @param data
	 * @return
	 * @throws JSONException
	 *             if parsing fails
	 */
	private RealTimeData getRealTimeData(JSONObject data) throws JSONException {

		String line = data.getString("PublishedLineName");
		String destination = data.getString("DestinationName");
		DateTime expectedArrivalTime = parseDate(data.getString("ExpectedArrivalTime"));
		DateTime timestamp = parseDate(data.getString("RecordedAtTime"));

		return new RealTimeData(line, destination, expectedArrivalTime,
				timestamp);

	}

	/**
	 * Method for running a JSON service from Ruter api.
	 * 
	 * Runs a given query string on a given service. Returns a
	 * {@link JSONObject} representing the ruter api response
	 * 
	 * @param service
	 *            service url
	 * @param query
	 * @return {@link JSONObject} containing the result
	 * @throws IOException
	 *             if web call fails
	 * @throws JSONException
	 *             if parsing fails
	 */
	private JSONObject runService(String service, String query)
			throws IOException, JSONException {
		/*
		 * Make a Resty service and call the service with location parameter We
		 * request a text resource instead of a JSONObject because the ruter
		 * service returns a JSON array, we want a valid object for the parser.
		 * 
		 * Solution: Wrap the array in JSON, we call the array resultArray
		 */
		TextResource json = new Resty().text(service + query);

		// We have the textual json, add {} wrapper and parse
		JSONObject response = new JSONObject("{\"result\":" + json.toString()
				+ "}");

		return response;
	}

	/**
	 * Reads a json response for the FindMatches ruter service.
	 * 
	 * The response will contain an array of locations that needs to be parsed
	 * into {@link RealTimeLocation} objects.
	 * 
	 * @param realTimeLocations
	 * @param response
	 * @return a {@link List} of parsed {@link RealTimeLocation} objects
	 * @throws JSONException
	 */
	private List<RealTimeLocation> parseRealTimeLocationsFromResponse(
			JSONObject response) throws JSONException {

		List<RealTimeLocation> realTimeLocations = new ArrayList<RealTimeLocation>();

		JSONArray resultArray = response.getJSONArray("result");

		for (int i = 0; i < resultArray.length(); i++) {

			JSONObject location = resultArray.getJSONObject(i);
			RealTimeLocation realTimeLocation = getRealTimeLocation(location);
			realTimeLocations.add(realTimeLocation);
		}

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

	/**
	 * Helper method that parses a {@link DateTime} represented as {@link String}
	 * @param dateString representation of a {@link DateTime}
	 * @return {@link DateTime}
	 */
	private DateTime parseDate(String dateString) {
			
		String timeAndOffset = dateString.substring(dateString.indexOf("(")+1, dateString.indexOf(")"));
		
		// We have a date represented by 1234325234+0100, lets divide and conquer
		Integer offsetMarker = timeAndOffset.indexOf("+");
		
		String time = timeAndOffset.substring(0, offsetMarker);
		String hoursOffset = timeAndOffset.substring(offsetMarker+1, offsetMarker+3);
		String minutesOffset = timeAndOffset.substring(offsetMarker+3, timeAndOffset.length());
		
		DateTimeZone dateTimeZone = DateTimeZone.forOffsetHoursMinutes(Integer.parseInt(hoursOffset), Integer.parseInt(minutesOffset));
		DateTime dateTime = new DateTime(Long.parseLong(time), dateTimeZone);
		
		return dateTime;
	}
}
