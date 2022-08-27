package fr.insa.TeamA.workshop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
@author Jean
*/
public class PasswordHandler implements Serializable
{
    // current password (hash code)
    private int hashCode;

    public PasswordHandler()
    {
        // There is no password
        hashCode = -1;
    }

    public void loadPasswordFromFile(Scanner file)
    {
        hashCode = Integer.parseInt(file.nextLine());
        System.out.println(hashCode);
    }

    public void savePasswordToFile(FileWriter file) throws IOException
    {
        file.write(String.valueOf(hashCode) + '\n');
    }

    public void changePassword(String oldPassword, String newPassword)
    {
        if(checkPassword(oldPassword)){
            hashCode = newPassword.hashCode();
        }
    }

    public boolean hasPassword()
    {
        return hashCode != -1;
    }

    public boolean checkPassword(String input)
    {
        return (hashCode == input.hashCode());
    }


    public boolean askPasswordPopup()
    {
        CheckPasswordDialog passwordDialog = new CheckPasswordDialog();

        boolean continuer = true;

        do {
            passwordDialog.showAndWait();

            if( passwordDialog.getPasswordField().hashCode() == hashCode
             || passwordDialog.getPasswordField().equals("cancel") )
                continuer = false;
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                //alert.initModality(Modality.WINDOW_MODAL);
                alert.setTitle("Incorrect.");
                alert.setContentText("Le mot de passe que vous avez entre est incorrect.\nVeuillez reessayer ou annuler.");
                alert.showAndWait();
            }
        }
        while(continuer);

        return passwordDialog.getPasswordField().hashCode() == hashCode;
    }

    public boolean askNewPasswordPopup()
    {
        NewPasswordDialog passwordDialog = new NewPasswordDialog();

        if(passwordDialog.isInputCorrect()){
            hashCode = passwordDialog.getPasswordField().hashCode();
            return true;
        }

        return false;
    }
}



/*
@author
 */
class CheckPasswordDialog extends Dialog<String>
{
    private PasswordField passwordField;

    public CheckPasswordDialog()
    {
        setTitle("Mot de passe");
        setHeaderText("Merci d'entrer le mot de passe ci-dessous :");

        ButtonType passwordButtonType = new ButtonType("Valider", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(passwordButtonType, ButtonType.CANCEL);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        HBox hBox = new HBox();
        hBox.getChildren().add(passwordField);
        hBox.setPadding(new Insets(20));

        HBox.setHgrow(passwordField, Priority.ALWAYS);

        getDialogPane().setContent(hBox);

        Platform.runLater(() -> passwordField.requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton == passwordButtonType && !passwordField.getText().isEmpty()) {
                return passwordField.getText();
            }
            return "cancel";
        });
    }

    public String getPasswordField() {
        return passwordField.getText();
    }
}


/*
@author Jean
 */
class NewPasswordDialog extends Stage
{
    private PasswordField passwordField, passwordField2;

    public NewPasswordDialog()
    {
        // Parameters of the window
        setTitle("Mot de passe");
        //setHeaderText("Merci d'entrer le mot de passe ci-dessous :");


        // The button
        Button passwordButton = new Button("Valider");
        Button cancelButton = new Button("Annuler");

        passwordButton.setOnAction(event -> {
            if(isInputCorrect()) {
                this.close();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur de saisie.");
                alert.show();
            }
        });

        cancelButton.setOnAction(event -> {
            // clear the password field
            passwordField.setText(new String());

            // close the window
            this.close();
        });

        // The first field
        passwordField = new PasswordField();
        passwordField.setPromptText("New password");

        // The second field
        passwordField2 = new PasswordField();
        passwordField2.setPromptText("Retype the new password");

        // The vertical box
        VBox hBox = new VBox();
        hBox.getChildren().addAll(passwordField, passwordField2, passwordButton, cancelButton);
        hBox.setPadding(new Insets(20));

        // Request focus on the first password field
        Platform.runLater(() -> passwordField.requestFocus());

        this.setScene(new Scene(hBox));
        this.showAndWait();
    }

    public String getPasswordField()
    {
        return passwordField.getText();
    }

    public boolean isInputCorrect()
    {
        return passwordField.getText().equals(passwordField2.getText()) && !passwordField.getText().isEmpty();
    }
}
