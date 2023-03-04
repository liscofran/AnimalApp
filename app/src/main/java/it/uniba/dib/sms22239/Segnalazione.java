package it.uniba.dib.sms22239;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Segnalazione {
    public String oggetto;

    public String idSegnalazione;
    public String provincia;
    public String descrizione;
    public boolean checkProprietario;
    public boolean checkEnte;
    public boolean checkVeterinario;
    public String uid;
    public String immagine;


    public Segnalazione() {
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

    public void writeSegnalazione(Segnalazione sgn, String oggetto, String provincia, String descrizione, boolean checkProprietario, boolean checkEnte, boolean checkVeterinario) {

        sgn.oggetto = oggetto;
        sgn.provincia = provincia;
        sgn.descrizione = descrizione;
        sgn.checkProprietario = checkProprietario ;
        sgn.checkEnte = checkEnte;
        sgn.checkVeterinario = checkVeterinario;



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Utente non autenticato
            return;
        }

        long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        int tmp = Math.abs(random.nextInt()); // generare un numero casuale

        sgn.idSegnalazione = Integer.toString(tmp);
        sgn.uid = user.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Segnalazioni").child(Integer.toString(tmp)).setValue(sgn);
    }

}
