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
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.Main_Adapter_Animale;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Album_Animali extends AppCompatActivity {

    RecyclerView recyclerView;
    Main_Adapter_Animale mainAdapter;
    SearchView searchView;
    Main_Adapter_Animale.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miei_animali);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                Intent intent = new Intent(Activity_Album_Animali.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Album_Animali.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Album_Animali.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Album_Animali.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Album_Animali.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Album_Animali.this, Preference.class));
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        FirebaseRecyclerOptions<Animale> options =
                new FirebaseRecyclerOptions.Builder<Animale>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Animale").orderByChild("Id_utente"),Animale.class)
                        .build();

        mainAdapter = new Main_Adapter_Animale(options, new Main_Adapter_Animale.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Animale animale = mainAdapter.getItem(position);
                String animalId = animale.Id;
                Intent intent = new Intent(Activity_Album_Animali.this, Activity_Animal_Profile.class);
                intent.putExtra("ANIMAL_CODE",animalId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    private void mysearch(String str) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        FirebaseRecyclerOptions<Animale> options =
                new FirebaseRecyclerOptions.Builder<Animale>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Animale").orderByChild("Id_utente").equalTo(mUser.getUid()).startAt(str).endAt(str+"\uf8ff"),Animale.class)
                        .build();

        mainAdapter = new Main_Adapter_Animale(options,listener);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
}