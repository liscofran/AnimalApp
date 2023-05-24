package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import it.uniba.dib.sms22239.Fragments.Fragment_Ente;
import it.uniba.dib.sms22239.Fragments.Fragment_Proprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_Veterinario;
import it.uniba.dib.sms22239.Fragments.Fragment_profile_veterinario;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar1;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Segnalazioni_Offerte extends AppCompatActivity {

    TextView creasegnalazione;
    TextView creaofferta;
    TextView ricercasegnalazione;
    TextView ricercaofferta;
    TextView miesegnalazioni;
    TextView mieofferte;
    ImageButton backbutton;


    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazioniofferte);

        autenticazione();

        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (flag == "veterinario"){

        }
        else{

        }

        creasegnalazione = findViewById(R.id.crea_segnalazione);

        creasegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Registrazione_Segnalazione.class);
                startActivity(intent);
            }
        });

        creaofferta = findViewById(R.id.creaofferta);

        creaofferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Registrazione_Offerte.class);
                startActivity(intent);
            }
        });

        mieofferte = findViewById(R.id.mieofferte);

        mieofferte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Offerte.class);
                startActivity(intent);
            }
        });

        ricercasegnalazione = findViewById(R.id.ricerca_segnalazione);

        ricercasegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Ricerca_Segnalazioni.class);
                startActivity(intent);
            }
        });

        miesegnalazioni = findViewById(R.id.mie_segnalazioni);

        miesegnalazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Segnalazione.class);
                startActivity(intent);
            }
        });

        ricercaofferta = findViewById(R.id.ricercaofferte);

        ricercaofferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Segnalazioni_Offerte.this, Activity_Ricerca_Offerte.class);
                startActivity(intent);
            }
        });

    }

    protected void autenticazione()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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



    public boolean isChangingConfigurations() {
        return false;
    }
}
