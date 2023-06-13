package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterOfferte;
import it.uniba.dib.sms22239.Adapters.RecyclerAdapterOfferta;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar1;
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

                List<Offerta> filteredList = new ArrayList<>();

                for (Offerta offerta : mainAdapterRicercaOfferta.getSnapshots())
                {
                    if (offerta != null && offerta.oggetto.startsWith(str))
                    {
                        filteredList.add(offerta);
                    }
                }

                RecyclerAdapterOfferta adapter = new RecyclerAdapterOfferta(filteredList, new RecyclerAdapterOfferta.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Offerta offerta = filteredList.get(position);
                        String offertaId = offerta.idOfferta;
                        Intent intent = new Intent(Activity_Ricerca_Offerte.this, Activity_profilo_Offerta.class);
                        intent.putExtra("OFFERTA_CODE",offertaId);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
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
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar1());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbar());
                    fragmentTransaction.commit();
                    flag = "altro";
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