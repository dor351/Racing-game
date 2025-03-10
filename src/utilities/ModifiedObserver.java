/*
 * Dor Yehoshua 315619098
 * Hadar Isaacson 209831262
 */

package utilities;
/**
 * The interface defining the contract for a modified observer.
 * 
 * Classes implementing this interface can act as observers and receive updates
 * when a race is modified.
 */
public interface ModifiedObserver 
{
	/**
     * Updates the race with the provided object.
     *
     * This method is called when a race is modified, and the observer is notified
     * of the update. The specific implementation of the method depends on the
     * observer's behavior.
     *
     * @param o the object providing the update
     */
	public void update_race(Object o);
}