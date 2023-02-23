package it.uniba.dib.sms22239;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Segnalazione {
    public String categoria;
    public String oggetto;
    public String provincia;
    public String descrizione;
    public boolean checkProprietario;
    public boolean checkEnte;
    public boolean checkVeterinario;
    public String uid;


    public Segnalazione() {
    }

    public String getCategoria() {
        return categoria;
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

    public void writeSegnalazione(Segnalazione sgn, String categoria, String oggetto, String provincia, String descrizione, boolean checkProprietario, boolean checkEnte, boolean checkVeterinario) {

        sgn.categoria = categoria;
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

        sgn.uid = user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Segnalazioni").push().setValue(sgn);
    }
}
