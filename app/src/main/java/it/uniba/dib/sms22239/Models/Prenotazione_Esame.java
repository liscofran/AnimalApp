package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

import java.util.Random;

public class Prenotazione_Esame extends Prenotazione {

    @PropertyName("TipoEsame")
    public String tipoEsame;

    @PropertyName("Esame")
    public String Esame;

    public Prenotazione_Esame() {
        super();
    }

    public void writeNewPrenotazione(Prenotazione_Esame p_e, String idAnimale, String id_appuntamento, String data)
    {
        p_e.id_animale = idAnimale;
        p_e.id_appuntamento = id_appuntamento;
        p_e.data = data;
        p_e.tipoEsame = null;
        p_e.Esame = null;
        p_e.tipo = "Esame";


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Prenotazioni").child(id_prenotazione).setValue(p_e);

    }
}