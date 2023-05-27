package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import it.uniba.dib.sms22239.Models.Prenotazione_Diagnosi;
import it.uniba.dib.sms22239.Models.Prenotazione_Esame;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Appuntamento_Animale extends AppCompatActivity {

    private TextView DataTextView;
    private TextView OraInizioTextView;
    private TextView OraFineTextView;
    private TextView CognomeVeterinarioTextView;
    private String data;
    private String orario_inizio;
    private String orario_fine;
    private String id_veterinario;
    private Spinner spinner;
    private String selectedItem;
    private String type = "Esame";
    private String idAppuntamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appuntamento_animale);

         idAppuntamento = getIntent().getStringExtra("id_appuntamento");
        String idAnimale = getIntent().getStringExtra("ANIMAL_CODE");
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Appuntamenti").child(idAppuntamento);

        DataTextView = findViewById(R.id.date);
        OraInizioTextView = findViewById(R.id.time_start);
        OraFineTextView = findViewById(R.id.time_end);
        CognomeVeterinarioTextView = findViewById(R.id.cognome_veterinario);

        //Spinner per la scelta dell'appuntamento


        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                data = dataSnapshot.child("data").getValue(String.class);
                orario_inizio = dataSnapshot.child("orario_inizio").getValue(String.class);
                orario_fine = dataSnapshot.child("orario_fine").getValue(String.class);
                id_veterinario = dataSnapshot.child("id_veterinario").getValue(String.class);

                //set delle variabili recuperate al layout
                DataTextView.setText("Data Appuntamento: " + data);
                OraInizioTextView.setText("Orario Inizio:  " + orario_inizio);
                OraFineTextView.setText("Orario Fine: " + orario_fine);


                Handler handler2 = new Handler(Looper.getMainLooper());

                // Esegui un'azione dopo un ritardo di mezzo secondo
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (idAppuntamento != null) {

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(id_veterinario);
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String cognome_veterinario = dataSnapshot.child("cognome").getValue(String.class);

                                    CognomeVeterinarioTextView.setText("Dottor: " + cognome_veterinario);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // Handle the case when idAppuntamento is null
                            Toast.makeText(Activity_Appuntamento_Animale.this, "Id appuntamento non valido", Toast.LENGTH_SHORT).show();
                        }

                    }


                }, 500);


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
                        Intent intent = new Intent(Activity_Appuntamento_Animale.this, Activity_Home.class);
                        startActivity(intent);
                    }
                });

                findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Activity_Appuntamento_Animale.this, Activity_Profile_Proprietario_Ente.class));
                    }
                });

                findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Activity_Appuntamento_Animale.this, Activity_Segnalazioni_Offerte.class));
                    }
                });

                findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Activity_Appuntamento_Animale.this, Activity_Animali.class));
                    }
                });

                findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Activity_Appuntamento_Animale.this, Activity_QRcode.class));
                    }
                });

                findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Activity_Appuntamento_Animale.this, Preference.class));
                    }
                });

                spinner = findViewById(R.id.spinner);
                spinner.setPrompt("");
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Activity_Appuntamento_Animale.this, R.array.appuntamento_options, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedItem = (String) parent.getItemAtPosition(position);

                        switch (selectedItem) {
                            case "Esame":
                                type = "Esame";
                                break;
                            case "Diagnosi":
                                type = "Diagnosi";
                                break;
                            default:
                                onNothingSelected(parent);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(Activity_Appuntamento_Animale.this, "Scelta non valida", Toast.LENGTH_SHORT).show();
                    }
                });

                // Seleziona il bottone "Prenotati"

                findViewById(R.id.prenotati).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (type == "Esame") {
                            Prenotazione_Esame p_e = new Prenotazione_Esame();
                            p_e.writeNewPrenotazione(p_e, idAnimale, idAppuntamento, data, orario_inizio, orario_fine, id_veterinario);
                            Intent intent = new Intent(Activity_Appuntamento_Animale.this, Activity_Calendario_Appuntamenti_Animale.class);
                            startActivity(intent);
                        } else {
                            Prenotazione_Diagnosi p_d = new Prenotazione_Diagnosi();
                            p_d.writeNewPrenotazione(p_d, idAnimale, idAppuntamento, data, orario_inizio, orario_fine, id_veterinario);
                            Intent intent2 = new Intent(Activity_Appuntamento_Animale.this, Activity_Calendario_Appuntamenti_Animale.class);
                            startActivity(intent2);
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
}
