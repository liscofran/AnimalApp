package it.uniba.dib.sms22239;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class Animale
{
    public String Id;
    public String nome;
    public String razza;
    public String sesso;
    public String data;
    public String Id_utente;
    public String immagine;

    public Animale() {

    }

    public void writeNewAnimal(Animale ani, String nome, String razza, String Id_utente, String sesso, String data)
    {
        ani.nome = nome;
        ani.razza = razza;
        ani.sesso = sesso;
        ani.Id_utente = Id_utente;
        ani.data = data;

        long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        int tmp = Math.abs(random.nextInt()); // generare un numero casuale

        ani.Id = Integer.toString(tmp);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Animale").child(Integer.toString(tmp)).setValue(ani);
    }
}
