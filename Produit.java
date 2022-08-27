package fr.insa.TeamA.workshop;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

/*
@author Jean, Julien, Matteo
 */
public class Produit implements Serializable
{
    private String designation;
    private String reference;
    private float quantity;

    public Produit(String newDesignation, String newReference, float newQuantity)
    {
        create(newDesignation, newReference, newQuantity);
    }

    public Produit(Scanner file)
    {
        loadFromFile(file);
    }

    public void create(String newDesignation, String newReference, float newQuantity)
    {
        designation = newDesignation;
        reference = newReference;
        quantity = newQuantity;
    }

    public String getReference()
    {
        return reference;
    }

    @Override public String toString() {
        return "Designation:"+designation+"  Reference:"+reference+"   Quantity:"+quantity;
    }


    public void loadFromFile(Scanner file)
    {
        designation = file.nextLine();
        reference = file.nextLine();
        quantity = Float.parseFloat(file.nextLine());
    }

    public void saveToFile(FileWriter file) throws IOException
    {
        file.write(designation + '\n');
        file.write(reference+'\n');
        file.write( String.valueOf(quantity) + '\n');
    }
}
