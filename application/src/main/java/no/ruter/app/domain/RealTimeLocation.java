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
	 */
	public RealTimeLocation(String name, Integer id) {
		super();
		this.name = name;
		this.id = id;
	}

	/*
	 * Member variables
	 */
	private String name;
	private Integer id;

	/*
	 * Getters and setters
	 */
	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	@Override
    public String toString() {
        return name;
    }

}
