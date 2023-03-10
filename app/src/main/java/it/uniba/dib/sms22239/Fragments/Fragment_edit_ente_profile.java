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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.R;


public class Fragment_edit_ente_profile extends Fragment {

    private FirebaseAuth mAuth;
    private EditText mNomeTextView;
    private EditText mtipoTextView;
    private TextView mlocalitaTextView;

    public Fragment_edit_ente_profile() {
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
        return inflater.inflate(R.layout.fragment_edit_profile_ente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        // Collega i componenti dell'interfaccia con le variabili
        EditText editName = getView().findViewById(R.id.ente_nome);
        EditText edittipo = getView().findViewById(R.id.ente_tipo);
        EditText editlocalita = getView().findViewById(R.id.ente_localita);


        Button saveProfileButton = getView().findViewById(R.id.save_profile_button);
        mNomeTextView = getView().findViewById(R.id.ente_nome);
        mtipoTextView = getView().findViewById(R.id.ente_tipo);
        mlocalitaTextView = getView().findViewById(R.id.ente_localita);

        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                String name = dataSnapshot.child("nome").getValue(String.class);
                String tipo = dataSnapshot.child("cognome").getValue(String.class);
                String localita = dataSnapshot.child("codice_fiscale").getValue(String.class);

                //set delle variabili recuperate al layout
                mNomeTextView.setText(name);
                mtipoTextView.setText(tipo);
                mlocalitaTextView.setText(localita);
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
                DatabaseReference mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

                //prende i dati inseriti in input e gli assegna alle variabili temporanee
                String newName = editName.getText().toString();
                String newtipo = edittipo.getText().toString();
                String newlocalita = editlocalita.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("tipo").setValue(newtipo);
                mDatabase.child("nome").setValue(newName);
                mDatabase.child("localita").setValue(newlocalita);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_ente());
                fragmentTransaction.commit();
            }
        });
    }}