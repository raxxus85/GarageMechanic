package garagemechanic.Dialogs;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author mark.milford
 */
public class InformationDialog implements DialogInterface{
    
    /**
     * //Method to create an information dialog box!
     * 
     * @param message 
     */
    public void create(String message){
        JOptionPane jOptionPane = new JOptionPane(message,JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = jOptionPane.createDialog("Message!");
        dialog.setAlwaysOnTop(true);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}
