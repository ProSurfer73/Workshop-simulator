package fr.insa.TeamA.workshop;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;



/** Class FormNewElements
 * contains subsidiary GUI elements of the program
 * @author Jean
 */
public class ElementsForms
{
    // Kind of equivalent to std::pair in C++
    // This pairs two different elements of undefined types
    private class Pair<F, S> {
        public Pair(F f, S s){
            first = f;
            second = s;
        }
        public F first;
        public S second;
    }


    ElementsForms() {}

    /** Method FormNewMachine
     * @return the new supposedly created, or null if nothing was created
     */
    public static Machine dialogToCreateNewMachine()
    {
        // Creating a new window
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Créer une nouvelle machine...");

        // Make the focus only possible on this window
        stage.initModality(Modality.APPLICATION_MODAL);

        // Creating a new superb GridPane !
        GridPane gridPane = new GridPane();

        // Set a padding of 40px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Add all the different labels
        gridPane.add(new Label("Désignation machine : "), 0,0);
        gridPane.add(new Label("Référence machine : "), 0,1);
        gridPane.add(new Label("Cout horaire : "), 0,2);
        gridPane.add(new Label("Chemin image : "), 0,3);
        gridPane.add(new Label("Largeur image : "), 0,4);
        gridPane.add(new Label("Hauteur image : "), 0,5);

        // Create all the different text fields
        TextField fieldDesignation = new TextField();
        TextField fieldReference = new TextField();
        TextField fieldCoutHoraire = new TextField();
        TextField fieldUrlImage = new TextField();

        // Add a spinner for the width
        Spinner<Integer> widthSpinner = new Spinner<>(5, 800, 80, 5);
        //widthSpinner.setEditable(true);
        gridPane.add(widthSpinner, 1, 4);

        // Add a spinner for the height
        Spinner<Integer> heightSpinner = new Spinner<>(5, 800, 80, 5);
        //heightSpinner.setEditable(true);
        gridPane.add(heightSpinner, 1, 5);

        // Add them to the gridpane
        gridPane.add(fieldDesignation, 1, 0);
        gridPane.add(fieldReference, 1, 1);
        gridPane.add(fieldCoutHoraire, 1, 2);
        gridPane.add(fieldUrlImage, 1, 3);

        // Add constraints in order for the gridpane to be large
        gridPane.getColumnConstraints().add(new ColumnConstraints(150)); // column 0 is 100 wide
        gridPane.getColumnConstraints().add(new ColumnConstraints(250));
        gridPane.setHgap(10); //horizontal gap in pixels r
        gridPane.setVgap(10);


        // Add the cancel button
        Button cancelButton = new Button("Annuler");
        cancelButton.setMinWidth(80.d);
        cancelButton.setCancelButton(true); // the button will be activated when pressing Escape
        cancelButton.setOnAction(actionEvent -> {
            stage.close();
        });
        gridPane.add(cancelButton, 0, 6);

        // Add the validation button
        Button validateButton = new Button("Valider");
        validateButton.setMinWidth(100.d);
        validateButton.setDefaultButton(true); // button activated when pressing Enter
        validateButton.setOnAction(event -> {
            float coutHoraire = -1;
            try { coutHoraire = Float.parseFloat(fieldCoutHoraire.getText()); }
            catch(NumberFormatException numberFormatException){}

            if( fieldDesignation.getText().isEmpty()
                    || fieldReference.getText().isEmpty()
                    || fieldCoutHoraire.getText().isEmpty() )
            {
                // Set a little color red in the background of the field
                if(fieldDesignation.getText().isEmpty()) {
                    fieldDesignation.setStyle("-fx-background-color:#ff5a5a");
                    fieldDesignation.textProperty().addListener(new ChangeListener<String>() {
                        @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            fieldDesignation.setStyle(!newValue.isEmpty()?"":"-fx-background-color:#ff5a5a");
                        }
                    });
                }
                if(fieldReference.getText().isEmpty()) {
                    fieldReference.setStyle("-fx-background-color:#ff5a5a");
                    fieldReference.textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            fieldReference.setStyle(!newValue.isEmpty() ? "" : "-fx-background-color:#ff5a5a");
                        }
                    });
                }
                if(fieldCoutHoraire.getText().isEmpty()) {
                    fieldCoutHoraire.setStyle("-fx-background-color:#ff5a5a");
                    fieldCoutHoraire.textProperty().addListener(new ChangeListener<String>() {
                        @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            fieldCoutHoraire.setStyle(!newValue.isEmpty()?"":"-fx-background-color:#ff5a5a");
                        }
                    });
                }

                // Count the number of fields left empty
                int nbFieldEmpty = 0;
                if(fieldDesignation.getText().isEmpty()) ++nbFieldEmpty;
                if(fieldCoutHoraire.getText().isEmpty()) ++nbFieldEmpty;
                if(fieldReference.getText().isEmpty()) ++nbFieldEmpty;

                // Create a simple error dialog
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                if(nbFieldEmpty >= 2){
                    alert.setHeaderText("Cases vides");
                    alert.setContentText(String.valueOf(nbFieldEmpty)+" champs obligatoires n'ont pas été remplis.");
                }
                else {
                    alert.setHeaderText("Case vide");
                    alert.setContentText("Un champs obligatoire n'a pas été rempli.");
                }
                alert.show();
            }
            else if(coutHoraire == -1)
            {
                fieldCoutHoraire.setStyle("-fx-background-color:#ff5a5a");
                fieldCoutHoraire.textProperty().addListener(new ChangeListener<String>() {
                    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
                    {
                        float testFloat = -1; // We consider that -1 is the 'error value'
                        try { testFloat = Float.parseFloat(newValue); }
                        catch(NumberFormatException numberFormatException){}
                        fieldCoutHoraire.setStyle((testFloat!=-1)?"":"-fx-background-color:#ff5a5a");
                    }
                });

                // Create a simple error dialog
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("\""+fieldCoutHoraire.getText()+"\" n'est pas un flottant.");
                alert.setContentText("Veuillez entrer un nombre à virgule pour le cout horaire.");
                alert.show();
            }
            else {
                // We close the window
                stage.close();

                // We use a little trick here, because we can't modify a local boolean of this method,
                // we use the property of resizability of the window as a boolean to express that all
                // parameters are good, an that we can create the object Machine
                stage.setResizable(true);
            }
        });
        gridPane.add(validateButton, 2, 6);

        // Add the import image button
        Button importImageButton = new Button();
        importImageButton.setText("Depuis le disque");
        gridPane.add(importImageButton, 2, 3);
        importImageButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Charger une image...");
            fileChooser.getExtensionFilters().addAll(
                    // here we define all the file formats that JavaFX can open
                    new FileChooser.ExtensionFilter("PNG, JPEG, BMP, GIF",
                            Arrays.asList("*.png","*.jpeg", "*.jpg", "*.bmp", "*.gif")),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);

            if(selectedFile != null){
                fieldUrlImage.setText("file:"+selectedFile.getAbsolutePath());
            }
        });



        stage.setScene(new Scene(gridPane));


        try
        {
            stage.showAndWait();
        }
        catch(Throwable ignore){}
        //catch(NullPointerException ignore){}

        // We return the machine created from the parameters entered by the user, null if not created
        // See above for the trick with stage.isResizable()

        if(stage.isResizable())
        {
            Machine newMachine = new Machine(fieldReference.getText(), fieldDesignation.getText(), 10,10, widthSpinner.getValue(), heightSpinner.getValue(), Float.parseFloat(fieldCoutHoraire.getText()));

            if(!fieldUrlImage.getText().isEmpty())
            {
                // Load the corresponding image
                try {
                    String path = fieldUrlImage.getText();
                    Image image = new Image(path);
                    newMachine.setImageView(image, 30, 30, widthSpinner.getValue(), heightSpinner.getValue(), fieldUrlImage.getText());
                }
                catch(Throwable e)
                {
                    new Alert(Alert.AlertType.ERROR, "Chargement de l'image correspondante impossible").show();
                }
            }

            return newMachine;
        }
        else
            return null;
    }

    /** Method dialogToDisplayMachine
     * /!\ not functionnal only prints an empty table /!\
     * the purpose of this method is to display all the machine in a gui and to propose the user to modify
     * and suppress some of them
     * @param listMachine the current list of the machines
     * @return the list of machines, after user intervention
     * @author Jean
     */
    public static ArrayList<Machine> dialogToDisplayMachine(ArrayList<Machine> listMachine)
    {
        // Creating a new window
        Stage stage = new Stage();
        stage.setResizable(false);

        // Create a new scene
        Scene scene = new Scene(new Group());
        stage.setTitle("Liste des machines de l'usine");
        stage.setWidth(650);
        stage.setHeight(500);

        // Creating a new table view
        TableView table = new TableView();

        // Creating the columns
        TableColumn designationCol = new TableColumn("Désignation");
        designationCol.setCellValueFactory(new PropertyValueFactory<Machine,String>("designation"));
        TableColumn referenceCol = new TableColumn("Référence");
        referenceCol.setCellValueFactory(new PropertyValueFactory<Machine,String>("reference"));
        TableColumn hourlyCostCol = new TableColumn("Cout horaire");
        hourlyCostCol.setCellValueFactory(new PropertyValueFactory<Machine,String>("hourlyCost"));



        // Add the columns to the table
        table.getColumns().addAll(designationCol, referenceCol, hourlyCostCol);


        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);


        ((Group)scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
        return null;
    }

    public static String dialogToAskBackgroundImage()
    {
        // Creating the window
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Changer l'image de fond");

        // Creating a new label
        Label label = new Label();
        label.setText("Chemin image : ");

        // Creating a new text field
        TextField field = new TextField();

        // Creating a new button
        Button button = new Button("Depuis le disque");
        button.setOnAction(event -> {
            // Ask the user for an image
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Charger une image...");
            fileChooser.getExtensionFilters().addAll(
                    // here we define all the file formats that JavaFX can open
                    new FileChooser.ExtensionFilter("PNG, JPEG, BMP, GIF",
                            Arrays.asList("*.png","*.jpeg", "*.jpg", "*.bmp", "*.gif")),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(stage);
            if(selectedFile != null) field.setText("file:"+selectedFile.getAbsolutePath());
        });

        // Add the cancel button
        Button cancelButton = new Button("Annuler");
        cancelButton.setCancelButton(true); // the button will be activated when pressing Escape
        cancelButton.setOnAction(actionEvent -> {
            stage.close();
            field.setText("");
        });

        // Add the validate button
        Button validateButton = new Button("Valider");
        validateButton.setDefaultButton(true); // button activated when pressing Enter
        validateButton.setOnAction(event -> {
            stage.close();
        });

        // When the user try to close the window
        stage.setOnCloseRequest(event -> {
            // If the user close the window, then the form is unvalidated
            field.setText("");
        });


        // We create a grid pane to organize all the components on the screen
        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(40, 40, 40, 40)); // Set a padding of 40px on each side
        gridpane.add(label, 0, 0);
        gridpane.add(field, 1, 0);
        gridpane.add(button, 2, 0);
        gridpane.add(cancelButton, 0, 1);
        gridpane.add(validateButton, 2, 1);

        // Add constraints in order for the gridpane to be large
        gridpane.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide
        gridpane.getColumnConstraints().add(new ColumnConstraints(250));
        gridpane.setHgap(10); //horizontal gap in pixels
        gridpane.setVgap(20);

        // Display the window with all the components included
        stage.setScene(new Scene(gridpane));
        stage.showAndWait();

        return field.getText();
    }

    public static Poste dialogNewPoste(ArrayList<Machine> allMachines)
    {
        Poste poste = null;
        Optional<String> command;

        TextInputDialog tid = new TextInputDialog();
        tid.setTitle("Créer un nouveau poste");
        tid.setHeaderText("Tapez la reference, suivie de la designation, suivie du type, et des references machines.\nExemple: \"reference designation type refMachine1 refMAchine2\"");
        command = tid.showAndWait();

        // If the user actually typed something
        if(command.isPresent())
        {
            String[] subCommands = command.get().split(" ");

            try {
                String reference = subCommands[0];
                String designation = subCommands[1];
                String type = subCommands[2];
                String[] yourArray = Arrays.copyOfRange(subCommands, 3, subCommands.length);


                ArrayList<Machine> selectedMachines = new ArrayList<>();

                for(String s : yourArray)
                {
                    boolean found=false;
                    for(Machine m : allMachines)
                    {
                        if(m.getReference().equals(s))
                        {
                            selectedMachines.add(m);
                            found=true;
                        }
                    }

                    if(!found) {
                        throw new Throwable("la reference '"+s+"' ne fait pas parti des machines.");
                    }
                }

                poste = new Poste(reference, designation, type, selectedMachines);
            }
            catch(Throwable e)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Impossible de créer le poste");
                alert.setContentText(e.getMessage());
                alert.show();
            }

        }

        return poste;
    }


    public static Gamme dialogNewGamme(ArrayList<Gamme> gammes, ArrayList<Machine> machines, ArrayList<Poste> postes, ArrayList<Operation> operations)
    {
        // What's gonna be returned
        Gamme returnedGamme = new Gamme("",new ArrayList<>());

        // Creating a new window
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Créer une nouvelle gamme...");

        // Make the focus only possible on this window
        stage.initModality(Modality.APPLICATION_MODAL);

        // Creating a new superb GridPane !
        GridPane gridpane = new GridPane();

        // Set a padding of 40px on each side
        gridpane.setPadding(new Insets(40, 40, 40, 40));

        // text fields
        ArrayList<TextField> textFields = new ArrayList<>();
        TextField textFieldReference = new TextField();

        // Validation button
        Button validateButton = new Button("Valider");
        validateButton.setMinWidth(100.d);
        validateButton.setDefaultButton(true); // button activated when pressing Enter
        validateButton.setOnAction(event -> {
            // verify if all forms are filled
            boolean filled = true;
            for(int i=0;i<textFields.size()-2; i+=2){
                if(textFields.get(i).getText().isEmpty() && !textFields.get(i+2).getText().isEmpty())
                    filled = false;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Saisie incorrecte");

            if(!filled){
                alert.setContentText("Certains champs n'ont pas été remplis !");
                alert.show();
            }
            else if(textFields.get(0).getText().isEmpty() && filled) {
                alert.setContentText("Aucun champs n'a pas été remplis !");
                alert.show();
            }
            else if(textFieldReference.getText().isEmpty()){
                alert.setContentText("La reference n'a pas été remplis !");
                alert.show();
            }
            else {

                // check if the reference wasn't already taken
                boolean found = false;
                for(Gamme g : gammes)
                {
                    if(g.getReference().equals(textFieldReference.getText()))
                        found = true;
                }

                if(found) {
                    alert.setContentText("La reference demande est déjà prise !");
                    alert.show();
                }
                else
                {
                    // Is there a problem ?
                    boolean everythingsFine = true;

                    // Array of the equipements present in the workshop
                    ArrayList<Equipement> arrayEquipements = new ArrayList<>();
                    for(Machine m : machines)
                        arrayEquipements.add(m);
                    for(Poste p : postes)
                        arrayEquipements.add(p);

                    // liste reference equipements already in the workshop
                    ArrayList<String> listeRefEquipement = new ArrayList<>();
                    for(Equipement e : arrayEquipements)
                        listeRefEquipement.add(e.getReference());

                    ArrayList<String> listeRefOperations = new ArrayList();
                    for(Operation o : operations)
                        listeRefOperations.add(o.getReferenceOperation());

                    ArrayList<Equipement> finalListeEquipements = new ArrayList<>();
                    boolean takeThat = true;
                    for(TextField tf : textFields)
                    {
                        if(takeThat)
                        {
                            if(listeRefEquipement.contains(tf.getText())) {
                                finalListeEquipements.add(arrayEquipements.get(listeRefEquipement.indexOf(tf.getText())));
                            }
                            else if(!tf.getText().isEmpty()){
                                // there is a problem
                                everythingsFine = false;
                                break;
                            }
                        }

                        takeThat = !takeThat;
                    }

                    ArrayList<Operation> finalListeOperations = new ArrayList<>();
                    takeThat = false;
                    for(TextField tf : textFields)
                    {
                        if(takeThat)
                        {
                            if(listeRefOperations.contains(tf.getText())){
                                finalListeOperations.add(operations.get(listeRefOperations.indexOf(tf.getText())));
                            }
                            else if(!tf.getText().isEmpty()) {
                                everythingsFine = false;
                                break;
                            }
                        }
                        takeThat = !takeThat;
                    }


                    if(!everythingsFine){
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setTitle("Erreur de saisie.");
                        alert2.setContentText("Certaine(s) reference(s) equipement ou operation entré(s) n'existe(nt) pas.");
                        alert2.show();
                    }
                    else {
                        // create the gamme
                        returnedGamme.creerGamme(textFieldReference.getText(), finalListeOperations);

                        // close the window
                        stage.close();
                    }
                }
            }
        });

        // Add the cancel button
        Button cancelButton = new Button("Annuler");
        cancelButton.setMinWidth(80.d);
        cancelButton.setCancelButton(true); // the button will be activated when pressing Escape
        cancelButton.setOnAction(event -> {
            stage.close();
        });

        final int maximum_steps = 20;

        // The change listener, to handle the events from the textfields
        final ChangeListener changeListener = new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                // if it's actually the last textfield
                // to fix: not a great way to detect the last textfield
                if( textFields.get(textFields.size()-2).getText().equals(newValue))
                {
                    // If the was empty but is now filled
                    if (oldValue.isEmpty() && !newValue.isEmpty() && textFields.size()<maximum_steps*2)
                    {
                        // we add another text field
                        TextField addedTextField = new TextField();
                        addedTextField.textProperty().addListener(this);
                        TextField addedTextField2 = new TextField();
                        addedTextField.setPromptText("réf. équipement");
                        addedTextField2.setPromptText("réf. opération");
                        textFields.add(addedTextField);
                        textFields.add(addedTextField2);
                        gridpane.add(addedTextField, 1, 5+textFields.size()/2);
                        gridpane.add(addedTextField2, 2, 5+textFields.size()/2);

                        // we add a new label
                        gridpane.add(new Label("reference n°"+textFields.size()/2+" : "), 0, 5+textFields.size()/2);

                        // set height
                        stage.setHeight(110+40+textFields.size()*14);
                    }
                    else if (newValue.isEmpty() && !oldValue.isEmpty())
                    {
                        if(textFields.size() < maximum_steps*2 || textFields.get(textFields.size()-4).getText().isEmpty()) {

                            // we delete the last text field
                            gridpane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 5 + textFields.size() / 2);
                            textFields.remove(textFields.size() - 1);
                            textFields.remove(textFields.size() - 1);

                            // reset the height of the window
                            stage.setHeight(110 + 40 + textFields.size() * 27);
                        }
                    }
                }
            }
        };


        // Initial configuration and position for the first textfield,
        // the first label, the cancel/validate buttons
        gridpane.add(new Label("reference n°1 :   "), 0, 5);
        TextField firstTextField = new TextField();
        TextField secondTextField = new TextField();
        firstTextField.setPromptText("réf. équipement");
        firstTextField.textProperty().addListener(changeListener);
        secondTextField.setPromptText("réf. opération");
        textFields.add(firstTextField);
        textFields.add(secondTextField);
        gridpane.add(new Label("Reference des opérations correspondantes:"), 1, 4);
        gridpane.add(firstTextField, 1, 5);
        gridpane.add(secondTextField, 2, 5);
        gridpane.add(validateButton, 1,25+5);
        gridpane.add(cancelButton, 0, 25+5);

        gridpane.add(new Label("reference gamme : "), 0, 3);
        gridpane.add(textFieldReference, 1, 3);




        // Display the window with all the components included
        stage.setScene(new Scene(gridpane));
        stage.showAndWait();


        if(returnedGamme.getReference().isEmpty())
            return null;
        else
            return returnedGamme;
    }

    public static Operation dialogNewOperation(ArrayList<Operation> operations, ArrayList<Machine> machines, ArrayList<Poste> postes)
    {
        // What the function will return
        // Here we create an "empty operation"
        Operation returnedOperation = new Operation("","","",0);

        // new stage (window)
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Créer une nouvelle opération");

        // new gridpane
        GridPane gridpane = new GridPane();

        // Set a padding of 40px on each side
        gridpane.setPadding(new Insets(40, 40, 40, 40));

        // add all the labels
        gridpane.add(new Label("Reference de l'opération : "), 0, 0);
        gridpane.add(new Label("Designation de l'operation : "), 0, 1);
        gridpane.add(new Label("Reference de l'equipement : "), 0, 2);
        gridpane.add(new Label("Duree de l'operation : "), 0, 3);

        // Create the textfields
        TextField refOTextField = new TextField();
        TextField designTextField = new TextField();
        TextField refETextField = new TextField();
        TextField durTextField = new TextField();

        // Add the textField to the gridpane !
        gridpane.add(refOTextField, 1, 0);
        gridpane.add(designTextField, 1, 1);
        gridpane.add(refETextField, 1, 2);
        gridpane.add(durTextField, 1, 3);

        // Create buttons
        Button validateButton = new Button("Valider");
        validateButton.setMinWidth(100.d);
        validateButton.setDefaultButton(true); // button activated when pressing Enter
        validateButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Saisie incorrecte");

            if(refOTextField.getText().isEmpty() || refETextField.getText().isEmpty()
                    || designTextField.getText().isEmpty() || durTextField.getText().isEmpty()) {
                alert.setContentText("Certains champs n'ont pas été remplis.");
                alert.show();
            }
            else {
                // check if the reference of the operation is not already taken
                boolean found = false;
                for(Operation m : operations)
                {
                    if(m.getReferenceOperation().equals(refOTextField.getText()))
                        found = true;
                }

                if(found){
                    alert.setContentText("La reference operation est deja prise.");
                    alert.show();
                }
                else {

                    // check if the reference of the equipement exists
                    found = false;
                    for(Machine m : machines)
                    {
                        if(m.getReference().equals(refETextField.getText()))
                            found = true;
                    }
                    for(Poste p : postes)
                    {
                        if(p.getReference().equals(refETextField.getText()))
                            found = true;
                    }

                    if(!found){
                        alert.setContentText("La reference equipement n'existe pas.");
                        alert.show();
                    }
                    else {

                        if(Integer.parseInt(durTextField.getText()) < 0){
                            //Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("La durée de l'opértion ne peut etre négative.");
                            alert.show();
                        }

                        returnedOperation.create(refOTextField.getText(), designTextField.getText(),
                                refETextField.getText(), Integer.parseInt(durTextField.getText()) );

                        stage.close();
                    }
                }
            }
        });
        Button cancelButton = new Button("Annuler");
        cancelButton.setMinWidth(80.d);
        cancelButton.setCancelButton(true); // the button will be activated when pressing Escape
        cancelButton.setOnAction(event -> {
            stage.close();
        });

        // Add the buttons to the gridpane
        gridpane.add(validateButton, 1, 4);
        gridpane.add(cancelButton, 0, 4);

        // Display the window with all the components included
        stage.setScene(new Scene(gridpane));
        stage.showAndWait();

        // Return the created operation if the operation was actually created
        if(returnedOperation.getReferenceOperation().isEmpty())
            return null;
        else
            return returnedOperation;
    }

    public static Produit dialogNewProduit(ArrayList<Produit> produitsExistants)
    {
        // The product that will be returned by the function
        // If a product is actually created
        Produit returnedProduct = new Produit("","",0);

        // new stage (window)
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Créer un nouveau type de produit");

        // new gridpane
        GridPane gridpane = new GridPane();

        // Set a padding of 40px on each side
        gridpane.setPadding(new Insets(40, 40, 40, 40));

        // add all the labels
        gridpane.add(new Label("Reference du produit : "), 0, 0);
        gridpane.add(new Label("Designation du produit : "), 0, 1);
        gridpane.add(new Label("Quantité initiale : "), 0, 2);

        // Create the textfields
        TextField referenceProduit = new TextField();
        TextField designationProduit = new TextField();
        TextField quantityTextField = new TextField();

        // Add the textField to the gridpane !
        gridpane.add(referenceProduit, 1, 0);
        gridpane.add(designationProduit, 1, 1);
        gridpane.add(quantityTextField, 1, 2);

        // Create buttons
        Button validateButton = new Button("Valider");
        validateButton.setMinWidth(100.d);
        validateButton.setDefaultButton(true); // button activated when pressing Enter
        validateButton.setOnAction(event -> {
            if(referenceProduit.getText().isEmpty()
                    || designationProduit.getText().isEmpty()
                    || quantityTextField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setContentText("Champs non remplis.");
                alert.show();
            }
            else
            {
                boolean found = false;

                for(Produit p : produitsExistants) {
                    if(p.getReference().equals(referenceProduit.getText()))
                        found = true;
                }

                if(found){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de saisie");
                    alert.setContentText("La reference entree existe déjà.");
                    alert.show();
                }
                else {
                    float newQuantity = -1;
                    try {
                        newQuantity = Float.parseFloat(quantityTextField.getText());
                    }
                    catch(NumberFormatException ignore){}

                    if(newQuantity < 0) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur de saisie");
                        alert.setContentText("Merci d'entrer un flottant positif.");
                        alert.show();
                    }
                    else {
                        // create the product using the method create
                        returnedProduct.create(referenceProduit.getText(), designationProduit.getText(), newQuantity);

                        // close the window
                        stage.close();
                    }
                }
            }
        });
        Button cancelButton = new Button("Annuler");
        cancelButton.setMinWidth(80.d);
        cancelButton.setCancelButton(true); // the button will be activated when pressing Escape
        cancelButton.setOnAction(event -> {
            stage.close();
        });

        // Add the buttons to the gridpane
        gridpane.add(validateButton, 1, 4);
        gridpane.add(cancelButton, 0, 4);

        // Display the window with all the components included
        stage.setScene(new Scene(gridpane));
        stage.showAndWait();

        // returned product
        if(returnedProduct.getReference().isEmpty())
            return null;
        else
            return returnedProduct;
    }

    public static void dialogPrintArray(final Object objects[], final String nameOfTheObject)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Liste des " + nameOfTheObject + "s de l'usine");
        alert.setHeaderText("Voici l'ensemble des "+nameOfTheObject+"s de l'usine :");

        String contentText = "";

        if(objects.length == 0)
            contentText = "Aucun "+nameOfTheObject+" crée.";

        else {
            for (Object m : objects) {
                contentText = " -> " + contentText + m.toString() + '\n';
            }
        }
        alert.setContentText(contentText);
        alert.show();
    }

    public static Operateur dialogNewOperateur()
    {
        Operateur returnedOperateur = new Operateur("","");

        // new stage (window)
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("");

        // new gridpane
        GridPane gridpane = new GridPane();

        // Set a padding of 40px on each side
        gridpane.setPadding(new Insets(40, 40, 40, 40));

        // Adding the labels to the gridpane
        gridpane.add(new Label("Reference : "), 0, 0);
        gridpane.add(new Label("Nom : "), 0, 1);

        // Addind text fields to the grid pane
        TextField referenceTextField = new TextField();
        TextField nameTextfield = new TextField();
        gridpane.add(referenceTextField, 1, 0);
        gridpane.add(nameTextfield, 1, 1);

        // Create buttons
        Button validateButton = new Button("Valider");
        validateButton.setMinWidth(100.d);
        validateButton.setDefaultButton(true); // button activated when pressing Enter
        validateButton.setOnAction(event -> {
            if(referenceTextField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Sasie incorrecte");
                alert.setContentText("La reference est un champs obligatoire.\nVeuillez au moins entrer la réference.");
                alert.show();
            }
            else {
                returnedOperateur.create(referenceTextField.getText(), nameTextfield.getText());
                stage.close();
            }
        });
        Button cancelButton = new Button("Annuler");
        cancelButton.setMinWidth(80.d);
        cancelButton.setCancelButton(true); // the button will be activated when pressing Escape
        cancelButton.setOnAction(event -> {
            stage.close();
        });
        gridpane.add(validateButton, 1, 4);
        gridpane.add(cancelButton, 0, 4);


        // actually prints the window
        stage.setScene(new Scene(gridpane));
        stage.showAndWait();

        if(returnedOperateur.getReference().isEmpty())
            return null;
        else
            return returnedOperateur;
    }

    public static boolean dialogDeleteProduit(ArrayList<Produit> elements)
    {
        if(!elements.isEmpty())
        {
            ArrayList<String> references = new ArrayList<>();

            for(Produit p : elements)
                references.add(p.getReference());


            ChoiceDialog<String> dialog = new ChoiceDialog<String>(references.get(0), references);
            dialog.setTitle("Supprimer un produit");
            dialog.setHeaderText("Voici un dialogue de choix");
            dialog.setContentText("Choississez la reference du produit que vous voulez supprimer :");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                // we delete the selectionned element
                elements.remove(references.indexOf(result.get()));

                // a product was selectionned
                return true;
            }
        }

        // No product as selectionned
        return false;
    }

    public static boolean dialogDeleteGamme(ArrayList<Gamme> operations)
    {
        if(!operations.isEmpty())
        {
            ArrayList<String> references = new ArrayList<>();

            for(Gamme g : operations)
                references.add(g.getReference());

            ChoiceDialog<String> dialog = new ChoiceDialog<String>(references.get(0), references);
            dialog.setTitle("Supprimer une gamme");
            dialog.setContentText("Choississez la reference de la gamme que vous voulez supprimer :");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                // we delete the selectionned element
                operations.remove(references.indexOf(result.get()));

                // a gamme was selectionned
                return true;
            }
        }
        return false;
    }

    public static boolean dialogDeleteMachine(ArrayList<Machine> machines, ObservableList<Node> nodes)
    {
        if(!machines.isEmpty())
        {
            ArrayList<String> references = new ArrayList<>();

            for(Machine p : machines)
                references.add(p.getReference());

            ChoiceDialog<String> dialog = new ChoiceDialog<String>(references.get(0), references);
            dialog.setTitle("Supprimer une machine");
            dialog.setHeaderText("Choississez la reference de la machine que vous souhaitez supprimer :");
            dialog.setContentText("C'est a vous:");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                // we delete the machine from the nodes
                nodes.remove(machines.get(references.indexOf(result.get())).getImageView());

                // we delete the selectionned element
                machines.remove(references.indexOf(result.get()));

                // a product was selectionned
                return true;
            }
        }

        // No product as selectionned
        return false;
    }

    public static boolean dialogDeleteOperateur(ArrayList<Operateur> operateurs)
    {
        if(!operateurs.isEmpty())
        {
            ArrayList<String> references = new ArrayList<>();

            for(Operateur p : operateurs)
                references.add(p.getReference());

            ChoiceDialog<String> dialog = new ChoiceDialog<String>(references.get(0), references);
            dialog.setTitle("Supprimer un opérateur:");
            dialog.setHeaderText("Selectionnez la référence de l'opérateur concernée :");
            dialog.setContentText("Choississez :");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                // we delete the selectionned element
                operateurs.remove(references.indexOf(result.get()));

                // a product was selectionned
                return true;
            }
        }

        // No product as selectionned
        return false;
    }

    public static boolean dialogDeleteOperation(ArrayList<Operation> operations)
    {
        if(!operations.isEmpty())
        {
            ArrayList<String> references = new ArrayList<>();

            for(Operation o : operations)
                references.add(o.getReference());

            ChoiceDialog<String> dialog = new ChoiceDialog<String>(references.get(0), references);
            dialog.setTitle("Supprimer une opération...");
            dialog.setHeaderText("Selectionnez la référence de l'opération concernée.");
            dialog.setContentText("Choissisez :");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                // we delete the selectionned element
                operations.remove(references.indexOf(result.get()));

                // a product was selectionned
                return true;
            }
        }
        // no operation was selectionned
        return false;
    }

    public static boolean dialogDeletePoste(ArrayList<Poste> postes)
    {
        if(!postes.isEmpty())
        {
            ArrayList<String> references = new ArrayList<>();

            for(Poste o : postes)
                references.add(o.getReference());

            ChoiceDialog<String> dialog = new ChoiceDialog<String>(references.get(0), references);
            dialog.setTitle("Supprimer un poste...");
            dialog.setHeaderText("Selectionnez la reference du poste que vous souhaitez supprimer.");
            dialog.setContentText("Choississez :");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                // we delete the selectionned element
                postes.remove(references.indexOf(result.get()));

                // a product was selectionned
                return true;
            }
        }
        // no operation was selectionned
        return false;
    }

    public static boolean dialogDeActivateGamme(ArrayList<Gamme> gammes, final boolean activateOrNot, ArrayList<Machine> machines)
    {
        if(!gammes.isEmpty())
        {
            ArrayList<String> references = new ArrayList<>();

            for(Gamme g : gammes) {
                if(g.isActivated() == activateOrNot)
                    references.add(g.getReference());
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<String>(references.get(0), references);
            dialog.setTitle("Supprimer un gamme à "+(activateOrNot?"":"des")+"activer...");
            dialog.setHeaderText("Selectionnez la reference de la gamme que vous souhaitez "+(activateOrNot?"":"des")+"activer.");
            dialog.setContentText("Choississez :");

            // Traditional way to get the response value.
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent())
            {
                // we activate the gamme selectionned and return if it was actually activated
                if(activateOrNot)
                    // we activate the gamme
                    return gammes.get(references.indexOf(result.get())).activate(machines);
                else
                    // we unactivate the gamme
                    return gammes.get(references.indexOf(result.get())).deactivate(machines);
            }
        }
        return false; // say that no gamme was activated
    }


}

