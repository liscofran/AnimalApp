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

import it.uniba.dib.sms22239.Main_Adapter_Offerte;
import it.uniba.dib.sms22239.Models.Offerta;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Ricerca_Offerta extends AppCompatActivity
{
    RecyclerView recyclerView;
    Main_Adapter_Offerte mainAdapterRicercaOfferta;
    SearchView searchView;
    Main_Adapter_Offerte.OnItemClickListener listener;
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
                Intent intent = new Intent(Activity_Ricerca_Offerta.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerta.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerta.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerta.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerta.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerta.this, Preference.class));
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

        FirebaseRecyclerOptions<Offerta> options =
                new FirebaseRecyclerOptions.Builder<Offerta>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Offerte"),Offerta.class)
                        .build();
        mainAdapterRicercaOfferta = new Main_Adapter_Offerte(options, new Main_Adapter_Offerte.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Offerta offerta = mainAdapterRicercaOfferta.getItem(position);
                String offertaId = offerta.idOfferta;
                Intent intent = new Intent(Activity_Ricerca_Offerta.this, Activity_profilo_Offerta.class);
                intent.putExtra("OFFERTA_CODE",offertaId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mainAdapterRicercaOfferta);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapterRicercaOfferta.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterRicercaOfferta.stopListening();
    }

    private void mysearch(String str) {

        FirebaseRecyclerOptions<Offerta> options =
                new FirebaseRecyclerOptions.Builder<Offerta>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Offerte").orderByChild("descrizione").startAt(str).endAt(str+"\uf8ff"),Offerta.class)
                        .build();

        mainAdapterRicercaOfferta = new Main_Adapter_Offerte(options,listener);
        mainAdapterRicercaOfferta.startListening();
        recyclerView.setAdapter(mainAdapterRicercaOfferta);
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

