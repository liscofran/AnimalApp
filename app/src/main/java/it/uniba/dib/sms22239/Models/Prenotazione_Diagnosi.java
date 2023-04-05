package it.uniba.dib.sms22239.Models;

public class Prenotazione_Diagnosi extends Prenotazione {

    Diagnosi diagnosi;
    String data;


    public Prenotazione_Diagnosi(Animale animale, Appuntamento appuntamento, int id_prenotazione) {
        super(animale, appuntamento, id_prenotazione);
    }
}