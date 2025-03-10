

package utilities;
import java.util.Vector;

/**
 * The ModifiedObserveable class represents an observable object that can be observed by multiple ModifiedObserver objects.
 * It provides methods for adding and removing observers, as well as updating all observers when a change occurs.
 */
public class ModifiedObserveable 
{
	
	//														*** FIELDS ***
	private Vector<ModifiedObserver> objects = new Vector<>();
	
	
	//														*** METHODS ***
	/**
     * Adds a ModifiedObserver object to the list of observers.
     *
     * @param object the ModifiedObserver object to be added
     */
	public void AddObject(ModifiedObserver object) { this.objects.add(object); }
	
	
	/**
     * Removes a ModifiedObserver object from the list of observers.
     *
     * @param object the ModifiedObserver object to be removed
     */
	public void DeleteObject(ModifiedObserver object) { this.objects.remove(object); }
	
	
	/**
     * Updates all observers by invoking their update_race() method.
     * This method is called when a change occurs in the observable object.
     */
	protected void UpdateObservers()
    {
        for( ModifiedObserver object : objects)
            object.update_race(this);
    }
}