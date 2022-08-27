package fr.insa.TeamA.workshop;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;


/** Class Gamme
 * Contain a list of operation
 * @author Jean, Matteo, Julien
 */
public class Gamme implements Serializable
{
	// Attribut reference
	private String refGamme;
	
	// tableaux redimensionnables
	private ArrayList<Operation> listeOperations;

	// Tells if the gamme is activated
	private boolean activated;

	/** Attributes Constructor
	 * @param newReference reference
	 * @param newOperations
	 */
	public Gamme(String newReference, ArrayList<Operation> newOperations)
	{
		// Initialise les attributs
		creerGamme(newReference, newOperations);
	}

	public Gamme(Scanner file, ArrayList<Poste> postes, ArrayList<Machine> machines) throws Throwable
	{
		loadFromFile(file, postes,  machines);
	}
	
	// creer une gamme
	public void creerGamme(String newReference, ArrayList<Operation> newOperations)
	{
		refGamme = newReference;
		listeOperations = newOperations;
		activated = false;
	}
	
	// remplacer un equipement du tableau
	public void modifierGamme(int index, Equipement equipement, Operation newOperations)
	{
		listeOperations.set(index, newOperations);
	}

	// supprimer la gamme
	public void supprimerGamme()
	{
		// Vide le contenu des deux tableaux
		listeOperations.clear();
	}
	
	
	// autre nom de la methode afficheGamme(), demandee dans le sujet
	public void afficher()
	{
		System.out.println("=> Gamme\n");
		
		for(Operation o : listeOperations) {
			System.out.println("- duree: "+o.getDuree());
		}
	}
	
	// autre nom de la methode coutGamme(), demandee dans le sujet
	public float getCoutGamme(ArrayList<Machine> listeMachines)
	{
		float coutTotal = 0;
		
		for(int i=0; i<listeMachines.size(); ++i)
		{
			coutTotal += listeMachines.get(i).getCoutHoraire() * listeOperations.get(i).getDuree();
		}
		
		return coutTotal;
	}
	
	
	// autre nom pour la methode dureeGamme(), demandee dans le sujet
	public float getDureeGamme()
	{
		float dureeTotale = 0;
		
		for(Operation o : listeOperations)
		{
			dureeTotale += o.getDuree();
		}
		
		return dureeTotale;
	}
	
	public String toString()
	{
		// renvoie la reference
		return refGamme;
	}

	public void loadFromFile(Scanner file, ArrayList<Poste> postes, ArrayList<Machine> machines) throws Throwable
	{
		// load the reference
		refGamme = file.nextLine();

		// Get the number of operations composing the Gamme
		final int numberElements = file.nextInt();

		// Load all the operations one by one
		for(int i=0; i<numberElements; ++i)
		{
			// Create and load a new operation from the file
			Operation o = new Operation(file);

			// Add it to the gamme
			listeOperations.add(o);
		}
	}

	public String getReference()
	{
		return refGamme;
	}

	public void saveToFile(FileWriter file) throws IOException
	{
		// save all the attributes
		file.write(refGamme + '\n');

		// save the number of operations contained in the gamme
		file.write( String.valueOf(listeOperations.size()) + '\n');

		// save all the operations one by one
		// ! Not really optimized but who cares?
		for(Operation o : listeOperations)
			o.saveToFile(file);
	}

	public boolean isActivated()
	{
		return activated;
	}

	// travail a poursuivre
	public boolean activate(ArrayList<Machine> machines)
	{
		boolean somethingWrong = false;

		// First we search all the machines concerned
		for(Operation o : listeOperations)
		{

		}


		return activated;
	}

	public boolean deactivate(ArrayList<Machine> machines)
	{
		return true;
	}
}
