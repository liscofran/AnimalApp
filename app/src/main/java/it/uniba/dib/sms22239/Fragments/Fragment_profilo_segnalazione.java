package it.uniba.dib.sms22239.Fragments;

import android.content.ActivityNotFoundException;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import it.uniba.dib.sms22239.Fragments.Fragment_edit_profile_proprietario;
import it.uniba.dib.sms22239.R;


public class Fragment_profilo_segnalazione extends Fragment
{
    private FirebaseAuth mAuth;
    private TextView mDescrizioneTextView;
    private TextView mProvinciaTextView;
    private TextView mOggettoTextView;

    private TextView utente;
    private CircleImageView Immaginesegnalazione;
    String id_utente;
    String nomeEcognome;
    public double latitude,longitude;
        String id_utente_segnalazione;
        String tmp1;

        String tmp2;

    public Fragment_profilo_segnalazione() {
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
            return inflater.inflate(R.layout.fragment_profilo_segnalazione, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String idSegnalazione = requireActivity().getIntent().getStringExtra("SEGNALAZIONE_CODE");

        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference mDatabase;
        DatabaseReference mDatabase1;

        // Recupera il riferimento al database
        mDatabase = database.getInstance().getReference().child("Segnalazioni").child(idSegnalazione);
        mDatabase1 = database.getInstance().getReference().child("User");


        mDescrizioneTextView = getView().findViewById(R.id.segnalazione_descrizione);
        mProvinciaTextView =  getView().findViewById(R.id.segnalazione_provincia);
        mOggettoTextView =  getView().findViewById(R.id.oggetto);
        Immaginesegnalazione = getView().findViewById(R.id.imageView2);
        utente = getView().findViewById(R.id.segnalazione_utente);

        ImageView mapBtn=getView().findViewById(R.id.mapBtn);
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

                id_utente = dataSnapshot.child("uid").getValue(String.class);
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

        mDatabase1.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String nome_utente = dataSnapshot.child(id_utente).child("nome").getValue(String.class);
                String cognome_utente = dataSnapshot.child(id_utente).child("cognome").getValue(String.class);
                nomeEcognome = nome_utente + " " + cognome_utente;
                utente.setText(nomeEcognome);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        getView().findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new Fragment_edit_segnalazione());
                fragmentTransaction.commit();
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