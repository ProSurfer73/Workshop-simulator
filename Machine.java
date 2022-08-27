package fr.insa.TeamA.workshop;

import java.io.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Scanner;
import static java.lang.Integer.max;



/** Class Machine
 * @author Jean, Julien, Matheo
 */
public class Machine extends Equipement implements Serializable
{
    // Position of the machine on the screen
	private int x;
	private int y;

	// Size of the two images
	private int heightImage;
	private int widthImage;

	// Current operation used by the machine
    private Operation operation;

	// The image printed on the screen to represent the machine
	private transient ImageView imageView;
	private String pathImage;

    /** File Constructor
     * load the attributes from the file
     * @param file the file
     */
	public Machine(Scanner file)
    {
        super(file);
        imageView = new ImageView();
        pathImage = new String();
        loadFromFile(file);
    }

    /** Attributes Constructor
     * @param reference reference
     * @param designation desgination
     * @param theX horizental coordinate on the workshop
     * @param theY vertical coordinate on the workshop
     * @param lecoutMachine hourly cost
     */
	public Machine(String reference,String designation, int theX, int theY, int width, int height, float lecoutMachine)
	{
	    // Appel du constructeur de la classe Equipement
        super(reference, designation, lecoutMachine);

        // Position
        x = theX;
        y = theY;

        // Dimension of the diplayed image
        heightImage = height;
        widthImage = width;

        // The machine is currently unactivated
        operation = null;

        // Create the objects related to the attributes
        imageView = new ImageView();
        pathImage = "";
	}

	    // Accesseurs //

    /** Getter X
     *
     * @return the horizental coordinate
     */
    public float getX(){
	    return x;
	}

    public float getY(){
	    return y;
	}
        
    public boolean getdispo(){
	    return (operation!=null);
	}

        
        // Mutateurs //

    public void setX(int nouveauX)
    {
        x = nouveauX;
        imageView.setX( max(0, nouveauX-(int)imageView.getFitWidth()/2) );
    }
        
    public void setY(int nouveauy)
    {
        y = nouveauy;
        imageView.setY( max(0,y-(int)imageView.getFitHeight()/2) );
    }

    /** Setter setOperation
     * Start/Stop the machine
     * @param newOp the new operation of the machine
     */
    public void setOperation(Operation newOp)
    {
        operation = newOp;
    }


    public void setImageView(Image image, int coordX, int coordY, int fitWidth, int fitHeight, String path)
    {
        imageView.setImage(image);
        imageView.setFitHeight(fitHeight);
        imageView.setFitWidth(fitWidth);
        this.setX(coordX);
        this.setY(coordY);
        pathImage = path;
    }

    public ImageView getImageView()
    {
        return imageView;
    }

    /** Method load from file
     * load the attributes of the object from a file
     * @param file the currently read file
     */
    @Override public void loadFromFile(Scanner file)
    {
        loadAttributesFromFile(file);

        x = Integer.parseInt(file.nextLine());
        y = Integer.parseInt(file.nextLine());
        //available = file.nextLine().equals("true");

        // skip the endline character
        String path = file.nextLine();
        if( !path.isEmpty() ) {
            setImageView(new Image(path,false), x, y,
                    Integer.parseInt(file.nextLine()), Integer.parseInt(file.nextLine()), path);
        }
    }

    /** Method saveToFile
     * save the attributes of the object to a designated file
     * @param file the file you want to write on
     */
    @Override public void saveToFile(FileWriter file) throws IOException
    {
        file.write("machine\n");
        saveAttributesToFile(file);
        file.write( String.valueOf(x) + '\n');
        file.write( String.valueOf(y) + '\n');
        //file.write( String.valueOf(available) + '\n');
        file.write( pathImage + '\n' );
        if(!pathImage.isEmpty()){
            file.write( String.valueOf((int)imageView.getFitWidth()) + '\n');
            file.write( String.valueOf((int)imageView.getFitWidth()) + '\n');
        }
    }

    public String getStrCharacteristics()
    {
        return "Designation: "+getDesignation()+"     reference: "+getReference()+"      x: "+x+"  y: "+y;
    }

    @Override public String toString()
    {
        return getStrCharacteristics();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException
    {
        // Calling the default deserialization logic
        ois.defaultReadObject();

        // Load the image of the image view
        if(!pathImage.isEmpty())
        {
            imageView = new ImageView();
            imageView.setImage(new Image(pathImage));
            imageView.setX(x);
            imageView.setY(y);
            imageView.setFitWidth(widthImage);
            imageView.setFitHeight(heightImage);
        }
    }



}
