package it.uniba.dib.sms22239.Models;

public class Prenotazione_Esame extends Prenotazione {
    public enum Riscontro {
        COMPLETATO, NON_ESEGUITO, DA_RIVEDERE
    }

     Riscontro riscontro;
     String id_animale, id_esame;



    public Prenotazione_Esame(int id_utente, Appuntamento appuntamento, int id_prenotazione) {
        super(id_utente,appuntamento, id_prenotazione );
        this.riscontro = Riscontro.NON_ESEGUITO;
    }

}