package it.uniba.dib.sms22239.Activities;

import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.uniba.dib.sms22239.Fragments.Fragment_toolbarEnte;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarProprietario;
import it.uniba.dib.sms22239.Fragments.Fragment_toolbarVeterinario;
import it.uniba.dib.sms22239.Models.Animale;
import it.uniba.dib.sms22239.Adapters.FirebaseRecyclerAdapterAnimale;
import it.uniba.dib.sms22239.Preference;
import it.uniba.dib.sms22239.R;
import it.uniba.dib.sms22239.Adapters.RecyclerAdapterAnimale;

public class Activity_Animale_Selezionato extends AppCompatActivity {

    String flag;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapterAnimale mainAdapter;
    SearchView searchView;
    String idAnimale;
    String c1,c2,c3,c4,c5,c6;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_pokemon);

        ImageButton backBtn2 = findViewById(R.id.back);
        idAnimale = getIntent().getStringExtra("ANIMAL_CODE");

        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        autenticazione();

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchView);
    }

    @Override
    protected void onStart()
    {
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

        FirebaseRecyclerOptions<Animale> options =
                new FirebaseRecyclerOptions.Builder<Animale>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Animale"),Animale.class)
                        .build();

        mainAdapter = new FirebaseRecyclerAdapterAnimale(options,null);

        c1= getString(R.string.selezionaan);
        c2= getString(R.string.a4);
        c3= getString(R.string.conferma);
        c4= getString(R.string.rela);
        c5= getString(R.string.a5);
        c6= getString(R.string.annulla);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapter.startListening();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                filter();
            }
        }, 200);

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
            if (animale.nome.startsWith(str) && (!Objects.equals(animale.Id, idAnimale)))
            {
                filteredList.add(animale);
            }
        }

        RecyclerAdapterAnimale adapter = new RecyclerAdapterAnimale(filteredList, new RecyclerAdapterAnimale.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Animale animale =  filteredList.get(position);
                String animalId = animale.Id;


                // creazione dello spinner
                Spinner spinner = new Spinner(Activity_Animale_Selezionato.this);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Activity_Animale_Selezionato.this, R.array.relazione_options, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                // creazione dell'AlertDialog con lo spinner
                new AlertDialog.Builder(Activity_Animale_Selezionato.this)
                        .setTitle(c1)
                        .setMessage(c2)
                        .setView(spinner)
                        .setPositiveButton(c3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedOption = spinner.getSelectedItem().toString();
                                Toast.makeText(Activity_Animale_Selezionato.this, c4 + " " + selectedOption + " " + c5, Toast.LENGTH_SHORT).show();

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);
                                mDatabase.child("idAnimaleRelazione").setValue(animalId);
                                mDatabase.child("relazione").setValue(selectedOption);

                                DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Animale").child(animalId);
                                mDatabase1.child("relazione").setValue(selectedOption);
                                mDatabase1.child("idAnimaleRelazione").setValue(idAnimale);
                                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_Home.class));
                            }
                        })
                        .setNegativeButton(c6, null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void filter(){

        List<Animale> ListSenzaAnimale = new ArrayList<>();

        for (Animale animale : mainAdapter.getSnapshots())
        {
            if (!Objects.equals(animale.Id, idAnimale))
            {
                ListSenzaAnimale.add(animale);
            }
        }

        RecyclerAdapterAnimale adapter = new RecyclerAdapterAnimale(ListSenzaAnimale, new RecyclerAdapterAnimale.OnItemClickListener() {
            @Override
            public void onItemClick(int position)
            {
                Animale animale =  ListSenzaAnimale.get(position);
                String animalId = animale.Id;

                // creazione dello spinner
                Spinner spinner = new Spinner(Activity_Animale_Selezionato.this);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Activity_Animale_Selezionato.this, R.array.relazione_options, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                // creazione dell'AlertDialog con lo spinner
                new AlertDialog.Builder(Activity_Animale_Selezionato.this)
                        .setTitle(c1)
                        .setMessage(c2)
                        .setView(spinner)
                        .setPositiveButton(c3, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedOption = spinner.getSelectedItem().toString();
                                Toast.makeText(Activity_Animale_Selezionato.this, c4+ " " +selectedOption + " " + c5, Toast.LENGTH_SHORT).show();

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);
                                mDatabase.child("idAnimaleRelazione").setValue(animalId);
                                mDatabase.child("relazione").setValue(selectedOption);


                                DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Animale").child(animalId);
                                mDatabase1.child("relazione").setValue(selectedOption);
                                mDatabase1.child("idAnimaleRelazione").setValue(idAnimale);
                                startActivity(new Intent(Activity_Animale_Selezionato.this, Activity_Home.class));
                            }
                        })
                        .setNegativeButton(c6, null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
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