package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.FirebaseDatabase;

public class Veterinario extends User
{
    public String nome;
    public String cognome;
    public String partita_iva;
    public String codice_fiscale;
    public String email;
    public String titolo_studio;

    public Veterinario() {

    }

    public void writeNewUser(Veterinario vet, String email, String password, String id) {
        vet.email = email;
        vet.password= password;
        vet.classe="Veterinario";
        vet.userId = id;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(id).setValue(vet);
    }
}
