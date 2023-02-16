package it.uniba.dib.sms22239;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class Veterinario extends User
{
    public String nome;
    public String cognome;
    public String partita_iva;
    private DatabaseReference mDatabase;

    public Veterinario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void writeNewUser(Veterinario vet, String email, String password) {

        long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        int id = random.nextInt(); // generare un numero casuale

        vet.email = email;
        vet.password= password;
        vet.userId = "Vet" + id;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Veterinario").child(vet.userId).setValue(vet);
    }
}
