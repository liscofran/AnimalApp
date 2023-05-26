package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.PropertyName;

import java.util.Random;

public class Prenotazione {

    @PropertyName("id_animale")
     public String id_animale;

    @PropertyName("id_appuntamento")
     public String id_appuntamento;

    @PropertyName("Tipo")
    public String tipo;

    @PropertyName("Data")
    public String data;

    @PropertyName("orario_inizio")
    public String orario_inizio;

    @PropertyName("orario_fine")
    public String orario_fine;

    @PropertyName("id_veterinario")
    public String id_veterinario;

    @PropertyName("id_prenotazione")
     public String id_prenotazione;


    public Prenotazione() {

        Random rand = new Random();

        // Generare un numero casuale
        int randomNumber = rand.nextInt();

        id_prenotazione = String.valueOf(randomNumber);

    }

    public String getId_veterinario() {
        return id_veterinario;
    }
}

