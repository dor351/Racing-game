package State;

import game.racers.Racer;


/**
 * The Active class represents an implementation of the state interface for an active racer.
 * It sets the racer's state to active when the `doAction` method is called.
 */
public class Active implements State 
{
    /**
     * Sets the racer's state to active.
     * @param racer The racer whose state is being set to active.
     */
	@Override
    public synchronized void doAction(Racer racer) { racer.getArena().updateState(racer); }    
    
    /**
     * Returns a string representation of the Active state.
     * @return The string "Active".
     */
    @Override
    public String toString() { return "Active"; }
}