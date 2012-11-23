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
	 * @param district
	 */
	public RealTimeLocation(String name, Integer id, String district) {
		super();
		this.name = name;
		this.id = id;
		this.district = district;
	}

	/*
	 * Member variables
	 */
	private String name;
	private Integer id;
	private String district;

	/*
	 * Getters and setters
	 */
	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}
	
	public String getDistrict() {
		return district;
	}

	@Override
    public String toString() {
        return name;
    }

}
