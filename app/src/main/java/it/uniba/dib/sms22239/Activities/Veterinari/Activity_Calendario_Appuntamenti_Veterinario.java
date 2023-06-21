package it.uniba.dib.sms22239.Activities.Veterinari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Proprietari.Activity_Profilo_Proprietario;
import it.uniba.dib.sms22239.Activities.Activity_QRcode;
import it.uniba.dib.sms22239.Activities.Annunci.Activity_Menu_Annunci;
import it.uniba.dib.sms22239.Fragments.Fragment_Appuntamento_Selezionato;
import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;
import it.uniba.dib.sms22239.R;


public class Activity_Calendario_Appuntamenti_Veterinario extends AppCompatActivity {

    private CalendarView calendarView;
    private DatabaseReference mDatabase;
    private ArrayList<Appuntamento> appuntamenti= new ArrayList<>();
    private ArrayList<Prenotazione> prenotazioni= new ArrayList<>();
    private String idAnimale;
    private String id_veterinario;


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
                Intent intent = new Intent(Activity_Calendario_Appuntamenti_Veterinario.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Veterinario.this, Activity_Profilo_Proprietario.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Veterinario.this, Activity_Menu_Annunci.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Veterinario.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Veterinario.this, Preference.class));
            }
        });
        findViewById(R.id.scheda).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Veterinario.this, Activity_Scheda_Veterinario.class));
            }
        });

        calendarView = findViewById(R.id.simpleCalendarView);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());

        mDatabase1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                id_veterinario = dataSnapshot.child(user.getUid()).getKey();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Appuntamenti");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //appuntamenti = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Appuntamento appuntamento = dataSnapshot.getValue(Appuntamento.class);
                    if(appuntamento.getId_veterinario().equals( id_veterinario))
                        appuntamenti.add(appuntamento);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_Calendario_Appuntamenti_Veterinario.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
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
                Fragment_Appuntamento_Selezionato dialog = new Fragment_Appuntamento_Selezionato(appuntamenti, data,idAnimale);
                dialog.show(getSupportFragmentManager(), "appuntamento_dialog");
            }
        });

        findViewById(R.id.appuntamento).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Calendario_Appuntamenti_Veterinario.this, Activity_Nuovo_Appuntamento.class));
            }
        });


    }
}






