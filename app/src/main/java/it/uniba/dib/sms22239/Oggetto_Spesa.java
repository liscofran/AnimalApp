package it.uniba.dib.sms22239;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class Oggetto_Spesa {
    @PropertyName("nome")
    public String nome;

    @PropertyName("id_animale")
    public String id_animale;

    @PropertyName("prezzo")
    public double prezzo;

    @PropertyName("quantita")
    public int quantita;

    @PropertyName("dataAcquisto")
    public String dataAcquisto;

    public Oggetto_Spesa() {

    }

    public void writeNewOggetto (Oggetto_Spesa os, String nome, double prezzo, int quantita, String dataAcquisto)
    {
        os.nome = nome;
        os.prezzo = prezzo;
        os.quantita = quantita;
        os.dataAcquisto = dataAcquisto;

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String animaleId = "idDellAnimaleDaRecuperare";
        mDatabase.child("Animale").child(animaleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Recupera l'ID dell'animale
                    os.id_animale = dataSnapshot.getKey();
                    // Fare qualcosa con l'ID dell'animale qui
                } else {
                    // L'animale non esiste nel database
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Si è verificato un errore durante la lettura del valore dal database
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.child("Oggetti").push().setValue(os);

    }
}
