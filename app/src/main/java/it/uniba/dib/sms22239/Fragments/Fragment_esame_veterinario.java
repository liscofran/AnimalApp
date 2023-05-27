package it.uniba.dib.sms22239.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Calendario_Prenotazioni_Animale;
import it.uniba.dib.sms22239.Activities.Activity_Prenotazioni_Veterinario;
import it.uniba.dib.sms22239.R;


public class Fragment_esame_veterinario extends Fragment {
    private ImageButton backButton;
    private ImageView imageView;
    private EditText dataEditText;
    private EditText orarioInizioEditText;
    private EditText orarioFineEditText;
    private EditText nomCognPropEditText;
    private EditText animaleEditText;
    private Spinner statoEsameSpinner;
    private Spinner tipoEsameSpinner;
    private String idAnimale;
    private String idProprietario;
    private String idPrenotazione;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esame_veterinario, container, false);



        backButton = view.findViewById(R.id.back);
        imageView = view.findViewById(R.id.imageView2);
        dataEditText = view.findViewById(R.id.data);
        orarioInizioEditText = view.findViewById(R.id.orario_inizio);
        orarioFineEditText = view.findViewById(R.id.orario_fine);
        nomCognPropEditText = view.findViewById(R.id.nom_cogn_prop);
        animaleEditText = view.findViewById(R.id.animale);
        statoEsameSpinner = view.findViewById(R.id.spinner);
        tipoEsameSpinner = view.findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> statoEsameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.statoesame, android.R.layout.simple_spinner_item);
        statoEsameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statoEsameSpinner.setAdapter(statoEsameAdapter);

        ArrayAdapter<CharSequence> tipoEsameAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.tipoesame, android.R.layout.simple_spinner_item);
        tipoEsameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoEsameSpinner.setAdapter(tipoEsameAdapter);

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
                    //idProprietario = dataSnapshot.child("id_proprietario").getValue(String.class);
                    idAnimale = dataSnapshot.child("id_animale").getValue(String.class);

                    dataEditText.setText(data);
                    orarioInizioEditText.setText(orarioInizio);
                    orarioFineEditText.setText(orarioFine);

                    String statoEsame = dataSnapshot.child("Esame").getValue(String.class);
                    int statoEsameIndex = statoEsameAdapter.getPosition(statoEsame);
                    statoEsameSpinner.setSelection(statoEsameIndex);

                    // Aggiungi un listener per gestire la modifica del valore nello spinner "statoEsameSpinner"
                    statoEsameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            String nuovoStatoEsame = adapterView.getItemAtPosition(position).toString();
                            // Aggiorna il valore nel database
                            dataSnapshot.child("Esame").getRef().setValue(nuovoStatoEsame);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            // Gestisci l'evento di nessuna selezione
                        }
                    });

                    // Aggiorna il valore selezionato nello spinner "tipoEsameSpinner"
                    String tipoEsame = dataSnapshot.child("tipoEsame").getValue(String.class);
                    int tipoEsameIndex = tipoEsameAdapter.getPosition(tipoEsame);
                    tipoEsameSpinner.setSelection(tipoEsameIndex);

                    // Aggiungi un listener per gestire la modifica del valore nello spinner "tipoEsameSpinner"
                    tipoEsameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                            String nuovoTipoEsame = adapterView.getItemAtPosition(position).toString();
                            // Aggiorna il valore nel database
                            dataSnapshot.child("tipoEsame").getRef().setValue(nuovoTipoEsame);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            // Gestisci l'evento di nessuna selezione
                        }
                    });


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

        view.findViewById(R.id.eliminaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Conferma eliminazione");
                builder.setMessage("Sei sicuro di voler eliminare questa prenotazione?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.removeValue(); // rimuovi la tupla dal database Firebase
                        Toast.makeText(getActivity(), "Prenotazione eliminata con successo!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), Activity_Prenotazioni_Veterinario.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        Button salvaButton = view.findViewById(R.id.salvaButton); // Sostituisci R.id.salva_button con l'ID effettivo del tuo pulsante "salva"

        salvaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuovoStatoEsame = statoEsameSpinner.getSelectedItem().toString();
                String nuovoTipoEsame = tipoEsameSpinner.getSelectedItem().toString();

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Prenotazioni").child(idPrenotazione);
                databaseRef.child("Esame").setValue(nuovoStatoEsame);
                databaseRef.child("tipoEsame").setValue(nuovoTipoEsame);

                // Avvia l'ActivityPrenotazioneVeterinario
                Intent intent = new Intent(getActivity(), Activity_Prenotazioni_Veterinario.class);
                startActivity(intent);

                // Chiudi l'Activity corrente (se necessario)
                getActivity().finish();
            }
        });



        return view;
    }
}

