package it.uniba.dib.sms22239.Models;

public class Prenotazione_Esame extends Prenotazione {


    Esame esame;
    String data;

    public Prenotazione_Esame(Animale animale, Appuntamento appuntamento, int id_prenotazione) {
        super(animale, appuntamento, id_prenotazione);
    }
}