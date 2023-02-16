package it.uniba.dib.sms22239;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Veterinario extends User
{
    public String nome;
    public String cognome;
    public String partita_iva;
    private DatabaseReference mDatabase;

    public Veterinario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void writeNewUser(Veterinario vet, String email, String password, String id) {

        vet.email = email;
        vet.password= password;


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Veterinario").child("V" +id).setValue(vet);
        vet.userId = "V" +id;
    }
}
