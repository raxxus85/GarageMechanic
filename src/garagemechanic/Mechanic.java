/*
 * The Mechanic class for the Garage Mechanic Program. Probably the most important class. 
 * Will control it's Customers, who in turn track their vehicles, who in turn track their 
 * maintanence work done. Hmmm. Will serialize an individual Mechanic when saving
 * program. Should be fun.
 */
package garagemechanic;

/**
 *
 * @author Mark
 */
public class Mechanic {
    String name;
    /**
     * Constructor for Mechanic
     * @param incomingName name for new Mechanic must be supplied when creating
     */
    public Mechanic(String incomingName){
        this.name = incomingName;
    }
    
    /**
     * Method to return the name of the Mechanic
     * @return 
     */
    public String getName(){
        return this.name;
    }
}
