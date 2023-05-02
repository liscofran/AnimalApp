package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

import java.util.Random;

public class Prenotazione_Esame extends Prenotazione {

    @PropertyName("Esame")
    Esame esame;

    @PropertyName("Data")
    String data;

    public Prenotazione_Esame(String id_animale, String id_appuntamento, String id_prenotazione) {
        super(id_animale, id_appuntamento, id_prenotazione);
    }

    public void writeNewPrenotazioneEsame(Prenotazione_Esame p_e, String idAnimale, String id_appuntamento, String data)
    {
        p_e.id_animale = idAnimale;
        p_e.id_appuntamento = id_appuntamento;
        p_e.data = data;
        p_e.esame = null;

        Random rand = new Random();

        // Generare un numero casuale
        int randomNumber = rand.nextInt();

        p_e.id_prenotazione = String.valueOf(randomNumber);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Prenotazioni_Esami").child(id_prenotazione).setValue(p_e);

    }
}