package fr.insa.TeamA.workshop;

import java.io.Serializable;

/*
@author Jean
 */
public class Operateur implements Serializable
{
    private String reference;
    private String name;

    Operateur(String newReference, String newName)
    {
        create(newReference, newName);
    }

    public void create(String newReference, String newName)
    {
        reference = newReference;
        name = newName;
    }

    public String getReference()
    {
        return reference;
    }

    @Override public String toString()
    {
        return "reference:"+reference+"   name:"+name;
    }
}
