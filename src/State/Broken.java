package State;

import game.racers.Racer;

/**
 * The Broken class represents an implementation of the state interface for a broken racer.
 * It sets the racer's state to broken when the `doAction` method is called.
 */
public class Broken implements State 
{
    /**
     * Sets the racer's state to broken.
     * @param racer The racer whose state is being set to broken.
     */
    @Override
    public synchronized void doAction(Racer racer) { racer.setState(this); }
    
    
    /**
     * Returns a string representation of the Broken state.
     * @return The string "Broken".
     */
    @Override
    public String toString() { return "Broken"; }
}
