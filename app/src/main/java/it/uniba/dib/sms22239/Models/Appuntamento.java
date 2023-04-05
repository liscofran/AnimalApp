package it.uniba.dib.sms22239.Models;

import java.util.Calendar;

public class Appuntamento {
     int id_veterinario;
     Calendar orario_inizio;
     Calendar orario_fine;
     String data;
     int id_appuntamento;

    public Appuntamento(int id_veterinario, Calendar inizio, Calendar fine, String data, int id_appuntamento) {
        this.id_veterinario = id_veterinario;
        this.orario_inizio = inizio;
        this.orario_fine = fine;
        this.data = data;
        this.id_appuntamento = id_appuntamento;
    }

    public void writeNewAppuntamento (int id_veterinario, Calendar inizio, Calendar fine, String data, int id_appuntamento, Appuntamento ap){

    }


}

