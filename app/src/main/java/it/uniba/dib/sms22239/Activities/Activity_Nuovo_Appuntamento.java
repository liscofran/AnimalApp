package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Nuovo_Appuntamento extends AppCompatActivity {

    private CalendarView calendarView;
    private Button salvaButton;
    String idVeterinario;
    private String idAppuntamento;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_appuntamento);

        // Inizializza gli elementi della UI
        calendarView = findViewById(R.id.simpleCalendarView);
        salvaButton = findViewById(R.id.salva);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

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
                Intent intent = new Intent(Activity_Nuovo_Appuntamento.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Nuovo_Appuntamento.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Nuovo_Appuntamento.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Nuovo_Appuntamento.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Nuovo_Appuntamento.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Nuovo_Appuntamento.this, Preference.class));
            }
        });

        // Imposta il listener sul CalendarView per ottenere la data selezionata dall'utente
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                // Creazione dell'oggetto Appuntamento con i dati selezionati dall'utente


                String data = LocalDate.of(year, month + 1, day).format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));


                DatabaseReference mDatabase1 = database.getInstance().getReference().child("User").child(user.getUid());

                // Aggiungi un listener per ottenere i dati del veterinario
                mDatabase1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Recupera l'ID del veterinario dal DataSnapshot
                        idVeterinario = dataSnapshot.getKey();


                        // Imposta il listener sul bottone "Salva"
                        salvaButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TimePicker timePicker = findViewById(R.id.ora_inizio);
                                LocalTime ora_inizio = LocalTime.of(timePicker.getHour(), timePicker.getMinute());

                                TimePicker timePicker2 = findViewById(R.id.ora_fine);
                                LocalTime ora_fine = LocalTime.of(timePicker2.getHour(), timePicker2.getMinute());

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                String ora_inizio_string = ora_inizio.format(formatter);
                                String ora_fine_string = ora_fine.format(formatter);


                                Appuntamento appuntamento = new Appuntamento(idVeterinario, ora_inizio_string, ora_fine_string, data, idAppuntamento);
                                appuntamento.writeNewAppuntamento(idVeterinario, ora_inizio_string, ora_fine_string, data, appuntamento);
                                Intent intent = new Intent(Activity_Nuovo_Appuntamento.this, Activity_Nuovo_Appuntamento.class);
                                startActivity(intent);
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}
