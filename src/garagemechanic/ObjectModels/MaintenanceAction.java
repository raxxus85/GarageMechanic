/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package garagemechanic.ObjectModels;

/**
 *
 * @author mark
 */
public class MaintenanceAction {
    private int vehicleId;
    private String maintenanceTypeName;
    private int odometer;
    private String notes;
    
    @Override
    public String toString(){
        String returnString = maintenanceTypeName + ", Odometer :" + odometer + ", Notes: " + notes;
        return returnString;
    }
    
    
    public MaintenanceAction(){
        
    }
    
    public int getVehicleId(){
        return this.vehicleId;
    }
    
    public void setVehicleId(int incomingVehicleId){
        this.vehicleId = incomingVehicleId;
    }
    
    public String getMaintenanceTypeName(){
        return this.maintenanceTypeName;
    }
    
    public void setMaintenanceTypeName(String incomingMaintenanceTypeName){
        this.maintenanceTypeName = incomingMaintenanceTypeName;
    }
    
    public int getOdometer(){
        return this.odometer;
    }
    
    public void setOdometer(int incomingOdometer){
        this.odometer = incomingOdometer;
    }
    
    public String getNotes(){
        return this.notes;
    }
    
    public void setNotes(String incomingNotes){
        this.notes = incomingNotes;
    }
}
