package garagemechanic;

import garagemechanic.ObjectModels.MaintenanceAction;
import garagemechanic.ObjectModels.MaintenanceType;
import garagemechanic.ObjectModels.Vehicle;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This will be the class that acts as the bridge between the client and the DB,
 * in this case SQLITE.
 * @author mark.milford
 */
public class DatabaseAccessor {
    private boolean databaseSelected = false;
    private String DBPath ="";
    private String DBFilePath = "";
    
    private Connection connection = null;  
    private ResultSet resultSet = null;  
    private Statement statement = null;  
    
    
    /**
     * Default constructor
     *  
    */
    public DatabaseAccessor(){
        //System.out.println("Database Accessor created.");
        //this.createTestDB();
    }
    
    public boolean getDatabaseSelectedBool(){
        return this.databaseSelected;
    }
    
    public void setDatabaseSelectedBool(boolean bool){
        this.databaseSelected = bool;
    }
    
    /**
     * Method to set the paths to a new database,aka open a database
     * @param incomingDatabasePath 
     */
    public void openNewDatabase(String incomingDatabasePath){
        // the file is being tested (.db, exists, etc) at the processor level, so we'll trust for now
        this.DBFilePath = incomingDatabasePath;
        System.out.println("The new db file path is " + this.DBFilePath);
        this.DBPath = "jdbc:sqlite:" + incomingDatabasePath;
        System.out.println("The new db path is " + this.DBPath);
        this.setDatabaseSelectedBool(true);
    }
    
    /**
     * Method to close the database connection. Used when password verification fails.
     */
    public void closeDatabaseConnection(){
        this.databaseSelected = false;
        this.DBPath ="";
        this.DBFilePath = "";
    }
    
    /**
     * Method to determine if the path supplied correlates to the DB.
     * // TODO ensure that the file name actually ends in .db, ensure the schema 
     * is correct, etc
     * @param dbPath The incoming pathname to check
     * @return true if it's a working GarageMechanic DB File, False if not
     */
    public boolean doesDBExist(){
        if(new File(this.DBFilePath).exists()){
            System.out.println("DB ACCESSOR IS SAYING IT DOES EXIST");
            return true;
        }
        else{
            System.out.println("DB ACCESSOR IS SAYING IT DOESN'T EXIST?");
            return false;
        }
    }
   
    /**
     * Method to take a vehicle Id and return the Vehicle Object
     * @param id
     * @return Vehicle Object
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public Vehicle getVehicleById(int id) throws ClassNotFoundException, SQLException{
         Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT * FROM Vehicles WHERE id="+id+";"); 
        Vehicle vehicle=null;
        while (resultSet.next()){
            vehicle = new Vehicle();
            vehicle.setId(Integer.parseInt(resultSet.getString(1)));
            vehicle.setMake(resultSet.getString(2));
            vehicle.setModel(resultSet.getString(3));
            vehicle.setYear(Integer.parseInt(resultSet.getString(4)));
            vehicle.setOdometer(Integer.parseInt(resultSet.getString(5)));
            vehicle.setNotes(resultSet.getString(6));
            vehicle.setCustomerName(resultSet.getString(7));
        }   
        return vehicle;
    }
    
    /**
     * Method to get the password from the database
     * @return password, the password as a String
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public String getDatabasePassword() throws ClassNotFoundException, SQLException{
        String password="";
        Class.forName("org.sqlite.JDBC");  

        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT * FROM Password;"); 
        while (resultSet.next()){
            password = resultSet.getString(1);
            
        }
        
        return password;
    }
    
    /**
     * Method to return an array of ALL SYSTEM MaintenanceTypes
     * @return arrayOfMaintenanceTypes, Array of MaintenanceTypes (all)
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public MaintenanceType[] getMaintenanceTypes() throws ClassNotFoundException, SQLException{
        // get count of all maintenance types, used to build the array
        int countOfMaintenanceTypes = this.getMaintenaceTypeCount();
        MaintenanceType[] arrayOfMaintenanceTypes = new MaintenanceType[countOfMaintenanceTypes];

         Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT * FROM MaintenanceTypes;"); 
        
        int counter = 0;
        
        while (resultSet.next()){
            MaintenanceType maintenanceType = new MaintenanceType();
            maintenanceType.setMaintenanceTypeName(resultSet.getString(1));
            maintenanceType.setDescription(resultSet.getString(2));
            maintenanceType.setInterval(Integer.parseInt(resultSet.getString(3)));
            arrayOfMaintenanceTypes[counter] = maintenanceType;
 
            counter ++;
        }   
        return arrayOfMaintenanceTypes;
    }
    
    /**
     * Method to get a specific MaintenanceType when supplied with that MaintenanceTypeName
     * @param maintenanceTypeName, the name of the MaintenanceType you want to get the information on
     * @return MaintenanceType Ojbect filled out
     */
    public MaintenanceType getMaintenanceType(String maintenanceTypeName) throws ClassNotFoundException, SQLException{
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT * FROM MaintenanceTypes WHERE MaintenanceTypeName ='"+maintenanceTypeName+"';"); 
        MaintenanceType maintenanceType = new MaintenanceType();
        while(resultSet.next()){
            maintenanceType.setMaintenanceTypeName(resultSet.getString(1));
            maintenanceType.setDescription(resultSet.getString(2));
            maintenanceType.setInterval(Integer.parseInt(resultSet.getString(3)));
        }
        return maintenanceType;
    }
    
    /**
     * Method to count the maintenance types and return it
     * @return count of maintenance types
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public int getMaintenaceTypeCount() throws SQLException, ClassNotFoundException{
         Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT COUNT(*) FROM MaintenanceTypes;"); 
        int maintenanceTypeCount = 0;
        while(resultSet.next()){
            maintenanceTypeCount = Integer.parseInt(resultSet.getString(1));
        }
        return maintenanceTypeCount;
    }
    
    /**
     * Method to get all the customer names for ALL mechanics
     * @return String[] of all the customer names
     */
    public String[] getAllCustomerNames() throws SQLException, ClassNotFoundException{
        int numberOfCustomers = this.getCustomerCount();
        int counter = 0;
        String[]customerNames = new String[numberOfCustomers];
        
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT CustomerName FROM Customers;");  
        while (resultSet.next()){
            //System.out.println(resultSet.getString(1));
            customerNames[counter] = resultSet.getString(1);
            counter = counter +1;
        }   
        
        return customerNames;
    }
    
    /**
     * Method to return a String[] of all the customers for a specific mechanic
     * @param mechanic, the name of the Mechanic you want to get the customers from
     * @return String[] of that mechanic's customers
     */
    public String[] getCustomerNames(String mechanic) throws ClassNotFoundException, SQLException{
        int customerCount= this.getCustomerCountByMechanic(mechanic);
        int counter =0;

        String[] temporaryCustomerArray = new String[customerCount];
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT CustomerName FROM Customers WHERE MechanicName='" + mechanic +"';");  
        while (resultSet.next()){
            //System.out.println(resultSet.getString(1));
            temporaryCustomerArray[counter] = resultSet.getString(1);
            counter = counter +1;
            }   
        return temporaryCustomerArray;
    }
    
    /**
     * Method to take a MaintenanceType Object and insert a maintenabce Type into the Database
     * @param maintenanceType, the maintenanceType object you want inserted into the database
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public void createNewMaintenanceType(MaintenanceType maintenanceType) throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        String name = maintenanceType.getMaintenanceTypeName();
        String description = maintenanceType.getDescription();
        int interval = maintenanceType.getInterval();
        this.statement.executeUpdate("INSERT INTO MaintenanceTypes VALUES('"+name+"','"+description+"',"+interval+");");
    }
    
    /**
     * Method to take a new customer name, new customer description, and their corresponding mechanic(IMPORTANT), and create 
     * said customer in the database
     * 
     * @param newCustomerName, the name of the new customer
     * @param description, the description for the new customer
     * @param mechanicName, the corresponding mechanic for the customer
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public void createNewCustomer(String newCustomerName, String description, String mechanicName) throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();          
        this.statement.executeUpdate("INSERT INTO Customers VALUES('"+newCustomerName+"','"+description+"','"+mechanicName+"');");
    }
       
    /**
     * PRIVATE method to get a count of how many maintenance actions a specific vehicle has
     * @param incomingVehicleId
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    private int getMaintenanceActionsCountForSpecificVehicle(int incomingVehicleId) throws ClassNotFoundException, SQLException{
        int countOfMaintenanceActionsForSpecificVehicle = 0;
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT COUNT(*) FROM MaintenanceActions WHERE VehicleId=" + incomingVehicleId + ";");   
        while (resultSet.next()){
            countOfMaintenanceActionsForSpecificVehicle = Integer.parseInt(resultSet.getString(1));
            }  
        return countOfMaintenanceActionsForSpecificVehicle;
    }
    
    /**
     * Method to create a new Maintenance Action, using the Maintenace Action Object
     * @param maintenanceAction, Maintenance Action object
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void createNewMaintenanceAction(MaintenanceAction maintenanceAction) throws ClassNotFoundException, SQLException{
        int vehicleId = maintenanceAction.getVehicleId();
        String maintenanceTypeName = maintenanceAction.getMaintenanceTypeName();
        int odometer = maintenanceAction.getOdometer();
        String notes = maintenanceAction.getNotes();
        
        Class.forName("org.sqlite.JDBC");  

        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();          
        this.statement.executeUpdate("INSERT INTO MaintenanceActions VALUES("+vehicleId+",'"+maintenanceTypeName+"',"+odometer+",'"+notes+"');");        
    }
    
    /**
     * Method to take a Vehicle Id and return a MaintenanceAction[] for that specific Vehicle
     * @param incomingVehicleId, the id of the vehicle you want to get the maintenance actions for
     * @return 
     */
    public MaintenanceAction[] getMaintenanceActionsForSpecificVehicle(int incomingVehicleId) throws ClassNotFoundException, SQLException{
        int countOfMaintenanceActions = this.getMaintenanceActionsCountForSpecificVehicle(incomingVehicleId);
        MaintenanceAction[] maintenanceActions = new MaintenanceAction[countOfMaintenanceActions];
        
        Class.forName("org.sqlite.JDBC");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT * FROM MaintenanceActions WHERE VehicleId=" + incomingVehicleId + ";");   
        int counter =0;
        while (resultSet.next()){
            MaintenanceAction maintenanceAction = new MaintenanceAction();
            maintenanceAction.setVehicleId(Integer.parseInt(resultSet.getString(1)));
            maintenanceAction.setMaintenanceTypeName(resultSet.getString(2));
            maintenanceAction.setOdometer(Integer.parseInt(resultSet.getString(3)));
            maintenanceAction.setNotes(resultSet.getString(4));
            maintenanceActions[counter] = maintenanceAction;
            counter++;            
        }   
        return maintenanceActions;
    }
    
    /**
     * query to get customer description
     */
    public String getCustomerDescription(String customerName) throws ClassNotFoundException, SQLException{
        String customerDescription="No Description";
 
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT Description FROM Customers WHERE CustomerName='" + customerName + "';");   
        while (resultSet.next()){
            customerDescription = resultSet.getString(1);
            }   

        return customerDescription;
    }
    
    
    /**
     * query to get the mechanic names and return a String[]
     */
    public String[] getMechanicNames() throws ClassNotFoundException, SQLException{
        int mechanicCount= this.getMechanicCount();
        int counter =0;
        //List<String> temporaryMechanicList = new ArrayList<String>(1);        
        String[] temporaryMechanicArray = new String[mechanicCount];

        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT NAME FROM Mechanics;");  
        while (resultSet.next()){
            //System.out.println(resultSet.getString(1));
            temporaryMechanicArray[counter] = resultSet.getString(1);
            counter = counter +1;
            }   

        return temporaryMechanicArray;
    }
    
    /**
     * query to count the mechanics
     */
    public int getMechanicCount(){
        int mechanicCount= 0;
 
        try{
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT COUNT(*) FROM Mechanics;");  
        while (resultSet.next()){
            mechanicCount = Integer.parseInt(resultSet.getString(1));
            }   
        }
        catch (Exception e){ 
            e.printStackTrace();  
            }
        finally{
            try{  
                resultSet.close();  
                statement.close();  
                connection.close();  
            } 
        catch (Exception e){ 
            e.printStackTrace();  
            }  
        }
        return mechanicCount;
    }
    
    /**
     * query to get mechanic description
     */
    public String getMechanicDescription(String mechanicName){
        String mechanicDescription="No Description";

        try{
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT Description FROM Mechanics WHERE Name='" + mechanicName + "';");   
        while (resultSet.next()){
            mechanicDescription = resultSet.getString(1);
            }   
        }
        catch (Exception e){ 
            e.printStackTrace();  
            }
        finally{
            try{  
                resultSet.close();  
                statement.close();  
                connection.close();  
            } 
        catch (Exception e){ 
            e.printStackTrace();  
            }  
        }
        return mechanicDescription;
    }
    

    /**
     * Method to create a new vehicle, will require all vehicle fields (NOTE: will fail if CustomerName isn't set correctly!)
     * @param uniqueId
     * @param make
     * @param model
     * @param year
     * @param odometer
     * @param notes
     * @param customerName
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public void createNewVehicle(int uniqueId, String make, String model, int year, int odometer, String notes, String customerName) throws SQLException, ClassNotFoundException{
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();          
        this.statement.executeUpdate("INSERT INTO Vehicles VALUES("+uniqueId+",'"+make+"','"+model+"',"+year+","+odometer+",'"+notes+"','"+customerName+"');");
    }
    
    /**
     * Method to take a new mechainc name, description, and password, and INSERT those values into the DATABASE 
     * @param newMechanicName
     * @param description
     * @param password
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public void createNewMechanic(String newMechanicName, String description) throws SQLException, ClassNotFoundException{

        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();          
        this.statement.executeUpdate("INSERT INTO Mechanics VALUES('"+newMechanicName+"','"+description+"');");
    }
    
    /**
     * Method to get a count of ALL customers in the database
     * @return customerCount, an int 
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public int getCustomerCount() throws SQLException, ClassNotFoundException{
        int customerCount =0;
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT COUNT(*) FROM Customers;");
        while (resultSet.next()){
            // should only be ONE because only one row is returned
            customerCount = Integer.parseInt(resultSet.getString(1));
            }   
        return customerCount;
    }
    
    /**
     * Method to return a count of all vehicles. Important for unique Id Creation. 
     * For example, if 4 vehicles found, next vehicle should have ID5, and so forth
     * @return number of vehicles in system.
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public int getVehicleCount() throws SQLException, ClassNotFoundException{
        int vehicleCount = 0;
        Class.forName("org.sqlite.JDBC");  
         
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT COUNT(*) FROM Vehicles;");
        while (resultSet.next()){
            // should only be ONE because only one row is returned
            vehicleCount = Integer.parseInt(resultSet.getString(1));
        }   
        return vehicleCount;
    }
    
    /**
     * Method to take a customer name and return how many vehicles belong to said customer
     * @param customerName, the name of the customer you want to find out how many vehicles they own
     * @return vehicleCount, and int that is a count of the number of vehicles they have
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public int getVehicleCountByCustomer(String customerName) throws ClassNotFoundException, SQLException{
        int vehicleCount = 0;
        Class.forName("org.sqlite.JDBC");  
         
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT COUNT(*) FROM Vehicles WHERE CustomerName='" + customerName + "';");
        while (resultSet.next()){
            // should only be ONE because only one row is returned
            vehicleCount = Integer.parseInt(resultSet.getString(1));
        }   
        return vehicleCount;
    }
    
    /**
     * Method to take a customer name and return an array of Vehicle objects that belong
     * to said customer
     * @param customerName, the name of the customer who's vehicles you're interested in
     * @return Vehicle[] for that customer
     */
    public Vehicle[] getVehiclesForCustomer(String customerName) throws ClassNotFoundException, SQLException{
        int vehicleCountForCustomer = this.getVehicleCountByCustomer(customerName);
        int counter = 0;
        Vehicle[] arrayOfVehicles = new Vehicle[vehicleCountForCustomer];
        
        Class.forName("org.sqlite.JDBC");  
        // id / make / model / year / odometer / notes / customerName
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT * FROM Vehicles WHERE CustomerName='" + customerName + "';");
        while (resultSet.next()){
            Vehicle newVehicle = new Vehicle();
            newVehicle.setId(Integer.parseInt(resultSet.getString(1)));
            newVehicle.setMake(resultSet.getString(2));
            newVehicle.setModel(resultSet.getString(3));
            newVehicle.setYear(Integer.parseInt(resultSet.getString(4)));
            newVehicle.setOdometer(Integer.parseInt(resultSet.getString(5)));
            newVehicle.setNotes(resultSet.getString(6));
            newVehicle.setCustomerName(resultSet.getString(7));
            arrayOfVehicles[counter] = newVehicle;
            counter++;
        }   
        
        
        return arrayOfVehicles;
    }
    
    /**
     * query to count the customers
     */
    public int getCustomerCountByMechanic(String mechanicName){
        int customerCount =0; 
        try{
        Class.forName("org.sqlite.JDBC");  
        //connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        this.connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        this.resultSet = statement.executeQuery("SELECT COUNT(*) FROM Customers WHERE MechanicName='" + mechanicName + "';");
        while (resultSet.next()){
            // should only be ONE because only one row is returned
            customerCount = Integer.parseInt(resultSet.getString(1));
            }   
        }
        catch (Exception e){ 
            e.printStackTrace();  
            }
        finally{
            try{  
                resultSet.close();  
                statement.close();  
                connection.close();  
            } 
        catch (Exception e){ 
            e.printStackTrace();  
            }  
        }
        return customerCount;
    }
    
   
    /**
     * Method to CREATE a BRAND new TEST Db. 
     * Need to add feature to supply path name to ensure proper placement...
     */
    public void createTestDB(){

        try{
        Class.forName("org.sqlite.JDBC");  
        // mac
        //this.connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        // pc
        this.connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\mark\\Documents\\NetBeansProjects\\GarageMechanic\\src\\garagemechanic\\garageMechanicTestDB.db");
        

         //connection = DriverManager.getConnection(this.DBPath);
        this.statement = connection.createStatement();  
        //statement.executeUpdate(null)
        
        this.statement.executeUpdate("PRAGMA foreign_keys = ON;"); // TURN ON FOREIGN KEY SUPPORT!!!!!!!!
        
        // create Mechanics Table
        System.out.println("Test1");
        this.statement.executeUpdate("BEGIN TRANSACTION;"); 
        this.statement.executeUpdate("CREATE TABLE Password(Password TEXT);");
        this.statement.executeUpdate("INSERT INTO Password VALUES('Password01');");
        System.out.println("Test2");
        this.statement.executeUpdate("CREATE TABLE Mechanics(Name TEXT PRIMARY KEY, Description TEXT);");
        System.out.println("Test3");
        this.statement.executeUpdate("INSERT INTO Mechanics VALUES('Mark Milford','Myself - prepopulated data');"); // NEED TO USE USER NAME HERE
        System.out.println("Test4");
        System.out.println("Mechanics done");
        
        // create CUSTOMER Table
        this.statement.executeUpdate("CREATE TABLE Customers(CustomerName TEXT PRIMARY KEY, Description TEXT,MechanicName TEXT, FOREIGN KEY(MechanicName) REFERENCES Mechanics(Name));");
        statement.executeUpdate("INSERT INTO Customers VALUES('Hannah Jarvis','a cutie!','Mark Milford');");
        statement.executeUpdate("INSERT INTO Customers VALUES('Michael Arnold','bestbuddy','Mark Milford');");
        System.out.println("Customers done");
        
        // create VEHICLE Table
        statement.executeUpdate("CREATE TABLE Vehicles(ID INTEGER PRIMARY KEY,Make TEXT, Model TEXT,Year INTEGER,Odometer INTEGER,Notes TEXT, CustomerName TEXT, "
                + "FOREIGN KEY(CustomerName) REFERENCES Customers(CustomerName));");
        statement.executeUpdate("INSERT INTO Vehicles VALUES(NULL,'Chevy','Cobalt',2005,'132000','Random Notes','Hannah Jarvis');");
        //statement.executeUpdate("INSERT INTO Vehicles VALUES('Ford','Focus','45000','Michael Arnold');");
        System.out.println("Vehicles done");
        
        // create MaintenanceTypes Table
        statement.executeUpdate("CREATE TABLE MaintenanceTypes(MaintenanceTypeName TEXT PRIMARY KEY,Description TEXT,Interval INTEGER);");
        statement.executeUpdate("INSERT INTO MaintenanceTypes VALUES('Oil Change','Typical Oil Change','3000');");
        statement.executeUpdate("Insert INTO MaintenanceTypes VALUES('Rotate Tires','Rotate tires, RF-> RR, LF->LR','15000');");
        System.out.println("MaintenanceTypes done");
        
        // create MaintenanceActions Table
        
        statement.executeUpdate("CREATE TABLE MaintenanceActions" 
                + "(VehicleID INTEGER,MaintenanceTypeName TEXT,Odometer INTEGER, Notes TEXT, "
                + "FOREIGN KEY(MaintenanceTypeName) REFERENCES MaintenanceTypes(MaintenanceTypeName),"
                + "FOREIGN KEY(VehicleID) REFERENCES Vehicles(ID));"); // add foreign key for Maintenance Name and Vehicle
        statement.executeUpdate("INSERT INTO MaintenanceActions VALUES('1','Oil Change','3000','Typical Oil Change,used 10w-30');");
        
        
        System.out.println("Committing....done");
        statement.executeUpdate("COMMIT;");
        
        System.out.println("THE PROGRAM'S DATABASE HAS BEEN SUCCESSFULLY BUILT");
        
        resultSet = statement.executeQuery("SELECT * FROM Mechanics;");
        
        while (resultSet.next()){
            System.out.println("Mechanic NAME:" + resultSet.getString("Name"));  
            }   
        }
        catch (Exception e){ 
            e.printStackTrace();  
            }
        finally{
            try{  
                resultSet.close();  
                statement.close();  
                connection.close();  
            } 
        catch (Exception e){ 
            e.printStackTrace();  
            }  
        }
    }
    
    //~~~~~~~~~~~~~~~~~ TESTING CRAP DOWN BELOW ~~~~~~~~~~~~~~~~~~~~~~~
     /**
     * Test method to do a simple query on mechanics
     */
    public void queryMechanics(){
        Connection connection = null;  
        ResultSet resultSet = null;  
        Statement statement = null;  
        try{
        Class.forName("org.sqlite.JDBC");  
        connection = DriverManager.getConnection("jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db");  
        statement = connection.createStatement();  
        resultSet = statement.executeQuery("SELECT * FROM Mechanics;");  
        while (resultSet.next()){
            System.out.println("Mechanic NAME:" + resultSet.getString("Name"));  
            }   
        }
        catch (Exception e){ 
            e.printStackTrace();  
            }
        finally{
            try{  
                resultSet.close();  
                statement.close();  
                connection.close();  
            } 
        catch (Exception e){ 
            e.printStackTrace();  
            }  
        }  
    }   
    
}
