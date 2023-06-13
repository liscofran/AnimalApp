package it.uniba.dib.sms22239.Activities;

import androidx.annotation.NonNull;
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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterOfferte;
import it.uniba.dib.sms22239.Adapters.RecyclerAdapterOfferta;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbar1;
import it.uniba.dib.sms22239.Models.Offerta;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Offerte extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapterOfferte mainAdapterOfferte;
    SearchView searchView;
    FirebaseRecyclerAdapterOfferte.OnItemClickListener listener;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ImageButton backbutton;
    String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offerte);

        autenticazione();

        backbutton = findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton aggiungiOfferte = findViewById(R.id.add_offerta_button);
        aggiungiOfferte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Offerte.this, Activity_Registrazione_Offerte.class);
                startActivity(intent);
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
        mainAdapterOfferte = new FirebaseRecyclerAdapterOfferte(options, new FirebaseRecyclerAdapterOfferte.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Offerta offerta = mainAdapterOfferte.getItem(position);
                String offertaId = offerta.idOfferta;
                Intent intent = new Intent(Activity_Offerte.this, Activity_profilo_Offerta.class);
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

        List<Offerta> filteredList = new ArrayList<>();

        for (Offerta offerta : mainAdapterOfferte.getSnapshots())
        {
            if (offerta != null && offerta.oggetto.startsWith(str))
            {
                filteredList.add(offerta);
            }
        }

        RecyclerAdapterOfferta filteredAdapter = new RecyclerAdapterOfferta(filteredList,new RecyclerAdapterOfferta.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Offerta offerta = filteredList.get(position);
                String offertaId = offerta.idOfferta;
                Intent intent = new Intent(Activity_Offerte.this, Activity_profilo_Offerta.class);
                intent.putExtra("OFFERTA_CODE",offertaId);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(filteredAdapter);
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
                String c1= getString(R.string.a3);

                // Gestisci l'evento di annullamento
                Log.e("Firebase", c1 + error.getMessage());
            }
        });
    }



}