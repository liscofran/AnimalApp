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

import it.uniba.dib.sms22239.Main_Adapter_Spese;
import it.uniba.dib.sms22239.Models.Oggetto_Spesa;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Spese extends AppCompatActivity {

    RecyclerView recyclerView;
    Main_Adapter_Spese mainAdapterSpese;
    SearchView searchView;
    Main_Adapter_Spese.OnItemClickListener listener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String idAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese);

        idAnimal = getIntent().getStringExtra("ANIMAL_CODE");

        // Imposta la Toolbar come action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea l'intento per aprire la pagina "activity_crea_oggetto_spesa"
                Intent intent = new Intent(Activity_Spese.this, Activity_Crea_Oggetto_Spesa.class);
                intent.putExtra("ANIMAL_CODE",idAnimal);
                startActivity(intent);

            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Spese.this, Activity_Home.class));
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Spese.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Spese.this, Preference.class));
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

        FirebaseRecyclerOptions<Oggetto_Spesa> options =
                new FirebaseRecyclerOptions.Builder<Oggetto_Spesa>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Oggetti").orderByChild("id_animale").equalTo(idAnimal),Oggetto_Spesa.class)
                        .build();
        mainAdapterSpese = new Main_Adapter_Spese(options, new Main_Adapter_Spese.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Oggetto_Spesa oggetto = mainAdapterSpese.getItem(position);
                String oggettoId = oggetto.id_animale;
            }
        });
        recyclerView.setAdapter(mainAdapterSpese);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapterSpese.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapterSpese.stopListening();
    }

    private void mysearch(String str) {

        FirebaseRecyclerOptions<Oggetto_Spesa> options =
                new FirebaseRecyclerOptions.Builder<Oggetto_Spesa>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Oggetti").orderByChild("oggetto").startAt(str).endAt(str+"\uf8ff"),Oggetto_Spesa.class)
                        .build();

        mainAdapterSpese = new Main_Adapter_Spese(options,listener);
        mainAdapterSpese.startListening();
        recyclerView.setAdapter(mainAdapterSpese);
    }




}








