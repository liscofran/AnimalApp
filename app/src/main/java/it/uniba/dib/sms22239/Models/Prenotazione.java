package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.PropertyName;

import java.util.Random;

public abstract class Prenotazione {

    @PropertyName("id_animale")
     public String id_animale;

    @PropertyName("id_appuntamento")
     public String id_appuntamento;

    @PropertyName("Tipo")
    public String tipo;

    @PropertyName("Data")
    public String data;

    @PropertyName("id_prenotazione")
     public String id_prenotazione;


    public Prenotazione() {

        Random rand = new Random();

        // Generare un numero casuale
        int randomNumber = rand.nextInt();

        id_prenotazione = String.valueOf(randomNumber);

    }
}

