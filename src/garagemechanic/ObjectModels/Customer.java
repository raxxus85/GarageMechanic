package garagemechanic.ObjectModels;

/**
 * Customer Class, to create Customer Objects to simplify data transfer techniques
 * @author mark
 */
public class Customer {
    private String customerName;
    private String description;
    private String mechanicName;
    
    public String getCustomerName(){
        return this.customerName;
    }
    
    public void setCustomerName(String incomingCustomerName){
        this.customerName=incomingCustomerName;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public void setDesription(String incomingDescription){
        this.description = incomingDescription;
    }
    
    public String getMechanicName(){
        return this.mechanicName;
    }
    
    public void setMechanicName(String incomingMechanicName){
        this.mechanicName = incomingMechanicName;
    }
}
