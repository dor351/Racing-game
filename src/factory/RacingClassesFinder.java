package factory;

//														*** IMPORTS ***
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import game.arenas.Arena;
import game.racers.Racer;


/**
The RacingClassesFinder class is responsible for finding and loading racing-related classes in the game package.
It follows the Singleton design pattern to ensure only one instance of the class exists.
*/
public class RacingClassesFinder 
{
	//													*** FIELDS ***
	private static String GAME_PACKAGE = "game";
	private static String GAME_PACKAGE_DIR = "src/game";
	private static RacingClassesFinder instance;
	
	

	//													*** METHODS ***
	/**
	 * Retrieves the singleton instance of RacingClassesFinder.
	 *
	 * @return The RacingClassesFinder instance.
	 */
	public static RacingClassesFinder getInstance() 
	{
		if (instance == null) 
			instance = new RacingClassesFinder();
		return instance;
	}

	private List<Class<?>> classList;
	private List<Class<?>> racersList;
	private List<Class<?>> arenasList;
	
	
	

	/**
	*Constructs a new instance of the RacingClassesFinder class.
	* The RacingClassesFinder class is responsible for finding and loading racing-related classes in the game package.
	* It follows the Singleton design pattern to ensure only one instance of the class exists.
	* This private constructor initializes the class by loading the classes from the game package directory,
	* and populates the arenasList and racersList based on the loaded classes.
	* If any ClassNotFoundException or FileNotFoundException occurs during class loading,
	* the stack trace will be printed for debugging purposes.
 	*/
	private RacingClassesFinder() 
	{
		try { this.classList = this.loadClasses(new File(GAME_PACKAGE_DIR), GAME_PACKAGE); } 
		catch (ClassNotFoundException e) { e.printStackTrace();} 
		catch (FileNotFoundException e) { e.printStackTrace(); }
		this.arenasList = loadArenas();
		this.racersList = loadRacers();
	}
	
	
	
	/**
	 * Retrieves the list of loaded arenas classes.
	 *
	 * @return The list of loaded arenas classes.
	 */
	public List<Class<?>> getArenasList() { return arenasList; }
	
	
	
	/**
	 * Retrieves the list of loaded racers classes.
	 *
	 * @return The list of loaded racers classes.
	 */
	public List<Class<?>> getRacersList() { return racersList; }
	
	
	
	/**
	 * Retrieves the names of the loaded arenas.
	 *
	 * @return The list of arena names.
	 */
	public List<String> getArenasNamesList() 
	{
		List<String> list = new ArrayList<>();
		for (Class<?> c : this.arenasList) 
		{
			String s = c.getName();
			list.add(s.substring(s.lastIndexOf('.') + 1));
		}
		return list;
	}
	
	
	
	/**
	 * Retrieves the names of the loaded racers.
	 *
	 * @return The list of racer names.
	 */
	public List<String> getRacersNamesList() 
	{
		List<String> list = new ArrayList<>();
		for (Class<?> c : this.racersList) 
		{
			String s = c.getName();
			list.add(s.substring(s.lastIndexOf('.') + 1));
		}
		return list;
	}
	
	
	
	/**
	* Loads and retrieves the list of Arena classes from the loaded classes.
	* @return The list of loaded Arena classes.
	*/
	private List<Class<?>> loadArenas() 
	{
		List<Class<?>> list = new ArrayList<>();
		for (Class<?> cls : classList) 
			if (Arena.class.isAssignableFrom(cls) && (Modifier.isAbstract(cls.getModifiers()) == false)) 
				list.add(cls);
		return list;
	}
	
	
	
	/**
	* Recursively loads and retrieves the list of classes from the specified directory and package name.
	* @param directory The directory to search for classes.
	* @param packageName The package name of the classes.
	* @return The list of loaded classes.
	* @throws ClassNotFoundException If a class is not found during loading.
	* @throws FileNotFoundException If the specified directory does not exist.
	*/
	private List<Class<?>> loadClasses(File directory, String packageName)
			throws ClassNotFoundException, FileNotFoundException 
	{
		List<Class<?>> list = new ArrayList<Class<?>>();

		if (!directory.exists()) throw new FileNotFoundException();
		File[] files = directory.listFiles();
		for (File file : files) 
		{
			if (file.isDirectory()) 
			{
				assert !file.getName().contains(".");
				list.addAll(loadClasses(file, packageName + "." + file.getName()));
			}
			else if (file.getName().endsWith(".java")) 
				list.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 5)));
		}
		return list;
	}
	
	
	
	/**
	* Loads and retrieves the list of Racer classes from the loaded classes.
	* @return The list of loaded Racer classes.
 	*/
	private List<Class<?>> loadRacers() 
	{
		List<Class<?>> list = new ArrayList<>();
		for (Class<?> cls : classList) 
			if (Racer.class.isAssignableFrom(cls) && (Modifier.isAbstract(cls.getModifiers()) == false)) 
				list.add(cls);
		return list;
	}
}