package garagemechanic.ObjectModels;

/**
 * Mechanic Class, to create Mechanic Objects to simplify data transfer techniques
 * @author mark
 */
public class Mechanic {
    private String name;
    private String description;
    
    public String getName(){
        return this.name;
    }
    
    public void setName(String incomingName){
        this.name=incomingName;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public void setDesription(String incomingDescription){
        this.description = incomingDescription;
    }
}
