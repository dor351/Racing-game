package game.racers;

import java.util.Hashtable;

import game.racers.air.Airplane;
import game.racers.air.Helicopter;
import game.racers.land.*;
import game.racers.naval.*;


/**
 * The Prototype class is responsible for managing the prototype racers.
 * It allows creating clones of racers based on their types.
 */
public class Prototype  
{
	private static Hashtable <String,Racer> RacerMap = new Hashtable <String,Racer>();
	
	 /**
     * Gets a clone of the racer based on its type.
     * 
     * @param racerType the type of racer to clone
     * @return a clone of the racer
     */
	public static Racer getRacer(String racerType) 
	{
		Racer proto = RacerMap.get(racerType);
		return (Racer) proto.clone();
	}
	
	
	/**
     * Adds a racer to the prototype racer map.
     * 
     * @param racer the racer to add
     */
	public static void addRacerToHash(Racer racer)
	{
		Prototype.RacerMap.put(""+racer.getSerialNumber(), racer);
	}
}