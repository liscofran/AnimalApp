package it.uniba.dib.sms22239.Activities.Animali;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import it.uniba.dib.sms22239.Fragments.Enti.Fragment_Toolbar_Ente;
import it.uniba.dib.sms22239.Fragments.Proprietari.Fragment_Toolbar_Proprietario;
import it.uniba.dib.sms22239.Fragments.Veterinari.Fragment_Toolbar_Veterinario;
import it.uniba.dib.sms22239.Models.Prenotazione_Diagnosi;
import it.uniba.dib.sms22239.Models.Prenotazione_Esame;
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
    private String flag,idAppuntamento;


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
                String c1= getString(R.string.data_app);
                String c2= getString(R.string.Ora_start);
                String c3= getString(R.string.ora_fine);
                String c4= getString(R.string.Dottore);
                String c5= getString(R.string.app1);
                data = dataSnapshot.child("data").getValue(String.class);
                orario_inizio = dataSnapshot.child("orario_inizio").getValue(String.class);
                orario_fine = dataSnapshot.child("orario_fine").getValue(String.class);
                id_veterinario = dataSnapshot.child("id_veterinario").getValue(String.class);

                //set delle variabili recuperate al layout
                DataTextView.setText(c1 + ": " + data);
                OraInizioTextView.setText(c2 + ": " + orario_inizio);
                OraFineTextView.setText(c3 + ": " + orario_fine);


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

                                    CognomeVeterinarioTextView.setText(c4 + ": " + cognome_veterinario);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // Handle the case when idAppuntamento is null
                            Toast.makeText(Activity_Appuntamento_Animale.this, c5, Toast.LENGTH_SHORT).show();
                        }

                    }


                }, 500);


                ImageButton backBtn2 = findViewById(R.id.back);
                backBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Activity_Appuntamento_Animale.this, Activity_Calendario_Appuntamenti_Animale.class);
                        startActivity(intent);
                    }
                });

                autenticazione();

                String c6= getString(R.string.esame);
                String c7= getString(R.string.diagnosi);
                spinner = findViewById(R.id.spinner);
                spinner.setPrompt("");
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Activity_Appuntamento_Animale.this, R.array.appuntamento_options, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedItem = (String) parent.getItemAtPosition(position);
                        if(selectedItem.equals(c6))
                        {
                            type = "Esame";
                        }else if(selectedItem.equals(c7))
                        {
                            type = "Diagnosi";
                        }else
                        {
                            onNothingSelected(parent);
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
                            Toast.makeText(Activity_Appuntamento_Animale.this, "Prenotazione esame effettuata con successo", Toast.LENGTH_SHORT).show();

                        } else {
                            Prenotazione_Diagnosi p_d = new Prenotazione_Diagnosi();
                            p_d.writeNewPrenotazione(p_d, idAnimale, idAppuntamento, data, orario_inizio, orario_fine, id_veterinario);
                            Intent intent2 = new Intent(Activity_Appuntamento_Animale.this, Activity_Calendario_Appuntamenti_Animale.class);
                            startActivity(intent2);
                            Toast.makeText(Activity_Appuntamento_Animale.this, "Prenotazione diagnosi effettuata con successo", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Veterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Proprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Ente());
                    fragmentTransaction.commit();
                    flag = "ente";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                String c4= getString(R.string.a3);
                Log.e("Firebase", c4 + error.getMessage());
            }
        });
    }
}
