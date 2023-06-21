package it.uniba.dib.sms22239.Activities.Annunci;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterSegnalazione;
import it.uniba.dib.sms22239.Adapters.RecyclerAdapterSegnalazione;
import it.uniba.dib.sms22239.Fragments.Enti.Fragment_Toolbar_Ente;
import it.uniba.dib.sms22239.Fragments.Proprietari.Fragment_Toolbar_Proprietario;
import it.uniba.dib.sms22239.Fragments.Veterinari.Fragment_Toolbar_Veterinario;
import it.uniba.dib.sms22239.R;
import it.uniba.dib.sms22239.Models.Segnalazione;

public class Activity_Ricerca_Segnalazioni extends AppCompatActivity
{
    RecyclerView recyclerView;
    FirebaseRecyclerAdapterSegnalazione mainAdapterRicercaSegnalazione;
    SearchView searchView;
    FirebaseRecyclerAdapterSegnalazione.OnItemClickListener listener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageButton backbutton;
    String classe;
    FirebaseRecyclerOptions<Segnalazione> options;
    String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricercasegnalazione);

        autenticazione();

        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(mUser.getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                classe = dataSnapshot.child("classe").getValue(String.class);

                if(classe.equals("Proprietario"))
                {
                    options = new FirebaseRecyclerOptions.Builder<Segnalazione>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("checkProprietario").equalTo(true),Segnalazione.class)
                            .build();
                }
                else if(classe.equals("Ente"))
                {
                    options = new FirebaseRecyclerOptions.Builder<Segnalazione>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("checkEnte").equalTo(true),Segnalazione.class)
                            .build();
                }
                else
                {
                    options = new FirebaseRecyclerOptions.Builder<Segnalazione>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("checkVeterinario").equalTo(true),Segnalazione.class)
                            .build();
                }

                mainAdapterRicercaSegnalazione = new FirebaseRecyclerAdapterSegnalazione(options, new FirebaseRecyclerAdapterSegnalazione.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Segnalazione segnalazione = mainAdapterRicercaSegnalazione.getItem(position);
                        String segnalazioneId = segnalazione.idSegnalazione;
                        Intent intent = new Intent(Activity_Ricerca_Segnalazioni.this, Activity_Profilo_Segnalazione.class);
                        intent.putExtra("SEGNALAZIONE_CODE",segnalazioneId);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(mainAdapterRicercaSegnalazione);
                mainAdapterRicercaSegnalazione.startListening();
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
        mainAdapterRicercaSegnalazione.stopListening();
    }

    private void mysearch(String str) {

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
                    options = new FirebaseRecyclerOptions.Builder<Segnalazione>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("checkProprietario").equalTo(true),Segnalazione.class)
                            .build();
                }
                else if(classe.equals("Ente"))
                {
                    options = new FirebaseRecyclerOptions.Builder<Segnalazione>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("checkEnte").equalTo(true),Segnalazione.class)
                            .build();
                }
                else
                {
                    options = new FirebaseRecyclerOptions.Builder<Segnalazione>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Segnalazioni").orderByChild("checkVeterinario").equalTo(true),Segnalazione.class)
                            .build();
                }

                List<Segnalazione> filteredList = new ArrayList<>();

                for (Segnalazione segnalazione : mainAdapterRicercaSegnalazione.getSnapshots())
                {
                    if (segnalazione != null && segnalazione.oggetto.startsWith(str))
                    {
                        filteredList.add(segnalazione);
                    }
                }

                RecyclerAdapterSegnalazione adapter = new RecyclerAdapterSegnalazione(filteredList, new RecyclerAdapterSegnalazione.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Segnalazione segnalazione = filteredList.get(position);
                        String segnalazioneId = segnalazione.idSegnalazione;
                        Intent intent = new Intent(Activity_Ricerca_Segnalazioni.this, Activity_Profilo_Segnalazione.class);
                        intent.putExtra("SEGNALAZIONE_CODE",segnalazioneId);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {}
        });
    }

    protected void autenticazione()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());
        Query query = myRef.orderByChild("classe");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Recupera il valore dell'attributo "classe"
                String classe = snapshot.child("classe").getValue(String.class);


                // Verifica il valore dell'attributo "classe"
                if (classe.equals("Veterinario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Veterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Proprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else if(classe.equals("Ente"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_Toolbar_Ente());
                    fragmentTransaction.commit();
                    flag = "ente";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                String c1= getString(R.string.a3);

                Log.e("Firebase", c1 + error.getMessage());
            }
        });
    }
}
