package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterOfferte;
import it.uniba.dib.sms22239.Models.Offerta;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Ricerca_Offerte extends AppCompatActivity
{
    RecyclerView recyclerView;
    FirebaseRecyclerAdapterOfferte mainAdapterRicercaOfferta;
    SearchView searchView;
    FirebaseRecyclerAdapterOfferte.OnItemClickListener listener;
    FirebaseRecyclerOptions<Offerta> options;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageButton backbutton;
    String classe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricercasegnalazione);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Ricerca_Offerte.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerte.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerte.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerte.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerte.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Offerte.this, Preference.class));
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

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(mUser.getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                classe = dataSnapshot.child("classe").getValue(String.class);
                if(classe.equals("Proprietario"))
                {
                    options = new FirebaseRecyclerOptions.Builder<Offerta>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Offerte").orderByChild("checkProprietario").equalTo(true),Offerta.class)
                            .build();
                }
                else if(classe.equals("Ente"))
                {
                    options = new FirebaseRecyclerOptions.Builder<Offerta>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Offerte").orderByChild("checkEnte").equalTo(true),Offerta.class)
                            .build();
                }
                else
                {
                    options = new FirebaseRecyclerOptions.Builder<Offerta>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Offerte").orderByChild("checkVeterinario").equalTo(true),Offerta.class)
                            .build();
                }

                mainAdapterRicercaOfferta = new FirebaseRecyclerAdapterOfferte(options, new FirebaseRecyclerAdapterOfferte.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Offerta offerta = mainAdapterRicercaOfferta.getItem(position);
                        String offertaId = offerta.idOfferta;
                        Intent intent = new Intent(Activity_Ricerca_Offerte.this, Activity_profilo_Offerta.class);
                        intent.putExtra("OFFERTA_CODE",offertaId);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(mainAdapterRicercaOfferta);
                mainAdapterRicercaOfferta.startListening();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        mainAdapterRicercaOfferta = new FirebaseRecyclerAdapterOfferte(options,listener);
        mainAdapterRicercaOfferta.startListening();
        recyclerView.setAdapter(mainAdapterRicercaOfferta);
    }

}

