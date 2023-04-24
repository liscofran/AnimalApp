package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.R;


public class Fragment_profilo_segnalazione_senza_modifica extends Fragment
{
    private FirebaseAuth mAuth;
    private TextView mDescrizioneTextView;
    private TextView mProvinciaTextView;
    private TextView mOggettoTextView;
    public double latitude,longitude;

    public Fragment_profilo_segnalazione_senza_modifica() {
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
        return inflater.inflate(R.layout.fragment_profilo_segnalazione_senza_modifica, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String idSegnalazione = requireActivity().getIntent().getStringExtra("SEGNALAZIONE_CODE");

        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);

        mDescrizioneTextView = getView().findViewById(R.id.segnalazione_descrizione);
        mProvinciaTextView =  getView().findViewById(R.id.segnalazione_provincia);
        mOggettoTextView =  getView().findViewById(R.id.oggetto);
        ImageButton mapBtn=getView().findViewById(R.id.mapBtn);
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
                String descrizione = dataSnapshot.child("descrizione").getValue(String.class);
                String provincia = dataSnapshot.child("provincia").getValue(String.class);
                String oggetto = dataSnapshot.child("oggetto").getValue(String.class);
                latitude=dataSnapshot.child("latitude").getValue(double.class);
                longitude=dataSnapshot.child("longitude").getValue(double.class);

                //set delle variabili recuperate al layout
                mDescrizioneTextView.setText(descrizione);
                mProvinciaTextView.setText(provincia);
                mOggettoTextView.setText(oggetto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        mapBtn.setOnClickListener((new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String label = "Posizione della Segnalazione"; // Nome del pin sulla mappa
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        }
        ));

    }
}