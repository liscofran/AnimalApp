package it.uniba.dib.sms22239.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Fragments.Fragment_profile_animale;
import it.uniba.dib.sms22239.Fragments.Fragment_profilo_animale_senza_modifica;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar1;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Animal_Profile extends AppCompatActivity
{
    protected String id_utente;
    protected String id_utente_animale;
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_animal);
        Load_setting();

        autenticazione();

        String idAnimale = getIntent().getStringExtra("ANIMAL_CODE");

        //Reference al Database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());

        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                id_utente_animale = dataSnapshot.child("Id_utente").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        mDatabase1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                id_utente = dataSnapshot.child(user.getUid()).getKey();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //Se l'utente che sta accedendo al profilo dell'animale è il proprietario permette di accedere
                //a modifiche e dati ulteriori

                if(id_utente_animale != null)
                {
                    if(id_utente_animale.equals(id_utente))
                    {
                        fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_animale());
                        fragmentTransaction.commit();
                    }
                }
                else
                {
                    fragmentTransaction.replace(R.id.fragment_container, new Fragment_profilo_animale_senza_modifica());
                    fragmentTransaction.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }

    private void Load_setting() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        String orien = sp.getString("ORIENTATION", "false");
        if ("1".equals(orien))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
        } else if ("2".equals(orien))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ("3".equals(orien))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    protected void autenticazione()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Recupera il valore dell'attributo "classe"
                String classe = snapshot.child("classe").getValue(String.class);


                // Verifica il valore dell'attributo "classe"
                if (classe.equals("Veterinario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar1());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar());
                    fragmentTransaction.commit();
                    flag = "altro";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                Log.e("Firebase", "Operazione annullata: " + error.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        Load_setting();
        super.onResume();
    }
}