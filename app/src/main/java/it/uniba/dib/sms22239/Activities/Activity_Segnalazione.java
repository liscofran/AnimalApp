package it.uniba.dib.sms22239.Activities;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterSegnalazione;
import it.uniba.dib.sms22239.R;
import it.uniba.dib.sms22239.Models.Segnalazione;

public class Activity_Segnalazione extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapterSegnalazione mainAdapterSegnalazione;
    SearchView searchView;
    FirebaseRecyclerAdapterSegnalazione.OnItemClickListener listener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageButton backbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button aggiungiSegnalazione = findViewById(R.id.add_segnalazione_button);
        aggiungiSegnalazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Segnalazione.this, Activity_Registrazione_Segnalazione.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazione.this, Activity_Home.class));
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("uid").equalTo(mUser.getUid()), Segnalazione.class)
                        .build();
        mainAdapterSegnalazione = new FirebaseRecyclerAdapterSegnalazione(options, new FirebaseRecyclerAdapterSegnalazione.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Segnalazione segnalazione = mainAdapterSegnalazione.getItem(position);
                String segnalazioneId = segnalazione.idSegnalazione;
                Intent intent = new Intent(Activity_Segnalazione.this, Activity_Profilo_Segnalazione.class);
                intent.putExtra("SEGNALAZIONE_CODE",segnalazioneId);
                startActivity(intent);
            }

        });
        recyclerView.setAdapter(mainAdapterSegnalazione);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapterSegnalazione.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterSegnalazione.stopListening();
    }

    private void mysearch(String str) {

        FirebaseRecyclerOptions<Segnalazione> options =
                new FirebaseRecyclerOptions.Builder<Segnalazione>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("descrizione").startAt(str).endAt(str+"\uf8ff"),Segnalazione.class)
                        .build();

        mainAdapterSegnalazione = new FirebaseRecyclerAdapterSegnalazione(options,listener);
        mainAdapterSegnalazione.startListening();
        recyclerView.setAdapter(mainAdapterSegnalazione);
    }



}