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
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import it.uniba.dib.sms22239.Adapters.RecyclerAdapterAnimale;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarEnte;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarProprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarVeterinario;
import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterAnimale;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Miei_Animali extends AppCompatActivity {

    String flag;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapterAnimale mainAdapter;
    SearchView searchView;
    FirebaseRecyclerAdapterAnimale.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miei_animali);

        autenticazione();
        ImageButton backBtn2 = findViewById(R.id.back);
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton aggiungiAnimaleButton = findViewById(R.id.add_animal_button);
        aggiungiAnimaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Miei_Animali.this, Activity_Registrazione_Animale.class);
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
                Intent intent = new Intent(Activity_Miei_Animali.this, Activity_Animal_Profile.class);
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

        List<Animale> filteredList = new ArrayList<>();

        for (Animale animale : mainAdapter.getSnapshots())
        {
            if (animale != null && animale.nome.startsWith(str))
            {
                filteredList.add(animale);
            }
        }

        RecyclerAdapterAnimale filteredAdapter = new RecyclerAdapterAnimale(filteredList,new RecyclerAdapterAnimale.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Animale animale = filteredList.get(position);
                String animalId = animale.Id;
                Intent intent = new Intent(Activity_Miei_Animali.this, Activity_Animal_Profile.class);
                intent.putExtra("ANIMAL_CODE",animalId);
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
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarVeterinario());
                    fragmentTransaction.commit();
                    flag = "veterinario";
                }
                else if(classe.equals("Proprietario"))
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarProprietario());
                    fragmentTransaction.commit();
                    flag = "proprietario";
                }
                else
                {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_toolbar, new Fragment_toolbarEnte());
                    fragmentTransaction.commit();
                    flag = "ente";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Gestisci l'evento di annullamento
                String c4= getString(R.string.a3);
                Log.e("Firebase", c4 + error.getMessage());
            }
        });
    }
}