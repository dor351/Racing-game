/*
 * Dor Yehoshua 315619098
 * Hadar Isaacson 209831262
 */
package State;

import game.racers.Racer;

/**
 * The Completed class represents an implementation of the state interface for a completed racer.
 * It sets the racer's state to completed when the `doAction` method is called.
 */
public class Completed implements State 
{
    /**
     * Sets the racer's state to completed.
     * @param racer The racer whose state is being set to completed.
     */
	@Override
    public void doAction(Racer racer) { racer.getArena().updateState(racer); }
    
    
    /**
     * Returns a string representation of the Completed state.
     * @return The string "Completed".
     */
    @Override
    public String toString() { return "Completed"; }
}