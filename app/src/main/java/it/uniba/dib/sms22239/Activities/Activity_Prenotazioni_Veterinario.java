package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.uniba.dib.sms22239.Fragments.Fragment_App_Pren_Dialog;
import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Prenotazioni_Veterinario extends AppCompatActivity {
    private CalendarView calendarView;
    private DatabaseReference mDatabase;
    private ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
    private ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
    private String id_veterinario;
    private String idAnimale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appuntamenti_veterinario);

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
                Intent intent = new Intent(Activity_Prenotazioni_Veterinario.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Prenotazioni_Veterinario.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Prenotazioni_Veterinario.this, Activity_Segnalazioni_Offerte.class));
            }
        });

//        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(Activity_Appuntamenti_Veterinario.this, Activity_Animali.class));
//            }
//        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Prenotazioni_Veterinario.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Prenotazioni_Veterinario.this, Preference.class));
            }
        });
        findViewById(R.id.scheda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Prenotazioni_Veterinario.this, Activity_Scheda_Veterinario.class));
            }
        });

        calendarView = findViewById(R.id.simpleCalendarView);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());

        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id_veterinario = dataSnapshot.child(user.getUid()).getKey();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Prenotazioni").child("id_veterinario");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Prenotazione prenotazione = dataSnapshot.getValue(Prenotazione.class);
                    if (prenotazione.getId_veterinario() == id_veterinario)
                        prenotazioni.add(prenotazione);
                }

                /*for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Prenotazione prenotazione = dataSnapshot.getValue(Prenotazione.class);
                    if(prenotazione.id_veterinario == id_veterinario)
                        prenotazioni.add(prenotazione);
                }*/

                /* Colora le date che hanno almeno un appuntamento
                for (Appuntamento appuntamento : appuntamenti) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date(appuntamento.getData()));
                    long timeInMillis = calendar.getTimeInMillis();
                    calendarView.setDateTextAppearance(R.style.MyCalendarViewStyle);
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_Prenotazioni_Veterinario.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }

        });

       /* // Colora le date che hanno almeno una prenotazione
        for (Prenotazione prenotazione : prenotazioni) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(prenotazione.getData());
            long timeInMillis = calendar.getTimeInMillis();
            calendarView.setBackgroundColor(Color.GREEN);
        }*/

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                // Recupera la data selezionata dall'utente
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Ottieni la data selezionata in formato "yyyy-MM-dd"
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String data = sdf.format(selectedDate.getTime());

                // Crea la finestra di dialogo delle prenotazioni
                ArrayList<Prenotazione> prenotazioni = null;
                Fragment_App_Pren_Dialog dialog = new Fragment_App_Pren_Dialog(appuntamenti, prenotazioni, data, idAnimale);
                dialog.show(getSupportFragmentManager(), "prenotazione_dialog");
            }
        });

    }
}

