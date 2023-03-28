package it.uniba.dib.sms22239.Models;

public class Prenotazione_Diagnosi extends Prenotazione {
    public enum Riscontro {
        POSITIVA, NEGATIVA, DA_RIVEDERE
    }

     Riscontro riscontro;
     String id_animale, id_diagnosi;

    public Prenotazione_Diagnosi(int id_utente, Appuntamento appuntamento, int id_prenotazione) {
        super(id_utente,appuntamento, id_prenotazione);
        this.riscontro = Riscontro.DA_RIVEDERE;
    }


}