package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class Animale
{
    public String Id;
    public String nome;
    public String razza;
    public String sesso;
    public String patologie;
    public String preferenzecibo;
    public String data;
    public String Id_utente;
    public String immagine;
    public String prop;
    public String descprop;

    public Animale()
    {

    }

    public void writeNewAnimal(int id, Animale ani, String nome, String razza,String patologie,String preferenzecibo, String Id_utente, String sesso, String data,String prop)
    {
        ani.nome = nome;
        ani.razza = razza;
        ani.sesso = sesso;
        ani.patologie = patologie;
        ani.preferenzecibo = preferenzecibo;
        ani.Id_utente = Id_utente;
        ani.data = data;
        ani.prop = prop;
        ani.descprop = "Da inserire...";

        ani.Id = Integer.toString(id);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Animale").child(Integer.toString(id)).setValue(ani);
    }
}
