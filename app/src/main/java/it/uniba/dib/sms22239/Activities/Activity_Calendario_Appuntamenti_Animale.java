package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.uniba.dib.sms22239.Fragments.Fragment_App_Dialog;
import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Calendario_Appuntamenti_Animale extends AppCompatActivity {

    private CalendarView calendarView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase1;
    private ArrayList<Appuntamento> appuntamenti = new ArrayList<>();
    private ArrayList<Prenotazione> prenotazioni= new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_appuntamenti_animale);

        String idAnimale = getIntent().getStringExtra("ANIMAL_CODE");

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
                Intent intent = new Intent(Activity_Calendario_Appuntamenti_Animale.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Animale.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Animale.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Animale.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Animale.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Animale.this, Preference.class));
            }
        });

        calendarView = findViewById(R.id.simpleCalendarView);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Appuntamenti");
        mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Prenotazioni");

        mDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                prenotazioni.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Prenotazione prenotazione = dataSnapshot.getValue(Prenotazione.class);
                        prenotazioni.add(prenotazione);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appuntamento appuntamento = dataSnapshot.getValue(Appuntamento.class);
                    String idAppuntamento = appuntamento.getId_appuntamento();

                    boolean hasPrenotazione = false;
                    for (Prenotazione prenotazione : prenotazioni) {
                        if (prenotazione.id_appuntamento.equals(idAppuntamento)) {
                            hasPrenotazione = true;
                            break;
                        }
                    }

                    if (!hasPrenotazione) {
                        appuntamenti.add(appuntamento);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                // Recupera la data selezionata dall'utente
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                // Ottieni la data selezionata in formato "yyyy-MM-dd"
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String data = sdf.format(selectedDate.getTime());

                // Crea la finestra di dialogo degli appuntamenti
                Fragment_App_Dialog dialog = new Fragment_App_Dialog(appuntamenti,  data, idAnimale);
                dialog.show(getSupportFragmentManager(), "appuntamento_dialog");
            }
        });
    }
}

/*
mDatabase2 =  FirebaseDatabase.getInstance().getReference().child("Prenotazioni");

mDatabase2.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(@NonNull DataSnapshot snapshot) {

        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
        Prenotazione prenotazione = dataSnapshot.getValue(Prenotazione.class);
        prenotazioni.add(prenotazione);
        }

        }

@Override
public void onCancelled(@NonNull DatabaseError error) {

        }
        });

 */