package Builder;

import game.arenas.Arena;
import game.arenas.exceptions.RacerLimitException;
import game.arenas.exceptions.RacerTypeException;
import game.arenas.land.LandArena;
import game.racers.Prototype;
import game.racers.Racer;
import game.racers.land.Car;
import java.util.ArrayList;
import factory.ArenaFactory;


/**
* The CarRaceBuilder class is responsible for building a car race by creating a set number of racers
* and initializing the land arena for the race.
* It provides methods for setting the number of cars, setting the land arena, and starting the race.
* The class operates as follows:
* Upon construction, the CarRaceBuilder takes the number of cars (N) as a parameter and initializes
* the cars ArrayList and the land Arena.
* The setCars method uses the Prototype pattern to add a car racer to the hash and creates N racers
* by cloning the prototype. Each racer is assigned a serial number and added to the cars ArrayList.
* The setLandArena method creates a LandArena using the ArenaFactory and sets it as the land arena for
* the race. It then attempts to add each car racer to the arena, handling any RacerLimitException or
* RacerTypeException that may occur.
* The startRace method initiates the race by calling the initRace method of the land arena. It throws
* an InterruptedException if interrupted during the race.
* The CarRaceBuilder also provides getter methods to retrieve the cars ArrayList and the land Arena.
*/
public class CarRaceBuilder 
{
	private ArrayList<Racer> cars;
	private Arena land;
	private	int N;
	private ArenaFactory factory_arena_builder = new ArenaFactory();
	

	/**
 	* Constructs a CarRaceBuilder with the specified number of cars.
 	* 
 	* @param N the number of cars in the race
 	*/
	public CarRaceBuilder(int N)
	{
		this.N = N;
		setCars(N);
		setLandArena();
	}
	
	/**
	 * Sets the number of cars in the race.
	 * 
	 * @param N the number of cars
	 * @return true if the cars are successfully set, false otherwise
	 */
	public boolean setCars(int N) 
	{
		Prototype.addRacerToHash(new Car());
		cars = new ArrayList<Racer>(N);
		for (int i = 0; i < N; i++) 
		{
			// Using prototype to make clone of car racer
			Racer racer = Prototype.getRacer("1");
			racer.setSerialNumber(i + 1);
			// Add racer[i] to the array list
			cars.add(racer);
		}
		return true;
	}
	
	/**
	 * Sets the land arena for the race and adds the car racers to the arena.
	 * 
	 * @return true if the land arena is successfully set, false otherwise
	 */
	public boolean setLandArena()
	{
		land = factory_arena_builder.getArena("LandArena", LandArena.getDefLength(), N);
		//land = new LandArena(LandArena.getDefLength(), N);
		for (Racer car : getCars()) 
		{
			// try add the racer to the arena
			try { land.addRacer(car); } 
			catch (RacerLimitException | RacerTypeException e) {}
		}
		
		// initialize the race
		try { startRace(); } 
		catch (InterruptedException e) {}
		
		return true;
	}
	
	/**
	 * Starts the race by initiating the land arena's race.
	 * 
	 * @throws InterruptedException if the race is interrupted
	 */
	// Strating race 
	public void startRace() throws InterruptedException { land.initRace(); }
	

	/**
	 * Retrieves the ArrayList of car racers in the race.
	 *	
	 * @return the ArrayList of car racers
	 */
	public ArrayList<Racer> getCars(){ return cars; }
	
	/**
	 * Retrieves the land arena for the race.
	 * 
	 * @return the land arena
	 */
	public Arena getArena(){ return land; }
}