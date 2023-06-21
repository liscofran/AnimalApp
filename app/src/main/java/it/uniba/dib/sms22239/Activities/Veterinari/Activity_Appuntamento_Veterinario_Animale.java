package it.uniba.dib.sms22239.Activities.Veterinari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

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
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Settings;
import it.uniba.dib.sms22239.Activities.Proprietari.Activity_Profilo_Proprietario;
import it.uniba.dib.sms22239.Activities.Activity_QRcode;
import it.uniba.dib.sms22239.Activities.Annunci.Activity_Menu_Annunci;
import it.uniba.dib.sms22239.Activities.Animali.Activity_Appuntamento_Animale;
import it.uniba.dib.sms22239.Fragments.Veterinari.Fragment_Appuntamento_Veterinario;
import it.uniba.dib.sms22239.R;

public class Activity_Appuntamento_Veterinario_Animale extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String id_utente = null;
    private String id_utente_veterinario = null;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appuntamento_veterinario);

        String idAppuntamento = getIntent().getStringExtra("id_appuntamento");
        String idAnimale = getIntent().getStringExtra("ANIMAL_CODE");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Appuntamenti").child(idAppuntamento);

        ImageButton backBtn2 = findViewById(R.id.back);
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Appuntamento_Veterinario_Animale.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Appuntamento_Veterinario_Animale.this, Activity_Profilo_Proprietario.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Appuntamento_Veterinario_Animale.this, Activity_Menu_Annunci.class));
            }
        });

/*        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Appuntamento.this, Activity_Animali.class));
            }
        });*/

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Appuntamento_Veterinario_Animale.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Appuntamento_Veterinario_Animale.this, Activity_Settings.class));
            }
        });

        mDatabase1.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                id_utente_veterinario = dataSnapshot.child("id_veterinario").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
               id_utente = dataSnapshot.child(user.getUid()).getKey();
                if(id_utente_veterinario.equals(id_utente)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame, new Fragment_Appuntamento_Veterinario());
                    fragmentTransaction.commit();
                }
                else
                {
                    Intent intent = new Intent(Activity_Appuntamento_Veterinario_Animale.this, Activity_Appuntamento_Animale.class);
                    intent.putExtra("id_appuntamento",idAppuntamento);
                    intent.putExtra("ANIMAL_CODE", idAnimale);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
