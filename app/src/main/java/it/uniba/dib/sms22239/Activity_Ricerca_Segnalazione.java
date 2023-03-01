package it.uniba.dib.sms22239;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class Activity_Ricerca_Segnalazione extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<LanguageData> mList = new ArrayList<LanguageData>();
    private LanguageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricercasegnalazione);

       /* findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Ricerca_Segnalazione.this, Activity_Home.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Activity_Profile_Proprietario_Ente.class));
            }
        });

        findViewById(R.id.annunci).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Registrazione_Animale.this, Activity_Segnalazioni_Offerte.class));
            }
        });

        findViewById(R.id.pet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Activity_Animali.class));
            }
        });

        findViewById(R.id.qr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Activity_QRcode.class));
            }
        });

        findViewById(R.id.impostazioni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_Ricerca_Segnalazione.this, Preference.class));
            }
        });*/

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addDataToList();
        adapter = new LanguageAdapter(mList);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String query) {

        if (query != null) {
            ArrayList<LanguageData> filteredList = new ArrayList<>();
            for (LanguageData i : mList) {
                if (i.getTitle().toLowerCase(Locale.ROOT).contains(query)) {
                    filteredList.add(i);
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(filteredList);
            }
        }
    }

    private void addDataToList() {
        mList.add(new LanguageData("Java", R.drawable.github_logo));
        mList.add(new LanguageData("Kotlin", R.drawable.animal_logo));
        mList.add(new LanguageData("C++", R.drawable.phone_logo));
        mList.add(new LanguageData("Python", R.drawable.pet));
        mList.add(new LanguageData("HTML", R.drawable.github_logo));
        mList.add(new LanguageData("Swift", R.drawable.github_logo));
        mList.add(new LanguageData("C#", R.drawable.github_logo));
        mList.add(new LanguageData("JavaScript", R.drawable.github_logo));
    }
}

