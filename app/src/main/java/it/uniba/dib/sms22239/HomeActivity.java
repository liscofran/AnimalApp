package it.uniba.dib.sms22239;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity {

    Button btnLogout;
    FirebaseAuth mAuth;
    Button btnIntent;
    Toolbar toolbar;
    RelativeLayout relativeLayout;
    String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Load_setting();

       /* relativeLayout= findViewById(R.id.home_relative_layout); //importante per il tema

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LoadSettings();

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Profile_Activity.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Segnalazioni.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PetActivity.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, QRcode.class));
            }
        });

        findViewById(R.id.scheda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SchedaActivity.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Preference.class));
            }
        });
        */
    }

    private void Load_setting() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String orien = sp.getString("ORIENTATION", "false");
        if ("1".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orien)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

        @Override
        protected void onStart() {
            super.onStart();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
            
        Query query = myRef.orderByChild("classe");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Controlla se l'utente esiste nel database Firebase
                if (snapshot.exists()) {
                    // Recupera il valore dell'attributo "classe"
                    String classe = snapshot.child("classe").getValue(String.class);

                    // L'utente è stato trovato
                    if (classe != null) {
                        // Verifica il valore dell'attributo "classe"
                        if (classe.equals("Veterinario")) {
                            // Lanciare il VeterinarioFragment
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_container, new VeterinarioFragment());
                            fragmentTransaction.commit();
                        } else if (classe.equals("Proprietario") || classe.equals("Ente")) {
                            // Lanciare il ProprietarioFragment
                            ProprietarioFragment proprietarioFragment = new ProprietarioFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, proprietarioFragment)
                                    .commit();
                           }
                    }
                } else {
                    // L'utente non è stato trovato
                    Log.d("User", "Utente non trovato");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                Log.e("Firebase", "Operazione annullata: " + error.getMessage());
            }
        });

    }



    private void logout() {
        mAuth.signOut();
        irMain();
    }

    private void irMain() {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(HomeActivity.this, "Logout effettuato con successo", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Load_setting();
    }


    public String getUserType() {
        return userType;
    }
}






