package it.uniba.dib.sms22239.Activities;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterAnimale;
import it.uniba.dib.sms22239.Adapters.RecyclerAdapterAnimale;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Album_Animali extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapterAnimale mainAdapter;
    SearchView searchView;
    FirebaseRecyclerAdapterAnimale.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_pokemon);

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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Animale").orderByChild("Id_utente").equalTo(mUser.getUid()),Animale.class)
                        .build();

        mainAdapter = new FirebaseRecyclerAdapterAnimale(options, new FirebaseRecyclerAdapterAnimale.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Animale animale = mainAdapter.getItem(position);
                String animalId = animale.Id;
                new AlertDialog.Builder(Activity_Album_Animali.this)
                        .setTitle("Selezione Animale")
                        .setMessage("Sei sicuro di voler selezionare questo animale?")
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Activity_Album_Animali.this, "Animale selezionato con successo!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Activity_Album_Animali.this, Activity_Animale_Selezionato.class);
                                intent.putExtra("ANIMALE_SELEZIONATO",animalId);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Annulla", null)
                        .show();
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

        private void mysearch(String str)
        {
            List<Animale> filteredList = new ArrayList<>();

            for (Animale animale : mainAdapter.getSnapshots())
            {
                if (animale != null && animale.nome.startsWith(str))
                {
                    filteredList.add(animale);
                }
            }

            RecyclerAdapterAnimale adapter = new RecyclerAdapterAnimale(filteredList, new RecyclerAdapterAnimale.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Animale animale = filteredList.get(position);
                    String animalId = animale.Id;
                    new AlertDialog.Builder(Activity_Album_Animali.this)
                            .setTitle("Selezione Animale")
                            .setMessage("Sei sicuro di voler selezionare questo animale?")
                            .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(Activity_Album_Animali.this, "Animale selezionato con successo!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Activity_Album_Animali.this, Activity_Animale_Selezionato.class);
                                    intent.putExtra("ANIMAL_CODE",animalId);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Annulla", null)
                            .show();
                }
            });
            recyclerView.setAdapter(adapter);
        }
}