/*
 * Dor Yehoshua 315619098
 * Hadar Isaacson 209831262
 */

package gui;
//										*** IMPORTS ***
import factory.RaceBuilder;
import factory.ArenaFactory;
import Builder.CarRaceBuilder;
import game.arenas.Arena;
import game.arenas.exceptions.RacerLimitException;
import game.arenas.exceptions.RacerTypeException;
import game.racers.Prototype;
import game.racers.Racer;
import utilities.EnumContainer.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//-----------------------------------------------------------------------------------------------------------------------
/**
 * The View class represents the graphical user interface (GUI) for a racing game.
 * It extends the JFrame class and implements the ActionListener interface.
 */
//										*** CLASS IMPLEMENT ***
public class View extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;

	//									*** FIELDS ***
	
    private static RaceBuilder builder = RaceBuilder.getInstance();
    private ArrayList<Racer> racers;
    private int num_of_racers = 0;
    private int max_racers = 8;
    private int arena_length = 1000;
    private int arena_height = 700;
    private ImageIcon racers_icons[] = null;
    private String choosen_arena = null;
    private Arena arena = null;
    private JTextField arena_lenght_text;
    private JTextField max_racers_text;
    private JTextField racer_name_text;
    private JTextField max_speed_text;
    private JTextField acceleration_text;
    private JTextField car_race_numRacers;
    private JComboBox<String> arena_combo;
    private JComboBox<String> racers_combo;
    private JComboBox<String> color_combo;
    private boolean race_started = false;
    private boolean race_finished = false;
    private JFrame info_table_frame = null;
    private JPanel controls_panel;
    private CarRaceBuilder cars_builder;
    private ArenaFactory factory_arena_builder = new ArenaFactory();
    
    
    //									*** CONSTRUCTORS ***
    /**
     * Creates a new View object.
     * Initializes the GUI components and sets up the window.
     */
    public View() 
    {
        super("Race"); //setting top title
        RightPanel(); //setting the conrol_panel
        this.setContentPane(leftPanel()); //setting left side panel for the arenas
        this.pack(); //pack it
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize(); //get screen size and fit it so the sizes
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        this.setLocation(x, y); //set location by x,y
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //when click on X button
        this.setVisible(true); //make the window visible
    }

    
    //									*** METHODS ***
    /**
     * Handles the actions performed by the user.
     * This method is called when an action event occurs.
     * This method handle the try and catches by herself.
     * @param e The ActionEvent object representing the action performed.
     */
    public void actionPerformed(ActionEvent e) 
    {
        switch(e.getActionCommand()) //make a switch case by the action command
        {
          case "Build arena": 
          {
            if(race_started && !race_finished) //if the race is stated and not finished then show messege
            {
                JOptionPane.showMessageDialog(this, "Race already started!");
                break;
            }

            arena_length = Integer.parseInt(arena_lenght_text.getText()); //must to parse the int to convert it
            max_racers = Integer.parseInt(max_racers_text.getText()); //must to parse the int to convert it
            
            if (arena_length < 100 || arena_length > 3000 || max_racers <= 0 || max_racers > 20) //check if the given values compare with the game borders 
            {
                JOptionPane.showMessageDialog(this, "Invalid input values! Please try again.");
                break;
            }
            
            //set default variables for starting the game
            num_of_racers = 0;
            race_started = false;
            race_finished = false;

            //updating the height of the screen
            int update_height = (max_racers + 1) * 60;
            
            if (update_height > 700)
                this.arena_height = update_height;
            else
                this.arena_height = 700;
            
            //start make the racers and set memory for the racers icons
            racers = new ArrayList<>();
            racers_icons = new ImageIcon[max_racers];
            choosen_arena = (String) arena_combo.getSelectedItem();

            // Using ArenaFactory to make dynamic load of the arena, using design pattern
            arena = factory_arena_builder.getArena(choosen_arena, arena_length, max_racers);
            refresh();
            break;
          }
          
          case "Add racer":
          {
            if(race_started) //if the race started than you cant add racer, so need to show message
            {
                JOptionPane.showMessageDialog(this, "The race already started, the racer was not added!");
                break;
            }
            else if(race_finished) //if the race is finished you cant add racers you need to make new race
            {
                JOptionPane.showMessageDialog(this, "The race has already finished, the racer was not added!");
                break;
            }
            else if(num_of_racers == max_racers) //if the arena is full then you can't add racers
            {
                JOptionPane.showMessageDialog(this, "The arena is already full, the racer was not added!");
                break;
            }
            else if(arena == null) //if there is no arena then error message will appear
            {
                JOptionPane.showMessageDialog(this, "Arena was not built, the racer was not added!");
                break;
            }
            
            //if everything o'k then start add racer
            if(acceleration_text.getText().equals("") || max_speed_text.getText().equals(""))
            {
            	JOptionPane.showMessageDialog(this, "Please fill 'Max speed' and 'Acceleration'!");
                return;
            }
            String racer_name = racer_name_text.getText();
            double acceleration = Double.parseDouble(acceleration_text.getText()); //need to parse the double value
            double maxSpeed = Double.parseDouble(max_speed_text.getText()); //need to parse the double value
            if (racer_name.isEmpty() || maxSpeed <= 0 || acceleration <= 0 ) //check if there is any problems with the given values 
            {
                JOptionPane.showMessageDialog(this, "Invalid input values! please try again, the racer was not added!");
                break;
            }

            String racer_type = (String) racers_combo.getSelectedItem(); //convert the selected item from the combo box
            String racer_class = null;
            Color color = null;
            
            //make switch on the racer type cases, for every case let the path to make the correct racer
            switch (racer_type)
            {
                case "Airplane":
                    racer_class = "game.racers.air.Airplane";
                    break;
                    
                case "Helicopter":
                    racer_class = "game.racers.air.Helicopter";
                    break;
                    
                case "Car":
                    racer_class = "game.racers.land.Car";
                    break;
                    
                case "RowBoat":
                    racer_class = "game.racers.naval.RowBoat";
                    break;
                    
                case "SpeedBoat":
                    racer_class = "game.racers.naval.SpeedBoat";
                    break;        
                    
                case "Bicycle":
                    racer_class = "game.racers.land.Bicycle";
                    break;
                    
                case "Horse":
                    racer_class = "game.racers.land.Horse";
                    break;
            }
            
            //make a switch on the color cases and let the color get the enum color
            switch ((String) color_combo.getSelectedItem())
            {
                case "Red":
                    color = Color.RED;
                    break;
                    
                case "Black":
                    color=Color.BLACK;
                    break;
                    
                case "Blue":
                    color = Color.BLUE;
                    break;
                    
                case "Yellow":
                    color = Color.YELLOW;
                    break;
                    
                case "Green":
                    color = Color.GREEN;
                    break;
            }

            //if the racer type is wheeled then build a wheeled racer
            if(racer_type.equals("Car") || racer_type.equals("Airplane") || racer_type.equals("Bicycle"))
            {
                try 
                {
                    Racer racer = builder.buildWheeledRacer(racer_class, racer_name_text.getText(), Double.parseDouble(max_speed_text.getText()), Double.parseDouble(acceleration_text.getText()), color, 3);
                    racers.add(racer);
                    arena.addRacer(racer);
                    arena.initRace();
                    Prototype.addRacerToHash(racer);
                    racer.introduce();
                }
                catch (RacerTypeException type) //Exception type 
                { 
                    JOptionPane.showMessageDialog(this, type.getMessage());
                    break;
                }
                catch (Exception ex) { break; }
            }
            else //if its the rest of the racers
            {
                try 
                {
                    Racer racer = builder.buildRacer(racer_class, racer_name_text.getText(), Double.parseDouble(max_speed_text.getText()), Double.parseDouble(acceleration_text.getText()), color);
                    racers.add(racer);
                    arena.addRacer(racer);
                    arena.initRace();
                    Prototype.addRacerToHash(racer);
                    racer.introduce();
                } 
                catch (RacerTypeException type) //Exception type 
                { 
                    JOptionPane.showMessageDialog(this, type.getMessage());
                    break;
                } 
                catch (Exception ex) { break; }
            }
            
            // add the correct icons for the racers by the given values of the player choices
            racers_icons[num_of_racers] = new ImageIcon(new ImageIcon("icons/" + racer_type + color + ".png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
            num_of_racers++;
            refresh(); //refreshing the screen to show the new racer icon on the screen 
            break;
          }

          case "Copy racer":
          {
          		if(arena == null || num_of_racers == 0) //if there is no arena or racers then show message 
                {
                    JOptionPane.showMessageDialog(this, "Please build arena first and add racers!");
                    break;
                }
                else if(race_started) //if the race already started then you cant start new one
                {
                    JOptionPane.showMessageDialog(this, "The race already started!");
                    break;
                }
                else if(race_finished) //if its finished then start a new one
                {
                    JOptionPane.showMessageDialog(this, "The race already finished!");
                    break;
                }
          		JPanel panel = new JPanel();
          		panel.setLayout(new GridLayout(3, 2));
  	          
          		// Create labels and text fields for inputs
          		JLabel serial_lable = new JLabel("Serial number:");
          		JTextField serial_field = new JTextField();
          		JLabel color_label = new JLabel("new color:");
          		JTextField color_field = new JTextField();
          		
          		
          		// Add the labels and fields to the panel
          		panel.add(serial_lable);
          		panel.add(serial_field);
          		panel.add(color_label);
          		panel.add(color_field);
          		
          		int result = JOptionPane.showConfirmDialog(null, panel, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
          		
          		String serialNumber = serial_field.getText();
          	    String colorString = color_field.getText();
          	    Color color = null;

          	    switch (colorString) 
          	    {
          	        case "Red":
          	            color = Color.RED;
          	            break;
          	        case "Black":
          	            color = Color.BLACK;
          	            break;
          	        case "Blue":
          	            color = Color.BLUE;
          	            break;
          	        case "Yellow":
          	            color = Color.YELLOW;
          	            break;
          	        case "Green":
          	            color = Color.GREEN;
          	            break;
          	    }

          	    if (color != null) 
          	    {
          	        Racer clonedRacer = Prototype.getRacer(serialNumber);
          	        if (clonedRacer != null) 
          	        {
          	            clonedRacer.setColor(color);
          	            racers.add(clonedRacer);
          	            try { arena.addRacer(clonedRacer); }
          	            catch (RacerLimitException | RacerTypeException e1) {}
          	            arena.initRace();
          	            Prototype.addRacerToHash(clonedRacer);
          	            
          	            clonedRacer.introduce();
          	            
          	            
          	            racers_icons[num_of_racers] = new ImageIcon(new ImageIcon("icons/" + clonedRacer.className() + color + ".png")
          	                    .getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
          	            num_of_racers++;
          	            refresh();    
          	        }
          	        else JOptionPane.showMessageDialog(this, "Invalid serial number value! Please try again.");
          	    }
          	    else
    	        {
          	    	Racer clonedRacer = Prototype.getRacer(serialNumber);
          	    	if (clonedRacer != null) 
          	    	{
          	    	racers.add(clonedRacer);
    	            try { arena.addRacer(clonedRacer); }
    	            catch (RacerLimitException | RacerTypeException e1) {}
    	            arena.initRace();
    	            Prototype.addRacerToHash(clonedRacer);
    	            clonedRacer.introduce();
    	            //System.out.println(""+clonedRacer.getSerialNumber());
    	            racers_icons[num_of_racers] = new ImageIcon(new ImageIcon("icons/" + clonedRacer.className() + clonedRacer.getColor() + ".png")
    	                    .getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
    	            num_of_racers++;
    	            refresh();
          	    	}
    	        }
          	    break;
          }
                    	
          case "Start race":
          {	
                if(arena == null || num_of_racers == 0) //if there is no arena or racers then show message 
                {
                    JOptionPane.showMessageDialog(this, "Please build arena first and add racers!");
                    break;
                }
                else if(race_started) //if the race already started then you cant start new one
                {
                    JOptionPane.showMessageDialog(this, "The race already started!");
                    break;
                }
                else if(race_finished) //if its finished then start a new one
                {
                    JOptionPane.showMessageDialog(this, "The race already finished!");
                    break;
                }
                
                //starting the race
                race_started = true;
                startRace();
                break;
          }
          
          case "Start cars race":
          {
        	  if(race_started) //if the race already started then you cant start new one
              {
                  JOptionPane.showMessageDialog(this, "The race already started!");
                  break;
              }
        	  
        	  JPanel panel = new JPanel();
        	  panel.setLayout(new GridLayout(3, 2));
	          
        	  // Create labels and text fields for inputs
        	  JLabel num_of_racers_lable = new JLabel("Number of racers:");
        	  car_race_numRacers = new JTextField();
        		
        	  // Add the labels and fields to the panel
        	  panel.add(num_of_racers_lable);
        	  panel.add(car_race_numRacers);
        	  
              int result = JOptionPane.showConfirmDialog(null, panel, "Enter Details", JOptionPane.OK_CANCEL_OPTION);
              
              max_racers = Integer.parseInt(car_race_numRacers.getText()); //must to parse the int to convert it
              cars_builder = new CarRaceBuilder(max_racers);
              arena = cars_builder.getArena();
              racers = cars_builder.getCars();
              arena_length = 800;
              choosen_arena = "LandArena";
              racers_icons = new ImageIcon[max_racers];
              num_of_racers = max_racers;
              
              // Add racers icons
              for(int racer_index = 0; racer_index < max_racers; racer_index++)
            	  racers_icons[racer_index] = new ImageIcon(new ImageIcon("icons/CarRed.png").getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT));
        	  
              // Starting race
              startRace();
              break;
          }
            
            case "Show info":
            {
                if(arena == null || num_of_racers == 0) //if there is no arena or racers
                {
                    JOptionPane.showMessageDialog(this, "Cannot show racers information, please try again!");
                    break;
                }

                //setting the table colomns
                int row = 0;
                String[] colomn_names = {"Racer name", "Current speed", "Max speed", "Current X location", "Finished"};
                String[][] info = new String[num_of_racers][5];
                
                //filling the active racers table
                for (Racer r : arena.getCompletedRacers()) 
                {
    				info[row][0] = r.getName();
    				info[row][1] = "" + r.getCurrentSpeed();
    				info[row][2] = "" + r.getMaxSpeed();
    				info[row][3] = "" + r.getCurrentLocation().getX();
    				info[row][4] = "COMP";
    				row++;
    			}

    			for (Racer r : arena.getActiveRacers()) 
    			{
    				info[row][0] = r.getName();
    				info[row][1] = "" + r.getCurrentSpeed();
    				info[row][2] = "" + r.getMaxSpeed();
    				info[row][3] = "" + r.getCurrentLocation().getX();
    				info[row][4] = "ACT";
    				row++;
    			}
    			for (Racer r : arena.getBrokenRacers()) 
    			{
    				info[row][0] = r.getName();
    				info[row][1] = "" + r.getCurrentSpeed();
    				info[row][2] = "" + r.getMaxSpeed();
    				info[row][3] = "" + r.getCurrentLocation().getX();
    				info[row][4] = "BROK";
    				row++;
    			}
    			for (Racer r : arena.getDisabledRacers()) 
    			{
    				info[row][0] = r.getName();
    				info[row][1] = "" + r.getCurrentSpeed();
    				info[row][2] = "" + r.getMaxSpeed();
    				info[row][3] = "" + r.getCurrentLocation().getX();
    				info[row][4] = "DIS";
    				row++;
    			}

                
                //making the info table frame
                JTable info_table = new JTable(info, colomn_names);
                info_table.setPreferredScrollableViewportSize(info_table.getPreferredSize());
                JScrollPane scrollPane = new JScrollPane(info_table); //make the table frame scrollable

                JPanel table = new JPanel();
                table.add(scrollPane);

                if (info_table_frame != null) info_table_frame.dispose();
                
                info_table_frame = new JFrame("Racers information");
                info_table_frame.setContentPane(table);
                info_table_frame.pack();
                info_table_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                info_table_frame.setVisible(true);
                break;
            }
        }
      }
  
    
    /**
     * Creates and configures the right panel of the GUI.
     * The right panel contains various controls and buttons for configuring the race.
     * This method initializes and sets the layout of the controls panel, adds labels, combo boxes, text fields,
     * and buttons to the panel, and sets their locations and sizes.
     * It also attaches action listeners to the buttons.
     */
    private void RightPanel()
    {
    	controls_panel = new JPanel();
        controls_panel.setLayout(null);
        controls_panel.setPreferredSize(new Dimension(140, arena_height)); 
        //^^setting the controls panel fit to every game

        String[] arenas_options = {"AerialArena", "LandArena", "NavalArena"};
        arena_combo = new JComboBox<String>(arenas_options);
        JLabel choose_arena_label = new JLabel("Choose arena:");
        controls_panel.add(choose_arena_label);
        choose_arena_label.setLocation(10,20);
        choose_arena_label.setSize(100,10);
        controls_panel.add(arena_combo);
        arena_combo.setLocation(10, 40);
        arena_combo.setSize(125, 20);
        
        JLabel arena_length_label = new JLabel("Arena Length:");
        controls_panel.add(arena_length_label);
        arena_length_label.setLocation(10,75);
        arena_length_label.setSize(100,10);
        arena_lenght_text = new JTextField("" + arena_length);
        controls_panel.add(arena_lenght_text);
        arena_lenght_text.setLocation(10,95);
        arena_lenght_text.setSize(100,25);

        JLabel max_racers_number_label = new JLabel("Max Racers number:");
        controls_panel.add(max_racers_number_label);
        max_racers_number_label.setLocation(10,135);
        max_racers_number_label.setSize(150,10);
        max_racers_text = new JTextField("" + max_racers);
        max_racers_text.setLocation(10,155);
        max_racers_text.setSize(100,25);
        controls_panel.add(max_racers_text);

        JButton build_arena_button = new JButton("Build arena");
        controls_panel.add(build_arena_button);
        build_arena_button.setSize(100,30);
        build_arena_button.setLocation(10,195);
        build_arena_button.addActionListener(this); //add listener to this button

        JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);
        seperator.setLocation(0, 240);
        seperator.setSize(150, 10);
        controls_panel.add(seperator);

        JLabel choose_racer_label = new JLabel("Choose racer:");
        controls_panel.add(choose_racer_label);
        choose_racer_label.setLocation(10,242);
        choose_racer_label.setSize(100,20);
        String[] racers_list = {"Airplane", "Helicopter", "Car", "Bicycle", "Horse", "RowBoat", "SpeedBoat"};
        racers_combo = new JComboBox<String>(racers_list);
        controls_panel.add(racers_combo);
        racers_combo.setLocation(10,262);
        racers_combo.setSize(120,20);

        JLabel choose_color_label = new JLabel("Choose color:");
        controls_panel.add(choose_color_label);
        choose_color_label.setLocation(10,290);
        choose_color_label.setSize(100,20);
        String[] Colors_list = {"Black", "Red", "Green", "Blue", "Yellow"};
        color_combo = new JComboBox<String>(Colors_list);
        controls_panel.add(color_combo);
        color_combo.setLocation(10,310);
        color_combo.setSize(120,20);

        JLabel racer_name_label = new JLabel("Racer name:");
        controls_panel.add(racer_name_label);
        racer_name_label.setLocation(10,340);
        racer_name_label.setSize(100,20);
        racer_name_text = new JTextField();
        controls_panel.add(racer_name_text);
        racer_name_text.setLocation(10,360);
        racer_name_text.setSize(100,25);

        JLabel max_speed_label = new JLabel("Max speed:");
        controls_panel.add(max_speed_label);
        max_speed_label.setLocation(10,400);
        max_speed_label.setSize(100,20);
        max_speed_text = new JTextField();
        controls_panel.add(max_speed_text);
        max_speed_text.setLocation(10,420);
        max_speed_text.setSize(100,25);

        JLabel acceleration_label = new JLabel("Acceleration");
        controls_panel.add(acceleration_label);
        acceleration_label.setLocation(10,460);
        acceleration_label.setSize(100,25);
        acceleration_text = new JTextField();
        controls_panel.add(acceleration_text);
        acceleration_text.setLocation(10,480);
        acceleration_text.setSize(100,25);

        JButton add_racer_button = new JButton("Add racer");
        controls_panel.add(add_racer_button);
        add_racer_button.setLocation(10,520);
        add_racer_button.setSize(100,30);
        add_racer_button.addActionListener(this); //add listener to this button
        
        JButton copy_racer_button = new JButton("Copy racer");
        controls_panel.add(copy_racer_button);
        copy_racer_button.setLocation(10,555);
        copy_racer_button.setSize(100,30);
        copy_racer_button.addActionListener(this); //add listener to this button
        
        JSeparator seperator2 = new JSeparator(SwingConstants.HORIZONTAL);
        seperator2.setLocation(0, 590);
        seperator2.setSize(150, 10);
        controls_panel.add(seperator2);

        JButton start_race_button = new JButton("Start race");
        start_race_button.setLocation(10,595);
        start_race_button.setSize(100,30);
        controls_panel.add(start_race_button);
        start_race_button.addActionListener(this); //add listener to this button

        JButton start_cars_race_button = new JButton("Start cars race");
        start_cars_race_button.setLocation(10,630);
        start_cars_race_button.setSize(100,30);
        start_cars_race_button.addActionListener(this); //add listener to this button
        controls_panel.add(start_cars_race_button);
        
        JButton showInfoButton = new JButton("Show info");
        showInfoButton.setLocation(10,665);
        showInfoButton.setSize(100,30);
        showInfoButton.addActionListener(this); //add listener to this button
        controls_panel.add(showInfoButton);
    }

    
    /**
     * Creates and configures the left panel of the GUI.
     * The left panel contains the arena image, racer icons, and the controls panel.
     * This method initializes and sets the layout of the arena panel, adds the arena image,
     * sets the locations and sizes of the image and racer icons, and attaches the controls panel to the main panel.
     * The main panel combines the arena panel, a vertical separator, and the controls panel using a BorderLayout.
     *
     * @return The main panel containing the left panel components.
     */
    private JPanel leftPanel() 
    {
        JPanel arena_panel = new JPanel();
        arena_panel.setLayout(null);
        arena_panel.setPreferredSize(new Dimension(arena_length + 80, arena_height));
        
        //setting choosen arena img
        ImageIcon image_icon = new ImageIcon(new ImageIcon("icons/" + choosen_arena + ".jpg").getImage().getScaledInstance(arena_length + 80, arena_height, Image.SCALE_DEFAULT)); // Set arena pic
        JLabel icon_label = new JLabel(image_icon);
        icon_label.setBounds(0, 0, arena_length + 80, arena_height);
        arena_panel.add(icon_label);
        // keep on spacing
        int Y_space_GAP = 50;
        
        //make icons labels of all the choosens racers
        for (int i = 0; i < num_of_racers; i++) 
        {
            JLabel new_icon_label = new JLabel(racers_icons[i]);
            new_icon_label.setBounds((int) racers.get(i).getCurrentLocation().getX(),
                    (int) racers.get(i).getCurrentLocation().getY() + (i * Y_space_GAP), 70, 70);
            icon_label.add(new_icon_label);
        }
        
        //show on screen
        JPanel main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout());
        main_panel.add(arena_panel, BorderLayout.WEST);
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        main_panel.add(separator, BorderLayout.CENTER);
        main_panel.add(controls_panel, BorderLayout.EAST);
    
        return main_panel;
    }
    
    
    /**
     * Refreshes the GUI by updating the controls panel, setting the content pane with the left panel,
     * adjusting the size of the window, centering it on the screen, and making it visible.
     * This method is typically called when there are changes in the GUI that need to be reflected on the screen.
     */
    private void refresh()
    {
        controls_panel.setPreferredSize(new Dimension(140, arena_height));
        this.setContentPane(leftPanel());
        this.pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        this.setLocation(x, y);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    } 
    
    
    /**
	*
    * Starts the race by creating a new thread for the race loop and starting threads for each racer.
	*
    * The race loop continuously refreshes the screen and checks if there are any active racers remaining.
	*
    * Once all racers have completed the race, the race is considered finished.
	*
    * The method operates as follows:
	*
    * Sets the race_started flag to true to indicate that the race has started.
    * Creates a new thread for the race loop and starts it.
    * For each active racer in the arena, creates a new thread and starts it.
    * @throws InterruptedException if the race thread is interrupted while sleeping
    */
    public void startRace()
    {
        //starting the race
        race_started = true;
        (new Thread() //making the thread, making method run for let the racers run 
        {
             public void run() 
             {
                 while (arena.hasActiveRacers()) 
                 {
                   try { Thread.sleep(30); } //try let the current racer sleep for 30[MS] 
                   catch (InterruptedException e) { e.printStackTrace(); }
                   refresh(); //refreshing the screen
                 }
                 race_finished = true; //when finish the loop the race is over
                 refresh(); //need a new refresh 
             }
        	}).start(); //make the thread start

        	//make threads for every racers and start the thread
            for(int i = 0 ;i < arena.getActiveRacers().size(); i++)
            {
                Thread thread = new Thread(arena.getActiveRacers().get(i)); //every thread is racers
                thread.start();
            }
    }
    
    
    /**
     * The main entry point of the application.
     * Creates an instance of the View class, starting the game.
     *
     * @param args the command line arguments (not used in this application)
     */
    public static void main(String[] args) { new View(); }//running the game 
}