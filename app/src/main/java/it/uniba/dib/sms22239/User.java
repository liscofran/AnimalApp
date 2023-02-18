package it.uniba.dib.sms22239;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

public abstract class User {
    public String email;
    public String password;
    public String userId;
    public String classe;
    protected DatabaseReference mDatabase;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}