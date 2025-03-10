package State;

import game.racers.Racer;

/**
 * The state interface represents the behavior of a state in the context of a racer.
 * Classes implementing this interface define the actions that can be performed
 * when the racer is in a particular state.
 */
public interface State 
{
    /**
     * Performs the action associated with the state on the given racer.
     * @param racer The racer on which the action is performed.
     */
    public void doAction(Racer racer);
}