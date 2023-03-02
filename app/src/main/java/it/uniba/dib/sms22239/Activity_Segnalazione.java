package it.uniba.dib.sms22239;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class Activity_Segnalazione extends AppCompatActivity {

    RecyclerView recyclerView;
    Main_Adapter_Segnalazione mainAdapterSegnalazione;
    SearchView searchView;
    Main_Adapter_Segnalazione.OnItemClickListener listener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazione);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("uid").equalTo(mUser.getUid()),Segnalazione.class)
                        .build();
        mainAdapterSegnalazione = new Main_Adapter_Segnalazione(options, new Main_Adapter_Segnalazione.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Segnalazione segnalazione = mainAdapterSegnalazione.getItem(position);
                String segnalazioneId = segnalazione.uid;
                Intent intent = new Intent(Activity_Segnalazione.this, Activity_Animali.class);
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

        mainAdapterSegnalazione = new Main_Adapter_Segnalazione(options,listener);
        mainAdapterSegnalazione.startListening();
        recyclerView.setAdapter(mainAdapterSegnalazione);
    }



}