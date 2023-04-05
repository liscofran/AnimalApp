package it.uniba.dib.sms22239.Models;

public abstract class Prenotazione {
     protected Animale animale;
     protected Appuntamento appuntamento;
     int id_prenotazione;

    public Prenotazione(Animale animale, Appuntamento appuntamento, int id_prenotazione) {
        this.animale = animale;
        this.appuntamento = appuntamento;
        this.id_prenotazione = id_prenotazione;
    }


}

