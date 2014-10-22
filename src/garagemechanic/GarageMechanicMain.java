
package garagemechanic;


import garagemechanic.Dialogs.DialogFactory;
import garagemechanic.Dialogs.DialogType;
import java.io.File;
import java.sql.*;

/**
 *
 * @author mark.milford
 */
public class GarageMechanicMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //DatabaseAccessor test = new DatabaseAccessor();
        //test.createTestDB();
        
        //DialogFactory dialogFactory = new DialogFactory();
        
        //System.out.print(DialogType.ERROR_MESSAGE);
        //dialogFactory.createDialogMessage(DialogType.INFORMATION_MESSAGE, "HEHEHHEHE");
        
        GarageMechanicProcessor testGarageMechanicProcessor = new GarageMechanicProcessor();
        
        
        // TODO code application logic here
        //String DataBasePath = "jdbc:sqlite:/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db";
        //DatabaseAccessor testDatabaseAccessor = new DatabaseAccessor(DataBasePath);

        //GarageMechanicProcessor testGarageMechanicProcessor = new GarageMechanicProcessor(testDatabaseAccessor);
        
        // TEST
        //System.out.println("Does the DB exist?");
        //String dbPath = "/Users/mark.milford/NetBeansProjects/GarageMechanic/src/garagemechanic/garageMechanic.db";
        //System.out.println(testDatabaseAccessor.doesDBExist(dbPath));
        //if(testDatabaseAccessor.doesDBExist(dbPath) == false){
        //    testDatabaseAccessor.createTestDB();
        //}
        
        // start main window of program
        //MainWindow testWindow = new MainWindow(testGarageMechanicProcessor);
        //testWindow.setVisible(true);
        

    }
    
   
    
    
            
}  

