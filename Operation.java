package fr.insa.TeamA.workshop;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/*
@author Jean, Julien, Matheo
 */
public class Operation implements Serializable
{
	// Attributs
	private String refOperation;
	private String dOperation;
	private String refEquipement;
	private int dureeOperation; // en millisecondes
	
	
	public Operation(Scanner reader)
	{
		loadFromFile(reader);
	}

	public Operation(String newRefOperation, String newDesignationOperation, String newRefEquipement, int newDureeOperation)
	{
		// Initialisation des attributs
		create(newRefOperation, newDesignationOperation, newRefEquipement, newDureeOperation);
	}

	public void create(String newRefOperation, String newDesignationOperation, String newRefEquipement, int newDureeOperation)
	{
		refOperation = newRefOperation;
		dOperation = newDesignationOperation;
		refEquipement = newRefEquipement;
		dureeOperation = newDureeOperation;
	}

	public float getDuree()
	{
		return dureeOperation;
	}

	public String getReferenceOperation()
	{
		return refOperation;
	}

	public String getReference() { return refOperation; }

	@Override public String toString() {
		return "Ref-operation:"+refOperation+"   Designation:"+dOperation
				+"  Ref-equipement:"+ refEquipement+"   Durée:"+dureeOperation;
	}

	public void loadFromFile(Scanner file)
	{
		// read all the attributes
		refOperation = file.nextLine();
		dOperation = file.nextLine();
		refEquipement = file.nextLine();
		dureeOperation = Integer.parseInt(file.nextLine());
	}

	public void saveToFile(FileWriter file) throws IOException
	{
		// save all the attributes
		file.write( refOperation + '\n' );
		file.write( dOperation + '\n' );
		file.write( refEquipement + '\n' );
		file.write( String.valueOf(dureeOperation) + '\n' );
	}
}
