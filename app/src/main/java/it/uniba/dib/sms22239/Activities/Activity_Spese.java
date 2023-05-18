package it.uniba.dib.sms22239.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterSpese;
import it.uniba.dib.sms22239.Adapters.RecyclerAdapterSpese;
import it.uniba.dib.sms22239.Models.Oggetto_Spesa;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;

public class Activity_Spese extends AppCompatActivity {


    RecyclerView recyclerView;
    FirebaseRecyclerAdapterSpese mainAdapterSpese;
    RecyclerAdapterSpese Speseadapter;
    SearchView searchView;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    List<Oggetto_Spesa> filteredList;
    String idAnimal, selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spese);

        idAnimal = getIntent().getStringExtra("ANIMAL_CODE");
        selectedItem = "";
        filteredList = new ArrayList<>();

        // Imposta la Toolbar come action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.add_item_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea l'intent per aprire la pagina "activity_crea_oggetto_spesa"
                Intent intent = new Intent(Activity_Spese.this, Activity_Crea_Oggetto_Spesa.class);
                intent.putExtra("ANIMAL_CODE", idAnimal);
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

        ImageButton backBtn2 = findViewById(R.id.back);
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

        Spinner spinner = findViewById(R.id.date_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.date_filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedItem = (String) parent.getItemAtPosition(position);

                if (!Objects.equals(selectedItem, "Tutti"))
                {
                    filter();
                    recyclerView.setAdapter(Speseadapter);
                }
                else
                {
                    recyclerView.setAdapter(mainAdapterSpese);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        FirebaseRecyclerOptions<Oggetto_Spesa> options =
                new FirebaseRecyclerOptions.Builder<Oggetto_Spesa>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Oggetti").orderByChild("id_animale").equalTo(idAnimal),Oggetto_Spesa.class)
                        .build();
        mainAdapterSpese = new FirebaseRecyclerAdapterSpese(options, new FirebaseRecyclerAdapterSpese.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
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

    private void mysearch(String str)
    {
        if (!Objects.equals(selectedItem, "Tutti"))
        {
            filter();
            List<Oggetto_Spesa> searchList = new ArrayList<Oggetto_Spesa>();

            for (Oggetto_Spesa oggetto : filteredList)
            {
                if (oggetto != null && oggetto.nome.startsWith(str))
                {
                    searchList.add(oggetto);
                }
            }

            RecyclerAdapterSpese adapter = new RecyclerAdapterSpese(searchList, new RecyclerAdapterSpese.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                }
            });

            recyclerView.setAdapter(adapter);
        }
        else
        {
            List<Oggetto_Spesa> searchList = new ArrayList<Oggetto_Spesa>();

            for (Oggetto_Spesa oggetto : mainAdapterSpese.getSnapshots())
            {
                if (oggetto != null && oggetto.nome.startsWith(str))
                {
                    searchList.add(oggetto);
                }
            }

            RecyclerAdapterSpese adapter = new RecyclerAdapterSpese(searchList, new RecyclerAdapterSpese.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                }
            });

            recyclerView.setAdapter(adapter);
        }
    }

    private void filter()
    {
        filteredList.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();
        LocalDate data = null;

        for (Oggetto_Spesa oggetto : mainAdapterSpese.getSnapshots())
        {
            try
            {
                data = LocalDate.parse(oggetto.dataAcquisto, formatter);
            }
            catch (DateTimeParseException e)
            {
                try
                {
                    // Dividi la stringa in giorno, mese e anno
                    String[] dateParts = oggetto.dataAcquisto.split("/");
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);

                    // Aggiungi uno "0" se necessario
                    if (day < 10) {
                        dateParts[0] = "0" + day;
                    }
                    if (month < 10) {
                        dateParts[1] = "0" + month;
                    }

                    // Ricostruisci la stringa con il formato corretto
                    oggetto.dataAcquisto = String.join("/", dateParts);
                    data = LocalDate.parse(oggetto.dataAcquisto, formatter);
                }
                catch (DateTimeParseException ex)
                {
                    System.out.println("La stringa non ha il formato corretto e non può essere corretta.");
                }
            }

            if (selectedItem.equals("Oggi") && data.equals(today))
            {
                filteredList.add(oggetto);
            }
            else if (selectedItem.equals("Ultima Settimana") && data.getYear() == today.getYear() && data.getMonth() == today.getMonth())
            {
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int selectedWeek = data.get(weekFields.weekOfWeekBasedYear());
                int currentWeek = today.get(weekFields.weekOfWeekBasedYear());

                if (selectedWeek == currentWeek) {
                    filteredList.add(oggetto);
                }
            }
            else if (selectedItem.equals("Ultimo Mese") && data.getMonth() == today.getMonth() && data.getYear() == today.getYear())
            {
                filteredList.add(oggetto);
            }
            else if (selectedItem.equals("Ultimo Anno") && data.getYear() == today.getYear())
            {
                filteredList.add(oggetto);
            }
        }

        Speseadapter = new RecyclerAdapterSpese(filteredList, new RecyclerAdapterSpese.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
    }
}















