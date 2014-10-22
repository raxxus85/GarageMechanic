package garagemechanic.Dialogs;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author mark.milford
 */
public class WarningDialog implements DialogInterface{
    
    /**
     * //Method to create a warning dialog box!
     * 
     * @param message 
     */
    public void create(String message){
        JOptionPane jOptionPane = new JOptionPane(message,JOptionPane.WARNING_MESSAGE);
        JDialog dialog = jOptionPane.createDialog("Warning Message!");
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}
