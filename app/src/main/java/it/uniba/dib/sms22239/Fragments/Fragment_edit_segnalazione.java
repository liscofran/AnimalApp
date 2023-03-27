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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Profile_Proprietario_Ente;
import it.uniba.dib.sms22239.Activities.Activity_Segnalazione;
import it.uniba.dib.sms22239.R;


public class Fragment_edit_segnalazione extends Fragment {

    private FirebaseAuth mAuth;
    private EditText mDescrizioneTextView;
    private EditText mProvinciaTextView;


    public Fragment_edit_segnalazione() {
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
        return inflater.inflate(R.layout.fragment_edit_segnalazione, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        String idSegnalazione = requireActivity().getIntent().getStringExtra("SEGNALAZIONE_CODE");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        DatabaseReference mDatabase;
        mDatabase = database.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);

        // Collega i componenti dell'interfaccia con le variabili
        EditText editDescrizione = getView().findViewById(R.id.segnalazione_descrizione);
        EditText editProvincia = getView().findViewById(R.id.segnalazione_provincia);


        Button saveProfileButton = getView().findViewById(R.id.save_button);
        mDescrizioneTextView = getView().findViewById(R.id.segnalazione_descrizione);
        mProvinciaTextView = getView().findViewById(R.id.segnalazione_provincia);

        Button backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Activity_Segnalazione.class);
                startActivity(intent);
            }
        });

        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                String descrizione = dataSnapshot.child("descrizione").getValue(String.class);
                String provincia = dataSnapshot.child("provincia").getValue(String.class);

                //set delle variabili recuperate al layout
                mDescrizioneTextView.setText(descrizione);
                mProvinciaTextView.setText(provincia);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

        // Imposta un listener di clic sul pulsante di salvataggio del profilo
        saveProfileButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = database.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newDescrizione = editDescrizione.getText().toString();
                String newProvincia = editProvincia.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("provincia").setValue(newProvincia);
                mDatabase.child("descrizione").setValue(newDescrizione);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profilo_segnalazione());
                fragmentTransaction.commit();
            }
        });
    }}