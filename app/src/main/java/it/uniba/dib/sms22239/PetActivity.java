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

public class PetActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    SearchView searchView;
    MainAdapter.OnItemClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button aggiungiAnimaleButton = findViewById(R.id.add_animal_button);
        aggiungiAnimaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetActivity.this, RegistrazioneAnimale.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PetActivity.this, HomeActivity.class));
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
        
        mainAdapter = new MainAdapter(options, new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Animale animale = mainAdapter.getItem(position);
                String animalId = animale.Id;
                Intent intent = new Intent(PetActivity.this, Animal_Activity.class);
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
        FirebaseRecyclerOptions<Animale> options =
                new FirebaseRecyclerOptions.Builder<Animale>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                        .child("Animale").orderByChild("razza").startAt(str).endAt(str+"\uf8ff"),Animale.class)
                        .build();

        mainAdapter = new MainAdapter(options,listener);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }
}