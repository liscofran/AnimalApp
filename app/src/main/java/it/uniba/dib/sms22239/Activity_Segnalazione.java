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

public class Activity_Segnalazione extends AppCompatActivity {

    RecyclerView recyclerView;
    Main_Adapter_Segnalazione mainAdapterSegnalazione;
    SearchView searchView;
    Main_Adapter.OnItemClickListener listener;
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
                Intent intent = new Intent(Activity_Segnalazione.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazione.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazione.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazione.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazione.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Segnalazione.this, Preference.class));
            }
        });

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads");


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postsnapshot: snapshot.getChildren())
                {
                    Upload upload = postsnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }
                mainAdapter
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PetActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        })

*/

        searchView = findViewById(R.id.searchView);

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
                Intent intent = new Intent(Activity_Segnalazione.this, Activity_Animal_Profile.class);
                intent.putExtra("SEGNALAZIONE_CODE",segnalazioneId);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mainAdapterSegnalazione);
    }
    @Override
    protected void onStart() {
        super.onStart();
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazione").orderByChild("Categoria").startAt(str).endAt(str+"\uf8ff"),Segnalazione.class)
                        .build();

        mainAdapterSegnalazione = new Main_Adapter_Segnalazione(options, (Main_Adapter_Segnalazione.OnItemClickListener) listener);
        mainAdapterSegnalazione.startListening();
        recyclerView.setAdapter(mainAdapterSegnalazione);
    }



}