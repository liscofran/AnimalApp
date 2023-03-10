package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.FirebaseDatabase;

public class Proprietario extends User
{
    public String nome;
    public String cognome;

    public Proprietario() {

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
