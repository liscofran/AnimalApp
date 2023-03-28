package it.uniba.dib.sms22239.Models;

public abstract class Prenotazione {
    private int id_utente;
    private Appuntamento appuntamento;
    private int id_prenotazione;

    public Prenotazione(int id_utente, Appuntamento appuntamento, int id_prenotazione) {
        this.id_utente = id_utente;
        this.appuntamento = appuntamento;
        this.id_prenotazione = id_prenotazione;
    }


}

