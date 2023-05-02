package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.PropertyName;

public abstract class Prenotazione {

    @PropertyName("id_animale")
     public String id_animale;

    @PropertyName("id_appuntamento")
     public String id_appuntamento;

    @PropertyName("id_prenotazione")
     public String id_prenotazione;

    public Prenotazione(String id_animale, String id_appuntamento, String id_prenotazione) {
        this.id_animale = id_animale;
        this.id_appuntamento = id_appuntamento;
        this.id_prenotazione = id_prenotazione;
    }


}

