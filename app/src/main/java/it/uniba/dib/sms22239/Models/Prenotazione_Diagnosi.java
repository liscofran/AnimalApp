package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Prenotazione_Diagnosi extends Prenotazione {

    Diagnosi diagnosi;
    String data;


    public Prenotazione_Diagnosi(String id_animale, String id_appuntamento, String id_prenotazione) {
        super(id_animale, id_appuntamento, id_prenotazione);
    }

    public void writeNewPrenotazioneDiagnosi(Prenotazione_Diagnosi p_d, String idAnimale, String id_appuntamento, String data)
    {
        p_d.id_animale = idAnimale;
        p_d.id_appuntamento = id_appuntamento;
        p_d.data = data;
        p_d.diagnosi = null;

        Random rand = new Random();

        // Generare un numero casuale
        int randomNumber = rand.nextInt();

        p_d.id_prenotazione = String.valueOf(randomNumber);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Prenotazioni_Diagnosi").child(id_prenotazione).setValue(p_d);

    }


}