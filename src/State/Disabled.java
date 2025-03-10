/*
 * Dor Yehoshua 315619098
 * Hadar Isaacson 209831262
 */
package State;

import game.racers.Racer;

/**
 * The Disabled class represents an implementation of the state interface for a disabled racer.
 * It sets the racer's state to disabled when the `doAction` method is called.
 */
public class Disabled implements State 
{
    /**
     * Sets the racer's state to disabled.
     * @param racer The racer whose state is being set to disabled.
     */
    @Override
    public void doAction(Racer racer) { racer.getArena().updateState(racer); }    
    
    /**
     * Returns a string representation of the Disabled state.
     * @return The string "Failed".
     */
    @Override
    public String toString() { return "Failed"; }
}