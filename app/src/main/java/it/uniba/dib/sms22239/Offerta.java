package it.uniba.dib.sms22239;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Offerta {

    public String categoria;
    public String oggetto;
    public String provincia;
    public String descrizione;
    public boolean checkProprietario;
    public boolean checkEnte;
    public boolean checkVeterinario;
    public String uid;


    public void writeOfferta(Offerta off, String categoria, String oggetto, String provincia, String descrizione, boolean checkProprietario, boolean checkEnte, boolean checkVeterinario) {

        off.categoria = categoria;
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

        off.uid = user.getUid();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Offerte").push().setValue(off);
    }
}
