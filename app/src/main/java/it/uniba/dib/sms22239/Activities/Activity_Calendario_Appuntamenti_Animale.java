package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.uniba.dib.sms22239.Fragments.Fragment_App_Dialog;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarEnte;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarProprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarVeterinario;
import it.uniba.dib.sms22239.Models.Appuntamento;
import it.uniba.dib.sms22239.Models.Prenotazione;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Calendario_Appuntamenti_Animale extends AppCompatActivity {

    String flag;
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

        autenticazione();

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