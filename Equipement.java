package fr.insa.TeamA.workshop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/** Class Equipment
 * is considered an equipment
 * @author Jean, Julien, Matteo
*/
public abstract class Equipement implements Serializable
{
	// - Attributs -
	private String refEquipement;
	private String dEquipement;
	private float coutHoraire;

	/** Protected Constructor
	 * Load the equipment from the file
	 * @param file the currently read file
	 */
	protected Equipement(Scanner file)
	{
		loadAttributesFromFile(file);
	}

	/** Public Constructor
	 * Creates a new equipment from the parameters given
	 * @param newReference the reference of the equipement
	 * @param newDesignation the designation of the equipment
	 * @param newCoutHoraire the hourly cost of this equipment
	 */
	public Equipement(String newReference, String newDesignation, float newCoutHoraire)
	{
		refEquipement = newReference;
		dEquipement = newDesignation;
		coutHoraire = 0;
		setHourlyCost(newCoutHoraire);
	}


	/** Getter getDesignation
	 * @return the designation of the Equipment
	 */
	public String getDesignation()
	{
		return dEquipement;
	}
	
	//
	public String getReference()
	{
		return refEquipement;
	}
	
	//
	public float getCoutHoraire()
	{
		return coutHoraire;
	}
	

	public String toString()
	{
		return refEquipement;
	}

	/** Setter setHourlyCost
	 * set and ensure that the hourly cost is positive
	 * @param nouveauCoutHoraire the new value
	 */
	protected void setHourlyCost(float nouveauCoutHoraire)
	{
		if(nouveauCoutHoraire >= 0){
			coutHoraire = nouveauCoutHoraire;
		}
	}

	/**	Function loadFromFile
	 * load the attributes of the equipement according to the file
	 * @param file the currently read file
	 */
	protected void loadAttributesFromFile(Scanner file)
	{
		refEquipement = file.nextLine();
		dEquipement = file.nextLine();
		coutHoraire = Float.parseFloat(file.nextLine());
	}

	/** Function saveAttributsToFile
	 * save the attributs of the equipment in the given file
	 * @param file the file currently written on
	 * @throws IOException if an error occurs during the writing process
	 */
	protected void saveAttributesToFile(FileWriter file) throws IOException
	{
		file.write(refEquipement+'\n');
		file.write(dEquipement+'\n');
		file.write(String.valueOf(coutHoraire)+'\n');
	}

	public abstract void loadFromFile(Scanner file);
	public abstract void saveToFile(FileWriter file) throws IOException;
}
