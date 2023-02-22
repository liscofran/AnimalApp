package it.uniba.dib.sms22239;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class RicercaSegnalazione extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ArrayList<LanguageData> mList = new ArrayList<LanguageData>();
    private LanguageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricercasegnalazione);

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

