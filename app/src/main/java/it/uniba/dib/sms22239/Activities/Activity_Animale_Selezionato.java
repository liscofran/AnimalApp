package it.uniba.dib.sms22239.Activities;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterAnimale;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;
import it.uniba.dib.sms22239.Adapters.RecyclerAdapterAnimale;

public class Activity_Animale_Selezionato extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapterAnimale mainAdapter;
    SearchView searchView;
    String idAnimale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_pokemon);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton backBtn2 = findViewById(R.id.back);

        idAnimale = getIntent().getStringExtra("ANIMAL_CODE");

        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Animale_Selezionato.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Animale_Selezionato.this, Preference.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                mysearch(newText);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mysearch(newText);
                return false;
            }
        });

        FirebaseRecyclerOptions<Animale> options =
                new FirebaseRecyclerOptions.Builder<Animale>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Animale"),Animale.class)
                        .build();

        mainAdapter = new FirebaseRecyclerAdapterAnimale(options,null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapter.startListening();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                filter();
            }
        }, 200);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    private void mysearch(String str)
    {
        List<Animale> filteredList = new ArrayList<>();

        for (Animale animale : mainAdapter.getSnapshots())
        {
            if (animale.nome.startsWith(str) && (!Objects.equals(animale.Id, idAnimale)))
            {
                filteredList.add(animale);
            }
        }

        RecyclerAdapterAnimale adapter = new RecyclerAdapterAnimale(filteredList, new RecyclerAdapterAnimale.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Animale animale =  filteredList.get(position);
                String animalId = animale.Id;
                String[] spinnerOptions = {"Non compatibile", "Amici", "Conviventi"};

                // creazione dello spinner
                Spinner spinner = new Spinner(Activity_Animale_Selezionato.this);
                spinner.setAdapter(new ArrayAdapter<String>(Activity_Animale_Selezionato.this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions));

                // creazione dell'AlertDialog con lo spinner
                new AlertDialog.Builder(Activity_Animale_Selezionato.this)
                        .setTitle("Selezione Animale")
                        .setMessage("Che tipo di relazione hanno questi 2 animali?")
                        .setView(spinner)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedOption = spinner.getSelectedItem().toString();
                                Toast.makeText(Activity_Animale_Selezionato.this, "Relazione "+ selectedOption + " aggiornata con successo!" , Toast.LENGTH_SHORT).show();

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);
                                mDatabase.child("idAnimalerelazione").setValue(animalId);
                                mDatabase.child("relazione").setValue(selectedOption);

                                DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Animale").child(animalId);
                                mDatabase1.child("relazione").setValue(selectedOption);
                                mDatabase1.child("idAnimalerelazione").setValue(idAnimale);
                                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_Home.class));
                            }
                        })
                        .setNegativeButton("Annulla", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void filter(){

        List<Animale> ListSenzaAnimale = new ArrayList<>();

        for (Animale animale : mainAdapter.getSnapshots())
        {
            if (!Objects.equals(animale.Id, idAnimale))
            {
                ListSenzaAnimale.add(animale);
            }
        }

        RecyclerAdapterAnimale adapter = new RecyclerAdapterAnimale(ListSenzaAnimale, new RecyclerAdapterAnimale.OnItemClickListener() {
            @Override
            public void onItemClick(int position)
            {
                Animale animale =  ListSenzaAnimale.get(position);
                String animalId = animale.Id;
                String[] spinnerOptions = {"Non compatibile", "Amici", "Conviventi"};

                // creazione dello spinner
                Spinner spinner = new Spinner(Activity_Animale_Selezionato.this);
                spinner.setAdapter(new ArrayAdapter<String>(Activity_Animale_Selezionato.this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions));

                // creazione dell'AlertDialog con lo spinner
                new AlertDialog.Builder(Activity_Animale_Selezionato.this)
                        .setTitle("Selezione Animale")
                        .setMessage("Che tipo di relazione hanno questi 2 animali?")
                        .setView(spinner)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedOption = spinner.getSelectedItem().toString();
                                Toast.makeText(Activity_Animale_Selezionato.this, "Relazione "+ selectedOption + " aggiornata con successo!" , Toast.LENGTH_SHORT).show();

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);
                                mDatabase.child("idAnimalerelazione").setValue(animalId);
                                mDatabase.child("relazione").setValue(selectedOption);


                                DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Animale").child(animalId);
                                mDatabase1.child("relazione").setValue(selectedOption);
                                mDatabase1.child("idAnimalerelazione").setValue(idAnimale);
                                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_Home.class));
                            }
                        })
                        .setNegativeButton("Annulla", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}