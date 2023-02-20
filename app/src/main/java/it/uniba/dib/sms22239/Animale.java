package it.uniba.dib.sms22239;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Random;

public class Animale
{
    public String Id;
    public String nome;
    public String razza;
    public String sesso;
    //public SimpleDateFormat data;
    public String Id_utente;
    public DatabaseReference mDatabase;

    public Animale() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void writeNewAnimal(Animale ani, String nome, String razza, String sesso, String data, String Id_utente) {
        ani.nome = nome;
        ani.razza = razza;
        //ani.sesso = sesso;
        ani.Id_utente = Id_utente;
        //ani.data = new SimpleDateFormat(data);

        long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        int tmp = Math.abs(random.nextInt()); // generare un numero casuale
        //ani.Id = Integer.toString(tmp);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Animale").child("ID").setValue(ani);
    }
}
