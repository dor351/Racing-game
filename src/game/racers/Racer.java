package game.racers;
import State.State;
//											***	IMPORTS	***
import game.arenas.Arena;
import utilities.EnumContainer.Color;
import utilities.Fate;
import utilities.Mishap;
import utilities.ModifiedObserveable;
import utilities.Point;

//-------------------------------------------------------------------------------------------------------------------------
/**
*This abstract class represents a Racer, which has a name, current and finish locations, an arena, a color, 
*maximum speed, acceleration, current speed and failure probability.
*This class includes methods that allow for the initialization of the race, 
*movement of the racer, introduction of the racer, describing the racer, and determining if the racer has a mishap.
*It also includes abstract methods that will be implemented by each specific racer class.
*/
public abstract class Racer extends ModifiedObserveable implements Runnable, Cloneable
{
	
	
	//										***	FIELDS	***
	private static int serial = 1;
	private int serialNumber;
	private String name;
	private Point currentLocation;
	private Point finish;
	private Arena arena;
	private double maxSpeed, acceleration, currentSpeed, failureProbability;
	private Color color;
	private Mishap mishap; 
	private State state;
	
	
	//										***	CONSTRUCTORS ***
	
	
	/**
	 * Constructs a racer object with a serial number, name, maximum speed, acceleration, 
	 * color, current speed, failure probability and a null mishap.
	 * 
	 * @param name				The racer's name.
	 * @param maxSpeed			The racer's maximum speed.
	 * @param acceleration		The racer's acceleration.
	 * @param color				The racer's color.
	 */
	public Racer(String name, double maxSpeed, double acceleration, Color color)
	{
		this.setSerialNumber(Racer.getSerial());
		this.setName(name);
		this.setMaxSpeed(maxSpeed);
		this.setAcceleration(acceleration);
		this.setColor(color);
		this.setCurrentSpeed(0);
		this.setFailureProbability(0);
		Racer.increasSerial();
	}
	
	
	
	
	
	//										***	METHODS	***
	
	/**
	 * Initializes the racer's race with the given arena, starting location and finish location.
	 * 
	 * @param arena				The arena to be used for the race.
	 * @param start				The starting location of the racer.
	 * @param finish			The finish location of the racer.
	 */
	public void initRace(Arena arena, Point start, Point finish)
	{
		this.setArena(arena);
		this.setCurrentLocation(start);
		this.setFinish(finish);
	}
	
	
	/**
	 * Moves the racer a distance based on its current speed, acceleration and friction. 
	 * Also determines if the racer has a mishap, and if so, generates and processes it. 
	 * 
	 * @param friction			The friction of the arena.
	 * @return					The current location of the racer.
	 */
	public Point move(double friction)
	{
		if( this.mishap == null ||(this.mishap.getFixable() && this.mishap.getTurnsToFix() == 0) )
		{
			this.mishap = null;
			if(Fate.breakDown()) 
			{
				this.mishap = Fate.generateMishap();
				System.out.println( this.getName() + " Has a new mishap! " + this.mishap );
				this.mishap.nextTurn();
				return this.check_move( friction, this.mishap.getReductionFactor());
			}
			else return this.check_move( friction, 1 );		
		}
		
		else 
		{
			if ( this.mishap.getFixable())
			{
				this.mishap.nextTurn();
				return this.check_move( friction, this.mishap.getReductionFactor());
			}
			else return this.check_move( friction, this.mishap.getReductionFactor());
		}
	}
	
	
	/**
	 * A helper method to avoid code duplication. Updates the racer's speed and location based on its current state.
	 * @param friction the friction factor to apply
	 * @param m a multiplier to adjust the acceleration
	 * @return the new location of the racer
	 */
	private Point check_move(double friction,double m)
	{
		if (this.currentSpeed < this.maxSpeed) this.setCurrentSpeed(this.currentSpeed + this.acceleration * friction*m);
		if (this.currentSpeed > this.maxSpeed) this.setCurrentSpeed(this.maxSpeed);
		this.setCurrentLocation(new Point((this.currentLocation.getX() + (1 * this.currentSpeed)), this.currentLocation.getY()));
		return this.getCurrentLocation();
	}
	
	
	/**
	 * Introduces the racer by printing its description to the console.
	 */
	public void introduce() { System.out.println("[" + this.className() + "] " + this.describeRacer()); }
	
	
	/**
	 * Returns a string describing the racer's characteristics.
	 * @return a string describing the racer
	 */
	public String describeRacer() 
	{
		return "name: " + this.getName() + ", SerialNumber: " + this.getSerialNumber() + ", maxSpeed: " + this.getMaxSpeed() + ", acceleration: " + this.getAcceleration() + ", color: " + this.getColor() + this.describeSpecific();
	}
	
	
	/**
	 * Determines if the racer is currently experiencing a mishap.
	 * @return true if the racer has a mishap and it has not been fixed, false otherwise
	 */
	public boolean hasMishap() { return this.getMishap() != null && this.getMishap().getTurnsToFix() > 0; }
	
	
	
	
	
	//										***	ABSTRACT METHODS	***
	
	/**
	 * Returns the class name of the racer.
	 * @return the class name of the racer
	 */
	public abstract String className();
	
	
	/**
	 * Returns a string containing any additional information specific to the racer's subclass.
	 * @return a string containing additional information about the racer
	 */
	public abstract String describeSpecific();	
	
	
	
	
	
	//										***	SETTERS & GETTERS	***
	
	/**
	 * Returns the static serial number of the racer class.
	 * @return the static serial number of the racer class
	 */
	public static int getSerial() { return Racer.serial; }
	
	
	/**
	 * Returns the serial number of the racer.
	 * @return the serial number of the racer
	 */
	public int getSerialNumber() { return this.serialNumber; }
	
	
	/**
	 * Returns the name of the racer.
	 * @return the name of the racer
	 */
	public String getName() { return this.name; }
	
	
	/**
	 * Returns the current location of the racer.
	 * @return the current location of the racer
	 */
	public Point getCurrentLocation() { return this.currentLocation; }
	
	
	/**
	 * Returns the finish line of the race.
	 * @return the finish line of the race
	 */
	public Point getFinish() { return this.finish; }
	
	
	/**
	 * Returns the arena where the race takes place.
	 * @return the arena where the race takes place
	 */
	public Arena getArena() { return this.arena; }
	
	
	/**
	 * Returns the maximum speed of the racer.
	 * @return the maximum speed of the racer
	 */
	public double getMaxSpeed() { return this.maxSpeed; }
	
	
	/**
	 * Returns the acceleration of the racer.
	 * @return the acceleration of the racer
	 */
	public double getAcceleration() { return this.acceleration; }
	
	

	/**
 	* Returns the current speed of the racer.
 	* @return the current speed of the racer
 	*/
	public double getCurrentSpeed() { return this.currentSpeed; }
	
	
	/**
	 * Returns the failure probability of the racer.
	 * @return the failure probability of the racer
	 */
	public double getFalureProbabilithy() { return this.failureProbability; }
	
	
	/**
	*Returns the mishap of the racer.
	*@return The mishap of the racer.
	*/
	public Mishap getMishap() { return this.mishap; }
	
	
	/**
	*Returns the color of the racer.
	*@return The color of the racer.
	*/
	public Color getColor() { return this.color; }
	
	
	/**
	Sets the name of the racer.
	@param name The name to set.
	@return True if the name was set successfully
	*/
	public boolean setName(String name) { this.name = name; return true; } 
	
	
	/**
	 * Sets the current location of the racer.
	 * If the specified location exceeds the length of the arena, it is adjusted to be within the bounds of the arena.
	 *
	 * @param currentLocation the new current location to be set
	 * @return true if the current location was successfully set.
	 */
	public boolean setCurrentLocation(Point currentLocation) 
	{ 
		if(currentLocation.getX() > arena.getLength()) 
			this.currentLocation = new Point(arena.getLength(), currentLocation.getY());
		else
			this.currentLocation = new Point(currentLocation.getX(), currentLocation.getY());
		return true;
	}
	
	
	/**
	*Sets the finish point of the racer.
	*@param finish The new finish point for the racer
	*@return true if set
	*/
	public boolean setFinish(Point finish) { this.finish = finish; return true; }
	
	
	/**
	*Sets the arena of the racer.
	*@param arena The new arena for the racer
	*@return true if set
	*/
	public boolean setArena(Arena arena) { this.arena = arena; return true; }
	
	
	/**
	*Sets the maximum speed of the racer.
	*@param maxSpeed The new maximum speed for the racer
	*@return true if set
	*/
	public boolean setMaxSpeed(double maxSpeed) { this.maxSpeed = maxSpeed; return true; }
	
	
	/**
	*Sets the acceleration of the racer.
	*@param acceleration The new acceleration for the racer
	*@return true if set
	*/
	public boolean setAcceleration(double acceleration) { this.acceleration = acceleration; return true; }
	
	
	/**
	*Sets the current speed of the racer.
	*@param currentSpeed The new current speed for the racer
	*@return true if set
	*/
	public boolean setCurrentSpeed(double currentSpeed) { this.currentSpeed = currentSpeed; return true; }
	
	
	/**
	*Sets the failure probability of the racer.
	*@param failureProbability The new failure probability for the racer
	*@return true if set
	*/
	public boolean setFailureProbability(double failureProbability) { this.failureProbability = failureProbability; return true; }
	
	
	/**
	*Sets the color of the racer.
	*@param color The new color for the racer.
	*@return true if set
	*/
	public boolean setColor(Color color) { this.color = color; return true; }
	
	
	/**
	*Sets the mishap of the racer.
	*@param mishap The new mishap for the racer.
	*@return true if set
	*/
	public boolean setMishap(Mishap mishap) 
	{
		this.mishap = new Mishap( mishap.getFixable(), mishap.getTurnsToFix(), mishap.getReductionFactor() );
		return true;
	}
	
	
	/**
	*Sets the serial number of the racer.
	*@param serialNumber The new serial number for the racer.
	*@return true if the serial number was set successfully, false otherwise.
	*/
	public boolean setSerialNumber(int serialNumber) { this.serialNumber = serialNumber; return true; }
	
	/**
	 * Sets the current state of the object.
	 * 
	 * @param current_state The state to be set.
	 */
	public void setState(State current_state) { state = current_state; }
	
	/**
	Increases the serial number of the racer by 1.
	*/
	public static void increasSerial() { Racer.serial++; }
	
	
	//								*** RUN METHOD ***
	/**
	 * Runs the racer's movement in the arena until it reaches the end.
	 * The racer continuously moves based on the arena's friction and updates its location.
	 * Once the racer reaches the end, it introduces itself, updates observers, and stops moving.
	 */
	public void run()
    {
        while (this.currentLocation.getX() < this.getArena().getLength())
        {
            try 
            {
                this.move(this.arena.getFriction());
                System.out.println(this.getCurrentLocation());
                Thread.sleep(100);
            }
            catch (InterruptedException ex) { ex.printStackTrace(); }
        }
        this.introduce();
        UpdateObservers();
    }
	
	
	//								*** PROTOTYPE ***
	/**
	 * Creates a clone of the current object.
	 * Using Design Pattern
	 * @return A new instance of the class that is a clone of the current object.
	 */
	public abstract Object clone();
	
}