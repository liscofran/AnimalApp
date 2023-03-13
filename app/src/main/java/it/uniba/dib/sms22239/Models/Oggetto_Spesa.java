package it.uniba.dib.sms22239.Models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

import java.util.Random;

public class Oggetto_Spesa {

    @PropertyName("id")
    public String id;

    @PropertyName("nome")
    public String nome;

    @PropertyName("id_animale")
    public String id_animale;

    @PropertyName("prezzo")
    public double prezzo;

    @PropertyName("quantita")
    public int quantita;

    @PropertyName("dataAcquisto")
    public String dataAcquisto;

    public Oggetto_Spesa() {

    }

    public void writeNewOggetto(Oggetto_Spesa os, String nome, double prezzo, int quantita, String dataAcquisto, String id_animale)
    {
        os.nome = nome;
        os.prezzo = prezzo;
        os.quantita = quantita;
        os.dataAcquisto = dataAcquisto;
        os.id_animale = id_animale;

        Random rand = new Random();

        // Generare un numero casuale
        int randomNumber = rand.nextInt();

        os.id = String.valueOf(randomNumber);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Oggetti").child(id).setValue(os);

    }
}
