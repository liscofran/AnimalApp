package it.uniba.dib.sms22239.Models;

import com.google.firebase.database.DatabaseReference;

public abstract class User {
    public String email;
    public String password;
    public String userId;
    public String classe;
    protected DatabaseReference mDatabase;

    public User() {

    }
}