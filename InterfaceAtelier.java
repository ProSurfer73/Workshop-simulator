package fr.insa.TeamA.workshop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;



/** Class InterfaceAtelier
 * Main class of the project, creates and manages the GUI
 * @author Jean
 */
public class InterfaceAtelier extends Application implements Serializable
{
    // The stage (the window)
    private transient Stage stage;
    private transient Scene scene;
    private transient Group group;
    private transient MenuBar menuBar;

    // All the different components of a workshop !
    private ArrayList<Machine> machines;
    private ArrayList<Poste> postes;
    private ArrayList<Produit> produits;
    private ArrayList<Operation> operations;
    private ArrayList<Gamme> gammes;
    private ArrayList<Operateur> operateurs;

    // Retain if changes haven't been saved
    // true -> changes should be saved
    private transient boolean modified;
    private transient File currentSaveFile;

    // Background image
    private transient ImageView imageView;
    private String pathBackgroundImage;

    // Forms when the user click on the menus
    private transient ElementsForms elementsForms;

    // Placement machine
    private transient Machine selectedMachine;

    // Password handler
    private PasswordHandler passwordHandler;

    // The icon loader
    private transient IconLoader iconLoader;

    /** Default constructor
     * Every attribute is initialised at the call of the 'start' method
     */
    public InterfaceAtelier()
    {
        imageView = new ImageView();
        iconLoader = new IconLoader();
    }

    /** Initialize or reset all the attributes of the object
     * "Create an empty workshop" so to speak
     */
    public void resetWorkshop()
    {
        // Reset every attributes of the workshop
        machines = new ArrayList<Machine>();
        postes = new ArrayList<Poste>();
        produits = new ArrayList<Produit>();
        operations = new ArrayList<Operation>();
        gammes = new ArrayList<Gamme>();
        operateurs = new ArrayList<Operateur>();
        elementsForms = new ElementsForms();
        currentSaveFile = null;
        pathBackgroundImage = null;
        selectedMachine = null;
        scene = null;
        loadBackgroundImage("default");
        modified = false;
        updateWindowName();
        group = new Group();
        passwordHandler = new PasswordHandler();
    }

    public void set(InterfaceAtelier a)
    {
        // reset all the correct attributes
        this.machines = a.machines;
        this.postes = a.postes;
        this.produits = a.produits;
        this.operations = a.operations;
        this.gammes = a.gammes;
        this.operations = a.operations;
        this.pathBackgroundImage = a.pathBackgroundImage;
        loadBackgroundImage(a.pathBackgroundImage);
        this.passwordHandler = a.passwordHandler;
        this.currentSaveFile = a.currentSaveFile;

        for(Machine m : machines)
        {
            if(m.getImageView() != null)
                group.getChildren().add(m.getImageView());
        }
    }

    /** Start Method
     * Main method of the class, is called when running the application
     * @param primaryStage the created stage (the window)
     * @throws Exception if something goes wrong
     */
    @Override public void start(Stage primaryStage) throws Exception
    {
        // Set a minimum width to the window to ensure that all menus are printed correctly
        primaryStage.setMinWidth(800);

        // Name simplification
        stage = primaryStage;

        // Initialize all the attributs of the object relating to the workshop
        resetWorkshop();

        // Don't let the user quit without asking him if he wants to save the changes
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            if(quitWithoutSaving())
                stage.close();
        });

        // Creates the corresponding menubar and scene
        prepareMenuBar();
        prepareScene();

        // Make the window appear on the screen
        primaryStage.show();
    }

    /** Method updateWindowName
     * redetermine the name of the window
     */
    private void updateWindowName()
    {
        stage.setTitle((currentSaveFile==null?"Atelier sans nom":currentSaveFile.getName())+(modified?" - Modifié":""));
    }

    /** Method quitWithoutSaving
     * ensure that the user is okay with quiting the workshop without saving it
     * @return true if the user is willing to quit, false if not
     */
    private boolean quitWithoutSaving()
    {
        if(modified){
            Alert alert = new Alert(Alert.AlertType.WARNING, "En quittant maintenant, sans sauvegarder, vous perderez vos modifications.\nSouhaitez-vous tout de même quitter?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Attention !");
            alert.setHeaderText("Des modifications ont été effectuées.");
            return alert.showAndWait().get() == ButtonType.YES;
        }
        return true;
    }


    /** Method loadWorkshopFromFile
     * load all the attributs of the workshop from a file and
     * display a popup if there was a problem with the file
     * @param file2 the "name and directory" of the file
     * @return true if the file has been loaded, false if something went wrong
     */
    public boolean loadWorkshopFromFile(File file2)
    {
        // The alert popup diplsayed only in case of an error (NOT displayed)
        Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de charger le fichier selectionné.");
        alert.setTitle("Erreur !");

        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(file2);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            final InterfaceAtelier ia = (InterfaceAtelier)in.readObject();

            // close the reaidng file
            in.close();
            file.close();

            // Ask the user for the password
            if(!ia.passwordHandler.hasPassword() || ia.passwordHandler.askPasswordPopup())
            {
                // delete all the children of things printed on the screen
                this.group.getChildren().clear();
                this.group.getChildren().add(imageView);

                // set the new workshop
                this.set(ia);

                // set the file name
                this.currentSaveFile = file2;

                // update window title
                modified = false;
                updateWindowName();

                // everything's fine, we return true
                return true;
            }
            else
                // the workshop wasn't loaded
                return false;
        }
        // In case of an exception
        catch (FileNotFoundException e) { alert.setContentText("Le fichier n'existe pas.\n"+e.getMessage()); }
        catch (InputMismatchException e) { alert.setContentText("Le fichier est corrompu.\n"+e.getMessage()); }
        catch (Throwable e) { alert.setContentText("Erreur inconnue\n"+e.getMessage()); }

        // We show the error dialog
        alert.show();
        return false;
    }

    /** Method saveWorkshopToFile
     * save all the attributs of the workshop to a specified file
     * @param file2 where the file will be saved
     * @return true if the file was saved correctly, false if the file couldn't be saved
     */
    public boolean saveWorkshopToFile(File file2)
    {
        try {
            // save the current workshop
            FileOutputStream file = new FileOutputStream(file2);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(this);

            // close the file
            out.close();
            file.close();

            // update window title
            modified = false;
            updateWindowName();
        }

        // In case of an error
        catch (IOException e)
        {
            // Display a simple error dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur !");
            alert.setHeaderText("Impossible de sauvegarder l'atelier");
            alert.setContentText(e.getMessage());

            return false;
        }

        return true;
    }

    /** Method loadBackgroundImage
     *
     * @param path where to find the file (url or disk file) it can also be equal to "none"
     *             which means no image will be displayed on the background
     */
    public void loadBackgroundImage(String path)
    {
        if(path.equals("none"))
        {
            // We remove the last image, and put no image at all
            imageView.setImage( null );
            modified = true;
            updateWindowName();
        }
        else if(!path.isEmpty())
        {
            // If this is the default image, then the path is the path of the default image
            if(path.equals("default"))
                path = "file:/Users/jean/IdeaProjects/Workshop/Images/usine vide 3.jpg";

            // We try to load the new image
            try
            {
                Image image = new Image(path); // Load the image, but not in the background
                imageView.setImage(image); // apply the image to the background
                modified = true; // the workshop was modified, right?
                updateWindowName();
                pathBackgroundImage = path;
            }
            catch(Throwable e)
            {
                // Display a message box
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur lors du chargement");
                alert.setHeaderText("L'image n'a pas pu être chargée.");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }

    /** Method: prepareScene
     * create the new corresponding scene
     */
    private void prepareScene()
    {
        // New vertical box
        VBox vbox = new VBox();

        // Finally displaying the window
        vbox.getChildren().add(menuBar);
        vbox.getChildren().add(group);

        // Automatically resize the displayed image to the size of the window
        imageView.fitWidthProperty().bind(vbox.widthProperty());
        imageView.fitHeightProperty().bind(vbox.heightProperty());
        group.getChildren().add(imageView);

        scene = new Scene(vbox);
        stage.setScene(scene);
    }

    /** prepareMenuBar
     * create a menubar, and defines the actions of each menubar
     */
    private void prepareMenuBar()
    {
        final Menu menu1 = new Menu("Fichier");
        final Menu menu2 = new Menu("Machines");
        final Menu menu3 = new Menu("Postes");
        final Menu menu4 = new Menu("Opérateurs");
        final Menu menu5 = new Menu("Opérations");
        final Menu menu6 = new Menu("Gamme");
        final Menu menu7 = new Menu("Stockage");
        final Menu menu8 = new Menu("Sécurité");
        final Menu menu9 = new Menu("?");
        menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu1,menu2,menu3,menu4,menu5,menu6,menu7,menu8,menu9);
        menu1.setGraphic(iconLoader.getImage(IconLoader.Icons.DISK));
        menu2.setGraphic(iconLoader.getImage(IconLoader.Icons.MACHINE));
        menu3.setGraphic(iconLoader.getImage(IconLoader.Icons.POSTE));
        menu4.setGraphic(iconLoader.getImage(IconLoader.Icons.OPERATEUR));
        menu5.setGraphic(iconLoader.getImage(IconLoader.Icons.OPERATION));
        menu6.setGraphic(iconLoader.getImage(IconLoader.Icons.GAMME));
        menu7.setGraphic(iconLoader.getImage(IconLoader.Icons.STOCKAGE));
        menu8.setGraphic(iconLoader.getImage(IconLoader.Icons.SECURITY));

        /////
        // first menu: Gestion du menu "Fichier"
        MenuItem menuItem11 = new MenuItem("Nouveau");
        MenuItem menuItem12 = new MenuItem("Nouvelle fenetre");
        MenuItem menuItem13 = new MenuItem("Ouvrir...");
        MenuItem menuItem14 = new MenuItem("Ouvrir récent");
        MenuItem menuItem15 = new MenuItem("Sauvegarder");
        MenuItem menuItem16 = new MenuItem("Sauvegarder sous...");
        menuItem11.setOnAction(action -> {
            // create a new workshop by erasing the last one
            if(quitWithoutSaving()){
                resetWorkshop();
                prepareMenuBar();
                prepareScene();
            }

        });
        menuItem12.setOnAction(action -> {
            // start the same app but in parallell ! Awesome.
            Platform.runLater(()->{
                try { new InterfaceAtelier().start(new Stage()); }
                catch(Throwable e) { e.printStackTrace(); }
            });
        });
        menuItem13.setOnAction(actionEvent -> {
            if(quitWithoutSaving()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Ouvrir un atelier existant...");
                File file = fileChooser.showOpenDialog(stage);
                if(file != null) loadWorkshopFromFile(file);
            }
        });
        menuItem15.setOnAction(actionEvent -> {
            if(currentSaveFile == null) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");
                File file = fileChooser.showSaveDialog(stage);
                if(file != null && saveWorkshopToFile(file)){
                    currentSaveFile = file;
                    modified = false;
                    updateWindowName();
                }
            }
            else if(saveWorkshopToFile(currentSaveFile)){
                modified = false;
                updateWindowName();
            }
        });
        menuItem16.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            File file = fileChooser.showSaveDialog(stage);
            if(file != null && saveWorkshopToFile(file)){
                currentSaveFile = file;
                updateWindowName();
            }
        });
        menu1.getItems().addAll(menuItem11,menuItem12,menuItem13,menuItem14,menuItem15,menuItem16);
        menuItem11.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem12.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem13.setGraphic(iconLoader.getImage(IconLoader.Icons.OPEN));
        menuItem14.setGraphic(iconLoader.getImage(IconLoader.Icons.OPEN));
        menuItem15.setGraphic(iconLoader.getImage(IconLoader.Icons.SAVE));
        menuItem16.setGraphic(iconLoader.getImage(IconLoader.Icons.SAVE));


        /////
        // menu2 : Gestion du menu "Machines"
        MenuItem menuItem21 = new MenuItem("Nouvelle machine...");
        MenuItem menuItem22 = new MenuItem("Afficher machines");
        MenuItem menuItem23 = new MenuItem("Supprimer machine");
        menu2.getItems().addAll(menuItem21,menuItem22,menuItem23);
        menuItem21.setOnAction(actionEvent -> {
            selectedMachine = elementsForms.dialogToCreateNewMachine();
            if(selectedMachine != null) {
                machines.add(selectedMachine);
                group.getChildren().add(selectedMachine.getImageView());
                scene.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        if(selectedMachine != null){
                            selectedMachine.setX((int)mouseEvent.getX());
                            selectedMachine.setY((int)mouseEvent.getY());
                        }
                    }
                });
                scene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent mouseEvent) {
                        selectedMachine = null;
                    }
                });
                modified=true;
                updateWindowName();
            }
        });
        menuItem22.setOnAction(event -> {
            elementsForms.dialogPrintArray(machines.toArray(),"machine");
        });
        menuItem23.setOnAction(event -> {
            elementsForms.dialogDeleteMachine(machines, group.getChildren());
        });
        menuItem21.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem22.setGraphic(iconLoader.getImage(IconLoader.Icons.MODIFY));
        menuItem23.setGraphic(iconLoader.getImage(IconLoader.Icons.DELETE));


        /////
        // menu3 : Gestion du menu "Postes"
        MenuItem menuItem31 = new MenuItem("Nouveau poste...");
        MenuItem menuItem32 = new MenuItem("Afficher postes");
        MenuItem menuItem33 = new MenuItem("Supprimer poste");
        menu3.getItems().addAll(menuItem31,menuItem32,menuItem33);
        menuItem31.setOnAction(event -> {
            Poste newPoste = elementsForms.dialogNewPoste(machines);
            if(newPoste != null) postes.add(newPoste);
        });
        menuItem32.setOnAction(event -> {
            elementsForms.dialogPrintArray(postes.toArray(), "poste");
        });
        menuItem33.setOnAction(event -> {
            elementsForms.dialogDeletePoste(postes);
        });
        menuItem31.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem32.setGraphic(iconLoader.getImage(IconLoader.Icons.MODIFY));
        menuItem33.setGraphic(iconLoader.getImage(IconLoader.Icons.DELETE));

        /////
        // menu4 : Gestion du menu "Operateurs"
        MenuItem menuItem41 = new MenuItem("Nouvel operateur...");
        MenuItem menuItem42 = new MenuItem("Afficher operateurs");
        MenuItem menuItem43 = new MenuItem("Supprimer operateur");
        menu4.getItems().addAll(menuItem41, menuItem42, menuItem43);
        menuItem41.setOnAction(event -> {
            Operateur o = elementsForms.dialogNewOperateur();
            if(o != null) operateurs.add(o);
        });
        menuItem42.setOnAction(event -> {
            elementsForms.dialogPrintArray(operateurs.toArray(), "opérateur");
        });
        menuItem43.setOnAction(event -> {
            elementsForms.dialogDeleteOperateur(operateurs);
        });
        menuItem41.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem42.setGraphic(iconLoader.getImage(IconLoader.Icons.MODIFY));
        menuItem43.setGraphic(iconLoader.getImage(IconLoader.Icons.DELETE));


        /////
        // menu5 : Gestion du menu "Operations"
        MenuItem menuItem51 = new MenuItem("Nouvelle opération");
        MenuItem menuItem52 = new MenuItem("Afficher opérations");
        MenuItem menuItem53 = new MenuItem("Supprimer opération");
        menu5.getItems().addAll(menuItem51,menuItem52,menuItem53);
        menuItem51.setOnAction(event -> {
            Operation operation = elementsForms.dialogNewOperation(operations,machines,postes);
            if(operation != null) operations.add(operation);
        });
        menuItem52.setOnAction(event -> {
            elementsForms.dialogPrintArray(operations.toArray(), "opération");
        });
        menuItem53.setOnAction(event -> {
            elementsForms.dialogDeleteOperation(operations);
        });
        menuItem51.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem52.setGraphic(iconLoader.getImage(IconLoader.Icons.MODIFY));
        menuItem53.setGraphic(iconLoader.getImage(IconLoader.Icons.DELETE));

        /////
        // menu6 : Gestion du menu "Gamme"
        MenuItem menuItem61 = new MenuItem("Nouvelle gamme");
        MenuItem menuItem62 = new MenuItem("Afficher gamme");
        MenuItem menuItem63 = new MenuItem("Supprimer gamme");
        MenuItem menuItem64 = new MenuItem("Activer gamme");
        MenuItem menuItem65 = new MenuItem("Désactiver gamme");
        menu6.getItems().addAll(menuItem61, menuItem62, menuItem63, menuItem64, menuItem65);
        menuItem61.setOnAction(event -> {
            Gamme gamme = elementsForms.dialogNewGamme(gammes, machines, postes, operations);
            if(gamme != null) gammes.add(gamme);
        });
        menuItem62.setOnAction(event -> {
            elementsForms.dialogPrintArray(gammes.toArray(), "gamme");
        });
        menuItem63.setOnAction(event -> {
            elementsForms.dialogDeleteGamme(gammes);
        });
        menuItem64.setOnAction(event -> {
            elementsForms.dialogDeActivateGamme(gammes, true, machines);
        });
        menuItem65.setOnAction(event -> {
            elementsForms.dialogDeActivateGamme(gammes, false, machines);
        });
        menuItem61.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem62.setGraphic(iconLoader.getImage(IconLoader.Icons.MODIFY));
        menuItem63.setGraphic(iconLoader.getImage(IconLoader.Icons.DELETE));
        menuItem64.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem65.setGraphic(iconLoader.getImage(IconLoader.Icons.DELETE));

        /////
        // menu7 : Gestion du menu "Stockage"
        MenuItem menuItem71 = new MenuItem("Definir zone stockage");
        MenuItem menuItem72 = new MenuItem("Afficher stockage");
        MenuItem menuItem73 = new MenuItem("Nouveau produit");
        MenuItem menuItem74 = new MenuItem("Afficher produits");
        MenuItem menuItem75 = new MenuItem("Supprimer produits");
        MenuItem menuItem76 = new MenuItem("Changer image de fond");
        menu7.getItems().addAll(/*menuItem71,*/menuItem72,menuItem73,menuItem74,menuItem75,menuItem76);
        menuItem72.setOnAction(event -> {
            elementsForms.dialogPrintArray(produits.toArray(), "produit");
        });
        menuItem73.setOnAction(event -> {
            Produit pr = elementsForms.dialogNewProduit(produits);
            if(pr != null) produits.add(pr);
        });
        menuItem74.setOnAction(event -> {
            elementsForms.dialogPrintArray(produits.toArray(), "produit");
        });
        menuItem75.setOnAction(event -> {
           elementsForms.dialogDeleteProduit(produits);
        });
        menuItem76.setOnAction(event -> {
            loadBackgroundImage( elementsForms.dialogToAskBackgroundImage() );
        });
        menuItem73.setGraphic(iconLoader.getImage(IconLoader.Icons.PLUS));
        menuItem74.setGraphic(iconLoader.getImage(IconLoader.Icons.PRODUCT));
        menuItem75.setGraphic(iconLoader.getImage(IconLoader.Icons.DELETE));
        menuItem72.setGraphic(iconLoader.getImage(IconLoader.Icons.STOCKAGE));
        menuItem76.setGraphic(iconLoader.getImage(IconLoader.Icons.IMAGE));

        /////
        // menu8 : Gestion du menu "Sécurité"
        MenuItem menuItem81 = new MenuItem("Ajouter un mot de passe..");
        menuItem81.setOnAction(event -> {
            // If the password of the file is actually
            if(passwordHandler.askNewPasswordPopup()){
                // Then the workshop has actually been modified
                modified = true; updateWindowName();
            }
        });
        menuItem81.setGraphic(iconLoader.getImage(IconLoader.Icons.ADDPASSWORD));
        menu8.getItems().add(menuItem81);

        /////
        // menu9 : Gestion du menu "?"
        MenuItem menuItem91 = new MenuItem("Aide");
        MenuItem menuItem92 = new MenuItem("Langue...");
        MenuItem menuItem93 = new MenuItem("À propos");
        menu9.getItems().addAll(menuItem91, menuItem92, menuItem93);
        menuItem91.setOnAction(event -> {
            // Display the 'Help' section
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Aide");
            alert.setHeaderText("Bienvenue dans l'aide.");
            alert.setContentText("Ce programme est un simulateur simplifié d'usine.\n\n" +
                    "En cliquant sur les menus, vous pouvez ajouter des machines, opérateurs, produits, gammes, et les visualiser.\n\n"
                    +"Vous pouvez aussi sauvegarder et charger l'usine en cliquant dans le menu Fichier.");
            alert.show();
        });
        menuItem91.setGraphic(iconLoader.getImage(IconLoader.Icons.HELP));
        menuItem92.setGraphic(iconLoader.getImage(IconLoader.Icons.LANGUAGE));
        menuItem93.setOnAction(event -> {
            // Display the 'About' section
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("A propos");
            alert.setHeaderText("Crédits");
            alert.setContentText("Bibliothèque graphique utilisée:\n JavaFX\n\nAuteur:\nInsa Strasbourg\nSTH1A - Groupe 5\nProgrammeur: Jean DIVOUX\nDesigner: Matteo DJODY + Julien DRAGO + Jean DIVOUX");
            alert.show();
        });
        menuItem93.setGraphic(iconLoader.getImage(IconLoader.Icons.ABOUT));
    }


    /** Main function
     * Method from where the program starts
     * @param args system parameters
     */
    public static void main(String[] args)
    {
        // call the start method
        launch(args);
    }
}
