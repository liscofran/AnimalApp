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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    private TextView mragionesociale;
    //private TextView mtipoTextView;

    private TextView mpivaTextView;
    private TextView msedelegaleTextView;

    private Spinner spinner;

    String selectedItem;

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
        spinner = getView().findViewById(R.id.spinner);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;
        mDatabase = database.getInstance().getReference().child("User").child(user.getUid());

        // Collega i componenti dell'interfaccia con le variabili
        EditText editragsociale = getView().findViewById(R.id.ente_ragione_sociale);
        EditText editsedelegale = getView().findViewById(R.id.ente_sede_legale);
        EditText editpiva = getView().findViewById(R.id.ente_piva);


        spinner.setPrompt("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ente_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        Button saveProfileButton = getView().findViewById(R.id.save_profile_button);
        mragionesociale = getView().findViewById(R.id.ente_ragione_sociale);
//        mtipoTextView = getView().findViewById(R.id.ente_tipo);
        msedelegaleTextView = getView().findViewById(R.id.ente_sede_legale);
        mpivaTextView = getView().findViewById(R.id.ente_piva);

        // Recupera i dati dal database e popola i campi
        mDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                //recupero dati e assegnazione alle variabili
                String ragsociale = dataSnapshot.child("ragione_sociale").getValue(String.class);
                String sedelegale = dataSnapshot.child("sede_legale").getValue(String.class);
                String piva = dataSnapshot.child("p_iva").getValue(String.class);

                //set delle variabili recuperate al layout
                mragionesociale.setText(ragsociale);
                msedelegaleTextView.setText(sedelegale);
                mpivaTextView.setText(piva);
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
                String newRagsociale = editragsociale.getText().toString();
                String newtipo = selectedItem;
                String newsedelegale = editsedelegale.getText().toString();
                String newpiva = editpiva.getText().toString();

                //modifica e salva i dati anche sul database
                mDatabase.child("ragione_sociale").setValue(newRagsociale);
                mDatabase.child("tipo").setValue(newtipo);
                mDatabase.child("sede_legale").setValue(newsedelegale);
                mDatabase.child("p_iva").setValue(newpiva);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_profile_ente());
                fragmentTransaction.commit();
            }
        });
    }}