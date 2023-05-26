package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

public class Prenotazione_Esame extends Prenotazione {

    @PropertyName("TipoEsame")
    public String tipoEsame;

    @PropertyName("statoEsame")
    public String statoEsame;

    public Prenotazione_Esame() {

    }

    public void writeNewPrenotazione(Prenotazione_Esame p_e, String idAnimale, String id_appuntamento, String data, String orario_inizio, String orario_fine, String id_veterinario)
    {
        p_e.id_animale = idAnimale;
        p_e.id_appuntamento = id_appuntamento;
        p_e.data = data;
        p_e.orario_inizio = orario_inizio;
        p_e.orario_fine = orario_fine;
        p_e.id_veterinario = id_veterinario;
        p_e.tipoEsame = "Altro";
        p_e.statoEsame = "Non eseguito";
        p_e.tipo = "Esame";


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Prenotazioni").child(id_prenotazione).setValue(p_e);

    }
}