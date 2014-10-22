package garagemechanic;

import garagemechanic.Dialogs.DialogFactory;
import garagemechanic.Dialogs.DialogType;
import garagemechanic.ObjectModels.Customer;
import garagemechanic.ObjectModels.MaintenanceAction;
import garagemechanic.ObjectModels.MaintenanceType;
import garagemechanic.ObjectModels.Mechanic;
import garagemechanic.ObjectModels.Vehicle;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 * Main class that acts as the main processor for the program
 * @author mark.milford
 */
public class GarageMechanicProcessor {
    private DatabaseAccessor currentDatabaseAccessor;

    private HomeWindow currentHomeWindow;

    private String currentMechanicName = "None Available";
    private String currentCustomerName = "None Available";
    private String currentVehicleName = "None Available";
   
    private Mechanic currentMechanic;
    private Customer currentCustomer;
    private Vehicle currentVehicle;

    
    
    private DialogFactory dialogFactory = new DialogFactory();
    
    /**
     * Main Constructor
     */
    public GarageMechanicProcessor(){
        this.currentDatabaseAccessor = new DatabaseAccessor();
        HomeWindow testWindow = new HomeWindow(this); // create main window, pass along processor
        this.currentHomeWindow = testWindow;
        this.currentHomeWindow.setVisible(true);
    }
    
    
    /**
     * Public Method to take a Mechanic Name and query the database to see if that name exists
     * @param attemptedMechanicName, the name you are wanting to know if it exists in the current database
     * @return true if mechanic name exists, false otherwise
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public boolean doesMechanicExist(String attemptedMechanicName) throws ClassNotFoundException, SQLException{
        boolean mechanicExists = false;
        String attemptedMechanicNameLowerCase = attemptedMechanicName.toLowerCase();
        
        String[] mechanicNames = this.currentDatabaseAccessor.getMechanicNames();
        
        for(String mechanicName : mechanicNames){
            if(mechanicName.toLowerCase().equals(attemptedMechanicNameLowerCase)){
                mechanicExists = true;
            }
        }
        return mechanicExists;
    }
    
    /**
     * Method to find out if there is a database available and selected
     * @return return true if the database accessor has a database, false if not
     */
    public boolean isDatabaseOpened(){
        return this.currentDatabaseAccessor.getDatabaseSelectedBool();
    }
    
    /**
     * Private method for the GUI to request the processor to pop a message based on GUI determined circumstances(ie, attempting to
     * add a Mechanic without first specifiying a name, throw an error!)
     * @param dialogType, the type of dialog box you want to pop
     * @param message , the message in the dialog box
     */
    public void createDialogMessage(DialogType dialogType, String message){
        this.dialogFactory.createDialogMessage(dialogType, message);
    }
    
    public boolean promptUserForDatabasePassword() throws ClassNotFoundException, SQLException{
        boolean userGuessedPassword = false;
        int passwordAttempts = 0;
        String password = this.currentDatabaseAccessor.getDatabasePassword();
        String userPasswordAttempt;
        while(passwordAttempts < 3){
            String message = "What is this database's password?";
            userPasswordAttempt = (String)JOptionPane.showInputDialog(currentHomeWindow, message);
            //if(userPasswordAttempt!=null){
            //    this.promptUserForDatabasePassword();
            //}
            if(userPasswordAttempt!=null && !userPasswordAttempt.isEmpty()){
                if(userPasswordAttempt.equals(password)){
                    userGuessedPassword = true;
                    String goodMessage ="Password accepted. Access granted.";
                    this.createDialogMessage(DialogType.INFORMATION_MESSAGE, goodMessage);
                    break;
                }
                passwordAttempts++;
            }else{
                // they didn't attempt to enter a correct password(ie closing dialog, not entering one, etc).
                this.promptUserForDatabasePassword();
            }
        }
        if(!userGuessedPassword){
            String badMessage ="You failed to guess the database's password three times! Access denied.";
            this.createDialogMessage(DialogType.ERROR_MESSAGE, badMessage);
        }
        return userGuessedPassword;
    }

    /**
     * Method to be ran when opening a new Database File
     * Should update the Database Accessor
     * @param incomingDatabasePath 
     */
    public void openNewDatabase(String incomingDatabasePath) throws ClassNotFoundException, SQLException{
        //ensure the last three characters are ".db"
        String databaseExtension = incomingDatabasePath.substring(Math.max(incomingDatabasePath.length() - 3, 0));
        String databaseExtensionLower = databaseExtension.toLowerCase();
        if(databaseExtensionLower.equals(".db")){
            System.out.println("That looks like a proper db file!");
            this.currentDatabaseAccessor.openNewDatabase(incomingDatabasePath);
            // perform password check here
            boolean userKnowsDatabasePassword = this.promptUserForDatabasePassword();
            if(!userKnowsDatabasePassword){
                // USER FAILED TO GUESS PASSWORD, close connection, exit this method
                this.currentDatabaseAccessor.closeDatabaseConnection();
                return;
            }
            //while(!userKnowsDatabasePassword){
            //    userKnowsDatabasePassword = this.promptUserForDatabasePassword();
            //}
            
            if(this.currentDatabaseAccessor.getMechanicCount() > 0){
                System.out.println("The Database contains mechanics");
                // get teh first mechanic returned and set it as the current. makes sense
                String[] mechanicList  = this.getMechanicNameArray();
                String firstMechanic = mechanicList[0];
                this.setCurrentMechanicName(firstMechanic);// this will take care of GUI updates
                
            }else if(this.currentDatabaseAccessor.getMechanicCount() == 0){
                System.out.println("the Database contains NO mechanics!");
            }
        }else{
           System.out.println("Well crap, that might not be a proper db file. ");
        }

    }
    /**
     * Method to get the current mechanic name
     * @return current mechanic name
     */
    public String getCurrentMechanicName(){
        return this.currentMechanicName;
    }
    

    
    /**
     * Method to set the current mechanic(name), used when changing between mechanics, VERY IMPORTANT, 
     * does GUI Update calls
     * @param mechanicName 
     */
    public void setCurrentMechanicName(String mechanicName) throws ClassNotFoundException, SQLException{ 
        System.out.println("Log - setCurrentMechanicName being called");
        System.out.println("Log - The mechanic you are trying to set it to is : " + mechanicName);
        if(!this.currentMechanicName.equals(mechanicName)){
            this.currentMechanicName = mechanicName;

            // since we are changing to a NEW mechanic, lets get the customer name array and set the FIRST one to current customer
            if(this.currentDatabaseAccessor.getCustomerCountByMechanic(this.currentMechanicName)> 0){
                String[] customerArrayForMechanic = this.currentDatabaseAccessor.getCustomerNames(this.currentMechanicName);
                String firstCustomer = customerArrayForMechanic[0];
                this.setCurrentCustomerName(firstCustomer);
                
            }else{
                this.currentCustomerName = "None Available";
            }
            // perform same check but for vehicles, but only perform if there is a current customer
            if(!this.currentCustomerName.equals("None Available")){
                // there is a customer, ensure that customer has vehicles first
                if(this.currentDatabaseAccessor.getVehicleCountByCustomer(this.currentCustomerName)>0){
                    // at least one vehicle, set current vehicle to one of them?
                    System.out.println("Current Customer has a vehicle, need to add it to current");
                    // get array of vehicle's, add first vehicle as current
                    Vehicle[] customerVehicles = this.currentDatabaseAccessor.getVehiclesForCustomer(currentCustomerName);           
                    this.currentVehicle = customerVehicles[0];
                    
                    this.currentVehicleName = this.currentVehicle.toString();
                    
                    //this.currentHomeWindow.updateVehicleTab();
                }
            }else{
                this.currentVehicleName="None Available";
            }

           
            // update the entire GUI, as you just updated the Mechanic which should waterfall
            // NOTE Set CurrentCustomer Name also updates the customer gui, so we're doing it twice(ok, but unneeded)
            this.currentHomeWindow.updateGUI();
        }else{
            // throw error?
            System.out.println("You tried setting a new mechanic to one that is already the current mechanic?");
        }
    }
    
    /**
     * Method to set the current vehicle name, used when changing between vehicles, does GUI update calls
     * @param vehicleName 
     */
    public void setCurrentVehicleName(String vehicleName) throws ClassNotFoundException, SQLException{
        System.out.println("log - setCurrentVehicleName");
        
        if(!this.currentVehicleName.equals(vehicleName)){
            this.currentVehicleName = vehicleName;
            this.currentHomeWindow.updateVehicleTab();
            //this.currentHomeWindow.updateMaintenanceActionsTab();
        }
    }
    
    public void setCurrentVehicle(Vehicle incomingVehicle){
        this.currentVehicle = incomingVehicle;
    }
    
    /**
     * Method to get the current vehicle name
     * @return this.currentVehicleName
     */
    public String getCurrentVehicleName(){
        return this.currentVehicleName;
    }
    
    /**
     * Method to get the current vehicle
     * @return this.currentVehicle
     */
    public Vehicle getCurrentVehicle(){
        return this.currentVehicle;
    }
    
    /**
     * Method to set the current customer name, used when changing between customers, does GUI Update calls
     * @param customerName 
     */
    public void setCurrentCustomerName(String customerName) throws ClassNotFoundException, SQLException{
        System.out.println("Log - setCurrentCustomerName being called");
        System.out.println("Log - the customer you are trying to set it to is " + customerName);
        
        if(!this.currentCustomerName.equals(customerName)){
            this.currentCustomerName = customerName;
            // NEW HERE TESTING JESUS GO OVER THIS
            // there is a customer, ensure that customer has vehicles first
            if(this.currentDatabaseAccessor.getVehicleCountByCustomer(this.currentCustomerName)>0){
                // at least one vehicle, set current vehicle to one of them?
                System.out.println("Current Customer has a vehicle, need to add it to current");
                // get array of vehicle's, add first vehicle as current
                Vehicle[] customerVehicles = this.currentDatabaseAccessor.getVehiclesForCustomer(currentCustomerName);           
                this.currentVehicle = customerVehicles[0];
                    
                this.currentVehicleName = this.currentVehicle.toString();
                    
                //this.currentHomeWindow.updateVehicleTab();
            }else{
                //current customer DOESN"T have a vehicle...
                this.currentVehicleName = "None Available";
            }
            // TIME TO UPDATE CUSTOMER GUI HERE
            this.currentHomeWindow.updateCustomerTab();
            // update vehicle tab
            this.currentHomeWindow.updateVehicleTab();
        }
    }
    

    /**
     * Method to create a new vehicle. Will take the new Vehicle Make/Model/Year/Odometer/Notes from the GUI, and will supply 
     * the current customer name AND find the NEXT Unique Vehicle Id
     * @param make
     * @param model
     * @param year
     * @param odometer
     * @param notes
     * @param customerName 
     * @return Vehicle object with fields filled out for evaluation
     */
    public Vehicle createNewVehicle(String make, String model, int year, int odometer, String notes) throws SQLException, ClassNotFoundException{
        // find next unique id (vehicle count + 1)
        int nextUniqueId = (this.currentDatabaseAccessor.getVehicleCount())+1;
        
        this.currentDatabaseAccessor.createNewVehicle(nextUniqueId, make, model, year, odometer, notes, this.currentCustomerName);
        Vehicle newVehicle = new Vehicle();
        newVehicle.setId(nextUniqueId);
        newVehicle.setMake(make);
        newVehicle.setModel(model);
        newVehicle.setYear(year);
        newVehicle.setOdometer(odometer);
        newVehicle.setNotes(notes);
        newVehicle.setCustomerName(this.getCurrentCustomerName());
        return newVehicle;
    }
    
    /**
     * Method to take a new customer name, new customer description, and use the current mechanic and add that 
     * customer to the current mechanic using the database accessor
     * @param newCustomerName
     * @param description
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
   public void createNewCustomer(String newCustomerName, String description) throws SQLException, ClassNotFoundException{
       this.currentDatabaseAccessor.createNewCustomer(newCustomerName, description, this.currentMechanicName);
   }
    
    public String getCurrentCustomerName(){
        return this.currentCustomerName;
    }
    
    /**
     * Method to get the current customer description
     * @return String of the current customer's description
     */
    public String getCurrentCustomerDescription() throws ClassNotFoundException, SQLException{
        return this.currentDatabaseAccessor.getCustomerDescription(currentCustomerName);
    }
    
    /**
     * Method to take a new customer name and find out if that name exists(case insensitive)
     * @param newCustomerName, the name of the new customer you are probably wanting to create
     * @return true if that customer exists, false if not
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public boolean doesCustomerExist(String newCustomerName) throws SQLException, ClassNotFoundException{
        boolean customerExists = false;
        String newCustomerNameLowerCase = newCustomerName.toLowerCase();

        String[] currentCustomerNames = this.currentDatabaseAccessor.getAllCustomerNames();
        
        for(String customerName : currentCustomerNames){
            String customerNameToLowerCase = customerName.toLowerCase();
            if(newCustomerNameLowerCase.equals(customerNameToLowerCase)){
                customerExists = true;
                break;
            }
        }
        return customerExists;
    }
    
    
    /**
     * Method to utilize the database accessor and create a new mechanic
     * @param newMechanicName
     * @param description
     * @param password
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public void createNewMechanic(String newMechanicName, String description) throws SQLException, ClassNotFoundException{
        this.currentDatabaseAccessor.createNewMechanic(newMechanicName, description);
    }
    
    /**
     * Method to access the DB accessor and return the current mechanic's description
     * @return 
     */
    public String getCurrentMechanicDescription(){
        return this.currentDatabaseAccessor.getMechanicDescription(currentMechanicName);
    }
        

    
    /**
     * method to get the names of the mechanics and TURN IT INTO A STRING ARRAY(drop downs only take array)
     * 
     * @return String[] to build the list of mechanic names for it's drop down
     */
    public String[] getMechanicNameArray(){
        if(currentDatabaseAccessor.getDatabaseSelectedBool() == true){
            if(currentDatabaseAccessor.getMechanicCount()== 0){
                // we have no mechanics in the database!
                return new String[] { "No Mechanics" };
            }
            else{
                String[] mechanicArrayUnsorted = null;
                try {
                    mechanicArrayUnsorted = currentDatabaseAccessor.getMechanicNames();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GarageMechanicProcessor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GarageMechanicProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
                return mechanicArrayUnsorted;
                }
        }
        else if(currentDatabaseAccessor.getDatabaseSelectedBool() == false){
            return new String[]{""};
        }
        else{
            return new String[]{"Crap, something bad happened"};                
        }
    }
    
    
    /**
     * Method to get all the customers for a specific Mechanic, with No Customers as the first one
     * @return String[] of Customers, with None Selected as the first one
     * @param mechanicName String, the mechanic who's customers you want to get
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public String[] getCustomerNameArray(String mechanicName) throws ClassNotFoundException, SQLException{
       if (mechanicName.equals("None Available")){
           return new String[] { "No Customers" };
       }
       int customerCount = this.currentDatabaseAccessor.getCustomerCountByMechanic(mechanicName);
       if(customerCount == 0){
           return new String[] { "No Customers" };
       }else{
           String[] customersForMechanic = this.currentDatabaseAccessor.getCustomerNames(mechanicName);
           return customersForMechanic;
       }
    }
    
    /**
     * Method to take a vehicle string(toString on a vehicle object) and return the ACTUAL Vehicle Object
     * for evaluation
     * @param vehicleString
     * @return Vehicle object
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public Vehicle getVehicleFromString(String vehicleString) throws ClassNotFoundException, SQLException{
        boolean digitOnePresent = Character.isDigit(vehicleString.charAt(3));
        boolean digitTwoPresent = Character.isDigit(vehicleString.charAt(4));
        boolean digitThreePresent = Character.isDigit(vehicleString.charAt(5));
        String id = "";
        // third digit will be present, year of the car (if only 1 digit ID)
        if(digitOnePresent && !(digitTwoPresent) && (digitThreePresent)){
            //System.out.println("Only ONE DIGIT LONG");
            //System.out.println(vehicleString.charAt(3));
            id = id + vehicleString.charAt(3);
        }else if(digitOnePresent && (digitTwoPresent) && !(digitThreePresent)){
            //System.out.println("TWO DIGITS LONG");
            //System.out.println(vehicleString.charAt(3));
            //System.out.println(vehicleString.charAt(4));
            id = id + vehicleString.charAt(3) + vehicleString.charAt(4);
        }else if(digitOnePresent && (digitTwoPresent) && (digitThreePresent)){
            //System.out.println("THREE DIGITS LONG");
            //System.out.println(vehicleString.charAt(3));
            //System.out.println(vehicleString.charAt(4));
            //System.out.println(vehicleString.charAt(5));
            id = id + vehicleString.charAt(3) + vehicleString.charAt(4)+vehicleString.charAt(5);
        }
        
        int realId = Integer.parseInt(id);
        Vehicle vehicle = this.currentDatabaseAccessor.getVehicleById(realId);
        
        return vehicle;
    }
    
    /**
     * Method to return an array of maintenance actions for the current vehicle
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public MaintenanceAction[] getMaintenanceActionsForCurrentVehicle() throws ClassNotFoundException, SQLException{

        // build array of maintenance actions for current vehicle, return it
        Vehicle vehicle = this.getCurrentVehicle();
        int currentVehicleId = vehicle.getId();
        
        MaintenanceAction[] maintenanceActionsForCurrentVehicle = this.currentDatabaseAccessor.getMaintenanceActionsForSpecificVehicle(currentVehicleId);     
        return maintenanceActionsForCurrentVehicle;
    }
    
    /**
     * Method to create a new Maintenance Action, requiring MaintenanceAction Object(with fields
     * maintenanceActionName, vehicle's odometer, and notes)
     * Will get the VehicleId by using the current Vehicle
     * @param maintenanceAction, the maintenanceAction you want to create(will add vehicle Id here)
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void createNewMaintenanceAction(MaintenanceAction maintenanceAction) throws ClassNotFoundException, SQLException{
        // add vehicle id to  maintenanceAction, and submit it to DB Accessor for process
        Vehicle vehicle = this.getCurrentVehicle();
        int currentVehicleId = vehicle.getId();
        
        maintenanceAction.setVehicleId(currentVehicleId);
        
        this.currentDatabaseAccessor.createNewMaintenanceAction(maintenanceAction);

    }
    
    /**
     * Method to get a sorted array of Vehicle objects(current is first), to help build the vehicle drop down
     * @return Vehicle[] array, sorted with current as first
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public Vehicle[] getVehicleDropdownListWithCurrentFirst() throws ClassNotFoundException, SQLException{
        Vehicle[] currentVehicleList = this.currentDatabaseAccessor.getVehiclesForCustomer(this.currentCustomerName);
        int numberOfVehiclesForCustomer = this.currentDatabaseAccessor.getVehicleCountByCustomer(this.currentCustomerName);
   
        Vehicle[] sortedVehicleList = new Vehicle[numberOfVehiclesForCustomer];
        // add current Vehicle to first position 
        sortedVehicleList[0] = this.currentVehicle;
        // go through unsorted list,and if not current vehicle, add to sorted list
        int count =1; // start at one, as you already added one to list
        for(Vehicle vehicle : currentVehicleList){
            // id's are unique
            if(vehicle.getId()!=this.currentVehicle.getId()){
                sortedVehicleList[count] = vehicle;
                count++;
            }
        }
        return sortedVehicleList;
    }
    
    /**
     * Method to return a String[] of all the maintenance types, used to build drop down
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public String[] getMaintenanceTypeDropdown() throws ClassNotFoundException, SQLException{
        // get all the maintenace types, build a String[] using those, and return it
        MaintenanceType[] maintenanceTypeArray = this.currentDatabaseAccessor.getMaintenanceTypes();
        int numberOfMaintenanceItems = this.currentDatabaseAccessor.getMaintenaceTypeCount();
        String[] maintenanceNamesInArray = new String[numberOfMaintenanceItems];
        int counter = 0;
        for(MaintenanceType maintenanceType : maintenanceTypeArray){
            maintenanceNamesInArray[counter] = maintenanceType.getMaintenanceTypeName();
            counter++;
        }
        return maintenanceNamesInArray;
    }
    
    /**
     * Method to take a Maintenance Type and call the associated DB Accessor method to create that in the DB
     * @param maintenanceType 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public void createNewMaintenanceType(MaintenanceType maintenanceType) throws SQLException, ClassNotFoundException{
        this.currentDatabaseAccessor.createNewMaintenanceType(maintenanceType);
    }
    
    /**
     * Method to take a maintenanceTypeName and determine if there is a MaintenanceType with same name already
     * in current Database
     * @param maintenanceTypeName, the name you want to check
     * @return true if already exists, false if not!
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public boolean doesMaintenanceTypeExist(String maintenanceTypeName) throws ClassNotFoundException, SQLException{
        String maintenanceTypeToLowerCase = maintenanceTypeName.toLowerCase();
        MaintenanceType[] maintenanceTypes = this.currentDatabaseAccessor.getMaintenanceTypes();
        boolean maintenanceTypeExists = false;
        
        for(MaintenanceType maintenanceType : maintenanceTypes){
            if(maintenanceTypeToLowerCase.equals(maintenanceType.getMaintenanceTypeName().toLowerCase())){
                maintenanceTypeExists = true;
                break;
                        
            }
        }
        return maintenanceTypeExists;
    }
    
    /**
     * Method to return a MaintenanceType Object when given a MaintenanceTypeName
     * @param maintenanceTypeName
     * @return maintenanceType Object
     */
    public MaintenanceType getMaintenanceType(String maintenanceTypeName) throws ClassNotFoundException, SQLException{
        
        return (this.currentDatabaseAccessor.getMaintenanceType(maintenanceTypeName));
    }
    
    /**
     * Method to return a mechanic name array that is sorted with the current as first
     * @return 
     */
    public String[] getMechanicDropdownNameListWithCurrentFirst(){
        String[] currentMechanicsAvailable = this.getMechanicNameArray();
        String mechanic = this.getCurrentMechanicName();

        String[] sortedMechanicDropdownList = this.getDropDownArrayWhereCurrentIsFirst(currentMechanicsAvailable, mechanic);
        
        return sortedMechanicDropdownList;
    }
    
    /**
     * Method to get a array, for a dropdown, and return that list with the current item first! This is for Mechanics, Vehicles, and Customers.
     * 
     * @param arrayToSort
     * @param type "Mechanics" for a mechanic list, "Customers" for a customer list, "Vehicles" for a vehicle list
     * @return that list with the current "type" FIRST
     */
    public String[] getDropDownArrayWhereCurrentIsFirst(String[] listToSort,String currentItem){
        List<String> tempList= new ArrayList<String>();
        // go through the array and pull out the "current" item, and add it FIRST to the temp list
        for(String itemToSort : listToSort){
            if(itemToSort.equals(currentItem)){
                tempList.add(itemToSort);
            }
        }
        // now go back through the array and pull all the other"non" current items, add them to the list
        for(String itemToSort : listToSort){
            if(!itemToSort.equals(currentItem)){
                tempList.add(itemToSort);
            }
        }
        String[] sortedArray = tempList.toArray(new String[tempList.size()]);
        return sortedArray;
    }
}
