/*
 * Dor Yehoshua 315619098
 * Hadar Isaacson 209831262
 */
package game.racers.air;


//															***	IMPORTS ***
import game.racers.Racer;
import utilities.EnumContainer.Color;


public class Helicopter extends Racer implements AerialRacer
{
	
	
	//														***	FIELDS ***
	static final String CLASS_NAME = "Helicopter";
	static final double DEFAULT_MAX_SPEED = 400, DEFAULT_ACCELERATION = 50;
	static final Color DEFAULT_color = Color.BLUE;
	
	
	//														***	CONSTRUCTORS ***
	
	/**
	 * Constructs a default Helicopter object with default parameters.
	 */
	public Helicopter() {this("Helicopter #" + Racer.getSerial(), Helicopter.DEFAULT_MAX_SPEED, Helicopter.DEFAULT_ACCELERATION, Helicopter.DEFAULT_color);}

	
	/**
	 * Constructs a Helicopter object with the given parameters.
	 * 
	 * @param name         - the racer's name
	 * @param maxSpeed     - the racer's maximum speed
	 * @param acceleration - the racer's acceleration
	 * @param color        - the racer's color
	 */
	public Helicopter(String name, double maxSpeed, double acceleration, Color color) 
	{
		super(name, maxSpeed, acceleration, color);
	}
	
	/**
	 * Constructs a new Helicopter object by copying the properties of another Helicopter object.
	 *
	 * @param other The Helicopter object to copy from.
	 */
	public Helicopter(Helicopter other) 
	{
		super(other.getName(), other.getMaxSpeed(), other.getAcceleration(), other.getColor());
	}

	//											***	OVVERRIDE ABSTRACT METHODS FROM RACER CLASS ***
	
	/**
	 * Returns the class name of this racer.
	 * 
	 * @return the class name of this racer
	 */
	@Override
	public String className() { return Helicopter.CLASS_NAME; }

	/**
	 * Returns a string describing the specific attributes of this racer.
	 * 
	 * @return a string describing the specific attributes of this racer
	 */
	@Override
	public String describeSpecific() { return ""; }

	
	/**
	 * Creates a new instance of Helicopter by cloning the current object.
	 *
	 * @return A new Helicopter object that is a clone of the current instance.
	 */
	@Override
	public Object clone() {return new Helicopter(this);}
}
