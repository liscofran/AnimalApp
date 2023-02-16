package it.uniba.dib.sms22239;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Ente extends User
{
    public String ragione_sociale;
    public String partita_iva;

    public Ente() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void writeNewUser(Ente ente, String email, String password, String id) {
        ente.email = email;
        ente.password= password;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Ente").child("E" + id).setValue(ente);
        ente.userId = "E" + id;
    }
}
