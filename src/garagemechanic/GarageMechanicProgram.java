/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package garagemechanic;

/**
 *
 * @author Mark
 */
public class GarageMechanicProgram {
    
    Mechanic currentMechanic;
    /**
     * main constructor for program
     */
    public GarageMechanicProgram(){
        // create and load main window frame
        MainWindowFrame mainWindowFrameVar = new MainWindowFrame(this);
        mainWindowFrameVar.setVisible(true);
    }
    
    /**
     * method to create a new mechanic AND assign it as the current
     * @param incomingName 
     */
    public void createMechanic(String incomingName){
        Mechanic newMechanic = new Mechanic(incomingName);
        this.currentMechanic = newMechanic;
    }
    
    /**
     * method to return the current mechanic
     * @return mechanic
     */
    public String getMechanicName(){
        
        if(this.currentMechanic!= null){
            return this.currentMechanic.getName();
        }
        else{
            String noMechanic = "None";
            return noMechanic;
        }
    }
}
