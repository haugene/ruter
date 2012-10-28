package no.ruter.app.domain;

/**
 * Represents a location for real time data.
 * 
 * @author Kristian
 * 
 */
public class RealTimeLocation {

	/**
	 * All variables constructor
	 * 
	 * @param name
	 * @param id
	 * @param xcoord
	 * @param ycoord
	 */
	public RealTimeLocation(String name, Integer id, Integer xcoord,
			Integer ycoord) {
		super();
		this.name = name;
		this.id = id;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
	}

	/*
	 * Member variables
	 */
	private String name;
	private Integer id;
	private Integer xcoord;
	private Integer ycoord;

	/*
	 * Getters and setters
	 */

	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public Integer getXcoord() {
		return xcoord;
	}

	public Integer getYcoord() {
		return ycoord;
	}

}
