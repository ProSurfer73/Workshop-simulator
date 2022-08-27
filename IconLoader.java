package fr.insa.TeamA.workshop;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.HashMap;

/*
Class IconLoader
Handles the loading of the icons
@author Jean
*/
public class IconLoader
{
    // Enumeration of the differents types of icons that cna be claimed
    public enum Icons {HELP, PRODUCT, KEY, ABOUT, ADDPASSWORD, SECURITY, LANGUAGE, STOCKAGE, DISK,
    VALIDATE, CANCEL, OPEN, GAMME, MACHINE, OPERATEUR, OPERATION, POSTE, SAVE, SAVEAS, REFERENCE,
    PLUS, MODIFY, DELETE, IMAGE};

    // Map the images
    private HashMap<Icons, Image> map;

    /**
     * Constructor
     */
    public IconLoader()
    {
        map = new HashMap<Icons,Image>();
    }

    /** Method getImage()
     * Get the image from the database and load it if needed
     * @param type the icon the user want to get
     * @return the image view corresponding to the laoded image
     */
    public ImageView getImage(final Icons type)
    {
        Image image;

        if(map.containsKey(type)) {
            image = map.get(type);
        }
        else {
            image = loadImage(type);
            map.put(type, image);
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    /** Method loadImage
     * Load the image and the image view from the disk
     * @param type the actual icon that needs to be loaded
     * @return the imageView resized and attached to the image
     */
    private static Image loadImage(final Icons type)
    {
        // directory of the images
        final String initialPath = "file:/Users/jean/IdeaProjects/Workshop/Images/";

        // The filename to be defined
        String filename="";

        // According to the type of icon given in argument of the method
        switch(type)
        {
            case HELP:
                filename = "a propos 2.jpg";
                break;
            case PRODUCT:
                filename = "produit.png";
                break;
            case KEY:
                filename = "mot de passe 3.png";
                break;
            case ADDPASSWORD:
                filename = "mot de passe 2.png";
                break;
            case SECURITY:
                filename = "mot de passe 1.png";
                break;
            case LANGUAGE:
                filename = "langue fr.png";
                break;
            case STOCKAGE:
                filename = "Stockage boite.png";
                break;
            case DISK:
                filename = "Stackage disque dur 2.png";
                break;
            case VALIDATE:
                filename = "Valider.png";
                break;
            case CANCEL:
                filename = "Annuler.png";
                break;
            case OPEN:
                filename = "dossier ouvert.png";
                break;
            case ABOUT:
                filename = "a propos.png";
                break;
            case GAMME:
                filename = "Gamme.png";
                break;
            case MACHINE:
                filename = "machine icone.png";
                break;
            case OPERATEUR:
                filename = "operateur test2.png";
                break;
            case OPERATION:
                filename = "operation.png";
                break;
            case SAVE:
            case SAVEAS:
                filename = "dissquette.png";
                break;
            case PLUS:
                filename = "ajouter4.png";
                break;
            case MODIFY:
                filename = "modifier.png";
                break;
            case DELETE:
                filename = "minus.png";
                break;
            case IMAGE:
                filename = "img.png";
                break;
            case POSTE:
                filename = "Afficher ecran 2.png";
                break;
        }

        // Loads the image from the file in the background
        return new Image(initialPath+filename);
    }
}
