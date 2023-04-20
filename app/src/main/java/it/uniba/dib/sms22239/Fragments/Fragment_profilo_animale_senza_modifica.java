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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Bluetooth;
import it.uniba.dib.sms22239.Activities.Activity_Multimedia;
import it.uniba.dib.sms22239.Activities.Activity_Proprieta;
import it.uniba.dib.sms22239.Activities.Activity_Spese;
import it.uniba.dib.sms22239.Fragments.Fragment_edit_animal_profile;
import it.uniba.dib.sms22239.R;

public class Fragment_profilo_animale_senza_modifica extends Fragment {
    private TextView mNomeTextView;
    private TextView mrazzaTextView;
    private TextView msessoTextView;

    private TextView midproprietarioTextView;

    private FirebaseAuth mAuth;


    public Fragment_profilo_animale_senza_modifica() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profilo_animale_senza_modifica, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String idAnimale = requireActivity().getIntent().getStringExtra("ANIMAL_CODE");

        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Animale").child(idAnimale);

        midproprietarioTextView = getView().findViewById(R.id.animal_proprietario);
        mNomeTextView = getView().findViewById(R.id.animal_nome);
        mrazzaTextView =  getView().findViewById(R.id.animal_razza);
        msessoTextView =  getView().findViewById(R.id.animal_sesso);

        Button backBtn = getView().findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // Recupera i dati dal database e popola le viste
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String proprietario = dataSnapshot.child("Id_utente").getValue(String.class);
                String nome = dataSnapshot.child("nome").getValue(String.class);
                String razza = dataSnapshot.child("razza").getValue(String.class);
                String sesso = dataSnapshot.child("sesso").getValue(String.class);

                //set delle variabili recuperate al layout
                midproprietarioTextView.setText(proprietario);
                mNomeTextView.setText("Nome" + nome);
                mrazzaTextView.setText("Razza" + razza);
                msessoTextView.setText("Sesso" +sesso);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
}