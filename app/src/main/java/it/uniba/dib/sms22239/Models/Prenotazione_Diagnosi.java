package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

import java.util.Random;

public class Prenotazione_Diagnosi extends Prenotazione {

    @PropertyName("Diagnosi")
    public String diagnosi;

    public Prenotazione_Diagnosi() {
    }

    public void writeNewPrenotazione(Prenotazione_Diagnosi p_d, String idAnimale, String id_appuntamento, String data)
    {
        p_d.id_animale = idAnimale;
        p_d.id_appuntamento = id_appuntamento;
        p_d.data = data;
        p_d.diagnosi = null;
        p_d.tipo = "Diagnosi";

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Prenotazioni").child(id_prenotazione).setValue(p_d);

    }


}