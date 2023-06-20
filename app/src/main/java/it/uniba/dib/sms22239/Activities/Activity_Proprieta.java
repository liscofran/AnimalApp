package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Proprieta extends AppCompatActivity
{
    private TextView midproprietarioTextView, proprietaTextView;
    private EditText modifica;
    private Button btn;
    private Spinner spinner;
    String selectedItem;
    private String proprietarioid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proprieta);

        spinner = findViewById(R.id.spinner);
        midproprietarioTextView = findViewById(R.id.cognome_veterinario);
        proprietaTextView = findViewById(R.id.animal_proprieta);
        modifica = findViewById(R.id.modifica);
        btn = findViewById(R.id.salva);

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
                Intent intent = new Intent(Activity_Proprieta.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Proprieta.this, Activity_Profile_Proprietario.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Proprieta.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Proprieta.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Proprieta.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Proprieta.this, Preference.class));
            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();

        String idAnimal = getIntent().getStringExtra("ANIMAL_CODE");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);

        DatabaseReference mDatabase1;
        mDatabase1 = database.getInstance().getReference().child("User");

        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                proprietarioid = dataSnapshot.child("Id_utente").getValue(String.class);
                String proprieta = dataSnapshot.child("prop").getValue(String.class);
                String descrizione = dataSnapshot.child("descprop").getValue(String.class);

                proprietaTextView.setText(proprieta);
                modifica.setText(descrizione);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        mDatabase1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String proprietarionome = dataSnapshot.child(proprietarioid).child("nome").getValue(String.class);
                String proprietariocognome = dataSnapshot.child(proprietarioid).child("cognome").getValue(String.class);

                String proprietario = proprietariocognome + " " + proprietarionome;

                //set delle variabili recuperate al layout
                midproprietarioTextView.setText(proprietario);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //Spinner Categoria

        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.proprieta_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener()
        {
            DatabaseReference Db2 = database.getInstance().getReference().child("Animale").child(idAnimal);

            @Override
            public void onClick(View view) {
                Db2.child("prop").setValue(selectedItem);
                String desc = modifica.getText().toString();
                if(desc != null) {
                    Db2.child("descprop").setValue(desc);
                }
                String c1= getString(R.string.c2);

                Toast.makeText(Activity_Proprieta.this, c1, Toast.LENGTH_SHORT).show();
            }
        });



    }
}
