/*
 * Dor Yehoshua 315619098
 * Hadar Isaacson 209831262
 */
package game.arenas;


//											*** IMPORTS ***
import java.util.ArrayList;

import game.arenas.air.AerialArena;
import game.arenas.exceptions.RacerLimitException;
import game.arenas.exceptions.RacerTypeException;
import game.arenas.land.LandArena;
import game.arenas.naval.NavalArena;
import game.racers.Racer;
import game.racers.air.AerialRacer;
import game.racers.land.LandRacer;
import game.racers.naval.NavalRacer;
import utilities.ModifiedObserver;
import utilities.Point;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * The abstract class representing an arena for a race.
 * 
 * This class provides the basic functionality and properties of an arena, including the length,
 * maximum number of racers, friction factor, and lists of active and completed racers. It also
 * implements the `ModifiedObserver` interface to handle updates from racer threads.
 * 
 * Subclasses of `Arena` represent specific types of arenas, such as aerial, naval, or land arenas,
 * and provide additional functionality and behavior specific to their type.
 */
public abstract class Arena implements ModifiedObserver
{
	
	
	//										***	FIELDS	***
	private ArrayList<Racer> activeRacers, brokenRacers, disabledRacers, completedRacers;
	private final double FRICTION;
	private final int MAX_RACERS;
	private final static int MIN_Y_GAP = 10;
	private double length;
	
	
	
	
	//										***	CONSTRUSTOR	***
	
	/**
	 * Constructs a new Arena with the given length, maximum number of racers, and friction factor.
	 *
	 * @param length the length of the arena
	 * @param maxRacers the maximum number of racers allowed in the arena
	 * @param friction the friction factor of the arena
	 */
	public Arena(double length, int maxRacers, double friction)
	{
		this.FRICTION = friction;
		this.length = length;
		this.MAX_RACERS = maxRacers;
		this.activeRacers = new ArrayList<>();
        this.completedRacers = new ArrayList<>();
        this.setBrokenRacers(new ArrayList<Racer>());
		this.setDisabledRacers(new ArrayList<Racer>());
	}
	
	
	
	
	//										***	METHODS	***
	
	/**
	 * Adds a new racer to the race.
	 *
	 * This method adds the specified racer to the race. 
	 * It performs various checks to ensure compatibility between the racer type and the arena type,
	 * as well as checks for racer and arena capacity limits. 
	 * If any of the checks fail, the method throws appropriate exceptions.
	 *
	 * @param newRacer the racer object to be added to the race
	 * @throws RacerLimitException if the race has reached its maximum capacity of racers
	 * @throws RacerTypeException if the racer type is not compatible with the arena type
	 */
	public void addRacer(Racer newRacer) throws RacerLimitException, RacerTypeException
	{
		// Check that the racer type is compatible with the arena type
        if(this instanceof AerialArena)
        	if(!(newRacer instanceof AerialRacer)) 
        		throw new RacerTypeException("" + newRacer.className(), " Aerial arena.");
        
        if(this instanceof NavalArena)
        	if(!(newRacer instanceof NavalRacer))
        		throw new RacerTypeException("" + newRacer.className(), " Naval arena.");
        
        if(this instanceof LandArena)
        	if(!(newRacer instanceof LandRacer))
        		throw new RacerTypeException("" + newRacer.className(), " Land arena.");
		
		//check if the arena is full
		if(this.activeRacers.size() == getMaxRacers())
			throw new RacerLimitException(getMaxRacers(), newRacer.getSerialNumber());
		
		// if not then add a new racer
		newRacer.AddObject(this);
		this.addActiveRacer(newRacer);
		
	}
	
	/**
	 * Starts the race by initializing the race and executing the active racers.
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting for the race to finish.
	 */
	public void startRace() throws InterruptedException 
	{
		initRace();
		ExecutorService ex;
		synchronized (activeRacers) 
		{
			ex = Executors.newFixedThreadPool(this.activeRacers.size());
			synchronized (this) { for (Racer racer : activeRacers) ex.execute(racer); }
		}
		ex.shutdown();
		ex.awaitTermination(10, TimeUnit.MINUTES);
	}
	
	/**
	 * Initializes the race by setting a starting Y_GAP for every player.
	 */
	public void initRace()
	{
		int STARTING_Y_GAP = 0;
		for(Racer active_racer: this.getActiveRacers())
		{
			active_racer.initRace(this, new Point(0,STARTING_Y_GAP), new Point(this.getLength(),STARTING_Y_GAP));
			STARTING_Y_GAP += Arena.getMin_Y_Gap();
		}
	}
	
	
	/**
	 * Returns whether there are any active racers currently in the arena.
	 *
	 * @return true if there are active racers, false otherwise
	 */
	public boolean hasActiveRacers() { synchronized (activeRacers) { return this.activeRacers.size() > 0; } }
	
	
	/**
	 * Plays a turn for all the racers in the arena, checking if each racer has crossed the finish line.
	 * If a racer crosses the finish line, they are removed from the active racers list and added 
	 * to the completed racers list.
	 */
	public void playTurn()
	{
		for(int racer_lim = 0; racer_lim < this.getActiveRacers().size(); racer_lim++)
		{
			this.getActiveRacers().get(racer_lim).move(this.getFriction());
			if (this.getActiveRacers().get(racer_lim).getCurrentLocation().getX() >= this.getLength())
			{
				this.crossFinishLine(this.getActiveRacers().get(racer_lim));
				racer_lim--;
			}
		}
	}
	
	
	/**
	*Removes the racer from the active racers list and adds it to the completed racers list.
	*@param racer the racer that crossed the finish line
	*/
	public void crossFinishLine(Racer racer)
	{
		this.removeActiveRacer(racer);
		this.addCompletedRacers(racer);
	}
	
	
	/**
	*Prints a description of the results of the race.
	*/
	public void showResults()
	{
		int i = 0;
		for (Racer racer: this.getCompletedRacers())
		{
			System.out.println("#" + i + " -> " + racer.describeRacer());
			i++;
		}
	}
	
	
	
	//											*** SETTERS & GETTERS ***	
	
	/**
	*Returns the friction factor of the arena.
	*@return the friction factor of the arena
	*/
	public final double getFriction() { return this.FRICTION; }
	
	
	/**
	*Returns the maximum number of racers allowed in the arena.
	*@return the maximum number of racers allowed in the arena
	*/
	public final int getMaxRacers() { return this.MAX_RACERS; }
	
	
	/**
	*Returns the minimum Y_GAP between racers in the arena.
	*@return the minimum Y_GAP between racers in the arena
	*/
	public final static int getMin_Y_Gap() { return MIN_Y_GAP; }
	
	
	/**
	*Returns the length of the arena.
	*@return the length of the arena
	*/
	public double getLength() { return this.length; }
		

	/**
	 * Return the active racers list
	 * @return the active racers list 
	 */
	public ArrayList<Racer> getActiveRacers() { synchronized (activeRacers) { return activeRacers; } }
	
	
	/**
	 * Retrieves the list of broken racers.
	 *
	 * @return The list of broken racers.
	 */
	public ArrayList<Racer> getBrokenRacers() { synchronized (activeRacers) { return brokenRacers; } }
	
	
	/**
	 * Return the completed racers list
	 * @return the completed racers list
	 */
	public ArrayList<Racer> getCompletedRacers() { synchronized (activeRacers) { return completedRacers; } }
	
	
	/**
	 * Retrieves the list of disabled racers.
	 *
	 * @return The list of disabled racers.
	 */
	public ArrayList<Racer> getDisabledRacers() { synchronized (activeRacers) { return disabledRacers; } }
	
	
	/**
	*Sets the length of the arena.
	*@param length the new length of the arena
	*@return true if the length was set successfully
	*/
	public boolean setlength(double length) { this.length = length; return true; }
	
	
	/**
	*Sets the list of active racers.
	*@param activeRacers the new list of active racers
	*@return true if the list of active racers was set successfully
	*/
	public boolean setActiveRacers(ArrayList<Racer> activeRacers) { this.activeRacers = activeRacers; return true; }
	
	
	/**
	*Sets the list of completed racers.
	*@param completedRacers the new list of completed racers
	*@return true if the list of completed racers was set successfully
	*/
	public boolean setCompletedRacers(ArrayList<Racer> completedRacers) { this.completedRacers = completedRacers; return true; }
	
	
	/**
	 * Sets the list of broken racers.
	 * 
	 * @param brokenRacers The list of broken racers.
	 */
	public void setBrokenRacers(ArrayList<Racer> brokenRacers) { this.brokenRacers = brokenRacers; }
	
	
	/**
	 * Sets the list of disabled racers.
	 * 
	 * @param disabledRacers The list of disabled racers.
	 */
	public void setDisabledRacers(ArrayList<Racer> disabledRacers) { this.disabledRacers = disabledRacers; }
	
	/**
	*Helper method for adding a racer to the active racers list.
	*@param newRacer the racer to add to the active racers list
	*@return true if the list of completed racers was set successfully
	*/
	public boolean addActiveRacer(Racer newRacer) { this.activeRacers.add(newRacer); return true; }//help function
	
	
	/**
	*Helper method for removing a racer from the active racers list.
	*@param racer the racer to remove from the active racers list
	*@return true if the racer was removed successfully
	*/
	public boolean removeActiveRacer(Racer racer) { this.activeRacers.remove(racer); return true; }//help function
	
	
	/**
	*Helper method for adding a racer to the completed racers list.
	*@param racer the racer to add to the completed racers list
	*@return true if the list of completed racers was set successfully
	*/
	public boolean addCompletedRacers(Racer newRacer) { this.completedRacers.add(newRacer); return true; }//help function
	
	
	/**
	*Removes the completed racers from the given list of racers.
	*@param racers a list of Racer objects to remove completed racers from
	*@return true if the list of completed racers was set successfully
	*/
	public boolean removeCompletedRacers(Racer racer) { this.completedRacers.remove(racer); return true; }//help function


	//							*** OVVERIDE SYNCHRONIZED METHOD ***
	/**
	 * Updates the race with the provided racer thread.
	 *
	 * This method is called when a racer thread notifies the observer about an update in the race.
	 * It prints a message indicating the current thread ID and invokes the 'crossFinishLine' method to process the racer's crossing of the finish line.
	 *
	 * @param racer_thread the racer thread object providing the update
	 */
	@Override
    public synchronized void update_race(Object racer_thread) 
	{ 
		System.out.println("im updating " + Thread.currentThread().getId());
        crossFinishLine((Racer) racer_thread);
	}




	public abstract void updateState(Racer racer);
}