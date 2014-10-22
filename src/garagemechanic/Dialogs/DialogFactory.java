package garagemechanic.Dialogs;


/**
 * The DialogFactory class. Creates specific dialogs based on input.
 * @author mark.milford
 */
public class DialogFactory {
        
    public void createDialogMessage(DialogType dialogType,String message){
        
        if(dialogType ==dialogType.WARNING_MESSAGE){
            WarningDialog newWarningDialog = new WarningDialog();
            newWarningDialog.create(message);
        }else if(dialogType == dialogType.ERROR_MESSAGE){
            ErrorDialog newErrorDialog = new ErrorDialog();
            newErrorDialog.create(message);
        }else if(dialogType == dialogType.INFORMATION_MESSAGE){
            InformationDialog newInformationDialog = new InformationDialog();
            newInformationDialog.create(message);
        }else if(dialogType == dialogType.ABOUT_WINDOW){
            new AboutWindow().setVisible(true);
        }


    }  
}
