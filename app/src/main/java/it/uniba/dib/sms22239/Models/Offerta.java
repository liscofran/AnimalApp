package it.uniba.dib.sms22239.Models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Offerta {
    public String oggetto;
    public String provincia;
    public String descrizione;
    public String idOfferta;
    public boolean checkProprietario;
    public boolean checkEnte;
    public boolean checkVeterinario;
    public String uid;
    public String immagine;

    public Offerta() {
    }

    public String getOggetto() {
        return oggetto;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getUid() {
        return uid;
    }


    public void writeOfferta(Offerta off,String oggetto, String provincia, String descrizione, boolean checkProprietario, boolean checkEnte, boolean checkVeterinario) {

        off.oggetto = oggetto;
        off.provincia = provincia;
        off.descrizione = descrizione;
        off.checkProprietario = checkProprietario ;
        off.checkEnte = checkEnte;
        off.checkVeterinario = checkVeterinario;



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Utente non autenticato
            return;
        }

        long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        int tmp = Math.abs(random.nextInt()); // generare un numero casuale

        off.idOfferta = Integer.toString(tmp);
        off.uid = user.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Offerte").child(Integer.toString(tmp)).setValue(off);
    }
}
