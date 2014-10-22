package garagemechanic.ObjectModels;

/**
 * Vehicle Class, to create Vehicle Objects to simplify data transfer techniques
 * @author mark
 */
public class Vehicle {
    private int id;
    private String make;
    private String model;
    private int year;
    private int odometer;
    private String notes;
    private String customerName;
    
    /**
     * Constructor requiring no fields
     */
    public Vehicle(){
        
    }
    
    /**
     * Constructor, requires all fields
     * @param incomingMake
     * @param incomingModel
     * @param incomingYear
     * @param incomingOdometer
     * @param incomingNotes 
     */
    public Vehicle(String incomingMake, String incomingModel, int incomingYear, int incomingOdometer, String incomingNotes){
        this.make = incomingMake;
        this.model = incomingModel;
        this.year = incomingYear;
        this.odometer = incomingOdometer;
        this.notes = incomingNotes;
    }
    
    @Override
    public String toString(){
        //String vehicleString = "Id:"+this.id+",Make:"+this.make+",Model:"+this.model+",Year:"+this.year+",Odometer:"+this.odometer+",Notes:"+this.notes+",CustomerName:"+this.customerName;
        String vehicleString = "Id:" + this.getId() + " " + this.getYear() +" "+ this.getMake() +" " + this.getModel();
        return vehicleString;
    }
    
    
    
    public int getId(){
        return this.id;
    }
    
    public void setId(int incomingId){
        this.id = incomingId;
    }
    
    public String getMake(){
        return this.make;
    }
    
    public void setMake(String incomingMake){
        this.make = incomingMake;
    }
    
    public String getModel(){
        return this.model;
    }
    
    public void setModel(String incomingModel){
        this.model = incomingModel;
    }
    
    public int getYear(){
        return this.year;
    }
    
    public void setYear(int incomingYear){
        this.year = incomingYear;
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
    
    public String getCustomerName(){
        return this.customerName;
    }
    
    public void setCustomerName(String incomingCustomerName){
        this.customerName = incomingCustomerName;
    }
}
