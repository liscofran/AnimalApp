package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Animal_Profile;
import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Activities.Activity_Profile_Proprietario_Ente;
import it.uniba.dib.sms22239.R;


public class Fragment_edit_animal_profile extends Fragment {

    private EditText mNomeTextView;
    private EditText mrazzaTextView;
    private EditText msessoTextView;
    private Spinner spinner;
    private ImageButton backButton;
    private String selectedItem;

    public Fragment_edit_animal_profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_edit_profile_animal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;
        String idAnimal = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);

        // Collega i componenti dell'interfaccia con le variabili
        EditText editName = getView().findViewById(R.id.animal_nome);
        EditText editRazza = getView().findViewById(R.id.animal_razza);
        EditText editSesso = getView().findViewById(R.id.animal_sesso);
        backButton.findViewById(R.id.back);

        Button saveProfileButton = getView().findViewById(R.id.salva);

        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView = getView().findViewById(R.id.animal_razza);
        msessoTextView = getView().findViewById(R.id.animal_sesso);

        spinner = getView().findViewById(R.id.status);
        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.proprieta_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Scelta non valida", Toast.LENGTH_SHORT).show();
            }
        });

        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                String proprietario = dataSnapshot.child("Id_utente").getValue(String.class);
                String name = dataSnapshot.child("nome").getValue(String.class);
                String razza = dataSnapshot.child("razza").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);
                String status = dataSnapshot.child("status").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mrazzaTextView.setText(razza);
                msessoTextView.setText(sesso);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Imposta un listener di clic sul pulsante di salvataggio del profilo
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newName = editName.getText().toString();
                String newRazza = editRazza.getText().toString();
                String newSesso = editSesso.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("nome").setValue(newName);
                mDatabase.child("razza").setValue(newRazza);
                mDatabase.child("sesso").setValue(newSesso);
                mDatabase.child("prop").setValue(selectedItem);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_animale());
                fragmentTransaction.commit();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_Animal_Profile.class);
                startActivity(intent);
            }
        });
    }}