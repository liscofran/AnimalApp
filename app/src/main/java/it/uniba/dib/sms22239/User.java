package it.uniba.dib.sms22239;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Random;

public class User {
    public String email;
    public String password;
    private DatabaseReference mDatabase;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password) {
        this.email = email;
        this.password= password;
    }

    public void writeNewUser(String email,String password) {
        long seed = System.currentTimeMillis(); // ottenere il tempo corrente
        Random random = new Random(seed); // creare un oggetto Random con il tempo come seme
        int id = random.nextInt(); // generare un numero casuale
        User user = new User(email,password);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Utente").child("user" + id).setValue(user);
    }
}