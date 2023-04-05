package it.uniba.dib.sms22239.Models;

public class Diagnosi {
    public enum Esito {
        POSITIVA, NEGATIVA, DA_RIVEDERE
    }

    String id_esame ;
    Esito esito;


    public Diagnosi(Esito esito) {
        this.esito = esito.DA_RIVEDERE;
    }
}
