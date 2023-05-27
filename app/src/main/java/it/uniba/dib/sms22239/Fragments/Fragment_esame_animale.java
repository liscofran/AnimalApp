package it.uniba.dib.sms22239.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Prenotazioni_Veterinario;
import it.uniba.dib.sms22239.R;


public class Fragment_esame_animale extends Fragment {
    private ImageButton backButton;
    private MaterialTextView imageView;
    private MaterialTextView dataEditText;
    private MaterialTextView orarioInizioEditText;
    private MaterialTextView orarioFineEditText;
    private MaterialTextView nomCognPropEditText;
    private MaterialTextView animaleEditText;
    private MaterialTextView statoEsameEditText;
    private MaterialTextView tipoEsameEditText;
    private String idAnimale;
    private String idProprietario;
    private String idPrenotazione;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esame_animale, container, false);



        backButton = view.findViewById(R.id.back);
        imageView = view.findViewById(R.id.imageView2);
        dataEditText = view.findViewById(R.id.data);
        orarioInizioEditText = view.findViewById(R.id.orario_inizio);
        orarioFineEditText = view.findViewById(R.id.orario_fine);
        nomCognPropEditText = view.findViewById(R.id.nom_cogn_prop);
        animaleEditText = view.findViewById(R.id.animale);
        statoEsameEditText = view.findViewById(R.id.statoEsame);
        tipoEsameEditText = view.findViewById(R.id.tipoEsame);

        // Aggiungi eventuali altri listener o codice di gestione qui


        idPrenotazione = getActivity().getIntent().getStringExtra("id_prenotazione");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Prenotazioni").child(idPrenotazione);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recupera i valori dal database utilizzando i nomi delle chiavi corrispondenti
                String data = dataSnapshot.child("Data").getValue(String.class);
                String orarioInizio = dataSnapshot.child("orario_inizio").getValue(String.class);
                String orarioFine = dataSnapshot.child("orario_fine").getValue(String.class);
                idAnimale = dataSnapshot.child("id_animale").getValue(String.class);
                String statoEsame = dataSnapshot.child("Esame").getValue(String.class);
                String tipoEsame = dataSnapshot.child("TipoEsame").getValue(String.class);

                dataEditText.setText(data);
                orarioInizioEditText.setText(orarioInizio);
                orarioFineEditText.setText(orarioFine);
                statoEsameEditText.setText(statoEsame);
                tipoEsameEditText.setText(tipoEsame);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler(Looper.getMainLooper());

// Esegui un'azione dopo un ritardo di mezzo secondo
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child("Animale").child(idAnimale);

                mDatabase1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nome = dataSnapshot.child("nome").getValue(String.class);
                        idProprietario = dataSnapshot.child("Id_utente").getValue(String.class);
                        animaleEditText.setText(nome);
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Handler handler2 = new Handler(Looper.getMainLooper());

// Esegui un'azione dopo un ritardo di mezzo secondo
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("User").child(idProprietario);

                        mDatabase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                // Recupera i valori dal database utilizzando i nomi delle chiavi corrispondenti
                                String nome = dataSnapshot.child("nome").getValue(String.class);
                                String cognome = dataSnapshot.child("cognome").getValue(String.class);

                                nomCognPropEditText.setText(nome + " " + cognome);

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }, 500);



            }
        }, 500);

        return view;
    }
}

