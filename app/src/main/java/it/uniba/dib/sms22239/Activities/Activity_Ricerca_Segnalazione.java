package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import it.uniba.dib.sms22239.Main_Adapter_Segnalazione;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;
import it.uniba.dib.sms22239.Models.Segnalazione;

public class Activity_Ricerca_Segnalazione extends AppCompatActivity
{
    RecyclerView recyclerView;
    Main_Adapter_Segnalazione mainAdapterRicercaSegnalazione;
    SearchView searchView;
    Main_Adapter_Segnalazione.OnItemClickListener listener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricercasegnalazione);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Ricerca_Segnalazione.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Preference.class));
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

        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        FirebaseRecyclerOptions<Segnalazione> options =
                new FirebaseRecyclerOptions.Builder<Segnalazione>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("uid"),Segnalazione.class)
                        .build();
        mainAdapterRicercaSegnalazione = new Main_Adapter_Segnalazione(options, new Main_Adapter_Segnalazione.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Segnalazione segnalazione = mainAdapterRicercaSegnalazione.getItem(position);
                String segnalazioneId = segnalazione.idSegnalazione;
                Intent intent = new Intent(Activity_Ricerca_Segnalazione.this, Activity_Profilo_Segnalazione_senza_modifica.class);
                intent.putExtra("SEGNALAZIONE_CODE",segnalazioneId);
                startActivity(intent);
            }

        });
        recyclerView.setAdapter(mainAdapterRicercaSegnalazione);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapterRicercaSegnalazione.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterRicercaSegnalazione.stopListening();
    }

    private void mysearch(String str) {

        FirebaseRecyclerOptions<Segnalazione> options =
                new FirebaseRecyclerOptions.Builder<Segnalazione>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("descrizione").startAt(str).endAt(str+"\uf8ff"),Segnalazione.class)
                        .build();

        mainAdapterRicercaSegnalazione = new Main_Adapter_Segnalazione(options,listener);
        mainAdapterRicercaSegnalazione.startListening();
        recyclerView.setAdapter(mainAdapterRicercaSegnalazione);
    }
/*
    private void filterList(String query) {

        if (query != null) {
            ArrayList<LanguageData> filteredList = new ArrayList<>();
            for (LanguageData i : mList) {
                if (i.getTitle().toLowerCase(Locale.ROOT).contains(query)) {
                    filteredList.add(i);
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(filteredList);
            }
        }
    }
    */

}

