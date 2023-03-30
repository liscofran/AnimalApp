package it.uniba.dib.sms22239.Fragments;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.R;


public class Fragment_edit_animal_profile extends Fragment {

    private EditText mNomeTextView;
    private EditText mrazzaTextView;
    private EditText msessoTextView;

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

        Button saveProfileButton = getView().findViewById(R.id.save_button);

        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView = getView().findViewById(R.id.animal_razza);
        msessoTextView = getView().findViewById(R.id.animal_sesso);

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


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_animale());
                fragmentTransaction.commit();
            }
        });
    }}