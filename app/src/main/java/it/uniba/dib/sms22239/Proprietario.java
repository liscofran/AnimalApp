package it.uniba.dib.sms22239;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import java.util.Random;

public class Proprietario extends User
{
    public String nome;
    public String cognome;
    private DatabaseReference mDatabase;

    public Proprietario() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void writeNewUser(Proprietario prop, String email, String password, String id) {

        //long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        //Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        //int id = random.nextInt(); // generare un numero casuale

        prop.email = email;
        prop.password= password;


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Proprietario").child("P" +id).setValue(prop);
        prop.userId = "P" +id;
    }
}
