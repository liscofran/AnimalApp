package it.uniba.dib.sms22239;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import java.util.Random;

public class Proprietario extends User
{
    public String nome;
    public String cognome;

    public Proprietario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void writeNewUser(Proprietario prop, String email, String password, String id) {
        prop.email = email;
        prop.password= password;
        prop.classe="Proprietario";


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(id).setValue(prop);
        prop.userId = id;
    }
}
