package it.uniba.dib.sms22239;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public abstract class User {
    public String email;
    public String password;
    public String userId;
    private DatabaseReference mDatabase;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String password, String userId) {
        this.email = email;
        this.password= password;
        this.userId= userId;
    }
}