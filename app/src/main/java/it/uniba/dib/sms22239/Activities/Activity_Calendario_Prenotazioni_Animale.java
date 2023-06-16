package it.uniba.dib.sms22239.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.uniba.dib.sms22239.Fragments.Fragment_App_Dialog;
import it.uniba.dib.sms22239.Fragments.Fragment_Pren_Dialog;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarEnte;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarProprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarVeterinario;
import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Calendario_Prenotazioni_Animale extends AppCompatActivity {

        private CalendarView calendarView;
        private DatabaseReference mDatabase;
        private ArrayList<Prenotazione> prenotazioni= new ArrayList<>();
        private String idAnimale;
        private String flag;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_prenotazioni_utente);

            idAnimale = getIntent().getStringExtra("ANIMAL_CODE");


            ImageButton backBtn2 = findViewById(R.id.back);
            backBtn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            autenticazione();

            calendarView = findViewById(R.id.simpleCalendarView);


           /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
            });*/


            mDatabase = FirebaseDatabase.getInstance().getReference().child("Prenotazioni");


            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    prenotazioni.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Prenotazione prenotazione = dataSnapshot.getValue(Prenotazione.class);
                        if (prenotazione.id_animale.equals(idAnimale)) {
                            prenotazioni.add(prenotazione);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Activity_Calendario_Prenotazioni_Animale.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
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
                    Fragment_Pren_Dialog dialog = new Fragment_Pren_Dialog(prenotazioni, data,idAnimale);
                    dialog.show(getSupportFragmentManager(), "appuntamento_dialog");
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
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarVeterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarProprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else if(classe.equals("Ente"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarEnte());
                    fragmentTransaction.commit();
                    flag = "ente";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                String c1= getString(R.string.a3);

                Log.e("Firebase", c1 + error.getMessage());
            }
        });
    }
    }