package it.uniba.dib.sms22239;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;

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
                            VeterinarioFragment veterinarioFragment = new VeterinarioFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, veterinarioFragment)
                                    .commit();
                        }
                        else if(classe.equals("Proprietario"))
                        {
                            // Lanciare il ProprietarioFragment
                            ProprietarioFragment proprietarioFragment = new ProprietarioFragment();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, proprietarioFragment)
                                    .commit();
                        }
                        else
                        {
                                // Lanciare l enteFragment
                                EnteFragment enteFragment = new EnteFragment();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, enteFragment)
                                        .commit();
                        }
                    }
                }
                else
                {
                    ProprietarioFragment proprietarioFragment = new ProprietarioFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, proprietarioFragment)
                            .commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                Log.e("Firebase", "Operazione annullata: " + error.getMessage());
            }
        });
    }
}