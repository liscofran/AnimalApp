package it.uniba.dib.sms22239.Models;

public class Esame {
    public enum Riscontro {
        COMPLETATO, NON_ESEGUITO, DA_RIVEDERE
    }

    public enum TipoEsame{
        SVERMINAZIONE,VACCINAZIONE,STERILIZZAZIONE,INTERVENTO,CONTROLLO
    }

    String id_esame ;
    Riscontro riscontro;


    public Esame(Riscontro riscontro) {
        this.riscontro = riscontro.NON_ESEGUITO;
    }
}
