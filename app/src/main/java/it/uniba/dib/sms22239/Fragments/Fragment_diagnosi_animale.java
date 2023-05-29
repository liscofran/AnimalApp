package it.uniba.dib.sms22239.Fragments;

import static androidx.core.content.ContentProviderCompat.requireContext;

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
import androidx.fragment.app.Fragment;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.dib.sms22239.Activities.Activity_Calendario_Prenotazioni_Animale;
import it.uniba.dib.sms22239.Activities.Activity_Home;
import it.uniba.dib.sms22239.Activities.Activity_Prenotazione;
import it.uniba.dib.sms22239.Activities.Activity_Prenotazioni_Veterinario;
import it.uniba.dib.sms22239.R;

public class Fragment_diagnosi_animale extends Fragment {


    private MaterialTextView imageView;
    private MaterialTextView dataEditText;
    private MaterialTextView orarioInizioEditText;
    private MaterialTextView orarioFineEditText;
    private MaterialTextView veterinarioEditText;
    private MaterialTextView animaleEditText;
    private MaterialTextView diagnosiEditText;
    private String id_veterinario;
    private String idAnimale;
    private String idProprietario;
    private String idPrenotazione;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diagnosi_animale, container, false);

        imageView = view.findViewById(R.id.imageView2);
        dataEditText = view.findViewById(R.id.data);
        orarioInizioEditText = view.findViewById(R.id.orario_inizio);
        orarioFineEditText = view.findViewById(R.id.orario_fine);
        veterinarioEditText = view.findViewById(R.id.nom_cogn_prop);
        animaleEditText = view.findViewById(R.id.animale);
        diagnosiEditText = view.findViewById(R.id.diagnosi);

        idPrenotazione = getActivity().getIntent().getStringExtra("id_prenotazione");


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Prenotazioni").child(idPrenotazione);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Recupera i valori dal database utilizzando i nomi delle chiavi corrispondenti
                    String data = dataSnapshot.child("Data").getValue(String.class);
                    String orarioInizio = dataSnapshot.child("orario_inizio").getValue(String.class);
                    String orarioFine = dataSnapshot.child("orario_fine").getValue(String.class);
                    id_veterinario = dataSnapshot.child("id_veterinario").getValue(String.class);                    idAnimale = dataSnapshot.child("id_animale").getValue(String.class);
                    String diagnosi = dataSnapshot.child("Diagnosi").getValue(String.class);

                    dataEditText.setText(data);
                    orarioInizioEditText.setText(orarioInizio);
                    orarioFineEditText.setText(orarioFine);
                    diagnosiEditText.setText(diagnosi);




                }
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
                        DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child("User").child(id_veterinario);

                        mDatabase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                // Recupera i valori dal database utilizzando i nomi delle chiavi corrispondenti
                                String cognome = dataSnapshot.child("cognome").getValue(String.class);

                                veterinarioEditText.setText("Dottor: " + cognome);


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }, 500);
            }
        }, 500);
        ImageView elimina = view.findViewById(R.id.eliminaButton);
        elimina.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent = new Intent(getActivity(), Activity_Calendario_Prenotazioni_Animale.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        return view;
    }



}
