package garagemechanic.ObjectModels;

/**
 * MaintenanceType Class, to create MaintenanceType Objects to simplify data transfer techniques
 * @author mark
 */
public class MaintenanceType {
    private String maintenanceTypeName;
    private String description;
    private int interval;
    
    /**
     * Main constructor, requires no fields
     */
    public MaintenanceType(){
        
    }
    
    @Override
    public String toString(){
        String maintenanceTypeString = this.maintenanceTypeName + " " + this.description + " " + this.interval;
        return maintenanceTypeString;
    }
    
    public String getMaintenanceTypeName(){
        return this.maintenanceTypeName;
    }
    
    public void setMaintenanceTypeName(String incomingMaintenanceTypeName){
        this.maintenanceTypeName = incomingMaintenanceTypeName;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public void setDescription(String incomingDescription){
        this.description = incomingDescription;
    }
    
    public int getInterval(){
        return this.interval;
    }
    
    public void setInterval(int incomingInterval){
        this.interval = incomingInterval;
    }
}
