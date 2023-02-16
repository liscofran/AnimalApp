package it.uniba.dib.sms22239;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class Ente extends User
{
    public String ragione_sociale;
    public String partita_iva;
    private DatabaseReference mDatabase;

    public Ente() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void writeNewUser(Ente ente, String email, String password) {

        long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        int id = random.nextInt(); // generare un numero casuale

        ente.email = email;
        ente.password= password;
        ente.userId = "Ente" + id;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Ente").child(ente.userId).setValue(ente);
    }
}
