package fr.insa.TeamA.workshop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;


/*
 * @author Jean, Julien, Matteo
 */
public class Poste extends Equipement implements Serializable
{
	// Attributs

	// references des machines du poste
	private ArrayList<String> refsMachine;

	public Poste(Scanner file)
	{
		super(file);
		loadFromFile(file);
	}
	
	// Constructor
	public Poste(String newReference, String newDesignation, String newType, ArrayList<Machine> machines)
	{
		// Call the constructor of the superclass
		super(newReference, newDesignation, 0);

		int newCoutHoraire = 0;

		for(Machine m : machines)
		{
			newCoutHoraire += m.getCoutHoraire();
		}

		setHourlyCost(newCoutHoraire);
	}

	// print the attributs of the object in the console
	public void print()
	{
		System.out.println("=> Poste\nReference: "+getReference());
	}

	@Override public void saveToFile(FileWriter file) throws IOException
	{
		// say it's a poste
		file.write("poste\n");

		// save the number of refs machines contained
		file.write(refsMachine.size() + '\n');

		// save all the references machines one by one
		for(int i=0; i<refsMachine.size(); ++i) {
			file.write( refsMachine.get(i) + '\n');
		}
	}

	@Override public void loadFromFile(Scanner file)
	{
		// ensure that the array of string is created and empty
		refsMachine = new ArrayList<String>();

		// get the number of reference machine present in the poste
		int numberMachines = file.nextInt();

		// load them one by one
		for(int i=0; i<numberMachines; ++i) {
			refsMachine.add( file.nextLine() );
		}
	}
}
