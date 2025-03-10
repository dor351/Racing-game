/*
 * Dor Yehoshua 315619098
 * Hadar Isaacson 209831262
 */
package factory;

import game.arenas.Arena;

/**
 * The ArenaFactory class is responsible for creating different types of arenas based on the arena type provided.
 * It uses the RaceBuilder to construct the arenas.
 */
public class ArenaFactory
{
	private static RaceBuilder builder = RaceBuilder.getInstance();;
	
	
	/**
     * Constructs an arena of the specified type with the given length and maximum number of racers.
     * 
     * @param arenaType   the type of arena to create (e.g., "AerialArena", "LandArena", "NavalArena")
     * @param arenaLength the length of the arena
     * @param maxRacers   the maximum number of racers allowed in the arena
     * @return the constructed arena
     */
	public Arena getArena(String arenaType, int arenaLength, int maxRacers)
	{
		try 
		{
	      if(arenaType == null) 
	    	  return null;
	      
	      if(arenaType.equalsIgnoreCase("AerialArena"))
	         return builder.buildArena("game.arenas.air.AerialArena",arenaLength,maxRacers);
	         
	      else if(arenaType.equalsIgnoreCase("LandArena"))
	         return builder.buildArena("game.arenas.land.LandArena",arenaLength,maxRacers);
	         
	      else if(arenaType.equalsIgnoreCase("NavalArena"))
	         return builder.buildArena("game.arenas.naval.NavalArena",arenaLength,maxRacers);
	      
		}
		catch(Exception exc){ System.out.println(exc); }
	    return null;
	}
}
