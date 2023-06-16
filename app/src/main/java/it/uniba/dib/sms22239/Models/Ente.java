package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.FirebaseDatabase;

public class Ente extends User
{
    public String ragione_sociale;
    public String partita_iva;
    public String tipo;
    public String sede_legale;

    public Ente() {

    }

    public void writeNewUser(Ente ente, String email, String password, String id) {
        ente.email = email;
        ente.password= password;
        ente.classe="Ente";

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(id).setValue(ente);
        ente.userId = id;
    }
}
