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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Animali;
import it.uniba.dib.sms22239.R;

public class Fragment_salute_animale extends Fragment {

    private EditText mPatologieEditText;
    private EditText mPrefciboEditText;
    private String idAnimal;
    private TextView titolo;
    private TextView patologie;
    private TextView preferenze_cibo;

    public Fragment_salute_animale() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_salute_animale, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase;
        idAnimal = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");
        mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);

        titolo = getView().findViewById(R.id.Salute);
        patologie = getView().findViewById(R.id.view_patologie);
        preferenze_cibo = getView().findViewById(R.id.view_preferenze_cibo);

        EditText editPatologie = getView().findViewById(R.id.patologie);
        EditText editPrefcibo = getView().findViewById(R.id.preferenze_cibo);

        ImageButton saveProfileButton = getView().findViewById(R.id.salva);


        mPatologieEditText = getView().findViewById(R.id.patologie);
        mPrefciboEditText = getView().findViewById(R.id.preferenze_cibo);

        ImageButton backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                String patologie = dataSnapshot.child("patologie").getValue(String.class);
                String prefcibo = dataSnapshot.child("preferenze_cibo").getValue(String.class);

                //set delle variabili recuperate al layout
                mPatologieEditText.setText(patologie);
                mPrefciboEditText.setText(prefcibo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Salva i dati del profilo e torna all'activity precedente
                DatabaseReference mDatabase = database.getInstance().getReference().child("Animale").child(idAnimal);

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newPatologie = editPatologie.getText().toString();
                String newPrefcibo = editPrefcibo.getText().toString();


                //modifica e salva i dati anche sul database
                mDatabase.child("patologie").setValue(newPatologie);
                mDatabase.child("preferenze_cibo").setValue(newPrefcibo);


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_animale());
                fragmentTransaction.commit();
            }
        });

    }

}