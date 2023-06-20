package it.uniba.dib.sms22239.Activities;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import it.uniba.dib.sms22239.Fragments.Fragment_Ente;
import it.uniba.dib.sms22239.Fragments.Fragment_Proprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_Veterinario;
import it.uniba.dib.sms22239.R;

public class Activity_Home extends AppCompatActivity
{
    FirebaseAuth mAuth;
    String c1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Load_setting();
    }

    private void Load_setting()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sp.getString("language", "it");
        setLocale(language);
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        autenticazione();
        Load_setting();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        autenticazione();
        Load_setting();
    }

    protected void autenticazione()
    {
        c1 = getString(R.string.a3);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Controlla se l'utente esiste nel database Firebase
                if (snapshot.exists())
                {
                    // Recupera il valore dell'attributo "classe"
                    String classe = snapshot.child("classe").getValue(String.class);

                    // L'utente è stato trovato
                    if (classe != null)
                    {
                        // Verifica il valore dell'attributo "classe"
                        if (classe.equals("Veterinario"))
                        {
                            // Lanciare il VeterinarioFragment
                            Fragment_Veterinario veterinarioFragment = new Fragment_Veterinario();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, veterinarioFragment)
                                    .commit();
                        }
                        else if(classe.equals("Proprietario"))
                        {
                            // Lanciare il ProprietarioFragment
                            Fragment_Proprietario proprietarioFragment = new Fragment_Proprietario();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, proprietarioFragment)
                                    .commit();
                        }
                        else
                        {
                                // Lanciare l enteFragment
                                Fragment_Ente enteFragment = new Fragment_Ente();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, enteFragment)
                                        .commit();
                        }
                    }
                }
                else
                {
                    Fragment_Proprietario proprietarioFragment = new Fragment_Proprietario();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, proprietarioFragment)
                            .commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                Log.e("Firebase", c1 + error.getMessage());
            }
        });
    }
}