package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

public class Appuntamento {

    @PropertyName("id_veterinario")
    private String id_veterinario;

    @PropertyName("orario_inizio")
    private String orario_inizio;

    @PropertyName("orario_fine")
    private String orario_fine;

    @PropertyName("data")
    private String data;

    @PropertyName("id_appuntamento")
    private String id_appuntamento;

    public Appuntamento() {
    }

    public Appuntamento(String id_veterinario, String inizio, String fine, String data, String id_appuntamento) {
        this.id_veterinario = id_veterinario;
        this.orario_inizio = String.valueOf(inizio);
        this.orario_fine = String.valueOf(fine);
        this.data = data;
        this.id_appuntamento = id_appuntamento;
    }

    public String getId_veterinario() {
        return id_veterinario;
    }

    public void setId_veterinario(String id_veterinario) {
        this.id_veterinario = id_veterinario;
    }

    public String getOrario_inizio() {
        return orario_inizio;
    }

    public void setOrario_inizio(String orario_inizio) {
        this.orario_inizio = orario_inizio;
    }

    public String getOrario_fine() {
        return orario_fine;
    }

    public void setOrario_fine(String orario_fine) {
        this.orario_fine = orario_fine;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId_appuntamento() {
        return id_appuntamento;
    }

    public void setId_appuntamento(String id_appuntamento) {
        this.id_appuntamento = id_appuntamento;
    }

    public void writeNewAppuntamento(String id_veterinario, String inizio, String fine, String data, Appuntamento appuntamento){

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("Appuntamenti").push().getKey();
        this.setId_veterinario(id_veterinario);
        this.setOrario_inizio(inizio);
        this.setOrario_fine(fine);
        this.setData(data);
        this.setId_appuntamento(key);
        mDatabase.child("Appuntamenti").child(key).setValue(this);
    }
}

