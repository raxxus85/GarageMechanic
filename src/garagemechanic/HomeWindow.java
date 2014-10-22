/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package garagemechanic;

import garagemechanic.Dialogs.DialogType;
import garagemechanic.ObjectModels.MaintenanceAction;
import garagemechanic.ObjectModels.MaintenanceType;
import garagemechanic.ObjectModels.Vehicle;
import java.io.File;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author mark.milford
 */
public class HomeWindow extends javax.swing.JFrame {
    private GarageMechanicProcessor currentGarageMechanicProcessor;
    
            
    /**
     * Creates new form HomeWindow
     */
    public HomeWindow(GarageMechanicProcessor garageMechanicProcessor) {
        this.currentGarageMechanicProcessor = garageMechanicProcessor;
        initComponents();
    }

    /**
     * Method to update entire GUI. Used when opening a new database, etc.
     */
    public void updateGUI() throws ClassNotFoundException, SQLException{
        this.updateMechanicTab();
        this.updateCustomerTab();
        this.updateVehicleTab();
        this.updateMaintenanceActionsTab();
        this.updateMaintenanceTypeTab();
    }
    
    /**
     * Method to update the entire Maintenance Actions Tabs
     */
    public void updateMaintenanceActionsTab() throws ClassNotFoundException, SQLException{
        // build and update the maintenance action items
        String[] maintenanceNames = this.currentGarageMechanicProcessor.getMaintenanceTypeDropdown();
        
        this.useMaintenanceActionTypeComboBox.removeAllItems();
        for(String maintenanceName : maintenanceNames){
            this.useMaintenanceActionTypeComboBox.addItem(maintenanceName);
        }
        
        // perform steps necessary to build out actual maintenance item list here.
        if(this.currentGarageMechanicProcessor.getCurrentVehicleName().equals("None Available")){
            this.maintenanceActionList.removeAll();           
            
            
        }else{
            // time to get the maintenace actions for the current vehicle...
            MaintenanceAction[] maintenanceActionsForCurrentVehicle = this.currentGarageMechanicProcessor.getMaintenanceActionsForCurrentVehicle();            
            this.maintenanceActionList.removeAll();
            // TIME TO FILL OUT LIST
            for(MaintenanceAction maintenanceAction : maintenanceActionsForCurrentVehicle){
                this.maintenanceActionList.add(maintenanceAction.toString());
            }
            
        }
    }
    
    /**
     * Method to update entire Mechanic Tab. If the current mechanic.equals("None Available"), then clear out items.
     */
    public void updateMechanicTab(){
        if(this.currentGarageMechanicProcessor.getCurrentMechanicName().equals("None Available")){
            System.out.println("Log- you tried to update the Mechanic tab when we don't have a current mechanic");
            this.currentMechanicTextField.setText("");
            this.mechanicNameTextField.setText("");
            this.mechanicDescriptionTextArea.setText("");
            this.mechanicComboBox.removeAllItems();
            this.mechanicComboBox.addItem("None Available");
        }else{
            this.currentMechanicTextField.setText(this.currentGarageMechanicProcessor.getCurrentMechanicName());
            this.mechanicNameTextField.setText(this.currentGarageMechanicProcessor.getCurrentMechanicName());
            this.mechanicDescriptionTextArea.setText(this.currentGarageMechanicProcessor.getCurrentMechanicDescription());
            // get a sorted list with the current mechanic as the first one, to rebuild the drop down
            String[] sortedMechanicDropdownList = this.currentGarageMechanicProcessor.getMechanicDropdownNameListWithCurrentFirst();
            this.mechanicComboBox.removeAllItems();
            for(String mechanicName : sortedMechanicDropdownList){
                this.mechanicComboBox.addItem(mechanicName);
            }
        }
    }
    
    /**
     * Method to update the entire Vehicle Tab, and only the Vehicle Tab
     */
    public void updateVehicleTab() throws ClassNotFoundException, SQLException{
        if(this.currentGarageMechanicProcessor.getCurrentVehicleName().equals("None Available")){
            System.out.println("Log - you tried to update the vehicle tab when we don't have a current vehicle");
            this.currentVehicleTextField.setText("None Available");
            this.vehicleMakeTextField.setText("");
            this.vehicleModelTextField.setText("");
            this.vehicleYearTextField.setText("");
            this.vehicleOdometerTextField.setText("");
            this.vehicleNotesTextArea.setText("");
            // gut combo box, no vehicles available here!
            this.vehicleComboBox.removeAllItems();
            
            // update maintenance actions now...
            this.updateMaintenanceActionsTab();
        }else{
            System.out.println("You tried to update the vehicle tab when we do have a current vehicle name(good)");
            this.currentVehicleTextField.setText(this.currentGarageMechanicProcessor.getCurrentVehicleName());
            // update vehicle fields here...
            Vehicle currentVehicle = this.currentGarageMechanicProcessor.getCurrentVehicle();
            
            this.vehicleMakeTextField.setText(currentVehicle.getMake());
            this.vehicleModelTextField.setText(currentVehicle.getModel());
            this.vehicleYearTextField.setText(Integer.toString(currentVehicle.getYear()));
            this.vehicleOdometerTextField.setText(Integer.toString(currentVehicle.getOdometer()));
            this.vehicleNotesTextArea.setText(currentVehicle.getNotes());
            // build out vehicle dropdown
            Vehicle[] sortedVehicleArray = this.currentGarageMechanicProcessor.getVehicleDropdownListWithCurrentFirst();
            this.vehicleComboBox.removeAllItems();
            
            for(Vehicle vehicle : sortedVehicleArray){
                this.vehicleComboBox.addItem(vehicle.toString());
            }
            
            System.out.println("TESTING " + this.currentGarageMechanicProcessor.getCurrentVehicle());
            // update maintenance actions now...
            this.updateMaintenanceActionsTab();
        }
    }

    /**
     * Method to update entire Customer Tab. If the current customer.equals("None Available"), then clear out items.
     */
    public void updateCustomerTab() throws ClassNotFoundException, SQLException{
        if(this.currentGarageMechanicProcessor.getCurrentCustomerName().equals("None Available")){
            System.out.println("Log - you tried to update a customer tab when we don't have a current custoner");
            // update current item bar!
            this.currentCustomerTextField.setText("None Available");
            this.customerComboBox.removeAllItems();
            this.customerComboBox.addItem("None Available");
            this.customerNameTextField.setText("");
            this.customerDescriptionTextArea.setText("");
           
        }else{
            System.out.println("Log - you tried to update the customer tab when we DO have a current customer(good)");
            // update current item bar!
            this.currentCustomerTextField.setText(this.currentGarageMechanicProcessor.getCurrentCustomerName());
            // IMPORTANT - update customer combo box now
            String currentCustomer = this.currentGarageMechanicProcessor.getCurrentCustomerName();
            String currentMechanic = this.currentGarageMechanicProcessor.getCurrentMechanicName();
            String[] currentCustomersAvailable = this.currentGarageMechanicProcessor.getCustomerNameArray(currentMechanic);

  
            String[] sortedCustomerDropdownList = this.currentGarageMechanicProcessor.getDropDownArrayWhereCurrentIsFirst(currentCustomersAvailable, currentCustomer);
            this.customerComboBox.removeAllItems();
            for(String customerName : sortedCustomerDropdownList){
                this.customerComboBox.addItem(customerName);
            }
            // update rest of customer fields
            this.customerNameTextField.setText(this.currentGarageMechanicProcessor.getCurrentCustomerName());
            this.customerDescriptionTextArea.setText(this.currentGarageMechanicProcessor.getCurrentCustomerDescription());
        }
    }
    
    /**
     * Method to update the entire maintenance type tab!
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void updateMaintenanceTypeTab() throws ClassNotFoundException, SQLException{
        String[] maintenanceNames = this.currentGarageMechanicProcessor.getMaintenanceTypeDropdown();
        
        this.maintenanceTypeComboBox.removeAllItems();
        for(String maintenanceName : maintenanceNames){
            this.maintenanceTypeComboBox.addItem(maintenanceName);
        }
        // we will use the first one as the 'current' maintenanceType
        String currentMaintenanceTypeName = maintenanceNames[0];
        MaintenanceType selectedMaintenanceType = this.currentGarageMechanicProcessor.getMaintenanceType(currentMaintenanceTypeName);
        // update fields based on first one, as thats the one we display
        this.maintenanceTypeDescriptionTextArea.setText(selectedMaintenanceType.getDescription());
        this.recommendedIntervalTextField.setText(Integer.toString(selectedMaintenanceType.getInterval()));
    }
    
    /**
     * Since we don't have the idea of a 'current' maintenaceType(and don't need one) we will
     * need a method to update that tab when a user is cycling through the maintenance types
     * @param maintenanceType 
     */
    public void updateMaintenanceTypeTab(String maintenanceTypeName) throws ClassNotFoundException, SQLException{
        MaintenanceType selectedMaintenanceType = this.currentGarageMechanicProcessor.getMaintenanceType(maintenanceTypeName);
        // update fields based on first one, as thats the one we display
        this.maintenanceTypeDescriptionTextArea.setText(selectedMaintenanceType.getDescription());
        this.recommendedIntervalTextField.setText(Integer.toString(selectedMaintenanceType.getInterval()));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        currentMechanicTextField = new javax.swing.JTextField();
        currentMechanicTextField.setEditable(false);
        currentMechanicLabel = new javax.swing.JLabel();
        customersTabbedPanel = new javax.swing.JTabbedPane();
        mechanicsLabel = new javax.swing.JPanel();
        mechanicNameTextField = new javax.swing.JTextField();
        mechanicNameTextField.setEditable(false);
        mechanicNameLabel = new javax.swing.JLabel();
        mechanicComboBox = new javax.swing.JComboBox();
        selectMechanicLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mechanicDescriptionTextArea = new javax.swing.JTextArea();
        mechanicDescriptionTextArea.setEditable(false);
        mechanicDescription = new javax.swing.JLabel();
        newMechanicNameTextField = new javax.swing.JTextField();
        newMechanicNameLabel = new javax.swing.JLabel();
        addMechanicButton = new javax.swing.JButton();
        addANewMechanicLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        newMechanicDescriptionTextArea = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        selectCustomerLabel = new javax.swing.JLabel();
        customerNameLabel = new javax.swing.JLabel();
        customerDescriptionLabel = new javax.swing.JLabel();
        customerComboBox = new javax.swing.JComboBox();
        customerNameTextField = new javax.swing.JTextField();
        customerNameTextField.setEditable(false);
        jScrollPane2 = new javax.swing.JScrollPane();
        customerDescriptionTextArea = new javax.swing.JTextArea();
        customerDescriptionTextArea.setEditable(false);
        jSeparator2 = new javax.swing.JSeparator();
        addNewCustomerLabel = new javax.swing.JLabel();
        newCustomerNameLabel = new javax.swing.JLabel();
        newCustomerDescriptionLabel = new javax.swing.JLabel();
        newCustomerNameTextField = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        newCustomerDescriptionTextArea = new javax.swing.JTextArea();
        addCustomerButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        selectVehicleLabel = new javax.swing.JLabel();
        vehicleComboBox = new javax.swing.JComboBox();
        vehicleMakeLabel = new javax.swing.JLabel();
        vehicleModelLabel = new javax.swing.JLabel();
        vehicleYearLabel = new javax.swing.JLabel();
        vehicleOdometerLabel = new javax.swing.JLabel();
        vehicleNotesLabel = new javax.swing.JLabel();
        vehicleMakeTextField = new javax.swing.JTextField();
        vehicleMakeTextField.setEditable(false);
        vehicleModelTextField = new javax.swing.JTextField();
        vehicleModelTextField.setEditable(false);
        vehicleYearTextField = new javax.swing.JTextField();
        vehicleYearTextField.setEditable(false);
        vehicleOdometerTextField = new javax.swing.JTextField();
        vehicleOdometerTextField.setEditable(false);
        jScrollPane5 = new javax.swing.JScrollPane();
        vehicleNotesTextArea = new javax.swing.JTextArea();
        addVehicleLabel = new javax.swing.JLabel();
        newVehicleMakeLabel = new javax.swing.JLabel();
        newVehicleModelLabel = new javax.swing.JLabel();
        newVehicleYearLabel = new javax.swing.JLabel();
        newVehicleOdometerLabel = new javax.swing.JLabel();
        newVehicleMakeTextField = new javax.swing.JTextField();
        newVehicleModelTextField = new javax.swing.JTextField();
        newVehicleYearTextField = new javax.swing.JTextField();
        newVehicleOdometerTextField = new javax.swing.JTextField();
        newVehicleNotesLabel = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        newVehicleNotesTextArea = new javax.swing.JTextArea();
        addVehicleButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        maintenanceActionsLabel = new javax.swing.JLabel();
        maintenanceActionList = new java.awt.List();
        jSeparator4 = new javax.swing.JSeparator();
        performMaintenanceActionLabel = new javax.swing.JLabel();
        maintenanceActionTypeLabel = new javax.swing.JLabel();
        currenVehicleOdometer = new javax.swing.JLabel();
        maintenanceActionNotesLabel = new javax.swing.JLabel();
        currentVehicleOdometerTextField = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        maintenanceActionNotesTextArea = new javax.swing.JTextArea();
        useMaintenanceActionTypeComboBox = new javax.swing.JComboBox();
        addMaintenanceActionButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        selectMaintenaceTypeLabel = new javax.swing.JLabel();
        maintenanceTypeComboBox = new javax.swing.JComboBox();
        recommendedIntervalLabel = new javax.swing.JLabel();
        recommendedIntervalTextField = new javax.swing.JTextField();
        recommendedIntervalTextField.setEditable(false);
        maintenanceTypeDescriptionLabel = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        maintenanceTypeDescriptionTextArea = new javax.swing.JTextArea();
        maintenanceTypeDescriptionTextArea.setEditable(false);
        jSeparator3 = new javax.swing.JSeparator();
        addMaintenanceActionTypeLabel = new javax.swing.JLabel();
        newMaintenanceActionTypeNameLabel = new javax.swing.JLabel();
        newRecommendedIntervalLabel = new javax.swing.JLabel();
        newMaintenanceTypeDescriptionLabel = new javax.swing.JLabel();
        newMaintenanceTypeNameTextField = new javax.swing.JTextField();
        newMaintenanceTypeIntervalTextField = new javax.swing.JTextField();
        jScrollPane9 = new javax.swing.JScrollPane();
        newMaintenanceTypeDescriptionTextArea = new javax.swing.JTextArea();
        addMaintenanceTypeButton = new javax.swing.JButton();
        currentCustomerLabel = new javax.swing.JLabel();
        currentCustomerTextField = new javax.swing.JTextField();
        currentCustomerTextField.setEditable(false);
        currentVehicleLabel = new javax.swing.JLabel();
        currentVehicleTextField = new javax.swing.JTextField();
        currentVehicleTextField.setEditable(false);
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openGarageMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutItem = new javax.swing.JMenuItem();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        currentMechanicLabel.setText("Current Mechanic");

        mechanicNameLabel.setText("Mechanic Name");

        mechanicComboBox.setModel(new javax.swing.DefaultComboBoxModel(this.currentGarageMechanicProcessor.getMechanicNameArray()));
        mechanicComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                mechanicComboBoxItemStateChanged(evt);
            }
        });

        selectMechanicLabel.setText("Select Mechanic");

        mechanicDescriptionTextArea.setColumns(20);
        mechanicDescriptionTextArea.setRows(5);
        jScrollPane1.setViewportView(mechanicDescriptionTextArea);

        mechanicDescription.setText("Mechanic Description");

        newMechanicNameLabel.setText("New Mechanic Name");

        addMechanicButton.setText("Add Mechanic");
        addMechanicButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMechanicButtonActionPerformed(evt);
            }
        });

        addANewMechanicLabel.setText("Add a New Mechanic");

        jLabel1.setText("New Mechanic Description");

        newMechanicDescriptionTextArea.setColumns(20);
        newMechanicDescriptionTextArea.setRows(5);
        jScrollPane3.setViewportView(newMechanicDescriptionTextArea);

        javax.swing.GroupLayout mechanicsLabelLayout = new javax.swing.GroupLayout(mechanicsLabel);
        mechanicsLabel.setLayout(mechanicsLabelLayout);
        mechanicsLabelLayout.setHorizontalGroup(
            mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mechanicsLabelLayout.createSequentialGroup()
                .addComponent(jSeparator1)
                .addContainerGap())
            .addGroup(mechanicsLabelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mechanicsLabelLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(selectMechanicLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(mechanicNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(mechanicDescription))
                        .addGap(75, 75, 75)
                        .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(mechanicComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 140, Short.MAX_VALUE)
                                .addComponent(mechanicNameTextField, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addContainerGap(299, Short.MAX_VALUE))
                    .addGroup(mechanicsLabelLayout.createSequentialGroup()
                        .addComponent(newMechanicNameLabel)
                        .addGap(51, 51, 51)
                        .addComponent(newMechanicNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110)
                        .addComponent(addANewMechanicLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mechanicsLabelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mechanicsLabelLayout.createSequentialGroup()
                                .addComponent(addMechanicButton)
                                .addGap(109, 109, 109))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mechanicsLabelLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(30, 30, 30)))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        mechanicsLabelLayout.setVerticalGroup(
            mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mechanicsLabelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mechanicComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectMechanicLabel))
                .addGap(59, 59, 59)
                .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mechanicNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mechanicNameLabel))
                .addGap(18, 18, 18)
                .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mechanicDescription)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mechanicsLabelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addANewMechanicLabel)
                        .addGap(8, 8, 8)
                        .addComponent(newMechanicNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mechanicsLabelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(newMechanicNameLabel)
                        .addGap(18, 18, 18)))
                .addGap(2, 2, 2)
                .addGroup(mechanicsLabelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(mechanicsLabelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addMechanicButton))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        customersTabbedPanel.addTab("Mechanics", mechanicsLabel);

        selectCustomerLabel.setText("Select Customer");

        customerNameLabel.setText("Customer Name");

        customerDescriptionLabel.setText("Customer Description");

        customerComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {""}));
        customerComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customerComboBoxItemStateChanged(evt);
            }
        });

        customerDescriptionTextArea.setColumns(20);
        customerDescriptionTextArea.setRows(5);
        jScrollPane2.setViewportView(customerDescriptionTextArea);

        addNewCustomerLabel.setText("Add New Customer");

        newCustomerNameLabel.setText("New Customer Name");

        newCustomerDescriptionLabel.setText("New Customer Description");

        newCustomerDescriptionTextArea.setColumns(20);
        newCustomerDescriptionTextArea.setRows(5);
        jScrollPane4.setViewportView(newCustomerDescriptionTextArea);

        addCustomerButton.setText("Add Customer");
        addCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCustomerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(customerDescriptionLabel)
                            .addComponent(selectCustomerLabel)
                            .addComponent(customerNameLabel))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(customerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(customerNameTextField)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(340, 340, 340)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(addCustomerButton)
                            .addComponent(addNewCustomerLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newCustomerNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newCustomerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(newCustomerDescriptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectCustomerLabel)
                    .addComponent(customerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerNameLabel)
                    .addComponent(customerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerDescriptionLabel)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addNewCustomerLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(newCustomerNameLabel)
                        .addComponent(newCustomerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(newCustomerDescriptionLabel))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                .addComponent(addCustomerButton))
        );

        customersTabbedPanel.addTab("Customers", jPanel2);

        selectVehicleLabel.setText("Select Vehicle");

        vehicleComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] {""}));
        vehicleComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                vehicleComboBoxItemStateChanged(evt);
            }
        });

        vehicleMakeLabel.setText("Vehicle Make");

        vehicleModelLabel.setText("Vehicle Model");

        vehicleYearLabel.setText("Vehicle Year");

        vehicleOdometerLabel.setText("Vehicle Odometer");

        vehicleNotesLabel.setText("Vehicle Notes");

        vehicleModelTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleModelTextFieldActionPerformed(evt);
            }
        });

        vehicleOdometerTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleOdometerTextFieldActionPerformed(evt);
            }
        });

        vehicleNotesTextArea.setColumns(20);
        vehicleNotesTextArea.setRows(5);
        jScrollPane5.setViewportView(vehicleNotesTextArea);

        addVehicleLabel.setText("Add Vehicle");

        newVehicleMakeLabel.setText("New Vehicle Make");

        newVehicleModelLabel.setText("New Vehicle Model");

        newVehicleYearLabel.setText("New Vehicle Year");

        newVehicleOdometerLabel.setText("New Vehicle Odometer");

        newVehicleNotesLabel.setText("New Vehicle Notes");

        newVehicleNotesTextArea.setColumns(20);
        newVehicleNotesTextArea.setRows(5);
        jScrollPane8.setViewportView(newVehicleNotesTextArea);

        addVehicleButton.setText("Add Vehicle");
        addVehicleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addVehicleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(selectVehicleLabel)
                        .addGap(86, 86, 86)
                        .addComponent(vehicleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(vehicleOdometerLabel)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(vehicleModelLabel)
                                .addComponent(vehicleMakeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(vehicleYearLabel)
                            .addComponent(vehicleNotesLabel))
                        .addGap(57, 57, 57)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(vehicleOdometerTextField)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(vehicleYearTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(vehicleModelTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(vehicleMakeTextField))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(addVehicleLabel))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newVehicleMakeLabel)
                            .addComponent(newVehicleModelLabel)
                            .addComponent(newVehicleYearLabel))
                        .addGap(49, 49, 49)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(newVehicleMakeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(newVehicleModelTextField)
                            .addComponent(newVehicleYearTextField)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newVehicleNotesLabel)
                            .addComponent(newVehicleOdometerLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(newVehicleOdometerTextField)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE))))
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(addVehicleButton)
                .addGap(167, 167, 167))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectVehicleLabel)
                    .addComponent(vehicleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addVehicleLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vehicleMakeLabel)
                    .addComponent(vehicleMakeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newVehicleMakeLabel)
                    .addComponent(newVehicleMakeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vehicleModelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vehicleModelLabel)
                    .addComponent(newVehicleModelLabel)
                    .addComponent(newVehicleModelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vehicleYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vehicleYearLabel)
                    .addComponent(newVehicleYearLabel)
                    .addComponent(newVehicleYearTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(vehicleOdometerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vehicleOdometerLabel)
                    .addComponent(newVehicleOdometerLabel)
                    .addComponent(newVehicleOdometerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(vehicleNotesLabel)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                    .addComponent(newVehicleNotesLabel)
                    .addComponent(jScrollPane8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addVehicleButton)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        customersTabbedPanel.addTab("Vehicles", jPanel3);

        maintenanceActionsLabel.setText("Maintenance Actions");

        performMaintenanceActionLabel.setText("Perform Maintenance Action");

        maintenanceActionTypeLabel.setText("Maintenance Action Type");

        currenVehicleOdometer.setText("Current Vehicle Odometer");

        maintenanceActionNotesLabel.setText("Maintenance Action Notes");

        maintenanceActionNotesTextArea.setColumns(20);
        maintenanceActionNotesTextArea.setRows(5);
        jScrollPane7.setViewportView(maintenanceActionNotesTextArea);

        useMaintenanceActionTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));

        addMaintenanceActionButton.setText("Add Maintenance Action to Vehicle");
        addMaintenanceActionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMaintenanceActionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(maintenanceActionList, javax.swing.GroupLayout.PREFERRED_SIZE, 793, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jSeparator4)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(384, 384, 384)
                        .addComponent(maintenanceActionsLabel))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(performMaintenanceActionLabel))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currenVehicleOdometer)
                            .addComponent(maintenanceActionNotesLabel)
                            .addComponent(maintenanceActionTypeLabel))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane7)
                            .addComponent(currentVehicleOdometerTextField)
                            .addComponent(useMaintenanceActionTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(addMaintenanceActionButton)))
                .addContainerGap(368, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(maintenanceActionsLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maintenanceActionList, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(performMaintenanceActionLabel)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maintenanceActionTypeLabel)
                    .addComponent(useMaintenanceActionTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currenVehicleOdometer)
                    .addComponent(currentVehicleOdometerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maintenanceActionNotesLabel)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addMaintenanceActionButton)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        customersTabbedPanel.addTab("Vehicle Maintenance Actions", jPanel4);

        selectMaintenaceTypeLabel.setText("Select Maintenance Action Type");

        maintenanceTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        maintenanceTypeComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                maintenanceTypeComboBoxItemStateChanged(evt);
            }
        });

        recommendedIntervalLabel.setText("Recommended Interval");

        maintenanceTypeDescriptionLabel.setText("Maintenance Type Description");

        maintenanceTypeDescriptionTextArea.setColumns(20);
        maintenanceTypeDescriptionTextArea.setRows(5);
        jScrollPane6.setViewportView(maintenanceTypeDescriptionTextArea);

        addMaintenanceActionTypeLabel.setText("Add Maintenance Action Type");

        newMaintenanceActionTypeNameLabel.setText("New Maintenance Action Type Name");

        newRecommendedIntervalLabel.setText("Recommended Interval");

        newMaintenanceTypeDescriptionLabel.setText("New Maintenance Type Description");

        newMaintenanceTypeDescriptionTextArea.setColumns(20);
        newMaintenanceTypeDescriptionTextArea.setRows(5);
        jScrollPane9.setViewportView(newMaintenanceTypeDescriptionTextArea);

        addMaintenanceTypeButton.setText("Add Maintenance Action Type");
        addMaintenanceTypeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMaintenanceTypeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(maintenanceTypeDescriptionLabel)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addGap(59, 59, 59)
                                            .addComponent(selectMaintenaceTypeLabel))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                            .addGap(76, 76, 76)
                                            .addComponent(recommendedIntervalLabel))))
                                .addGap(84, 84, 84)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane6)
                                    .addComponent(recommendedIntervalTextField)
                                    .addComponent(maintenanceTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addGap(27, 27, 27)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(newMaintenanceActionTypeNameLabel)
                                        .addComponent(newMaintenanceTypeDescriptionLabel)
                                        .addComponent(newRecommendedIntervalLabel))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(newMaintenanceTypeIntervalTextField)
                                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                        .addComponent(newMaintenanceTypeNameTextField)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                    .addGap(296, 296, 296)
                                    .addComponent(addMaintenanceActionTypeLabel)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(296, 296, 296)
                                .addComponent(addMaintenanceTypeButton)))
                        .addGap(0, 342, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectMaintenaceTypeLabel)
                    .addComponent(maintenanceTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recommendedIntervalLabel)
                    .addComponent(recommendedIntervalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maintenanceTypeDescriptionLabel)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addMaintenanceActionTypeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newMaintenanceActionTypeNameLabel)
                    .addComponent(newMaintenanceTypeNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newRecommendedIntervalLabel)
                    .addComponent(newMaintenanceTypeIntervalTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(newMaintenanceTypeDescriptionLabel)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(addMaintenanceTypeButton)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        customersTabbedPanel.addTab("Maintenance Action Types", jPanel5);

        currentCustomerLabel.setText("Current Customer");

        currentVehicleLabel.setText("Current Vehicle");

        fileMenu.setText("File");

        openGarageMenuItem.setText("Open Garage");
        openGarageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openGarageMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openGarageMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText("Help");

        aboutItem.setText("About");
        aboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(currentMechanicLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentMechanicTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentCustomerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentCustomerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentVehicleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(currentVehicleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(customersTabbedPanel, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentMechanicTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentMechanicLabel)
                    .addComponent(currentCustomerLabel)
                    .addComponent(currentCustomerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentVehicleLabel)
                    .addComponent(currentVehicleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addComponent(customersTabbedPanel))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void openGarageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openGarageMenuItemActionPerformed
        // Build JFileChooser, etc
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "GarageMechanic Database Files", "db");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(null);
        File testFile = chooser.getSelectedFile();
        // ensure testFile isn't null (will happen if they cancel window
        if(testFile != null){
            String filePath = testFile.getAbsolutePath();
            try {
                this.currentGarageMechanicProcessor.openNewDatabase(filePath);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_openGarageMenuItemActionPerformed

    private void aboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutItemActionPerformed
        // User clicked About, create and Open an About Window
        this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ABOUT_WINDOW, null);
    }//GEN-LAST:event_aboutItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        // User clicked exit, lets exit by God
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void mechanicComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_mechanicComboBoxItemStateChanged
        // TODO add your handling code here:
        
        if (evt.getStateChange() == 1){

            String mechanicJustSelected = (String)this.mechanicComboBox.getSelectedItem();
            System.out.println("A mechanic was SELECTED! He was : " + mechanicJustSelected);
            try {
                this.currentGarageMechanicProcessor.setCurrentMechanicName(mechanicJustSelected);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_mechanicComboBoxItemStateChanged

    private void customerComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_customerComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == 1){

            String customerJustSelected = (String)this.customerComboBox.getSelectedItem();
            System.out.println("A Customer was SELECTED! He was : " + customerJustSelected);
            try {
                this.currentGarageMechanicProcessor.setCurrentCustomerName(customerJustSelected);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_customerComboBoxItemStateChanged

    private void addMechanicButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMechanicButtonActionPerformed
        // New Mechanic Button was pressed
        // verify that 1) text field NOT empty 2) mechanic doesn't already exists 3) add mechanic 4) update GUI
        String newMechanicName = newMechanicNameTextField.getText();

        String newMechanicDescription = newMechanicDescriptionTextArea.getText();
           
        if(!this.currentGarageMechanicProcessor.isDatabaseOpened()){
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ERROR_MESSAGE, "You can't add a Mechanic without either"
                    + " opening a GarageMechanic Database File or creating one!");
            return;
        }
        
        if(newMechanicName.equals("")){
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, "You attempted to add a "
                    + "Mechanic without specifiying a name!");
            return;
        }
        if(newMechanicDescription.equals("")){
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, "You attempted to add a "
                    + "Mechanic without specifiying a description!");
            return;
        }
        

        System.out.println("The button was pressed AND there was a name!");
        try {
            // ensure that the mechanic name doesn't exist, if it does, throw an error!
            if(this.currentGarageMechanicProcessor.doesMechanicExist(newMechanicName)){
                // mechanic EXISTS!
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ERROR_MESSAGE, "Mechanic : " + newMechanicName.toUpperCase() + " already"
                        + " exists!");
            }else{
                
                    // all checks are good, time to add that mechanic into the database
                    this.currentGarageMechanicProcessor.createNewMechanic(newMechanicName, newMechanicDescription);
                    System.out.println("Mechanci was added with name:"+ newMechanicName);
                
                // mechanic added, clear the add mechanic fields now
                newMechanicNameTextField.setText("");
                
                newMechanicDescriptionTextArea.setText("");
                
                    // update the GUI, as we now have a new mechanic
                    this.updateGUI();
                
                // throw information dialog to user that mechanic was created
                String message = "New mechanic, " + newMechanicName + ", has been created!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.INFORMATION_MESSAGE, message);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addMechanicButtonActionPerformed

    private void addCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCustomerButtonActionPerformed
        // After clicking add customer button, ensure that fields are set, customer doesn't exist for that mechanic
        // then add that customer, reset GUI
        
        String currentMechanicName = this.currentGarageMechanicProcessor.getCurrentMechanicName();
        
        String customerName = this.newCustomerNameTextField.getText();
        String customerDescription = this.newCustomerDescriptionTextArea.getText();
        // ensure there is a database to add a customer to!
        if(!this.currentGarageMechanicProcessor.isDatabaseOpened()){
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ERROR_MESSAGE, "You can't add a Customer without either"
                    + " opening a GarageMechanic Database File or creating one!");
            return;
        }
        
        if(customerName.equals("")){
            String message = "You attempted to add a Customer without specifying a name! Specify a name first.";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }else if(customerDescription.equals("")){
            String message = "You attemped to add a Customer without specifying a description! Specify a description first.";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        
        //boolean doesCurrentMechanicHaveThatCustomer = this.currentGarageMechanicProcessor.doesMechanicHaveThisCustomer(customerName);
        boolean doesCustomerExist = true;
        try {
            doesCustomerExist = this.currentGarageMechanicProcessor.doesCustomerExist(customerName);
        } catch (SQLException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(doesCustomerExist){
            String message ="There is a Customer with that name already! Please specifiy a different Customer Name!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        
        try {
            // add customer
            this.currentGarageMechanicProcessor.createNewCustomer(customerName, customerDescription);
            // if current Customer is false, we should set it to this one!
            String currentCustomer = this.currentGarageMechanicProcessor.getCurrentCustomerName();
            if(currentCustomer.equals("None Available")){
                this.currentGarageMechanicProcessor.setCurrentCustomerName(customerName);
            }
           
            // clear fields
            this.newCustomerNameTextField.setText("");
            this.newCustomerDescriptionTextArea.setText("");
            // update gui
            this.updateCustomerTab();
            // pop friendly message to user
            String message = "New Customer, " + customerName + ", has been added!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.INFORMATION_MESSAGE, message);
        } catch (SQLException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addCustomerButtonActionPerformed

    private void vehicleOdometerTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleOdometerTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vehicleOdometerTextFieldActionPerformed

    private void vehicleComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_vehicleComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == 1){
            String vehicleJustSelected = (String)this.vehicleComboBox.getSelectedItem();
            System.out.println("A new vehicle was selected:"+vehicleJustSelected);
            try {
                Vehicle vehicle = this.currentGarageMechanicProcessor.getVehicleFromString(vehicleJustSelected);
                this.currentGarageMechanicProcessor.setCurrentVehicle(vehicle);
                this.currentGarageMechanicProcessor.setCurrentVehicleName(vehicle.toString());
            } catch (SQLException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_vehicleComboBoxItemStateChanged

    private void vehicleModelTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleModelTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vehicleModelTextFieldActionPerformed

    private void addVehicleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addVehicleButtonActionPerformed
        try {
            // ensure that there is a database, and all the fields are filled out before creating new vehicle for customer
            if(!this.currentGarageMechanicProcessor.isDatabaseOpened()){
                String message = "You attempted to create a Vehicle when there is no Garage Mechanic Database! Either create one, or open one!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ERROR_MESSAGE, message);
                return;
            }
            System.out.println(newVehicleMakeTextField.getText());
            if(this.newVehicleMakeTextField.getText().equals("")){
                String message = "You attempted to create a Vehicle without specifiying a Vehicle Make!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
                return;
            }
            if(this.newVehicleModelTextField.getText().equals("")){
                String message = "You attempted to create a Vehicle without specifying a Vehicle Model!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
                return;
            }
            if(this.newVehicleYearTextField.getText().equals("")){
                String message = "You attempted to create a Vehicle without specifying a Vehicle Year!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
                return;
            }
            if(this.newVehicleOdometerTextField.getText().equals("")){
                String message = "You attempted to create a Vehicle without specifying a Vehicle Odometer!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
                return;
            }
            if(this.newVehicleNotesTextArea.getText().equals("")){
                String message = "You attempted to create a Vehicle without specifying Vehicle Notes!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
                return;
            }
            
            
            // the relevant vehicle information(convert to int if necessary)
            String newVehicleMake = this.newVehicleMakeTextField.getText();
            String newVehicleModel = this.newVehicleModelTextField.getText();
            int newVehicleYear = Integer.parseInt(this.newVehicleYearTextField.getText());
            int newVehicleOdometer = Integer.parseInt(this.newVehicleOdometerTextField.getText());
            String newVehicleNotes = this.newVehicleNotesTextArea.getText();
            // create new vehicle here
            Vehicle newVehicle = this.currentGarageMechanicProcessor.createNewVehicle(newVehicleMake, newVehicleModel, newVehicleYear, newVehicleOdometer, newVehicleNotes);
            
            String currentVehicleName = this.currentGarageMechanicProcessor.getCurrentVehicleName();
            if(currentVehicleName.equals("None Available")){
                this.currentGarageMechanicProcessor.setCurrentVehicle(newVehicle);
                this.currentGarageMechanicProcessor.setCurrentVehicleName(newVehicleMake.toString());
            }
            // update gui
            this.updateVehicleTab();
            // alert user that vehicle has been CREATED
            String message = "Vehicle has been created!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.INFORMATION_MESSAGE, message);
            // CLEAR all entered data from GUI
            this.newVehicleMakeTextField.setText("");
            this.newVehicleModelTextField.setText("");
            this.newVehicleYearTextField.setText("");
            this.newVehicleOdometerTextField.setText("");
            this.newVehicleNotesTextArea.setText("");
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        

        
        
    }//GEN-LAST:event_addVehicleButtonActionPerformed

    private void maintenanceTypeComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_maintenanceTypeComboBoxItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == 1){
            String maintenanceTypeJustSelected = (String)this.maintenanceTypeComboBox.getSelectedItem();
            System.out.println("A new maintenanceType was selected:"+maintenanceTypeJustSelected);  
            try {
                this.updateMaintenanceTypeTab(maintenanceTypeJustSelected);
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_maintenanceTypeComboBoxItemStateChanged

    private void addMaintenanceTypeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMaintenanceTypeButtonActionPerformed
        // user is attempting to create a maintenance type
        // ensure 1) DB connected 2) fields filled out 3) MaintenaceType with same name doesn't exist
        // ensure db connected
        boolean databaseAvailable = this.currentGarageMechanicProcessor.isDatabaseOpened();
        if(!databaseAvailable){
            String message = "You must first connect to a Garage Mechanic Database or create a new one!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ERROR_MESSAGE, message);
            return;
        }
        // ensure name, interval, and description are filled out
        String newMaintenanceTypeName = this.newMaintenanceTypeNameTextField.getText();
        //int newMaintenanceTypeInterval = Integer.parseInt(this.newMaintenanceTypeIntervalTextField.getText());
        String newMaintenanceTypeInterval =this.newMaintenanceTypeIntervalTextField.getText();
        String newMaintenanceTypeDescription = this.newMaintenanceTypeDescriptionTextArea.getText();
        if(newMaintenanceTypeName.equals("")){
            String message = "You tried to create a Maintenace Action Type without first specifiying a name!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        if(newMaintenanceTypeInterval.equals("")){
            String message = "You tried to create a Maintenace Action Type without first specifiying an interval!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        if(newMaintenanceTypeDescription.equals("")){
            String message = "You tried to create a Maintenace Action Type without first specifiying a description!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        try {
            // ENSURE that a maintenanceType with that name doesn't exist
            boolean doesMaintenanceTypeExist = this.currentGarageMechanicProcessor.doesMaintenanceTypeExist(newMaintenanceTypeName);
            if(doesMaintenanceTypeExist){
                String message="You tried to create a Maintenace Action Type with a name that conflicts with another, please change your Action Type Name!";
                this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ERROR_MESSAGE, message);
                return;
            }            
            // create maintenance type
            MaintenanceType newMaintenanceType = new MaintenanceType();
            
            newMaintenanceType.setMaintenanceTypeName(newMaintenanceTypeName);
            int interval = Integer.parseInt(newMaintenanceTypeInterval);
            newMaintenanceType.setInterval(interval);
            newMaintenanceType.setDescription(newMaintenanceTypeDescription);
            
            this.currentGarageMechanicProcessor.createNewMaintenanceType(newMaintenanceType);
            // // update gui
            this.updateMaintenanceTypeTab();
            this.updateMaintenanceActionsTab();
            // alert user of success
            String message = "You have successfully created a new Maintenance Action Type!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.INFORMATION_MESSAGE, message);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addMaintenanceTypeButtonActionPerformed

    private void addMaintenanceActionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMaintenanceActionButtonActionPerformed
        // Ensure that we have a DB connection
        boolean databaseConnection = this.currentGarageMechanicProcessor.isDatabaseOpened();
        if(!databaseConnection){
            String message= "You attempted to add a Maintenance Task but we have no Garage Mechanic Database open. "
                    + "Either open an existing one or create one!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.ERROR_MESSAGE, message);
            return;
        }
        // ensure there is a current vehicle!
        if(this.currentGarageMechanicProcessor.getCurrentVehicleName().equals("None Available")){
            String message = "You attemped to add a Maintenance Task but there isn't a vehicle available!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        // ensure that both fields are set
        if(this.currentVehicleOdometerTextField.getText().equals("")){
            String message = "You attemped to add a Maintenance Task but didn't specify the Odometer! "
                    + "Please enter the Vehicle's odometer at the time of service";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        if(this.maintenanceActionNotesTextArea.getText().equals("")){
            String message = "You attempted to add a Maintenance Task but didn't specify the Maintenace Notes!"
                    + " Please enter the Maintenance Notes for the Task";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.WARNING_MESSAGE, message);
            return;
        }
        // create the maintenance task
        String selecetedMaintenanceType = (String)this.useMaintenanceActionTypeComboBox.getSelectedItem();
        int odometer = Integer.parseInt(this.currentVehicleOdometerTextField.getText());
        String notes = this.maintenanceActionNotesTextArea.getText();
        try {
            MaintenanceAction maintenanceAction = new MaintenanceAction();
            maintenanceAction.setMaintenanceTypeName(selecetedMaintenanceType);
            maintenanceAction.setOdometer(odometer);
            maintenanceAction.setNotes(notes);
            //this.currentGarageMechanicProcessor.createNewMaintenanceAction(selecetedMaintenanceType, odometer, notes);
            this.currentGarageMechanicProcessor.createNewMaintenanceAction(maintenanceAction);
            
            // update gui
            // clear out used values
            this.currentVehicleOdometerTextField.setText("");
            this.maintenanceActionNotesTextArea.setText("");
            
            this.updateMaintenanceActionsTab();
            
            // alert user of success
            String message = "You have successfully entered a new Maintenance Action!";
            this.currentGarageMechanicProcessor.createDialogMessage(DialogType.INFORMATION_MESSAGE, message);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(HomeWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addMaintenanceActionButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // SHOULD NEVER DO THIS
                System.out.println("WE SHOULD NOT BE DOING THIS!!");
                GarageMechanicProcessor test = new GarageMechanicProcessor();
                new HomeWindow(test).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutItem;
    private javax.swing.JLabel addANewMechanicLabel;
    private javax.swing.JButton addCustomerButton;
    private javax.swing.JButton addMaintenanceActionButton;
    private javax.swing.JLabel addMaintenanceActionTypeLabel;
    private javax.swing.JButton addMaintenanceTypeButton;
    private javax.swing.JButton addMechanicButton;
    private javax.swing.JLabel addNewCustomerLabel;
    private javax.swing.JButton addVehicleButton;
    private javax.swing.JLabel addVehicleLabel;
    private javax.swing.JLabel currenVehicleOdometer;
    private javax.swing.JLabel currentCustomerLabel;
    private javax.swing.JTextField currentCustomerTextField;
    private javax.swing.JLabel currentMechanicLabel;
    private javax.swing.JTextField currentMechanicTextField;
    private javax.swing.JLabel currentVehicleLabel;
    private javax.swing.JTextField currentVehicleOdometerTextField;
    private javax.swing.JTextField currentVehicleTextField;
    private javax.swing.JComboBox customerComboBox;
    private javax.swing.JLabel customerDescriptionLabel;
    private javax.swing.JTextArea customerDescriptionTextArea;
    private javax.swing.JLabel customerNameLabel;
    private javax.swing.JTextField customerNameTextField;
    private javax.swing.JTabbedPane customersTabbedPanel;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private java.awt.List maintenanceActionList;
    private javax.swing.JLabel maintenanceActionNotesLabel;
    private javax.swing.JTextArea maintenanceActionNotesTextArea;
    private javax.swing.JLabel maintenanceActionTypeLabel;
    private javax.swing.JLabel maintenanceActionsLabel;
    private javax.swing.JComboBox maintenanceTypeComboBox;
    private javax.swing.JLabel maintenanceTypeDescriptionLabel;
    private javax.swing.JTextArea maintenanceTypeDescriptionTextArea;
    private javax.swing.JComboBox mechanicComboBox;
    private javax.swing.JLabel mechanicDescription;
    private javax.swing.JTextArea mechanicDescriptionTextArea;
    private javax.swing.JLabel mechanicNameLabel;
    private javax.swing.JTextField mechanicNameTextField;
    private javax.swing.JPanel mechanicsLabel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel newCustomerDescriptionLabel;
    private javax.swing.JTextArea newCustomerDescriptionTextArea;
    private javax.swing.JLabel newCustomerNameLabel;
    private javax.swing.JTextField newCustomerNameTextField;
    private javax.swing.JLabel newMaintenanceActionTypeNameLabel;
    private javax.swing.JLabel newMaintenanceTypeDescriptionLabel;
    private javax.swing.JTextArea newMaintenanceTypeDescriptionTextArea;
    private javax.swing.JTextField newMaintenanceTypeIntervalTextField;
    private javax.swing.JTextField newMaintenanceTypeNameTextField;
    private javax.swing.JTextArea newMechanicDescriptionTextArea;
    private javax.swing.JLabel newMechanicNameLabel;
    private javax.swing.JTextField newMechanicNameTextField;
    private javax.swing.JLabel newRecommendedIntervalLabel;
    private javax.swing.JLabel newVehicleMakeLabel;
    private javax.swing.JTextField newVehicleMakeTextField;
    private javax.swing.JLabel newVehicleModelLabel;
    private javax.swing.JTextField newVehicleModelTextField;
    private javax.swing.JLabel newVehicleNotesLabel;
    private javax.swing.JTextArea newVehicleNotesTextArea;
    private javax.swing.JLabel newVehicleOdometerLabel;
    private javax.swing.JTextField newVehicleOdometerTextField;
    private javax.swing.JLabel newVehicleYearLabel;
    private javax.swing.JTextField newVehicleYearTextField;
    private javax.swing.JMenuItem openGarageMenuItem;
    private javax.swing.JLabel performMaintenanceActionLabel;
    private javax.swing.JLabel recommendedIntervalLabel;
    private javax.swing.JTextField recommendedIntervalTextField;
    private javax.swing.JLabel selectCustomerLabel;
    private javax.swing.JLabel selectMaintenaceTypeLabel;
    private javax.swing.JLabel selectMechanicLabel;
    private javax.swing.JLabel selectVehicleLabel;
    private javax.swing.JComboBox useMaintenanceActionTypeComboBox;
    private javax.swing.JComboBox vehicleComboBox;
    private javax.swing.JLabel vehicleMakeLabel;
    private javax.swing.JTextField vehicleMakeTextField;
    private javax.swing.JLabel vehicleModelLabel;
    private javax.swing.JTextField vehicleModelTextField;
    private javax.swing.JLabel vehicleNotesLabel;
    private javax.swing.JTextArea vehicleNotesTextArea;
    private javax.swing.JLabel vehicleOdometerLabel;
    private javax.swing.JTextField vehicleOdometerTextField;
    private javax.swing.JLabel vehicleYearLabel;
    private javax.swing.JTextField vehicleYearTextField;
    // End of variables declaration//GEN-END:variables
}
