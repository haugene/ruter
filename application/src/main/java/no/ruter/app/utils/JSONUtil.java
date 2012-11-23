package no.ruter.app.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.ruter.app.domain.RealTimeData;
import no.ruter.app.domain.RealTimeLocation;
import no.ruter.app.enums.VehicleType;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.Resty;
import us.monoid.web.TextResource;

/**
 * Utility class for handling JSON calls.
 * 
 * This class contains helper methods for running service calls, as well as
 * parsing JSON to domain objects, etc..
 * 
 * @author Kristian
 * 
 */
public class JSONUtil {
	
	public final static String WEB_SERVICE_HOST_URL = "http://api-test.trafikanten.no/";
	public final static String FIND_MATCHES = "RealTime/FindMatches/";
	public final static String GET_DATA = "RealTime/GetRealTimeData/";
	public final static String PLACE_NEAR_ME = "Place/GetClosestStopsByCoordinates/";
	
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
	public JSONObject runService(String service, String query)
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
	public List<RealTimeData> parseRealTimeDataFromResponse(JSONObject response)
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
	public List<RealTimeLocation> parseRealTimeLocationsFromResponse(
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
		DateTime expectedDepartureTime = parseDate(data.getString("ExpectedDepartureTime"));
		DateTime timestamp = parseDate(data.getString("RecordedAtTime"));
		String platformName = data.getString("DeparturePlatformName");
		VehicleType vehicleType = VehicleType.valueOf(data.getString("VehicleMode").toUpperCase());
	
		return new RealTimeData(line, destination, expectedDepartureTime,
				timestamp, platformName, vehicleType);
	
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
		String district = location.getString("District");

		return new RealTimeLocation(name, id, district);
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
