package it.uniba.dib.sms22239;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Activity_Offerte extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapterOfferte mainAdapterOfferte;
    SearchView searchView;
    MainAdapterOfferte.OnItemClickListener listener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerte);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button aggiungiOfferte = findViewById(R.id.add_offerta_button);
        aggiungiOfferte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Offerte.this, Activity_Registrazione_Offerte.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Offerte.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Offerte.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Offerte.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Offerte.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Offerte.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Offerte.this, Preference.class));
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Offerte").orderByChild("uid").equalTo(mUser.getUid()),Offerta.class)
                        .build();
        mainAdapterOfferte = new MainAdapterOfferte(options, new MainAdapterOfferte.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Offerta offerta = mainAdapterOfferte.getItem(position);
                String offertaId = offerta.uid;
                Intent intent = new Intent(Activity_Offerte.this, Activity_Animali.class);
                intent.putExtra("OFFERTA_CODE",offertaId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mainAdapterOfferte);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapterOfferte.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterOfferte.stopListening();
    }

    private void mysearch(String str) {

        FirebaseRecyclerOptions<Offerta> options =
                new FirebaseRecyclerOptions.Builder<Offerta>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Offerte").orderByChild("oggetto").startAt(str).endAt(str+"\uf8ff"),Offerta.class)
                        .build();

        mainAdapterOfferte = new MainAdapterOfferte(options,listener);
        mainAdapterOfferte.startListening();
        recyclerView.setAdapter(mainAdapterOfferte);
    }



}